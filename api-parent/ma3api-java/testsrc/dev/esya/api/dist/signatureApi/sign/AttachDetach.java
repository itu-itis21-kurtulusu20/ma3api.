package dev.esya.api.dist.signatureApi.sign;

import dev.esya.api.dist.signatureApi.SampleBase;
import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

public class AttachDetach extends SampleBase {

    @Test
    public void attachNewSignatureToExisting() throws Exception {

        Context c = createContext();
        SignatureContainer sc = SignatureFactory.createContainer(signatureFormat, c);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature s1 = sc.createSignature(cert);
        s1.addContent(getContent(), false);
        s1.sign(signer);

        Signature s2 = sc.createSignature(cert);
        s2.addContent(getContent(), false);
        s2.sign(signer);
        SmartCardManager.getInstance().logout();
        SignatureContainer existing = readSignatureContainer("bes_enveloping", c);
        existing.addExternalSignature(s1);
        existing.addExternalSignature(s2);

        dosyaYaz(existing, "bes_multiple_attached");
    }

    @Test
    public void detachSignature() throws Exception {
        SignatureContainer existing = readSignatureContainer("bes_multiple_attached");
        // remove third signature!
        existing.getSignatures().get(2).detachFromParent();
        dosyaYaz(existing, "bes_multiple_detached");
    }

    @Test
    public void detachCounterLeaf() throws Exception {
        SignatureContainer existing = readSignatureContainer("serial_to_serial_bes");

        // first counter
        Signature cs = existing.getSignatures().get(0).getCounterSignatures().get(0);
        // counter of counter
        Signature cc = cs.getCounterSignatures().get(0);

        cc.detachFromParent();
        dosyaYaz(existing, "bes_serial_detached_leaf");
    }

    @Test
    public void detachCounterMiddle() throws Exception {
        SignatureContainer existing = readSignatureContainer("serial_to_serial_bes");

        // first counter
        Signature cs = existing.getSignatures().get(0).getCounterSignatures().get(0);

        cs.detachFromParent();
        dosyaYaz(existing, "bes_serial_detached_middle");
    }

    // attach to A
    // detach from A
    // detach serial
    // detach subserial

    @Test
    public void validateAttached() throws Exception {
        SignatureContainer sc = readSignatureContainer("bes_multiple_attached", createContext());
        Assert.assertEquals(3, sc.getSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetached() throws Exception {
        SignatureContainer sc = readSignatureContainer("bes_multiple_detached");
        Assert.assertEquals(2, sc.getSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetachedSerialLeaf() throws Exception {
        SignatureContainer sc = readSignatureContainer("bes_serial_detached_leaf");
        Assert.assertEquals(1, sc.getSignatures().size());
        Assert.assertEquals(1, sc.getSignatures().get(0).getCounterSignatures().size());
        Assert.assertEquals(0, sc.getSignatures().get(0).getCounterSignatures().get(0).getCounterSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateDetachedSerialMiddle() throws Exception {
        SignatureContainer sc = readSignatureContainer("bes_serial_detached_middle");
        Assert.assertEquals(1, sc.getSignatures().size());
        Assert.assertEquals(0, sc.getSignatures().get(0).getCounterSignatures().size());
        ContainerValidationResult cvr = sc.verifyAll();
        System.out.println(cvr);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }
}
