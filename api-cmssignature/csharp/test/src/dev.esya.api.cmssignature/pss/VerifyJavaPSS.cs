using System;
using System.IO;
using NUnit.Framework;
using Org.BouncyCastle.Utilities.Encoders;
using test.esya.api.cmssignature;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace dev.esya.api.cmssignature.pss
{
    class VerifyJavaPSS : CMSSignatureTest
    {
        [Test]
        public void verify_Java_Crypto_WithPSS_WithPfx()
        {
            PublicKey pubKey = new PublicKey(getSignerCertificate().getSubjectPublicKeyInfo());
            verifyCrypto_WithPSS(pubKey, "SignatureJava1.txt");
        }

        [Test]
        public void verify_Java_Crypto_WithPSS_WithSmartCard()
        {
            P11SmartCard p11SmartCard = new P11SmartCard(CardType.AKIS);
            long slotNo = 1;
            p11SmartCard.openSession(slotNo);
            p11SmartCard.login("12345");

            byte[] certBytes = p11SmartCard.getSignatureCertificates()[0];
            ECertificate cert = new ECertificate(certBytes);

            PublicKey publicKey = new PublicKey(cert.getSubjectPublicKeyInfo());

            verifyCrypto_WithPSS(publicKey, "SignatureJava2.txt");
        }


        [Test]
        public void verify_Java_CMS_WithPSS_WithPfx()
        {
            verifyCMSSignature("SignatureJava3.txt");
        }

        [Test]
        public void verify_Java_CMS_WithPSS_WithSmartCard()
        {
            verifyCMSSignature("SignatureJava4.txt");
        }


        public void verifyCrypto_WithPSS(PublicKey pubKey, string fileName)
        {

            string signature;
            byte[] signatureBytes;

            string data;
            byte[] dataBytes;

            bool result;


            RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

            StreamReader reader = new StreamReader("T:\\Temp\\Pss\\" + fileName);

            for (int i = 0; i < RSAPSSTest.TEST_COUNT; i++)
            {
                data = reader.ReadLine();
                if (data != null && data.StartsWith("D:"))
                {
                    dataBytes = Hex.Decode(data.Substring(2));
                    signature = reader.ReadLine();

                    if (signature != null && signature.StartsWith("S:"))
                    {

                        signatureBytes = Hex.Decode(signature.Substring(2));

                        result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, dataBytes, signatureBytes, pubKey);
                        Assert.True(result);

                    }
                    else
                        throw new NullReferenceException("Error in signature file");
                }
                else
                    throw new NullReferenceException("Error in data file");
            }
        }




        public void verifyCMSSignature(string fileName)
        {
            string data;
            byte[] dataBytes;

            string signature;
            byte[] signatureBytes = null;
            SignedDataValidationResult sdvr;

            StreamReader reader = new StreamReader("T:\\Temp\\Pss\\" + fileName);

            for (int i = 0; i < RSAPSSTest.TEST_COUNT; i++)
            {
                data = reader.ReadLine();
                if (data != null && data.StartsWith("D:"))
                {
                    dataBytes = Hex.Decode(data.Substring(2));
                    signature = reader.ReadLine();

                    if (signature != null && signature.StartsWith("S:"))
                    {
                        signatureBytes = Hex.Decode(signature.Substring(2));

                        sdvr = ValidationUtil.validate(signatureBytes, null);
                        Assert.AreEqual(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
                    }

                    else
                        throw new NullReferenceException("Error in signature file");
                }
                else
                    throw new NullReferenceException("Error in data file");
            }
        }
    }
}
