using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.profile;

namespace test.esya.api.signature
{
    [TestFixture]
    public class TurkishESigProfile : BaseTest
    {
        [Test]
        public void createP1()
        {
            SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());
            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), false);
            signature.setSigningTime(DateTime.UtcNow);

            signature.sign(settings.getSigner());
            write(container, FileNames.TURKISH_ESIG_P1);
        }

        [Test]
        public void createP2()
        {
            SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());
            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), false);
            signature.setSigningTime(DateTime.UtcNow);
            signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P2v1);

            signature.sign(settings.getSigner());
            signature.upgrade(SignatureType.ES_T);
            write(container, FileNames.TURKISH_ESIG_P2);
        }

        [Test]
        public void createP3()
        {

            Context context = createContext();
            context.getConfig().setCertificateValidationPolicies(config.getCRLOnlyPolicies());

            SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), context);
            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), false);
            signature.setSigningTime(DateTime.UtcNow);
            signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P3v1);

            signature.sign(settings.getSigner());
            signature.upgrade(SignatureType.ES_XL);
            write(container, FileNames.TURKISH_ESIG_P3);
        }

        [Test]
        public void createP4()
        {
            Context context = createContext();
            context.getConfig().setCertificateValidationPolicies(config.getOCSPOnlyPolicies());

            SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), context);
            Signature signature = container.createSignature(settings.getSignersCertificate());

            signature.addContent(settings.getContent(), false);
            signature.setSigningTime(DateTime.UtcNow);
            signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P4v1);

            signature.sign(settings.getSigner());
            signature.upgrade(SignatureType.ES_XL);
            write(container, FileNames.TURKISH_ESIG_P4);
        }

        [Test]
        public void createP4WithA()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.TURKISH_ESIG_P4);
            sc.getSignatures()[0].upgrade(SignatureType.ES_A);
            write(sc, FileNames.TURKISH_ESIG_A);
        }

        [Test]
        public void createP4WithAWithA()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.TURKISH_ESIG_A, createContext());
            sc.getSignatures()[0].addArchiveTimestamp();
            write(sc, FileNames.TURKISH_ESIG_AA);
        }

        [Test]
        public void validateP1()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P1);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateP2()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P2);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateP3()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P3);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateP4()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P4);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateP4A()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_A);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateP4AA()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_AA);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

    }
}
