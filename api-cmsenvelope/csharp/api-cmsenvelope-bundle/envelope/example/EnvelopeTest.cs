using System;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace tr.gov.tubitak.uekae.esya.api.envelope.example
{
    [TestFixture]
    public class EnvelopeTest
    {
        [SetUp]
        public static void loadLicense()
        {
            TestConstants.setLicense();
        }

        [Test]
        public void testToEncrypt()
        {
            using (Stream plainInputStream = new MemoryStream(ASCIIEncoding.ASCII.GetBytes("test")), encryptedOutputStream = new MemoryStream())
            {
                //MemoryStream encryptedOutputStream = new MemoryStream();
                ECertificate cert = TestConstants.getReceiverCert();

                EnvelopeConfig config = new EnvelopeConfig();
                config.setPolicy(TestConstants.GetPolicyFile());

                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream); 
                cmsGenerator.addRecipients(config,new ECertificate[] { cert });
                cmsGenerator.generate(encryptedOutputStream);

                using (MemoryStream bais = new MemoryStream(((MemoryStream)encryptedOutputStream).ToArray()))
                {
                    Assert.AreEqual("test", ASCIIEncoding.ASCII.GetString(testToDecrypt(bais)));
                }
            }
        }

        [Test]
        public void testAddingNewRecipientsToEnvelope()
        {
            //Add first recipient
            using (Stream plainInputStream = new MemoryStream(ASCIIEncoding.ASCII.GetBytes("test")), envelopeWithOneRecipient = new MemoryStream())
            {
                //MemoryStream envelopeWithOneRecipient = new MemoryStream();
                ECertificate cert = TestConstants.getReceiverCert();

                EnvelopeConfig config = new EnvelopeConfig();
                config.setPolicy(TestConstants.GetPolicyFile());

                CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
                cmsGenerator.addRecipients(config,new ECertificate[] { cert });         
                cmsGenerator.generate(envelopeWithOneRecipient);
                //CMS Envelope with one recipient is generated.

                //Add a new recipient to envelope
                using (MemoryStream bais = new MemoryStream(((MemoryStream)envelopeWithOneRecipient).ToArray()), envelopeWithTwoRecipient = new MemoryStream())
                {
                    //MemoryStream envelopeWithTwoRecipient = new MemoryStream();

                    SmartCard sc = new SmartCard(CardType.AKIS);
                    long slot = sc.getSlotList()[0];
                    long session = sc.openSession(slot);
                    sc.login(session, TestConstants.getPIN());

                    CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(bais);
                    IDecryptorStore decryptor = new SCDecryptor(sc, session);
                    cmsParser.addRecipients(config,envelopeWithTwoRecipient, decryptor, cert);

                    sc.logout(session);
                }
            }
        }

        [Test]
        public void testGettingCertificateFromLDAP()
        {
            ECertificate originalCert = TestConstants.getReceiverCert();
            ECertificate adcert = LDAPUtil.readEncCertificatesFromDirectory(TestConstants.getEMailAddressForLDAP())[0];
            Assert.AreEqual(originalCert.getBytes(), adcert.getBytes());
        }

        [Test]
        public void testSmartCardRandomSeed()
        {
            //SmartCard'ın seed olarak atanması uygulama yaşam döngüsünde bir kere yapılmalıdır!!!
            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            SmartCardSeed scSeed = new SmartCardSeed(CardType.AKIS, slot);
            Crypto.getRandomGenerator().removeAllSeeders();
            Crypto.getRandomGenerator().addSeeder(scSeed);


            //Test
            byte[] randomBytes = new byte[256];
            Crypto.getRandomGenerator().nextBytes(randomBytes);

            Console.WriteLine("RandomBytes:" + StringUtil.ToHexString(randomBytes) );
        }


        private byte[] testToDecrypt(Stream encryptedInputStream)
        {
            using (MemoryStream decryptedOutputStream = new MemoryStream())
            {
                SmartCard sc = new SmartCard(CardType.AKIS);
                long slot = sc.getSlotList()[0];
                long session = sc.openSession(slot);
                sc.login(session, TestConstants.getPIN());

                CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
                IDecryptorStore decryptor = new SCDecryptor(sc, session);
                cmsParser.open(decryptedOutputStream, decryptor);

                sc.logout(session);

                return decryptedOutputStream.ToArray();
            }
        }

    }
}
