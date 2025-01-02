package dev.esya.api.dist.signatureApi.profiles;

import dev.esya.api.dist.signatureApi.validation.Validation;
import org.junit.Assert;
import org.junit.Test;
import dev.esya.api.dist.signatureApi.SampleBase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.profile.TurkishESigProfiles;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

import java.util.Calendar;

public class TurkishESignatureProfile extends SampleBase {

    @Test
    public void createP1() throws Exception {
        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, createContext());

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);
        signature.addContent(getContent(), false);
        signature.setSigningTime(Calendar.getInstance());

        signature.sign(signer);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "turkish_profile_p1_bes");
    }

    @Test
    public void createP2() throws Exception {
        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, createContext());

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);
        signature.addContent(getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P2v1);

        signature.sign(signer);
        signature.upgrade(SignatureType.ES_T);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "turkish_profile_p2_t");
    }

    @Test
    public void createP3() throws Exception {

        Context context = createContext();
        context.getConfig().setCertificateValidationPolicies(getCRLOnlyPolicy());

        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, context);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);
        signature.addContent(getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P3v1);

        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "turkish_profile_p3_xl_crl");
    }

    @Test
    public void createP4() throws Exception {
        Context context = createContext();
        context.getConfig().setCertificateValidationPolicies(getOCSPOnlyPolicy());

        SignatureContainer container = SignatureFactory.createContainer(signatureFormat, context);

        boolean checkQCStatement = getCheckQCStatement();
        //Get qualified or non-qualified certificate.
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(checkQCStatement);
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPIN(), cert);

        Signature signature = container.createSignature(cert);
        signature.addContent(getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P4v1);

        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        SmartCardManager.getInstance().logout();
        dosyaYaz(container, "turkish_profile_p4_xl_ocsp");
    }

    @Test
    public void createP4WithA() throws Exception {
        SignatureContainer sc = readSignatureContainer("turkish_profile_p4_xl_ocsp");
        sc.getSignatures().get(0).upgrade(SignatureType.ES_A);
        dosyaYaz(sc, "turkish_profile_p4_a");
    }

    @Test
    public void createP4WithAWithA() throws Exception {
        SignatureContainer sc = readSignatureContainer("turkish_profile_p4_a", createContext());
        sc.getSignatures().get(0).addArchiveTimestamp();
        dosyaYaz(sc, "turkish_profile_p4_aa");
    }

    @Test
    public void validateP1() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p1_bes");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP2() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p2_t");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP3() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p3_xl_crl");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p4_xl_ocsp");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4A() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p4_a");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4AA() throws Exception {
        ContainerValidationResult cvr = Validation.validateSignature("turkish_profile_p4_aa");
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

}
