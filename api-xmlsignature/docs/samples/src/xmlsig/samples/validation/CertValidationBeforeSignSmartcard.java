package xmlsig.samples.validation;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.JSmartCardManager;
import xmlsig.samples.utils.SampleBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Sample for signing a document with smart card only if certificate is valid
 * @author suleyman.uslu
 *
 */
public class CertValidationBeforeSignSmartcard extends SampleBase {

    public static final String SIGNATURE_FILENAME = "validate_before_sign_smartcard.xml";

    /**
     * Creates BES using smart card with only valid certificates
     * @throws Exception
     */
    @Test
    public void createBESWithCertificateCheckSmartcard() throws Exception {

        try {

            // false-true gets non-qualified certificates while true-false gets qualified ones
            X509Certificate cert = JSmartCardManager.getInstance().getSignatureCertificate(true);
            ECertificate certificate = new ECertificate(cert.getEncoded());

            // check validity of signing certificate
            boolean valid = CertValidation.validateCertificate(certificate);

            if(valid) {
                // create context with working directory
                Context context = new Context(BASE_DIR);

                // create signature according to context,
                // with default type (XADES_BES)
                XMLSignature signature = new XMLSignature(context);

                // add document as reference, but do not embed it
                // into the signature (embed=false)
                signature.addDocument("./sample.txt", "text/plain", false);

                signature.getSignedInfo().setSignatureMethod(SignatureMethod.RSA_SHA256);

                // add certificate to show who signed the document
                signature.addKeyInfo(certificate);

                // now sign it by using smart card
                signature.sign(JSmartCardManager.getInstance().getSigner(PIN, cert));

                signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
            }
            else {
                throw new ESYAException("Certificate " + CERTIFICATE.toString() + " is not a valid certificate!");
            }
        }
        catch (XMLSignatureException x){
            // cannot create signature
            x.printStackTrace();
        }
        catch (IOException x){
            // probably couldn't write to the file
            x.printStackTrace();
        }
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
