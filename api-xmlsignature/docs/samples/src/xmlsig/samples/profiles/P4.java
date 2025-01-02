package xmlsig.samples.profiles;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Profile 4 sample
 * @author: suleyman.uslu
 */
public class P4 extends SampleBase {

    public static final String SIGNATURE_FILENAME = "p4.xml";

    /**
     * Creates signature according to the profile 4 specifications. Revocation data
     * must be OCSP in this profile.
     * @throws Exception
     */
    @Test
    public void createP4() throws Exception
    {
        try {
            // create context with working directory
            Context context = new Context(BASE_DIR);

            // add resolver to resolve policies
            context.addExternalResolver(POLICY_RESOLVER);

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document as reference, but do not embed it
            // into the signature (embed=false)
            signature.addDocument("./sample.txt", "text/plain", false);

            // add certificate to show who signed the document
            signature.addKeyInfo(CERTIFICATE);

            // set time now
            signature.setSigningTime(Calendar.getInstance());

            // set policy info defined and required by profile
            signature.setPolicyIdentifier(OID_POLICY_P4,
                    "Uzun Dönemli ve ÇİSDuP Kontrollü Güvenli Elektronik İmza Politikası",
                    "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
            );

            // now sign it by using private key
            signature.sign(PRIVATE_KEY);

            // upgrade to XL
            signature.upgrade(SignatureType.ES_XL);

            signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
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
