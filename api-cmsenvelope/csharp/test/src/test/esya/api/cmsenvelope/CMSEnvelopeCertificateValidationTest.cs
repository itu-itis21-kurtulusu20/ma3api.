using System;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace test.esya.api.cmsenvelope.integration
{
    [TestFixture]
    public class CMSEnvelopeCertificateValidationTest
    {
        static String valid_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
        static String valid_pfx_pass = "231291";

        static String sign_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
        static String sign_pfx_pass = "231291";

        static String revoked_bydate_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
        static String revoked_bydate_pfx_pass = "231291";

        static PfxParser validPfx;
        static PfxParser signPfx;
        static PfxParser revokedPfx;

        [Test]
        public void testEncryptionWithCertificateValidationValid()
        {
            validPfx = new PfxParser(valid_pfx_path, valid_pfx_pass);
            ECertificate cert = validPfx.getCertificate();
            PrivateKey privKey = validPfx.getPrivateKey();

            testEncryptionWithCertificateValidation(cert, privKey);

        }


        public void testEncryptionWithCertificateValidation(ECertificate cert, PrivateKey privKey)
        {
            EnvelopeConfig config = new EnvelopeConfig();
            config.setPolicy(TestData.EncryptionPolicyFile);

            CmsEnvelopeGenerator envelopeGenerator = new CmsEnvelopeGenerator(ASCIIEncoding.ASCII.GetBytes(TestData.plainString));
            envelopeGenerator.addRecipients(config, cert);
            byte [] envelopedData = envelopeGenerator.generate();

            Pair<ECertificate, IPrivateKey> recipient = new Pair<ECertificate, IPrivateKey>(cert, privKey);
            MemoryDecryptor decryptor = new MemoryDecryptor(recipient);
            CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(envelopedData);
            byte[] decryptedDataBytes = cmsParser.open(decryptor);

            Assert.AreEqual(ASCIIEncoding.ASCII.GetBytes(TestData.plainString), decryptedDataBytes);
        }


    }
}
