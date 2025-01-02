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
 * Parallel enveloped signature sample
 * @author: suleyman.uslu
 */
public class ParallelEnveloped extends SampleBase {

    public static final String SIGNATURE_FILENAME = "parallel_enveloped.xml";

    /**
     * Creates two signatures in a document, that signs same inner data
     * @throws Exception
     */
    @Test
    public void createParallelEnveloped() throws Exception
    {
        Context context = new Context(BASE_DIR);

        SignedDocument signatures = new SignedDocument(context);

        Document doc = Resolver.resolve("./sample.txt", context);
        String fragment = signatures.addDocument(doc);

        XMLSignature signature1 = signatures.createSignature();

        // add document as inner reference
        signature1.addDocument("#"+fragment, "text/plain", false);

        // add certificate to show who signed the document
        signature1.addKeyInfo(CERTIFICATE);

        // now sign it by using private key
        signature1.sign(PRIVATE_KEY);

        XMLSignature signature2 = signatures.createSignature();

        // add document as inner reference
        signature2.addDocument("#"+fragment, "text/plain", false);

        // add certificate to show who signed the document
        signature2.addKeyInfo(CERTIFICATE);

        // now sign it by using private key
        signature2.sign(PRIVATE_KEY);

        // write combined document
        signatures.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
