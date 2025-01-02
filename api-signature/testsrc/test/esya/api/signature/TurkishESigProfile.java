package test.esya.api.signature;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.profile.TurkishESigProfiles;

import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * @author ayetgin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TurkishESigProfile extends BaseTest
{
    @Test
    public void createP1() throws Exception
    {
        SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());
        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), false);
        signature.setSigningTime(Calendar.getInstance());

        signature.sign(settings.getSigner());
        write(container, FileNames.TURKISH_ESIG_P1);
    }

    @Test
    public void createP2() throws Exception
    {
        SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());
        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P2v1);

        signature.sign(settings.getSigner());
        signature.upgrade(SignatureType.ES_T);
        write(container, FileNames.TURKISH_ESIG_P2);
    }

    @Test
    public void createP3() throws Exception
    {

        Context context = createContext();
        context.getConfig().setCertificateValidationPolicies(config.getCRLOnlyPolicies());

        SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), context);
        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P3v1);

        signature.sign(settings.getSigner());
        signature.upgrade(SignatureType.ES_XL);
        write(container, FileNames.TURKISH_ESIG_P3);
    }

    @Test
    public void createP4() throws Exception
    {
        Context context = createContext();
        context.getConfig().setCertificateValidationPolicies(config.getOCSPOnlyPolicies());

        SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), context);
        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), false);
        signature.setSigningTime(Calendar.getInstance());
        signature.setSignaturePolicy(TurkishESigProfiles.SIG_POLICY_ID_P4v1);

        signature.sign(settings.getSigner());
        signature.upgrade(SignatureType.ES_XL);
        write(container, FileNames.TURKISH_ESIG_P4);
    }

    @Test
    public void createP4WithA() throws Exception
    {
        SignatureContainer sc = readSignatureContainer(FileNames.TURKISH_ESIG_P4);
        sc.getSignatures().get(0).upgrade(SignatureType.ES_A);
        write(sc, FileNames.TURKISH_ESIG_A);
    }

    @Test
    public void createP4WithAWithA() throws Exception
    {
        SignatureContainer sc = readSignatureContainer(FileNames.TURKISH_ESIG_A, createContext());
        sc.getSignatures().get(0).addArchiveTimestamp();
        write(sc, FileNames.TURKISH_ESIG_AA);
    }

    @Test
    public void validateP1() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P1);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP2() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P2);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP3() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P3);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_P4);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4A() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_A);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateP4AA() throws Exception
    {
        ContainerValidationResult cvr = validateSignature(FileNames.TURKISH_ESIG_AA);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

}
