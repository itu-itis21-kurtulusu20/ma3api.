package dev.esya.api.dist.signatureApi.sign;

import dev.esya.api.dist.signatureApi.SampleBase;
import dev.esya.api.dist.signatureApi.validation.Validation;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

import static org.junit.Assert.assertEquals;

public class Bes extends SampleBase {

    @Test
    public void createDetached() throws Exception {
        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, createContext());

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);

        signature.addContent(getContent(), false);

        signature.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "bes_detached");
    }

    @Test
    public void createEnveloping() throws Exception {

        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, createContext());
        //container.getContext().setConfig(new Config("./config/esya-signature-config.xml"));
        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);

        signature.addContent(getContent(), true);

        signature.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "bes_enveloping");
    }

    @Test
    public void validateDetached() throws Exception {

        Context c = createContext();
        ContainerValidationResult cvr = Validation.validateSignature("bes_detached", c);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        assertEquals(1, cvr.getSignatureValidationResults().size());
    }

    @Test
    public void validateEnveloping() throws Exception {

        ContainerValidationResult cvr = Validation.validateSignature("bes_enveloping");
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        assertEquals(1, cvr.getSignatureValidationResults().size());
    }

}
