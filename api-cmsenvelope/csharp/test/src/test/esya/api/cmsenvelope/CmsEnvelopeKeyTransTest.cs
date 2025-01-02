using System;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace test.esya.api.cmsenvelope
{
    [TestFixture]
    class CMSEnvelopeKeyTransTest
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

            for (int i = 1; i < 2048; i++)
            {

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(i);
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg, dataToBeEncrypted);
            }
        }
        
        [Test, TestCaseSource("TestCases")]
        public void testKeyTransWithStream(CipherAlg asymmetricAlg, CipherAlg symmetricAlg) 
        {
            tryKeyTransWithStream(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg);
        }


        [Test, TestCaseSource("TestCases")]
        public void testKeyTransWithStreamUpTo2048LengthData(CipherAlg asymmetricAlg, CipherAlg symmetricAlg)
        {

            for (int i = 1; i < 2048; i++)
            {

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(i);
                tryKeyTransWithStream(dataToBeEncrypted, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg);

            }
        }

        private void tryKeyTrans(ECertificate recipientCert, IPrivateKey aPrivate, CipherAlg cipherAlg, CipherAlg symmetricAlg)
        {
            tryKeyTrans(recipientCert, aPrivate, cipherAlg, symmetricAlg, Encoding.ASCII.GetBytes(TestData.plainString));
        }
        private void tryKeyTrans(ECertificate cert, IPrivateKey privKey, CipherAlg cipherAlg, CipherAlg symmetricAlgorithm, byte[] dataToBeEncrypted)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            TestData.configureCertificateValidation(config);

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(Encoding.ASCII.GetBytes(TestData.plainString), symmetricAlgorithm);
            cmsGenerator.addRecipients(config, cert);
            byte[] encryptedCMS = cmsGenerator.generate();

            byte[] plainData = CMSEnvelopeTestUtil.DecryptWithMemory(cert, privKey, encryptedCMS);
            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(plainData));
        }

        private void tryKeyTransWithStream(ECertificate cert, IPrivateKey privKey, CipherAlg cipherAlg, CipherAlg symmetricAlgorithm)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            TestData.configureCertificateValidation(config);

            String encryptedFileName = TestData.plainFile.DirectoryName + "\\Encrypted" + TestData.plainFile.Name;

            using (
                FileStream encryptedOutputStream = new FileStream(encryptedFileName, FileMode.Create),
                plainInputStream = new FileStream(TestData.plainFile.FullName, FileMode.Open, FileAccess.Read))
            {
                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream, symmetricAlgorithm);
                cmsGenerator.addRecipients(config, cert);
                cmsGenerator.generate(encryptedOutputStream);

                encryptedOutputStream.Close();
                plainInputStream.Close();
            }

            String decryptedFileName = TestData.plainFile.DirectoryName + "\\Decrypted" + TestData.plainFile.Name;

            //DecryptWithStream to check
            CMSEnvelopeTestUtil.DecryptWithStream(privKey, cert, encryptedFileName, decryptedFileName);
            CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
        }

        private static void tryKeyTransWithStream(byte[] dataToBeEncrypted, ECertificate recipientCert, IPrivateKey aPrivate, CipherAlg asymmetricAlg, CipherAlg symmetricAlg)
        {
            MemoryStream plainInputStream = new MemoryStream(dataToBeEncrypted);
            MemoryStream encryptedOutputStream = new MemoryStream();

            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(asymmetricAlg);
            TestData.configureCertificateValidation(config);
            config.skipCertificateValidation();

            CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream, symmetricAlg);
            cmsGenerator.addRecipients(config, recipientCert);
            cmsGenerator.generate(encryptedOutputStream);

            //Decrypt to check
            byte[] decryptedBytes = CMSEnvelopeTestUtil.DecryptWithStream(aPrivate, recipientCert, new MemoryStream(encryptedOutputStream.ToArray()));
            Assert.AreEqual(dataToBeEncrypted, decryptedBytes);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void test3DESAlgorithmForKeyTrans() 
        {
            tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void test3DESAlgorithmForKeyTransWithStream() 
        {
            tryKeyTransWithStream(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void testRC2AlgorithmForKeyTrans() 
        {
            tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void testRC2AlgorithmForKeyTransWithStream()
        {
            tryKeyTransWithStream(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void testInvalidModeForKeyTrans() 
        {
            tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);
        }

        [Test, ExpectedException(typeof(CMSException))]
        public void testInvalidModeForKeyTransWithStream()
        {
           tryKeyTransWithStream(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);
        }
    }
}
