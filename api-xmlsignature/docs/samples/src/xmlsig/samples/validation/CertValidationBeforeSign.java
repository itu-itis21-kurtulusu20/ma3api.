package xmlsig.samples.validation;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.SampleBase;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Sample for signing a document only if certificate is valid
 * @author suleyman.uslu
 *
 */
public class CertValidationBeforeSign extends SampleBase {

    public static final String SIGNATURE_FILENAME = "validate_before_sign.xml";

    /**
     * Creates BES with only valid certificates
     * @throws Exception
     */
    @Test
    public void createBESWithCertificateCheck() throws Exception {

        try {
            // check validity of signing certificate
            boolean valid = CertValidation.validateCertificate(CERTIFICATE);

            if(valid) {
                // create context with working directory
                Context context = new Context(BASE_DIR);

                // create signature according to context,
                // with default type (XADES_BES)
                XMLSignature signature = new XMLSignature(context);

                // add document as reference, but do not embed it
                // into the signature (embed=false)
                signature.addDocument("./sample.txt", "text/plain", false);

                // add certificate to show who signed the document
                signature.addKeyInfo(CERTIFICATE);

                // now sign it by using private key
                signature.sign(PRIVATE_KEY);

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
