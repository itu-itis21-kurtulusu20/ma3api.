package dev.esya.api.pades.pdfbox;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.util.Calendar;

public class T02_ESTSign extends PAdESBaseTest {

    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA1;
    private RSAPSSParams signatureAlgParams = null;



    @Test
    public void CAdESEmbeddedESTSignTest() throws Exception{

        PAdESContext context = testSettings.createContext();
        context.setSignWithTimestamp(true);

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_T, context);



        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\est-1.pdf");
    }


    @Test
    public void ESTSignTest() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(testSettings.getPdfFile());

        BaseSigner signer = settings.getSignerInterface(signatureAlg, signatureAlgParams);
        ECertificate eCertificate = settings.getSignerCertificate();

        // add signature
        Signature signature = signatureContainer.createSignature(eCertificate);
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(signer);
        signature.upgrade(SignatureType.ES_T);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_T, context);
        checkType(1, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\est-2.pdf");
    }

    @Test
    public void BEStoESTUpgrade() throws Exception
    {
        PAdESContext context = testSettings.createContext();

        SignatureContainer signatureContainer = new PAdESContainer();
        signatureContainer.setContext(context);

        signatureContainer.read(new FileInputStream("T:\\api-parent\\temp\\pdfbox\\bes-1.pdf"));

        Signature signature = signatureContainer.getSignatures().get(0);
        signature.upgrade(SignatureType.ES_T);
        signatureContainer.write(signatureBytes);

        checkIsValid(context);

        checkType(0, SignatureType.ES_T, context);
        checkType(1, SignatureType.ES_BES, context);

        AsnIO.dosyayaz(signatureBytes.toByteArray(), "T:\\api-parent\\temp\\pdfbox\\est-3.pdf");
    }


}
