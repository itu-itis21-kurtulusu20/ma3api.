package tr.gov.tubitak.uekae.esya.api.xades.example.multiple;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.xades.example.XadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.xades.example.validation.XadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignedDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Parallel enveloped signature sample
 */
public class ParallelEnveloped extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "parallel_enveloped.xml";
    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
    private RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

    /**
     * Creates two signatures in a document, that signs same inner data
     *
     * @throws Exception
     */
    @Test
    public void createParallelEnveloped() throws Exception {

        Context context = createContext();

        SignedDocument signatures = new SignedDocument(context);

        // read and add an external XML
        Document doc = Resolver.resolve("./sample.xml", context);
        String fragment = signatures.addDocument(doc);

        XMLSignature signature1 = signatures.createSignature();

        // add document as inner reference
        signature1.addDocument("#" + fragment, "text/xml", false);

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        signature1.addKeyInfo(cert);

        // now sign it by using smart card
        signature1.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

        XMLSignature signature2 = signatures.createSignature();

        // add document as inner reference
        signature2.addDocument("#" + fragment, "text/plain", false);

        // add certificate to show who signed the document
        signature2.addKeyInfo(cert);

        // now sign it by using smart card
        signature2.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

        // write combined document
        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signatures.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validateParallel(SIGNATURE_FILENAME);

    }

    @Test
    public void createParallelEnveloped_TwoSteps() throws Exception {

        Context context = createContext();

        SignedDocument signedDocument1 = new SignedDocument(context);

        // read and add an external XML
        Document doc1 = Resolver.resolve("./sample.xml", context);
        String fragment = signedDocument1.addDocument(doc1);

        XMLSignature signature1 = signedDocument1.createSignature();

        // add document as inner reference
        signature1.addDocument("#" + fragment, "text/xml", false);

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        signature1.addKeyInfo(cert);

        // now sign it by using smart card
        byte[] dtbs1 = signature1.initAddingSignature(signatureAlg, rsapssParams);

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert, signatureAlg.getName(), rsapssParams);
        byte[] signatureValue1 = signer.sign(dtbs1);

        ByteArrayOutputStream signatureBytes = new ByteArrayOutputStream();
        signedDocument1.write(signatureBytes);

        byte[] bsdBytes1 = signatureBytes.toByteArray();

        finishSigning(bsdBytes1, context, signatureValue1);

        Document doc2 = Resolver.resolve(getTestDataFolder() + SIGNATURE_FILENAME, context);
        SignedDocument signedDocument2 = new SignedDocument(doc2, context);

        XMLSignature signature2 = signedDocument2.createSignature();

        // add document as inner reference
        signature2.addDocument("#" + fragment, "text/plain", false);

        // add certificate to show who signed the document
        signature2.addKeyInfo(cert);

        // now sign it by using smart card
        byte[] dtbs2 = signature2.initAddingSignature(signatureAlg, rsapssParams);
        byte[] signatureValue2 = signer.sign(dtbs2);

        signatureBytes.reset();
        signedDocument2.write(signatureBytes);

        byte[] bsdBytes2 = signatureBytes.toByteArray();

        finishSigning(bsdBytes2, context, signatureValue2);

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validateParallel(SIGNATURE_FILENAME);

    }

    private void finishSigning(byte[] bsdBytes, Context context, byte [] signature) throws Exception
    {
        InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);

        SignedDocument signedDocument = new SignedDocument(xmlDocument, context);

        signedDocument.finishAddingSignature(signature);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signedDocument.write(fileOutputStream);
        fileOutputStream.close();
    }
}
