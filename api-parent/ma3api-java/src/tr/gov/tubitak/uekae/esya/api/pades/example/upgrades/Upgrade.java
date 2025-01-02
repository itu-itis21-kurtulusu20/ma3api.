package tr.gov.tubitak.uekae.esya.api.pades.example.upgrades;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.example.PadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class Upgrade extends PadesSampleBase {

    @Test
    public void createEST() throws Exception {
        // read
        PAdESContext context = createContext();
        context.setSignWithTimestamp(true);
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, new FileInputStream(getTestFile()), context);

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);

        // write to file
        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-est.pdf"));
    }

    @Test
    public void upgradeBESToEST() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(new FileInputStream(getTestDataFolder() + "signed-bes.pdf"), createContext());

        // upgrade signature
        Signature signature = signatureContainer.getSignatures().get(0);
        signature.upgrade(SignatureType.ES_T);

        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-bes-to-est.pdf"));
    }

    @Test
    public void validateEST() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-est.pdf"), createContext());

        ContainerValidationResult validationResult = signatureContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        signatureContainer.close();

        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void validateEST2() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-bes-to-est.pdf"), createContext());

        ContainerValidationResult validationResult = signatureContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        signatureContainer.close();

        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void createLT() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestFile()), createContext());

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.sign(signer);

        // add timestamp
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-lt.pdf"));
    }

    @Test
    public void createLTA() throws Exception {
        // read
        PAdESContext context = createContext();
        context.setSignWithTimestamp(true);
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestFile()), context);

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);

        // add timestamp
        signature.upgrade(SignatureType.ES_A);
        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-lta.pdf"));
    }

    @Test
    public void upgradeMiddle() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-seq.pdf"), createContext());

        // add signature
        Signature first = signatureContainer.getSignatures().get(0);

        // add timestamp
        Exception exception = null;
        try {
            first.upgrade(SignatureType.ES_A);
        } catch (Exception x) {
            exception = x;
        }

        // close the container
        signatureContainer.close();

        Assert.assertTrue("Cant upgrade signature if not last one", exception instanceof NotSupportedException);
    }

    @Test
    public void validateLT() throws Exception {
        // read
        Context context = createContext();
        context.getConfig().getCertificateValidationConfig().setUseValidationDataPublishedAfterCreation(false);
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-lt.pdf"), createContext());

        ContainerValidationResult validationResult = signatureContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        signatureContainer.close();

        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, validationResult.getResultType());
    }

    @Test
    public void validateLTA() throws Exception {
        // read
        Context context = createContext();
        context.getConfig().getCertificateValidationConfig().setUseValidationDataPublishedAfterCreation(false);
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-lta.pdf"), createContext());

        ContainerValidationResult validationResult = signatureContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        signatureContainer.close();

        Assert.assertEquals(validationResult.getResultType(), ContainerValidationResultType.ALL_VALID);
    }

    @Test
    public void createLTAA() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestFile()), createContext());

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.sign(signer);

        // add timestamp
        signature.upgrade(SignatureType.ES_A);

        // get last signature which is timestamp
        signature = signatureContainer.getSignatures().get(signatureContainer.getSignatures().size() - 1);
        signature.upgrade(SignatureType.ES_A);
        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-lta-a.pdf"));
    }

    @Test
    public void validateLTAA() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-lta-a.pdf"), createContext());

        ContainerValidationResult validationResult = signatureContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        signatureContainer.close();

        Assert.assertEquals(validationResult.getResultType(), ContainerValidationResultType.ALL_VALID);
    }

    @Test
    public void createLTInTwoSteps() throws Exception
    {
        PAdESContext context = createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(new FileInputStream(getTestFile()));

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        PAdESSignature signature = (PAdESSignature) signatureContainer.createSignature(eCertificate);

        byte [] dtbs = signature.initSign(SignatureAlg.RSA_SHA256, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signatureContainer.write(bos);
        bos.close();

        byte [] signatureValue = signer.sign(dtbs);

        byte[] unfinishedBytes = bos.toByteArray();
        finishSigning(unfinishedBytes, signatureValue);

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-ES_XL-inTwoSteps.pdf"), createContext());

        ContainerValidationResult validationResult = readContainer.verifyAll();
        readContainer.close();
        System.out.println(validationResult);
        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    private void finishSigning(byte[] unfinishedBytes, byte [] signature) throws Exception
    {
        FileUtil.writeBytes(getTestDataFolder() + "unfinished-1.pdf", unfinishedBytes);

        PAdESContext context = createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        ByteArrayInputStream bis = new ByteArrayInputStream(unfinishedBytes);
        signatureContainer.read(bis);
        bis.close();

        signatureContainer.finishSign(signature);
        signatureContainer.getSignatures().get(0).upgrade(SignatureType.ES_XL);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + "signed-ES_XL-inTwoSteps.pdf");
        signatureContainer.write(fileOutputStream);
    }
}
