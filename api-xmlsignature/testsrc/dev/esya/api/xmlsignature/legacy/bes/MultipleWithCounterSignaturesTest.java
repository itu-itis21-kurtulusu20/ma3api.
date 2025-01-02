package dev.esya.api.xmlsignature.legacy.bes;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.security.PrivateKey;


/**
 * @author ahmety
 * date: Sep 2, 2009
 */
public class MultipleWithCounterSignaturesTest extends XMLBaseTest
{

    public void testEnvelopedMultipleSignaturesTest() throws Exception
    {
        // read certificates and keys
        PrivateKey pk1 = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));
        PrivateKey pk2 = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private2.key"));

        ECertificate certificate1 = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika1.cer");
        ECertificate certificate2 = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika2.cer");


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
        s1.addKeyInfo(certificate1);
        s1.addDocument("#"+embeddedId, null, false);
        s1.sign(pk1);

        // create another signature, signing an http resource
        XMLSignature s2 = doc.createSignature();
        s2.setSignatureMethod(SignatureMethod.RSA_SHA256);
        s2.addKeyInfo(certificate1);
        s2.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
        s2.sign(pk1);

        // create signature 3, signing embedded xml
        XMLSignature s3 = doc.createSignature();
        s3.setSignatureMethod(SignatureMethod.RSA_SHA256);
        s3.addKeyInfo(certificate2);
        s3.addDocument("#"+embeddedId2, null, false);
        s3.sign(pk2);


        // create counter signatures for signature #2, #3
        XMLSignature counter1 = s2.createCounterSignature();
        XMLSignature counter2 = s3.createCounterSignature();

        counter1.addKeyInfo(certificate2);
        counter2.addKeyInfo(certificate2);

        counter1.sign(pk2);
        counter2.sign(pk2);

        // output final document
        File sigFile = new File(BASE_MA3 +"XAdES/BES/enveloped-multiple-counter-signatures.xml");
        doc.write(new FileOutputStream(sigFile));

        // now read it back
        Context context2 = new Context();
        context2.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        SignedDocument doc2 = new SignedDocument(new FileDocument(sigFile, "text/xml"), context2);

        // and verify :)
        ValidationResult coreResult = doc2.verify();
        assertTrue(coreResult.getType()==ValidationResultType.VALID);
        // validate countersignatures
        for (int i=0; i<doc2.getRootSignatureCount();i++){
            XMLSignature signature = doc2.getSignature(i);
            List<XMLSignature> counters = signature.getAllCounterSignatures();
            for (XMLSignature counterSignature : counters) {
                ValidationResult counterResult = counterSignature.verify();
                assertTrue(counterResult.getType() == ValidationResultType.VALID);
            }
        }
    }
}
