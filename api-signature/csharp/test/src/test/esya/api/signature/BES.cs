
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature
{
    [TestFixture]
    public class BES : BaseTest
    {
        [Test]
        public void createDetached()
        {
            SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());

            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), false);

            signature.sign(settings.getSigner());

            write(container, FileNames.BES_DETACHED);
        }
        [Test]
        public void createEnveloping()
        {
            SignatureContainer container = SignatureFactory.createContainer(config.getFormat());

            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), true);

            signature.sign(settings.getSigner());

            write(container, FileNames.BES_ENVELOPING);
        }
        [Test]
        public void validateDetached()
        {
            Context c = createContext();
            ContainerValidationResult cvr = validateSignature("bes_detached", c);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
            Assert.AreEqual(1, cvr.getSignatureValidationResults().Count);
        }
        [Test]
        public void validateEnveloping()
        {
            ContainerValidationResult cvr = validateSignature("bes_enveloping");
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
            Assert.AreEqual(1, cvr.getSignatureValidationResults().Count);
        }

    }
}
