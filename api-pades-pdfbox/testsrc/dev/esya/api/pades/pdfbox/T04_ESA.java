package dev.esya.api.pades.pdfbox;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.util.Calendar;

public class T04_ESA extends PAdESBaseTest {

    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
    private RSAPSSParams signatureAlgParams = null;


    @Test
    public void signEsa() throws Exception
    {
        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_A);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context);  // Zaman Damgası
        checkType(2, SignatureType.ES_BES, context);  // Zaman Damgası

        AsnIO.dosyayaz(signatureBytes.toByteArray(),"T:\\api-parent\\temp\\pdfbox\\esa-pdfBox.pdf");
    }

    @Test
    public void upgradeFromXLtoESA() throws Exception
    {
        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox.pdf");
        signatureContainer.read(fis);

        Signature signature = signatureContainer.getLatestSignerSignature();
        signature.upgrade(SignatureType.ES_A);

        signatureBytes.reset();
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context);  // Zaman Damgası
        checkType(2, SignatureType.ES_BES, context);  // Zaman Damgası

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\xlongToESAUpgrade-pdfbox.pdf");
    }

    @Test
    public void upgradeFromESAtoESA() throws Exception {

        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\esa-pdfBox.pdf");
        signatureContainer.read(fis);

        Signature signature = signatureContainer.getLatestSignerSignature();
        signature.upgrade(SignatureType.ES_A);

        signatureBytes.reset();
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context);  // Zaman Damgası
        checkType(2, SignatureType.ES_A, context);  // Zaman Damgası
        checkType(3, SignatureType.ES_BES, context);  // Zaman Damgası

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\esaToESA-pdfbox.pdf");
    }
}
