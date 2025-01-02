package dev.esya.api.dist.signatureApi.sign;

import dev.esya.api.dist.signatureApi.validation.Validation;
import org.junit.Assert;
import org.junit.Test;
import dev.esya.api.dist.signatureApi.SampleBase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

public class Multiple extends SampleBase {

    @Test
    public void createParallelToExisting() throws Exception {

        Context c = createContext();
        SignatureContainer sc = readSignatureContainer("bes_enveloping", c);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature s = sc.createSignature(cert);
        s.addContent(getContent(), true);
        s.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(sc, "parallel_bes");
    }

    @Test
    public void createParallelToExistingDetached() throws Exception {
        Context c = createContext();
        c.setData(null);
        SignatureContainer sc = readSignatureContainer("bes_detached", c);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature s = sc.createSignature(cert);
        s.addContent(getContent(), false);
        s.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(sc, "parallel_bes_detached");
    }

    @Test
    public void createSerialToExisting() throws Exception {
        Context c = createContext();
        SignatureContainer sc = readSignatureContainer("bes_enveloping", c);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature cs = sc.getSignatures().get(0).createCounterSignature(cert);
        cs.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(sc, "serial_bes");
    }

    @Test
    public void createSerialToSerial() throws Exception {
        Context c = createContext();
        SignatureContainer sc = readSignatureContainer("serial_bes", c);
        Signature s = sc.getSignatures().get(0);
        Signature counter = s.getCounterSignatures().get(0);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature counterOfCounter = counter.createCounterSignature(cert);
        counterOfCounter.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(sc, "serial_to_serial_bes");

    }

    @Test
    public void validateSerial() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("serial_bes");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateParallel() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("parallel_bes");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateParallelDetached() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("parallel_bes_detached");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateSerialToSerial() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("serial_to_serial_bes");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

}
