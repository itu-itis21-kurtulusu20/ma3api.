package test.esya.api.xmlsignature.conversion;

import org.junit.Test;
import org.w3c.dom.Document;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.Calendar;

public class T01_BES  extends XMLSignatureTestBase {

    private final String DIRECTORY = "T:\\api-xmlsignature\\test-output\\java\\ma3\\conversion\\";
    private String PLAINFILENAME = "../../../../docs/samples/signatures/sample.txt";

    //T:/api-xmlsignature/test-output/java/ma3/conversion/../../../../docs/samples/signatures/sample.txt

    @Test
    public void testCreateEnveloping() throws Exception
    {
        String FILE_PATH = DIRECTORY + "BES_Enveloping.xml";

        Context context = createContext(DIRECTORY);
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        signature.write(fos);

        XMLValidationUtil.checkSignatureIsValid( DIRECTORY, FILE_PATH);
    }

    @Test
    public void testCreateEnveloped() throws Exception
    {
        String FILE_PATH = DIRECTORY + "BES_Enveloped.xml";

        Document envelopeDoc = createSimpleEnvelope();
        Context context =  createContext(DIRECTORY);
        context.setDocument(envelopeDoc);

        XMLSignature signature = new XMLSignature(context, false);
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());
        signature.addDocument("#data1","text/xml",false);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        FileOutputStream fos = new FileOutputStream(FILE_PATH);

        Source source = new DOMSource(envelopeDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, new StreamResult(fos));

        XMLValidationUtil.checkSignatureIsValid( DIRECTORY, FILE_PATH);
    }


    @Test
    public void testCreateDetached() throws Exception
    {
        String FILE_PATH = DIRECTORY + "BES_Detached.xml";

        Context context = createContext(DIRECTORY);
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME,PLAINFILEMIMETYPE,false);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        signature.write(fos);

        XMLValidationUtil.checkSignatureIsValid(DIRECTORY, FILE_PATH);
    }

}
