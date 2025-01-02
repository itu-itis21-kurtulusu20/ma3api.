using System;
using System.IO;
using System.Linq;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.envelope.example
{
    [TestFixture]
    public class EnvelopeExample
    {
        [SetUp]
        public static void loadLicense()
        {
            TestConstants.setLicense();
        }

        [Test]
        public void encryptWithLDAP()
        {
            int size = 50;
            byte[] data = new byte[size];
            Random rand = new Random();
            rand.NextBytes(data);

            Console.WriteLine("Plain Data:\t" + BitConverter.ToString(data));

            //Get certificate
            ECertificate[] certs = LDAPUtil.readEncCertificatesFromDirectory(TestConstants.getEMailAddressForLDAP());

            if (certs == null || certs.Length == 0)
                throw new Exception("Certificate Can not find");

            ECertificate cert = certs[0];

            Stream plainInputStream = new MemoryStream(data);
            MemoryStream encryptedOutputStream = new MemoryStream();

            EnvelopeConfig config = new EnvelopeConfig();
            config.setPolicy(TestConstants.GetPolicyFile());

            CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
            cmsGenerator.addRecipients(config, new ECertificate[] { cert });
            cmsGenerator.generate(encryptedOutputStream);

            MemoryStream bais = new MemoryStream(encryptedOutputStream.ToArray());


            byte[] decrypted = _decrypt(bais);
            Console.WriteLine("Decypted Data:\t" + BitConverter.ToString(decrypted));

            if (data.SequenceEqual(decrypted) == false)
            {
                Console.WriteLine(false);
                throw new Exception("Not equal");
            }
        }

        private static byte[] _decrypt(MemoryStream encryptedInputStream)
        {
            MemoryStream decryptedOutputStream = new MemoryStream();

            SmartCard sc = new SmartCard(CardType.AKIS);
            long slot = sc.getSlotList()[0];
            long session = sc.openSession(slot);
            sc.login(session, TestConstants.getPIN());

            CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
            IDecryptorStore decryptor = new SCDecryptor(sc, session);
            cmsParser.open(decryptedOutputStream, decryptor);

            sc.logout(session);
            sc.closeSession(session);

            return decryptedOutputStream.ToArray();
        }
    }
}
