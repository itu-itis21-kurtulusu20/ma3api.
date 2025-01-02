package test.esya.api.xmlsignature.creation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import test.esya.api.xmlsignature.XMLSignatureTestBase;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;


/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 22.11.2012
 * Time: 09:49
 * To change this template use File | Settings | File Templates.
 */

@RunWith(Parameterized.class)
public class T01_SignWithBES extends XMLSignatureTestBase {


    private SignatureAlg signatureAlg;
    private RSAPSSParams rsapssParams;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                                            {SignatureAlg.RSA_SHA1,   null},
                                            {SignatureAlg.RSA_SHA256, null},
                                            {SignatureAlg.RSA_SHA384, null},
                                            {SignatureAlg.RSA_SHA512, null},

                                            {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1)},
                                            {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256)},
                                            {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384)},
                                            {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512)}
        });
    }

    public T01_SignWithBES(SignatureAlg signatureAlg, RSAPSSParams rsapssParams) {
        this.signatureAlg = signatureAlg;
        this.rsapssParams = rsapssParams;
    }

    @Test
    public void testCreateEnveloping() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(signatureAlg,rsapssParams));
        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid( BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void testCreateEnveloping_TwoSteps() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());

        byte[] dtbs = signature.initAddingSignature(signatureAlg, rsapssParams);

        signature.write(signatureBytes);

        BaseSigner signer = getSignerInterface(signatureAlg, rsapssParams);

        byte[] signatureValue = signer.sign(dtbs);
        finishSigning(signatureBytes.toByteArray(), context, signatureValue);

        XMLValidationUtil.checkSignatureIsValid( null, signatureBytes.toByteArray());
    }

    private void finishSigning(byte[] bsdBytes, Context context, byte [] signatureValue) throws Exception
    {
        InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
        XMLSignature signature = XMLSignature.parse(xmlDocument, context);

        signature.finishAddingSignature(signatureValue);

        signatureBytes.reset();
        signature.write(signatureBytes);
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
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(signatureAlg,rsapssParams));

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
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(signatureAlg,rsapssParams));

        signature.write(signatureBytes);

        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }

    @Test
    public void createCounterSignature() throws Exception {

        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);

        signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
        signature.addKeyInfo(getSignerCertificate());
        signature.setSigningTime(Calendar.getInstance());
        signature.sign(getSignerInterface(signatureAlg, rsapssParams));

        // create counter signatures for signature
        XMLSignature counter = signature.createCounterSignature();
        counter.addKeyInfo(getSecondSignerCertificate());
        counter.sign(getSecondSignerInterface(signatureAlg, rsapssParams));

        // output final document
        signature.write(signatureBytes);
        XMLValidationUtil.checkSignatureIsValid(BASEDIR, signatureBytes.toByteArray());
    }
}
