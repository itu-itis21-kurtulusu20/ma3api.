package test.esya.api.xmlsignature.creation.ec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by sura.emanet on 31.12.2019.
 */
@RunWith(Parameterized.class)
public class T01_SignWithBES_EC extends XMLSignatureTestBase {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T01_SignWithBES_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Test
    public void testCreateEnveloping() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getECSignerInterface(signatureAlg));
        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid( BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateEnveloped() throws Exception
    {
        Document envelopeDoc = createSimpleEnvelope();
        Context context =  createContext();
        context.setDocument(envelopeDoc);

        XMLSignature signature = new XMLSignature(context, false);
        envelopeDoc.getDocumentElement().appendChild(signature.getElement());
        signature.addDocument("#data1","text/xml",false);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getECSignerInterface(signatureAlg));

        Source source = new DOMSource(envelopeDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, new StreamResult(signatureBytes));

        XMLValidationUtil.checkSignatureIsValid( BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateDetached() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME,PLAINFILEMIMETYPE,false);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getECSignerInterface(signatureAlg));

        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void createCounterSignature() throws Exception {

        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);

        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getECSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getECSignerInterface(signatureAlg));

        // create counter signatures for signature
        XMLSignature counter = signature.createCounterSignature();
        counter.addKeyInfo(getSecondSignerCertificate());
        counter.sign(getSecondSignerInterface(SignatureAlg.RSA_SHA256));

        // output final document
        signature.write(signatureBytes);
        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }
}
