package dev.esya.api.pades.pdfbox.ec;

import dev.esya.api.pades.pdfbox.PAdESBaseTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T03_XLONG_ECSign extends PAdESBaseTest {

    // for second signatures
    private SignatureAlg rsaSignatureAlg = SignatureAlg.RSA_SHA256;

    private SignatureAlg ecSignatureAlg;
    private RSAPSSParams signatureAlgParams = null;

    public T03_XLONG_ECSign(SignatureAlg ecSignatureAlg) {
        this.ecSignatureAlg = ecSignatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    @Test
    public void T01_signXLong() throws Exception {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getECSignerInterface(ecSignatureAlg);
        ECertificate eCertificate = settings.getECSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_XL, context);
        checkType(1, SignatureType.ES_BES, context); // Zaman Damgası

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox-ec.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void T02_signXLongXLong() throws Exception {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\xlong-pdfBox-ec.pdf"));

        BaseSigner signer = settings.getSecondSignerInterface(rsaSignatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSecondSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_XL);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_A, context);
        checkType(1, SignatureType.ES_A, context); // Zaman Damgası
        checkType(2, SignatureType.ES_XL, context);
        checkType(3, SignatureType.ES_BES, context); // Zaman Damgası

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\xlong-xlong-pdfBox-ec.pdf", signatureBytes.toByteArray());
    }
}
