package dev.esya.api.pades.pdfbox.ec;

import dev.esya.api.pades.pdfbox.PAdESBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

@RunWith(Parameterized.class)
public class T04_ESA_ECSign extends PAdESBaseTest {

    private SignatureAlg ecSignatureAlg;

    public T04_ESA_ECSign(SignatureAlg ecSignatureAlg) {
        this.ecSignatureAlg = ecSignatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    @Test
    public void signEsa() throws Exception {
        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getECSignerInterface(ecSignatureAlg);
        ECertificate eCertificate = settings.getECSignerCertificate();

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

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\esa-pdfBox-ec.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void upgradeFromXLtoESA() throws Exception {
        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox-ec.pdf");
        signatureContainer.read(fis);

        Signature signature = signatureContainer.getLatestSignerSignature();
        signature.upgrade(SignatureType.ES_A);

        signatureBytes.reset();
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context);  // Zaman Damgası
        checkType(2, SignatureType.ES_BES, context);  // Zaman Damgası

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\xlongToESAUpgrade-pdfbox-ec.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void upgradeFromESAtoESA() throws Exception {

        PAdESContext context = testSettings.createContext();
        PAdESContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\esa-pdfBox-ec.pdf");
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

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\esaToESA-pdfbox-ec.pdf", signatureBytes.toByteArray());
    }
}
