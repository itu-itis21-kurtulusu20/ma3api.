using System;
using System.Collections.Generic;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors
{
    public class MSCerStoreDecryptor : IDecryptorStore
    {
        private readonly X509Store mKS = new X509Store();

        public MSCerStoreDecryptor()
        {
            try
            {
                /*mKS = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
                mKS.load(null);*/
                mKS.Open(OpenFlags.ReadOnly);

            }
            catch (Exception e)
            {
                throw new CMSException("Error in loading MS Certificate Store", e);
            }

        }

        public byte[] decrypt(ECertificate aCert, IDecryptorParams aParams)
        {
            byte[] decrypted = null;
            if (aParams is RSADecryptorParams)
            {
                try
                {
                    Asn1DerEncodeBuffer buf = new Asn1DerEncodeBuffer();
                    aCert.getObject().Encode(buf);

                    //CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    //java.security.cert.Certificate sunCert = cf.generateCertificate(new ByteArrayInputStream(buf.getMsgCopy()));
                    X509Certificate2 sunCert = new X509Certificate2(buf.MsgCopy);

                    //String alias = mKS.getCertificateAlias(sunCert);
                    //PrivateKey key = (PrivateKey) mKS.getKey(alias, null);
                    X509Certificate2 foundCert = null;
                    X509Certificate2Collection certCollection = mKS.Certificates;
                    foreach (X509Certificate2 cert in certCollection)
                    {
                        if (cert.Equals(sunCert) && cert.HasPrivateKey)
                        {
                            foundCert = cert;
                            break;
                        }
                    }

                    byte[] encrypted = ((RSADecryptorParams)aParams).getEncryptedKey();
                    RSACryptoServiceProvider cipher = (RSACryptoServiceProvider)foundCert.PrivateKey;
                    decrypted = cipher.Decrypt(encrypted, false);                       
                }
                catch (Exception e)
                {
                    throw new CMSException("Decryption failed", e);
                }
            }
            else if (aParams is ECDHDecryptorParams)
            {
                throw new CryptoException("SunMSCAPI has no support to ECDH");
            }
            return decrypted;
        }


        public ECertificate[] getEncryptionCertificates()
        {
            try
            {
                List<ECertificate> list = new List<ECertificate>();

                //Enumeration<String> aliases = mKS.aliases();
                X509Certificate2Collection certCollection = mKS.Certificates;
                foreach (X509Certificate2 cert in certCollection)
                {
                    if (cert.HasPrivateKey)
                    {
                        ECertificate esyaCer = new ECertificate(cert.RawData);
                        list.Add(esyaCer);
                    }
                }
                return list.ToArray();

            }
            catch (Exception e)
            {
                throw new CMSException("Retrieval of certificates with private keys failed", e);
            }

        }


    }
}
