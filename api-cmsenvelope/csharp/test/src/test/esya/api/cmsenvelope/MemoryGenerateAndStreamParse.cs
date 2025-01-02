using System;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace test.esya.api.cmsenvelope
{
    class MemoryGenerateAndStreamParse
    {

        public static Object[] TestCases =
       {
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES128_CBC},
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES192_CBC},
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES256_CBC},
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES128_GCM},
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES192_GCM},
            new object[] {CipherAlg.RSA_OAEP,CipherAlg.AES256_GCM},

            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES128_CBC},
            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES192_CBC},
            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES256_CBC},
            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES128_GCM},
            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES192_GCM},
            new object[] {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES256_GCM},

            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES128_CBC},
            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES192_CBC},
            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES256_CBC},
            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES128_GCM},
            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES192_GCM},
            new object[] {CipherAlg.RSA_PKCS1, CipherAlg.AES256_GCM}
        };


        [Test, TestCaseSource("TestCases")]
        public void testKeyTrans(CipherAlg asymmetricAlg, CipherAlg symmetricAlg)
        {
            tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testKeyTransUpTo2048LengthData(CipherAlg asymmetricAlg, CipherAlg symmetricAlg)
        {

            for(int i = 1; i<2048; i++)
            {
                byte[] dataToBeEncrypted = RandomUtil.generateRandom(i);
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg, dataToBeEncrypted);
            }
        }

        private void tryKeyTrans(ECertificate recipientCert, IPrivateKey aPrivate, CipherAlg cipherAlg, CipherAlg symmetricAlg) 
        {
            tryKeyTrans(recipientCert, aPrivate, cipherAlg, symmetricAlg, Encoding.ASCII.GetBytes(TestData.plainString));
        }

        private void tryKeyTrans(ECertificate recipientCert, IPrivateKey aPrivate, CipherAlg asymmetricAlg, CipherAlg symmetricAlg, byte[] dataToBeEncrypted) 
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(asymmetricAlg);
            TestData.configureCertificateValidation(config);

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(dataToBeEncrypted, symmetricAlg);
            cmsGenerator.addRecipients(config, recipientCert);
            byte[] encryptedCMS = cmsGenerator.generate();

            byte[] decryptedBytes = CMSEnvelopeTestUtil.DecryptWithStream(aPrivate, recipientCert, new MemoryStream(encryptedCMS));
            Assert.AreEqual(dataToBeEncrypted, decryptedBytes);
        }
    }
}
