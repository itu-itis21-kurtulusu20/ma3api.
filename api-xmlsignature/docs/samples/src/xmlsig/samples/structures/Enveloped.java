package xmlsig.samples.structures;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;

/**
 * Enveloped BES sample
 */
@RunWith(JUnit4.class)
public class Enveloped extends SampleBase
{
    public static final String SIGNATURE_FILENAME = "enveloped.xml";

    /**
     * Create enveloped BES
     * @throws Exception
     */
    @Test
    public void createEnveloped() throws Exception
    {
        // here is our custom envelope xml
        Document envelopeDoc = newEnvelope();


        // create context with working directory
        Context context = new Context(BASE_DIR);

        // define where signature belongs to
        context.setDocument(envelopeDoc);


        // create signature according to context,
        // with default type (XADES_BES)
        XMLSignature signature = new XMLSignature(context, false);

        // attach signature to envelope
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());

        // add document as reference,
        signature.addDocument("#data1", "text/xml", false);

        // add certificate to show who signed the document
        signature.addKeyInfo(CERTIFICATE);

        // now sign it by using private key
        signature.sign(PRIVATE_KEY);


        // this time we do not use signature.write because we need to write
        // whole document instead of signature
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
