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
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class T01_BES_ECSign extends PAdESBaseTest {

    // for second signatures
    private SignatureAlg rsaSignatureAlg = SignatureAlg.RSA_SHA256;

    private SignatureAlg ecSignatureAlg;
    private RSAPSSParams signatureAlgParams = null;

    public T01_BES_ECSign(SignatureAlg ecSignatureAlg) {
        this.ecSignatureAlg = ecSignatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    @Test
    public void T01_BESSignTest() throws Exception {
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
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\bes-ec-1.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void T02_AddBEStoBESTest() throws Exception {
        FileInputStream fis = new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-ec-1.pdf");

        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(fis);

        // ikinci imza için de EC kullanılacak
     // BaseSigner signer = settings.getSecondSignerInterface(rsaSignatureAlg, signatureAlgParams);
     // ECertificate eCertificate = settings.getSecondSignerCertificate();
        BaseSigner signer = settings.getECSignerInterface(ecSignatureAlg);
        ECertificate eCertificate = settings.getECSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);
        checkType(1, SignatureType.ES_BES, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\bes-ec-2.pdf", signatureBytes.toByteArray());
    }

    @Test
    public void T03_Two_BESSignTest() throws Exception {

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

        signatureContainer.write(signatureBytes);

        context = testSettings.createContext();
        signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);
        signatureContainer.read(new ByteArrayInputStream(signatureBytes.toByteArray()));

        BaseSigner signer2 = settings.getSecondSignerInterface(rsaSignatureAlg, signatureAlgParams);
        ECertificate certificate2 = settings.getSecondSignerCertificate();
        Signature signature2 = signatureContainer.createSignature(certificate2);
        signature2.setSigningTime(Calendar.getInstance());
        signature2.sign(signer2);

        signatureBytes.reset();
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_BES, context);
        checkType(1, SignatureType.ES_BES, context);

        FileUtil.writeBytes("T:\\api-parent\\temp\\pdfbox\\bes-ec-3.pdf", signatureBytes.toByteArray());
    }
}
