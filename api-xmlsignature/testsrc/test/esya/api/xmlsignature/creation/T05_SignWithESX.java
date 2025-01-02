package test.esya.api.xmlsignature.creation;

import org.junit.Test;
import org.w3c.dom.Document;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 22.11.2012
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public class T05_SignWithESX extends XMLSignatureTestBase {

    @Test
    public void testCreateEnveloping() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE,true);
        signature.addKeyInfo(getSignerCertificate());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        signature.upgrade(SignatureType.ES_X_Type1);

        signature.write(signatureBytes);
        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateEnveloped()  throws Exception
    {
        Document envelopeDoc = createSimpleEnvelope();

        Context context =  createContext();
        context.setDocument(envelopeDoc);
        XMLSignature signature = new XMLSignature(context, false);
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());
        signature.addDocument("#data1","text/xml",false);
        signature.addKeyInfo(getSignerCertificate());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        signature.upgrade(SignatureType.ES_X_Type1);

        Source source = new DOMSource(envelopeDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.transform(source, new StreamResult(signatureBytes));

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateDetached()  throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE,false);
        signature.addKeyInfo(getSignerCertificate());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        signature.upgrade(SignatureType.ES_X_Type1);

        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }
}
