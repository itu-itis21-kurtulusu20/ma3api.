package xmlsig.samples.multiple;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import xmlsig.samples.structures.Detached;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;

/**
 * Counter signature sample
 * @author: suleyman.uslu
 */
public class CounterDetached extends SampleBase {

    public static final String SIGNATURE_FILENAME = "counter_detached.xml";

    /**
     * Adds counter signature to a detached one
     * @throws Exception
     */
    @Test
    public void signCounterDetached() throws  Exception{
        Context context = new Context(BASE_DIR);

        // read previously created signature, you need to run Detached.java first
        Document doc = Resolver.resolve(Detached.SIGNATURE_FILENAME, context);
        XMLSignature signature = XMLSignature.parse(doc, context);

        // create counter signature
        XMLSignature counterSignature = signature.createCounterSignature();

        // sign
        counterSignature.addKeyInfo(CERTIFICATE);
        counterSignature.sign(PRIVATE_KEY);

        // signature contains itself and counter signature
        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
