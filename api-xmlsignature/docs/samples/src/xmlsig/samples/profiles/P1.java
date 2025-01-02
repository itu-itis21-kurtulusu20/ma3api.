package xmlsig.samples.profiles;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Profile 1 sample
 * @author: suleyman.uslu
 */
public class P1 extends SampleBase {

    public static final String SIGNATURE_FILENAME = "p1.xml";

    /**
     * Creates signature according to the profile 1 specifications
     * @throws Exception
     */
    @Test
    public void createP1() throws Exception
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

            // now sign it by using private key
            signature.sign(PRIVATE_KEY);

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
