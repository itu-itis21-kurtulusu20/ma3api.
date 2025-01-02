using System;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace test.esya.api.cmsenvelope
{
    public class CMSEnvelopeKeyAgreementTest
    {
        private EnvelopeConfig config = new EnvelopeConfig();


        public static Object[] TestCases =
        {
            new object[] {KeyAgreementAlg.ECDH_SHA1KDF},
            new object[] {KeyAgreementAlg.ECDH_SHA224KDF},
            new object[] {KeyAgreementAlg.ECDH_SHA256KDF},
            new object[] {KeyAgreementAlg.ECDH_SHA384KDF},
            new object[] {KeyAgreementAlg.ECDH_SHA512KDF},
            new object[] {KeyAgreementAlg.ECCDH_SHA1KDF},
            new object[] {KeyAgreementAlg.ECCDH_SHA224KDF},
            new object[] {KeyAgreementAlg.ECCDH_SHA256KDF},
            new object[] {KeyAgreementAlg.ECCDH_SHA512KDF}
        };

        [Test, TestCaseSource("TestCases")]
        public void testKeyAgreement(KeyAgreementAlg keyAgreementAlg)
        {
            tryKeyAgreement(keyAgreementAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testKeyAgreementWithStream(KeyAgreementAlg keyAgreementAlg) 
        {
            tryKeyAgreementWithStream(keyAgreementAlg);
        }

        private void tryKeyAgreement(KeyAgreementAlg agreementAlg)
        {      
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(Encoding.ASCII.GetBytes(TestData.plainString));
            cmsGenerator.addRecipients(config, TestData.recipientEcCert);
            byte[] encryptedCMS = cmsGenerator.generate();

            byte[] plainData = CMSEnvelopeTestUtil.DecryptWithMemory(TestData.recipientEcCert, TestData.recipientEcKeyPair.getPrivate(), encryptedCMS);
            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(plainData));
        }

        private void tryKeyAgreementWithStream(KeyAgreementAlg agreementAlg)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            //add key agreement recipient
            String encryptedFileName = TestData.plainFile.DirectoryName + "\\Encrypted" + TestData.plainFile.Name;
            using (
                FileStream encryptedOutputStream = new FileStream(encryptedFileName, FileMode.Create),
                plainInputStream = new FileStream(TestData.plainFile.FullName, FileMode.Open, FileAccess.Read))
            {
                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(config, TestData.recipientEcCert);
                cmsGenerator.generate(encryptedOutputStream);

                encryptedOutputStream.Close();
                plainInputStream.Close();
            }
            String decryptedFileName = TestData.plainFile.DirectoryName + "\\Decrypted" + TestData.plainFile.Name;

            //DecryptWithStream to check
            CMSEnvelopeTestUtil.DecryptWithStream(TestData.recipientEcKeyPair.getPrivate(), TestData.recipientEcCert, encryptedFileName, decryptedFileName);
            CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
        }
    }
}
