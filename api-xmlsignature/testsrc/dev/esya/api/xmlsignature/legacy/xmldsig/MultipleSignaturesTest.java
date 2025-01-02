package dev.esya.api.xmlsignature.legacy.xmldsig;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ahmety
 * date: Sep 2, 2009
 */
public class MultipleSignaturesTest extends XMLBaseTest
{

    public void testEnvelopedMultipleSignaturesTest() throws Exception
    {
        // test purposed key generation
        KeyPair kp = KeyPairGenerator.getInstance("RSA").genKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        PublicKey publicKey = kp.getPublic();

        File toBeSigned = new File(BASELOC+"ma3\\include\\SampleData.xml");

        // create context to be shared via signatures
        Context context = new Context();
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        // create document that will contain multiple signatures
        SignedDocument doc = new SignedDocument(context);

        // embed a document
        String embeddedId = doc.addDocument(new FileDocument(toBeSigned, "text/xml"));

        // embed a xml
        String embeddedId2 = doc.addXMLDocument(new FileDocument(toBeSigned, "text/xml"));

        // create signature 1, signing embedded doc
        XMLSignature s1 = doc.createSignature();
        s1.setSignatureMethod(SignatureMethod.RSA_SHA256);
        s1.addKeyInfo(publicKey);
        s1.addDocument("#"+embeddedId, null, false);
        s1.sign(privateKey);

        // create signature 2, signing embedded xml
        XMLSignature s3 = doc.createSignature();
        s3.setSignatureMethod(SignatureMethod.RSA_SHA256);
        s3.addKeyInfo(publicKey);
        s3.addDocument("#"+embeddedId2, null, false);
        s3.sign(privateKey);

        // create another signature, signing an http resource
        XMLSignature s2 = doc.createSignature();
        s2.setSignatureMethod(SignatureMethod.RSA_SHA256);
        s2.addKeyInfo(publicKey);
        s2.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
        s2.sign(privateKey);

        // output final document
        File sigFile = new File(BASE_MA3 +"signatureTypes/enveloped-multiple-signatures.xml");
        doc.write(new FileOutputStream(sigFile));

        // now read it back
        Context context2 = new Context();
        context2.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        SignedDocument doc2 = new SignedDocument(new FileDocument(sigFile, "text/xml"), context2);

        // and verify :)
        ValidationResult vr = doc2.verify();
        assertTrue(vr.getType()==ValidationResultType.VALID);
    }
}
