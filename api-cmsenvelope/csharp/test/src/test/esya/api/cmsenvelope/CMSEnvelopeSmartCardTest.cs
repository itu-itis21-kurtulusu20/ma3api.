using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;


namespace test.esya.api.cmsenvelope
{

    [TestFixture]
    class CMSEnvelopeSmartCardTest
    {
        public static readonly String PIN = "12345";
     
        static SmartCard sc = null;
        static CardType cardType = CardType.AKIS;
        static long sessionID = 0;
        static long slotNo = 0;

        private static readonly String plainDataFileName = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\test.txt";
        private FileInfo plainFile = new FileInfo(plainDataFileName);

        [SetUp]
        public static void initSmartcard()
        {
            sc = new SmartCard(cardType);
            slotNo = sc.getSlotList()[0];
            sessionID = sc.openSession(slotNo);
            sc.login(sessionID, PIN);
        }

        [TearDown]
        public static void destroySmartcard() 
        {
            sc.logout(sessionID);
            sc.closeSession(sessionID);
        }

        [Test]
        public void testKeyTransOAEPSHA1AES128()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP, CipherAlg.AES128_CBC);
        }

        [Test]
        public void testKeyTransOAEPSHA1AES192()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP, CipherAlg.AES192_CBC);
        }

        [Test]
        public void testKeyTransOAEPSHA1AES256()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP, CipherAlg.AES256_CBC);
        }

        [Test]
        public void testKeyTransOAEPSHA256AES128()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES128_CBC);
        }

        [Test]
        public void testKeyTransOAEPSHA256AES192()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES192_CBC);
        }

        [Test]
        public void testKeyTransOAEPSHA256AES256()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_CBC);
        }

        [Test]
        public void testKeyTransPKCS1AES128()
        {
            tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES128_CBC);
        }

        [Test]
        public void testKeyTransPKCS1AES192()
        {
            tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES192_CBC);
        }

        [Test]
        public void testKeyTransPKCS1AES256()
        {
            tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES256_CBC);
        }

        [Test]
        [ExpectedException(typeof(CMSException))]
        public void test3DESAlgorithmForKeyTrans()
        {
            tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
        }

        [Test]
        [ExpectedException(typeof(CMSException))]
        public void testRC2AlgorithmForKeyTrans()
        {
           tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
        }

        [Test]
        [ExpectedException(typeof(CMSException))]
        public void testInvalidModeForKeyTrans()
        {       
            tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);      
        }

        [Test]
        public void testAddingNewRecipientsToEnvelopeWithOAEPSHA1()
        {
            addNewRecipientsToEnvelope(CipherAlg.RSA_OAEP);
        }

        [Test]
        public void testAddingNewRecipientsToEnvelopeWithOAEPSHA256()
        {
            addNewRecipientsToEnvelope(CipherAlg.RSA_OAEP_SHA256);
        }

        [Test]
        public void testKeyTransOAEPDefaultWithMemoryStream()
        {       
            List<byte[]> certs = sc.getEncryptionCertificates(sessionID);
            ECertificate cert = new ECertificate(certs[0]);

            MemoryStream plainInputStream = new MemoryStream(Encoding.ASCII.GetBytes(TestData.plainString));
            MemoryStream encryptedOutputStream = new MemoryStream();

            CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
            cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG, cert);
            cmsGenerator.generate(encryptedOutputStream);

            encryptedOutputStream.Close();
            plainInputStream.Close();

            MemoryStream EncryptedInputStream = new MemoryStream(encryptedOutputStream.ToArray());
            MemoryStream decryptedOutputStream = new MemoryStream();
            CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(EncryptedInputStream);
            IDecryptorStore decryptor = new SCDecryptor(sc, sessionID);
            cmsParser.open(decryptedOutputStream, decryptor);

            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(decryptedOutputStream.ToArray()));
        }

        [Test]
        public void testKeyTransOAEPDefaultWithFileStream()
        {

            List<byte[]> certs = sc.getEncryptionCertificates(sessionID);
            ECertificate cert = new ECertificate(certs[0]);

            String encryptedFileName = plainFile.DirectoryName + "\\Encrypted" + plainFile.Name;

            using (FileStream encryptedOutputStream = new FileStream(encryptedFileName, FileMode.Create),
                plainInputStream = new FileStream(plainFile.FullName, FileMode.Open, FileAccess.Read))
            {

                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG, cert);
                cmsGenerator.generate(encryptedOutputStream);

                encryptedOutputStream.Close();
                plainInputStream.Close();
            }
            String decryptedFileName = plainFile.DirectoryName + "\\Decrypted" + plainFile.Name;

            using (
                FileStream EncryptedInputStream = new FileStream(encryptedFileName, FileMode.Open),
                    DecryptedOutputStream = new FileStream(decryptedFileName, FileMode.Create))
            {

                CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(EncryptedInputStream);
                IDecryptorStore decryptor = new SCDecryptor(sc, sessionID);
                cmsParser.open(DecryptedOutputStream, decryptor);
            }
        }

        private void tryKeyTrans(CipherAlg cipherAlg, CipherAlg symmetricAlgorithm)
        {
           
            List<byte[]> cert = sc.getEncryptionCertificates(sessionID);
            ECertificate cer = new ECertificate(cert[0]);

            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            TestData.configureCertificateValidation(config);

            CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(Encoding.ASCII.GetBytes(TestData.plainString), symmetricAlgorithm);
            cmsGenerator.addRecipients(config, cer);
            byte[] encryptedCMS = cmsGenerator.generate();

            CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
            IDecryptorStore decryptor = new SCDecryptor(sc, sessionID);
            byte[] decryptedBytes = cmsParser.open(decryptor);

            Assert.AreEqual(TestData.plainString, Encoding.ASCII.GetString(decryptedBytes));
            
        }

        private void addNewRecipientsToEnvelope(CipherAlg cipherAlg)
        {
            
            List<byte[]> certs = sc.getEncryptionCertificates(sessionID);
            ECertificate cert = new ECertificate(certs[0]);

            EnvelopeConfig config = new EnvelopeConfig();
            config.setRsaKeyTransAlg(cipherAlg);
            TestData.configureCertificateValidation(config);

            //Add first recipient
            using (
                Stream plainInputStream = new MemoryStream(Encoding.ASCII.GetBytes(TestData.plainString)),
                    envelopeWithOneRecipient = new MemoryStream())
            {
                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(config, cert);
                cmsGenerator.generate(envelopeWithOneRecipient);
                //CMS Envelope with one recipient is generated.

                //Add a new recipient to envelope
                using (
                    MemoryStream bais = new MemoryStream(((MemoryStream)envelopeWithOneRecipient).ToArray()),
                        envelopeWithTwoRecipient = new MemoryStream())
                {

                    CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(bais);
                    IDecryptorStore decryptor = new SCDecryptor(sc, sessionID);
                    cmsParser.addRecipients(config, envelopeWithTwoRecipient, decryptor, cert);
                }
            }
        }
    }
}
