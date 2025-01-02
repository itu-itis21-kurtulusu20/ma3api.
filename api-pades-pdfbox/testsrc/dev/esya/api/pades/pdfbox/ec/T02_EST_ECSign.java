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
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

@RunWith(Parameterized.class)
public class T02_EST_ECSign extends PAdESBaseTest {

    private SignatureAlg ecSignatureAlg;

    public T02_EST_ECSign(SignatureAlg ecSignatureAlg) {
        this.ecSignatureAlg = ecSignatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    @Test
    public void CAdESEmbeddedESTSignTest() throws Exception {

        PAdESContext context = testSettings.createContext();
        context.setSignWithTimestamp(true);

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getECSignerInterface(ecSignatureAlg);
        ECertificate eCertificate = settings.getECSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);
        checkType(0, SignatureType.ES_T, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\est-ec-1.pdf", signatureBytes.toByteArray());
    }


    @Test
    public void ESTSignTest() throws Exception {
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
        signature.upgrade(SignatureType.ES_T);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_T, context);
        checkType(1, SignatureType.ES_BES, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\est-ec-2.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void BEStoESTUpgrade() throws Exception {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-ec-1.pdf"));

        Signature signature = signatureContainer.getSignatures().get(0);
        signature.upgrade(SignatureType.ES_T);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_T, context);
        checkType(1, SignatureType.ES_BES, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\est-ec-3.pdf", signatureBytes.toByteArray());
    }
}
