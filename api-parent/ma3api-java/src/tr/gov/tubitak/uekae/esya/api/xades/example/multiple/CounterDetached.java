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
import java.io.FileOutputStream;

/**
 * Counter signature sample
 */
public class CounterDetached extends XadesSampleBase {

    public static final String COUNTER_DETACHED_XML = "counter_detached.xml";
    private static final String DETACHED_XML = "detached.xml";
    private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
    private RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

    /**
     * Adds counter signature to a detached one
     *
     * @throws Exception
     */
    @Test
    public void signCounterDetached() throws Exception {
        Context context = createContext();

        // read previously created signature, you need to run Detached.java first
        Document doc = Resolver.resolve(DETACHED_XML, context);
        XMLSignature signature = XMLSignature.parse(doc, context);

        // create counter signature
        XMLSignature counterSignature = signature.createCounterSignature();

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        counterSignature.addKeyInfo(cert);

        // now sign it by using smart card
        counterSignature.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

        // signature contains itself and counter signature
        signature.write(new FileOutputStream(getTestDataFolder() + COUNTER_DETACHED_XML));

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(COUNTER_DETACHED_XML);

    }

    @Test
    public void signCounterDetached_TwoSteps() throws Exception {
        Context context = createContext();

        // read previously created signature, you need to run Detached.java first
        Document doc = Resolver.resolve(DETACHED_XML, context);
        XMLSignature signature = XMLSignature.parse(doc, context);

        // create counter signature
        XMLSignature counterSignature = signature.createCounterSignature();

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        // add certificate to show who signed the document
        counterSignature.addKeyInfo(cert);

        byte[] dtbs = counterSignature.initAddingSignature(signatureAlg, rsapssParams);
        ByteArrayOutputStream signatureBytes = new ByteArrayOutputStream();
        signature.write(signatureBytes);

        // now sign it by using smart card
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert, signatureAlg.getName(), rsapssParams);
        byte[] signatureValue = signer.sign(dtbs);

        finishSigning(signatureBytes.toByteArray(), context, signatureValue);

        // signature contains itself and counter signature

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(COUNTER_DETACHED_XML);

    }

    private void finishSigning(byte[] bsdBytes, Context context, byte [] signature) throws Exception
    {
        InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);

        XMLSignature xmlSignature = XMLSignature.parse(xmlDocument, context);

        xmlSignature.finishAddingSignature(signature);

        FileOutputStream fos = new FileOutputStream(getTestDataFolder() + COUNTER_DETACHED_XML);
        xmlSignature.write(fos);
        fos.close();
    }
}
