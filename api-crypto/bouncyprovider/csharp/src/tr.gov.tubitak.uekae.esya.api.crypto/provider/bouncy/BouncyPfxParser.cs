
using System.Collections;
using System.Collections.Generic;
using System.IO;

using Org.BouncyCastle.Asn1.Pkcs;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.X509;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyPfxParser : IPfxParser
    {   
        private readonly List<Pair<ECertificate, IPrivateKey>> ECentries = new List<Pair<ECertificate, IPrivateKey>>();
        private Pair<ECertificate, IPrivateKey> signingKeyCertPair = new Pair<ECertificate, IPrivateKey>();

        public BouncyPfxParser()
        {

        }

        public BouncyPfxParser(string aPFXFilePath, string aPassword)
        {
            loadPfx(aPFXFilePath, aPassword);
        }


        //In .pfx file it is possible that, some key entries may not have certificates or some certificates may not have key entries
        //This part has been updated for keeping all possible combinations mentioned above in a list called "ECentries"  
        public void loadPfx(string filePath, string password)
        {   
            FileStream fin = new FileStream(filePath, FileMode.Open, FileAccess.Read);
            Pkcs12StoreBuilder storeBuilder = new Pkcs12StoreBuilder();
            Pkcs12Store pkcs12Store = storeBuilder.Build();
            pkcs12Store.Load(fin, password.ToCharArray());
            fin.Close();
            IEnumerable aliases = pkcs12Store.Aliases;
            IEnumerator aliasesEnumerator = aliases.GetEnumerator();
          
            while (aliasesEnumerator.MoveNext())
            {              
                ECertificate cert = null;               
                IPrivateKey privKey = null;

                string alias = (string)aliasesEnumerator.Current;

                X509CertificateEntry bouncyCert = pkcs12Store.GetCertificate(alias);
                if (bouncyCert != null)
                {
                    X509Certificate x509Certificate = bouncyCert.Certificate;
                    cert = new ECertificate(x509Certificate.GetEncoded());
                }

                AsymmetricKeyEntry bouncyKeyEntry = pkcs12Store.GetKey(alias);
                if (bouncyKeyEntry != null)
                {
                    AsymmetricKeyParameter asymmetricKeyParameter = bouncyKeyEntry.Key;
                    PrivateKeyInfo pki = PrivateKeyInfoFactory.CreatePrivateKeyInfo(asymmetricKeyParameter);
                    var privateKeyInfo = new EPrivateKeyInfo(pki.GetDerEncoded());
                    privKey = new PrivateKey(privateKeyInfo);
                }
                Pair<ECertificate, IPrivateKey> pair = new Pair<ECertificate, IPrivateKey>(cert, privKey);
                ECentries.Add(pair);           
            }
            if (ECentries.Count == 0)
                throw new ESYARuntimeException("No certificate and key found in PFX!");
        }

        public ECertificate getFirstCertificate()
        {         
            foreach (Pair<ECertificate, IPrivateKey> certificatesAndKeys in ECentries)            
                if (certificatesAndKeys.first() != null && certificatesAndKeys.second() != null)                               
                    return certificatesAndKeys.getmObj1();

            return null;
        }

        public IPrivateKey getFirstPrivateKey()
        {
            foreach (Pair<ECertificate, IPrivateKey> certificatesAndKeys in ECentries)
                if ((certificatesAndKeys.first() != null && certificatesAndKeys.second() != null))
                    return certificatesAndKeys.getmObj2();

            return null;
        }

        public Pair<ECertificate, IPrivateKey> getFirstSigningKeyCertPair()
        {
            signingKeyCertPair = null;
            foreach (Pair<ECertificate, IPrivateKey> certificatesAndKeys in ECentries)
            {
                if (certificatesAndKeys.first() != null && certificatesAndKeys.second() != null && 
                    certificatesAndKeys.first().getExtensions().getKeyUsage().isDigitalSignature())
                {
                    signingKeyCertPair = certificatesAndKeys;
                    break;
                }
            }

            return signingKeyCertPair;
        }

        public List<Pair<ECertificate, IPrivateKey>> getCertificatesAndKeys()
        {
            return ECentries;
        }
    }
}
