

using System;
using NUnit.Framework;
using test.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature
{
    [TestFixture]
    public class AttachDetach : BaseTest
    {
        [Test]
        public void attachNewSignatureToExisting()
        {
            Context c = createContext();
            SignatureContainer sc = SignatureFactory.createContainer(signatureFormat, c);

            Signature s1 = sc.createSignature(settings.getSignersCertificate());
            s1.addContent(settings.getContent(), false);
            s1.sign(settings.getSigner());

            Signature s2 = sc.createSignature(settings.getSignersCertificate());
            s2.addContent(settings.getContent(), false);
            s2.sign(settings.getSigner());

            SignatureContainer existing = readSignatureContainer(FileNames.BES_ENVELOPING, c);
            existing.addExternalSignature(s1);
            existing.addExternalSignature(s2);

            write(existing, FileNames.BES_MULTIPLE_ATTACHED);
        }
        [Test]
        public void detachSignature()
        {
            SignatureContainer existing = readSignatureContainer(FileNames.BES_MULTIPLE_ATTACHED);
            // remove third signature!
            existing.getSignatures()[2].detachFromParent();
            write(existing, FileNames.BES_MULTIPLE_DETACHED);
        }
        [Test]
        public void detachCounterLeaf()
        {
            SignatureContainer existing = readSignatureContainer(FileNames.SERIAL_TO_SERIAL_BES);

            // first counter
            Signature cs = existing.getSignatures()[0].getCounterSignatures()[0];
            // counter of counter
            Signature cc = cs.getCounterSignatures()[0];

            cc.detachFromParent();
            write(existing, FileNames.BES_SERIAL_DETACHED_LEAF);
        }
        [Test]
        public void detachCounterMiddle()
        {
            SignatureContainer existing = readSignatureContainer(FileNames.SERIAL_TO_SERIAL_BES);

            // first counter
            Signature cs = existing.getSignatures()[0].getCounterSignatures()[0];

            cs.detachFromParent();
            write(existing, FileNames.BES_SERIAL_DETACHED_MIDDLE);
        }

        // attach to A
        // detach from A
        // detach serial
        // detach subserial
        [Test]
        public void validateAttached()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.BES_MULTIPLE_ATTACHED, createContext());
            Assert.AreEqual(3, sc.getSignatures().Count);
            ContainerValidationResult cvr = sc.verifyAll();
            Console.WriteLine(cvr);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }
        [Test]
        public void validateDetached()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.BES_MULTIPLE_DETACHED);
            Assert.AreEqual(2, sc.getSignatures().Count);
            ContainerValidationResult cvr = sc.verifyAll();
            Console.WriteLine(cvr);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }
        [Test]
        public void validateDetachedSerialLeaf()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.BES_SERIAL_DETACHED_LEAF);
            Assert.AreEqual(1, sc.getSignatures().Count);
            Assert.AreEqual(1, sc.getSignatures()[0].getCounterSignatures().Count);
            Assert.AreEqual(0, sc.getSignatures()[0].getCounterSignatures()[0].getCounterSignatures().Count);
            ContainerValidationResult cvr = sc.verifyAll();

            Console.WriteLine(cvr);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }
        [Test]
        public void validateDetachedSerialMiddle()
        {
            SignatureContainer sc = readSignatureContainer(FileNames.BES_SERIAL_DETACHED_MIDDLE);
            Assert.AreEqual(1, sc.getSignatures().Count);
            Assert.AreEqual(0, sc.getSignatures()[0].getCounterSignatures().Count);
            ContainerValidationResult cvr = sc.verifyAll();
            Console.WriteLine(cvr);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }
    }
}
