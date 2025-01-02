package tr.gov.tubitak.uekae.esya.api.xades.example.sign;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.xades.example.XadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.xades.example.validation.XadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * XL type signature signing sample
 */
public class XL extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "xl.xml";

    /**
     * Creates detached XL type signature
     *
     * @throws Exception
     */
    @Test
    public void createXL() throws Exception {

        // create context with working directory
        Context context = createContext();

        // create signature according to context,
        // with default type (XADES_BES)
        XMLSignature signature = new XMLSignature(context);

        // add document as reference, but do not embed it
        // into the signature (embed=false)
        signature.addDocument("./sample.txt", "text/plain", false);

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        signature.addKeyInfo(cert);

        // now sign it by using smart card
        signature.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

        // upgrade to XL
        signature.upgrade(SignatureType.ES_XL);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);

    }

    @Test
    public void createXLInTwoSteps() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument("./sample.txt", "text/plain", false);

        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        signature.addKeyInfo(cert);

        byte[] dtbs = signature.initAddingSignature(SignatureAlg.RSA_SHA256, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signature.write(bos);
        bos.close();

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

        byte[] signatureValue = signer.sign(dtbs);
        finishSigning(bos.toByteArray(), context, signatureValue);

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);
    }

    private void finishSigning(byte[] bsdBytes, Context context, byte [] signatureValue) throws Exception
    {
        InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
        XMLSignature signature = XMLSignature.parse(xmlDocument, context);

        signature.finishAddingSignature(signatureValue);
        signature.upgrade(SignatureType.ES_XL);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();
    }
}
