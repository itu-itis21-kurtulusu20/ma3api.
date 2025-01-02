package xmlsig.samples.structures;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>Create sample enveloping signature
 *
 * <p>Enveloping means signed data is keep "in" the signature.
 *
 * <p>this is usually done by BASE64 encoding the data to be signed.
 *
 * @author ayetgin
 */
@RunWith(JUnit4.class)
public class Enveloping extends SampleBase
{
    public static final String SIGNATURE_FILENAME = "enveloping.xml";

    @Test
    public void createEnvelopingBes() throws Exception
    {
        try {
            // create context with working directory
            Context context = new Context(BASE_DIR);

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document as reference, and keep BASE64 version of data
            // in an <Object tag, in a way that reference points to
            // that <Object
            // (embed=true)
            signature.addDocument("./sample.txt", "text/plain", true);

            // add certificate to show who signed the document
            signature.addKeyInfo(CERTIFICATE);

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
