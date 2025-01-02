package xmlsig.samples.attributes;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Adds SigningTime attribute to BES
 * @author: suleyman.uslu
 */
public class SigningTimeAttribute extends SampleBase {

    public static final String SIGNATURE_FILENAME = "signing_time.xml";

    private static XMLGregorianCalendar getTime(){
        Calendar cal = new GregorianCalendar();
        cal.get(Calendar.SECOND);
        return XmlUtil.createDate(cal);
    }

    /**
     * Creates BES with SigningTime attribute
     * @throws Exception
     */
    @Test
    public void createBESWithSigningTime() throws Exception
    {
        try {
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

            // add signing time
            signature.getQualifyingProperties().getSignedSignatureProperties().setSigningTime(getTime());

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
