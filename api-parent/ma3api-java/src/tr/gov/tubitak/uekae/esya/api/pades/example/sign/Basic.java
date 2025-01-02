package tr.gov.tubitak.uekae.esya.api.pades.example.sign;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.pades.example.PadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class Basic extends PadesSampleBase {

    @Test
    public void signBES() throws Exception {
        // read
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestFile()), createContext());

        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-bes.pdf"));

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-bes.pdf"), createContext());

        ContainerValidationResult validationResult = readContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        readContainer.close();

        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void validateSignedPdf() throws Exception {

        SignatureContainer sc = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-bes.pdf"), createContext());

        ContainerValidationResult validationResult = sc.verifyAll();
        System.out.println(validationResult);

        // close the container
        sc.close();

        assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
    }

    @Test
    public void readWriteTest() throws Exception {

        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-bes.pdf"), createContext());

        signatureContainer.write(new FileOutputStream(getTestDataFolder() + "signed-rewrite.pdf"));
    }

    @Test
    public void signBesInTwoSteps() throws Exception
    {
        PAdESContext context = createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(new FileInputStream(getTestFile()));

        //Get qualified or non-qualified certificate.
        ECertificate eCertificate = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), eCertificate);

        // add signature
        PAdESSignature signature = (PAdESSignature) signatureContainer.createSignature(eCertificate);

        byte [] dtbs = signature.initSign(SignatureAlg.fromName(signer.getSignatureAlgorithmStr()), signer.getAlgorithmParameterSpec());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signatureContainer.write(bos);
        bos.close();

        // sign dtbs (Data to be signed)
        byte [] signatureValue = signer.sign(dtbs);

        byte[] unfinishedBytes = bos.toByteArray();

        // add signature to unfinishedBytes
        finishSigning(unfinishedBytes, signatureValue);

        // read and validate
        SignatureContainer readContainer = SignatureFactory.readContainer(SignatureFormat.PAdES,
                new FileInputStream(getTestDataFolder() + "signed-bes-inTwoSteps.pdf"), createContext());

        ContainerValidationResult validationResult = readContainer.verifyAll();
        System.out.println(validationResult);

        // close the container
        readContainer.close();

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

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + "signed-bes-inTwoSteps.pdf");
        signatureContainer.write(fileOutputStream);
    }
}
