using System;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace test.esya.api.cmsenvelope
{
    public class CMSEnvelopeAddRecipientToParserTest
    {
        public static Object[] TestCases =
        {
            new object[] {KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_PKCS1},
            new object[] {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_OAEP},
            new object[] {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_OAEP_SHA256},
            new object[] {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_PKCS1}
        };

        [Test, TestCaseSource("TestCases")]
        public void testKeyAgreementAndKeyTrans(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg) 
        {
            tryKeyAgreementAndKeyTrans(keyAgreementAlg, keyTransAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testKeyAgreementAndKeyTransWithStream(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg) 
        {
            tryKeyAgreementAndKeyTransWithStream(keyAgreementAlg, keyTransAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testAddingRecipientToParser(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg)
        {
            tryAddingRecipientToParser(keyAgreementAlg, keyTransAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testAddingRecipientToParserWithStream(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg)
        {
            tryAddingRecipientToParserWithStream(keyAgreementAlg, keyTransAlg);
        }

        [Test, TestCaseSource("TestCases")]
        public void testKeyTransOAEPDefaultWithByteStream(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg) 
        {
            tryKeyTransOAEPDefaultWithMemoryStream(keyAgreementAlg, keyTransAlg);
        }

        private void tryKeyAgreementAndKeyTrans(KeyAgreementAlg agreementAlg, CipherAlg cipherAlg)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(Encoding.ASCII.GetBytes(TestData.plainString));
            cmsGenerator.addRecipients(config, TestData.recipientCert);
            cmsGenerator.addRecipients(config, TestData.recipientEcCert);

            byte[] encryptedCMS = cmsGenerator.generate();

            byte[] plainData = null;
            plainData = CMSEnvelopeTestUtil.DecryptWithMemory(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), encryptedCMS);
            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(plainData));

            plainData = CMSEnvelopeTestUtil.DecryptWithMemory(TestData.recipientEcCert, TestData.recipientEcKeyPair.getPrivate(), encryptedCMS);
            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(plainData));
        }

        private void tryKeyAgreementAndKeyTransWithStream(KeyAgreementAlg agreementAlg, CipherAlg cipherAlg)
        {
            String encryptedFileName = TestData.plainFile.DirectoryName + "\\Encrypted" + TestData.plainFile.Name;

            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            using (
                FileStream encryptedOutputStream = new FileStream(encryptedFileName, FileMode.Create),
                plainInputStream = new FileStream(TestData.plainFile.FullName, FileMode.Open, FileAccess.Read))
            {
                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(config, TestData.recipientCert);
                cmsGenerator.addRecipients(config, TestData.recipientEcCert);
                cmsGenerator.generate(encryptedOutputStream);

                encryptedOutputStream.Close();
                plainInputStream.Close();
            }
            String decryptedFileName = TestData.plainFile.DirectoryName + "\\Decrypted" + TestData.plainFile.Name;

            bool result;

            //DecryptWithStream to check
            CMSEnvelopeTestUtil.DecryptWithStream(TestData.recipientKeyPair.getPrivate(), TestData.recipientCert, encryptedFileName, decryptedFileName);
            result = CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
            Assert.True(result);

            //DecryptWithStream to check
            CMSEnvelopeTestUtil.DecryptWithStream(TestData.recipientEcKeyPair.getPrivate(), TestData.recipientEcCert, encryptedFileName, decryptedFileName);
            result = CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
            Assert.True(result);
        }

        private void tryAddingRecipientToParser(KeyAgreementAlg agreementAlg, CipherAlg cipherAlg)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            Pair<ECertificate, IPrivateKey> RSAEncEntries = CertificateTestConstants.GetRSAEncCertificateAndKey();
            Pair<ECertificate, IPrivateKey> ECEncEntries = CertificateTestConstants.GetECEncCertificateandKey();

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(Encoding.ASCII.GetBytes(TestData.plainString));
            cmsGenerator.addRecipients(config, ECEncEntries.first());
            byte[] encryptedCMS = cmsGenerator.generate();

            Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(ECEncEntries.first(), ECEncEntries.second());
            IDecryptorStore decryptor = new MemoryDecryptor(recipient);
            CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
            encryptedCMS = cmsParser.addRecipients(config, decryptor, RSAEncEntries.first());

            recipient = new Pair<ECertificate, IPrivateKey>(RSAEncEntries.first(), RSAEncEntries.second());
            decryptor = new MemoryDecryptor(recipient);
            cmsParser = new CmsEnvelopeParser(encryptedCMS);
            byte[] plainData = cmsParser.open(decryptor);

            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(plainData));
        }

        private void tryAddingRecipientToParserWithStream(KeyAgreementAlg agreementAlg, CipherAlg cipherAlg)
        {

            //Create new encrypted file

            String encryptedFileName = TestData.plainFile.DirectoryName + "\\Encrypted" + TestData.plainFile.Name;

            Pair<ECertificate, IPrivateKey> RSAEncEntries = CertificateTestConstants.GetRSAEncCertificateAndKey();
            Pair<ECertificate, IPrivateKey> ECEncEntries = CertificateTestConstants.GetECEncCertificateandKey();

            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            config.setEcKeyAgreementAlg(agreementAlg);
            TestData.configureCertificateValidation(config);

            using (
                FileStream encryptedOutputStream = new FileStream(encryptedFileName, FileMode.Create),
                    plainInputStream = new FileStream(TestData.plainFile.FullName, FileMode.Open, FileAccess.Read))
            {
                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(config, ECEncEntries.first());
                cmsGenerator.generate(encryptedOutputStream);

                encryptedOutputStream.Close();
                plainInputStream.Close();
            }

            String newRecAddEncFileName = TestData.plainFile.DirectoryName + "\\RecAddedEnc" + TestData.plainFile.Name;
            //Add new recipient to encrypted file
            using (
                FileStream encryptedInputStream = new FileStream(encryptedFileName, FileMode.Open),
                    newRecAddOutputStream = new FileStream(newRecAddEncFileName, FileMode.Create))
            {
                Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(ECEncEntries.first(), ECEncEntries.second());
                IDecryptorStore decryptor = new MemoryDecryptor(recipient);
                CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
                cmsParser.addRecipients(config, newRecAddOutputStream, decryptor, RSAEncEntries.first());

                newRecAddOutputStream.Close();
                encryptedInputStream.Close();

                //DecryptWithStream to check new recipient added correctly
                String decryptedFileName = TestData.plainFile.DirectoryName + "\\Decrypted" + TestData.plainFile.Name;

                bool result;

                CMSEnvelopeTestUtil.DecryptWithStream(ECEncEntries.second(), ECEncEntries.first(), newRecAddEncFileName, decryptedFileName);
                result = CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
                Assert.True(result);

                //DecryptWithStream to check
                CMSEnvelopeTestUtil.DecryptWithStream(RSAEncEntries.second(), RSAEncEntries.first(), newRecAddEncFileName, decryptedFileName);
                result = CMSEnvelopeTestUtil.CompareFiles(TestData.plainFile.FullName, decryptedFileName);
                Assert.True(result);
            }
        }

        private void tryKeyTransOAEPDefaultWithMemoryStream(KeyAgreementAlg agreementAlg, CipherAlg cipherAlg)
        {
            MemoryStream plainInputStream = new MemoryStream(Encoding.ASCII.GetBytes(TestData.plainString));
            MemoryStream encryptedOutputStream = new MemoryStream();

            CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
            cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG, TestData.recipientCert);
            cmsGenerator.generate(encryptedOutputStream);

            MemoryStream bais = new MemoryStream(encryptedOutputStream.ToArray());
            byte[] decryptedBytes = CMSEnvelopeTestUtil.DecryptWithStream(TestData.recipientKeyPair.getPrivate(), TestData.recipientCert, bais);

            Assert.AreEqual(Encoding.ASCII.GetBytes(TestData.plainString), decryptedBytes);
        }
    }
}
