package test.esya.api.signature;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tr.gov.tubitak.uekae.esya.api.signature.*;

/**
 * @author ayetgin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AttachDetach extends BaseTest
{
    @Test
    public void attachNewSignatureToExisting() throws Exception
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

    @Test
    public void detachSignature() throws Exception {
        SignatureContainer existing = readSignatureContainer(FileNames.BES_MULTIPLE_ATTACHED);
        // remove third signature!
        existing.getSignatures().get(2).detachFromParent();
        write(existing, FileNames.BES_MULTIPLE_DETACHED);
    }

    @Test
    public void detachCounterLeaf() throws Exception {
        SignatureContainer existing = readSignatureContainer(FileNames.SERIAL_TO_SERIAL_BES);

        // first counter
        Signature cs = existing.getSignatures().get(0).getCounterSignatures().get(0);
        // counter of counter
        Signature cc = cs.getCounterSignatures().get(0);

        cc.detachFromParent();
        write(existing, FileNames.BES_SERIAL_DETACHED_LEAF);
    }

    @Test
    public void detachCounterMiddle() throws Exception {
        SignatureContainer existing = readSignatureContainer(FileNames.SERIAL_TO_SERIAL_BES);

        // first counter
        Signature cs = existing.getSignatures().get(0).getCounterSignatures().get(0);

        cs.detachFromParent();
        write(existing, FileNames.BES_SERIAL_DETACHED_MIDDLE);
    }

    // attach to A
    // detach from A
    // detach serial
    // detach subserial

    @Test
    public void validateAttached() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.BES_MULTIPLE_ATTACHED, createContext());
        Assert.assertEquals(3, sc.getSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetached() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.BES_MULTIPLE_DETACHED);
        Assert.assertEquals(2, sc.getSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetachedSerialLeaf() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.BES_SERIAL_DETACHED_LEAF);
        Assert.assertEquals(1, sc.getSignatures().size());
        Assert.assertEquals(1, sc.getSignatures().get(0).getCounterSignatures().size());
        Assert.assertEquals(0, sc.getSignatures().get(0).getCounterSignatures().get(0).getCounterSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetachedSerialMiddle() throws Exception {
        SignatureContainer sc = readSignatureContainer(FileNames.BES_SERIAL_DETACHED_MIDDLE);
        Assert.assertEquals(1, sc.getSignatures().size());
        Assert.assertEquals(0, sc.getSignatures().get(0).getCounterSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }
}
