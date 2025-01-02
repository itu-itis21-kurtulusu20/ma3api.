package xmlsig.samples.structures;

import org.junit.Test;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transform;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;

/**
 * Enveloped transform BES sample
 * @author: suleyman.uslu
 */
public class EnvelopedTransform extends SampleBase {

    public static final String SIGNATURE_FILENAME = "enveloped_transform.xml";

    /**
     * Create enveloped transform BES
     * @throws Exception
     */
    @Test
    public void createEnvelopedTransform() throws Exception
    {
        Document envelopeDoc = newEnvelope();

        Context context = new Context(BASE_DIR);
        context.setDocument(envelopeDoc);

        XMLSignature signature = new XMLSignature(context, false);

        // attach signature to envelope
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());

        Transforms transforms = new Transforms(context);
        transforms.addTransform(new Transform(context, TransformType.ENVELOPED.getUrl()));

        // add whole document(="") with envelope transform, with SHA256
        // and don't include it into signature(false)
        signature.addDocument("", "text/xml", transforms, DigestMethod.SHA_256, false);

        // add certificate to show who signed the document
        signature.addKeyInfo(CERTIFICATE);

        // now sign it by using private key
        signature.sign(PRIVATE_KEY);


        Source source = new DOMSource(envelopeDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        // write to file
        transformer.transform(source, new StreamResult(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME)));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
