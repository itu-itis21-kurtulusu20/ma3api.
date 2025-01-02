package dev.esya.api.xmlsignature.legacy.bes;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;

import java.security.PrivateKey;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author ahmety
 * date: Sep 10, 2009
 */
public class CounterSignatureTest extends XMLBaseTest
{
    public void testBESCounterSignatureSignature() throws Exception
    {
        // test purposed key generation

        PrivateKey pk1 = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private1.key"));
        PrivateKey pk2 = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, AsnIO.dosyadanOKU(BASE_MA3_INCLUDE+"private2.key"));

        ECertificate sert1 = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika1.cer");
        ECertificate sert2 = ECertificate.readFromFile(BASE_MA3_INCLUDE+"Sertifika2.cer");


        // create context to be shared via signatures
        Context context = new Context();
        context.setValidateCertificates(false);
        context.addExternalResolver(XMLBaseTest.OFFLINE_RESOLVER);

        // create document that will contain multiple signatures
        XMLSignature signature = new XMLSignature(context);


        // create another signature, signing an http resource
        signature.setSignatureMethod(SignatureMethod.RSA_SHA256);
        signature.addKeyInfo(sert1);
        signature.addDocument("http://www.w3.org/TR/xml-stylesheet", null, false);
        signature.sign(pk1);

        // create counter signatures for signature #2, #3
        XMLSignature counter = signature.createCounterSignature();
        counter.addKeyInfo(sert2);
        counter.sign(pk2);

        // output final document
        File sigFile = new File(BASE_MA3 +"XAdES/BES/counter-signature.xml");
        signature.write(new FileOutputStream(sigFile));

        // now read it back
        Context context2 = new Context();
        context2.setValidateCertificates(false);
        context2.addExternalResolver(OFFLINE_RESOLVER);
        XMLSignature signatureRead = XMLSignature.parse(new FileDocument(sigFile, "text/xml"), context2);

        // and verify :)
        ValidationResult core = signatureRead.verify();
        assertTrue("Signature core validation ", core.getType()== ValidationResultType.VALID);

        // validate countersignatures
        List<XMLSignature> counters = signature.getAllCounterSignatures();
        for (int i=0; i<counters.size();i++){
            XMLSignature counterSignature = counters.get(i);

            ValidationResult result = counterSignature.verify();
            assertTrue("Counter signature #"+i, result.getType()== ValidationResultType.VALID);
        }
    }

}
