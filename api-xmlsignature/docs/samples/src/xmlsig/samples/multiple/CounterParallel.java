package xmlsig.samples.multiple;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignedDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;

/**
 * Counter signature to a parallel signature sample
 * @author: suleyman.uslu
 */
public class CounterParallel extends SampleBase {

    public static final String SIGNATURE_FILENAME = "counter_parallel.xml";

    /**
     * Adds counter signature to a parallel detached one
     * @throws Exception
     */
    @Test
    public void signCounterParallel() throws  Exception{
        Context context = new Context(BASE_DIR);

        // read previously created signature
        Document signatureFile = Resolver.resolve(ParallelDetached.SIGNATURE_FILENAME, context);
        SignedDocument signedDocument = new SignedDocument(signatureFile, context);

        // get first signature
        XMLSignature signature = signedDocument.getSignature(0);

        // create counter signature to the first one
        XMLSignature counterSignature = signature.createCounterSignature();

        // sign
        counterSignature.addKeyInfo(CERTIFICATE);
        counterSignature.sign(PRIVATE_KEY);

        // signed doc contains both previous signature and now a counter signature
        // in first signature
        signedDocument.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
