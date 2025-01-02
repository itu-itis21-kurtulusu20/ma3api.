package tr.gov.tubitak.uekae.esya.api.xades.example.sign;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.xades.example.XadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.xades.example.validation.XadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

import java.io.FileOutputStream;

/**
 * X2 type signature signing sample
 */
public class X2 extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "x2.xml";

    /**
     * Creates detached X2 type signature
     *
     * @throws Exception
     */
    @Test
    public void createX2() throws Exception {

        // create context with working directory
        Context context = createContext();

        // create signature according to context,
        // with default type (XADES_BES)
        XMLSignature signature = new XMLSignature(context);

        // add document as reference, but do not embed it
        // into the signature (embed=false)
        signature.addDocument("./sample.txt", "text/plain", false);

        signature.getSignedInfo().setSignatureMethod(SignatureMethod.RSA_SHA256);

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        signature.addKeyInfo(cert);

        // now sign it by using smart card
        signature.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

        // upgrade to X2
        signature.upgrade(SignatureType.ES_X_Type2);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);

    }
}
