

using NUnit.Framework;
using test.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature
{
    [TestFixture]
    public class Multiple : BaseTest
    {
        [Test]
        public void createParallelToExisting()
        {
            Context c = createContext();
            SignatureContainer sc = readSignatureContainer(FileNames.BES_ENVELOPING, c);
            Signature s = sc.createSignature(settings.getSignersCertificate());
            s.addContent(settings.getContent(), true);
            s.sign(settings.getSigner());
            write(sc, FileNames.PARALLEL_BES);
        }

        [Test]
        public void createParallelToExistingDetached()
        {
            Context c = createContext();
            c.setData(null);
            SignatureContainer sc = readSignatureContainer(FileNames.BES_DETACHED, c);
            Signature s = sc.createSignature(settings.getSignersCertificate());
            s.addContent(settings.getContent(), false);
            s.sign(settings.getSigner());
            write(sc, FileNames.PARALLEL_BES_DETACHED);
        }

        [Test]
        public void createSerialToExisting()
        {
            Context c = createContext();
            SignatureContainer sc = readSignatureContainer(FileNames.BES_ENVELOPING, c);
            Signature cs = sc.getSignatures()[0].createCounterSignature(settings.getSignersCertificate());
            cs.sign(settings.getSigner());
            write(sc, FileNames.SERIAL_BES);
        }

        [Test]
        public void createSerialToSerial()
        {
            Context c = createContext();
            SignatureContainer sc = readSignatureContainer(FileNames.SERIAL_BES, c);
            Signature s = sc.getSignatures()[0];
            Signature counter = s.getCounterSignatures()[0];
            Signature counterOfCounter = counter.createCounterSignature(settings.getSignersCertificate());
            counterOfCounter.sign(settings.getSigner());
            write(sc, FileNames.SERIAL_TO_SERIAL_BES);

        }

        [Test]
        public void validateSerial()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.SERIAL_BES);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateParallel()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.PARALLEL_BES);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateParallelDetached()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.PARALLEL_BES_DETACHED);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

        [Test]
        public void validateSerialToSerial()
        {
            ContainerValidationResult cvr = validateSignature(FileNames.SERIAL_TO_SERIAL_BES);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }

    }
}
