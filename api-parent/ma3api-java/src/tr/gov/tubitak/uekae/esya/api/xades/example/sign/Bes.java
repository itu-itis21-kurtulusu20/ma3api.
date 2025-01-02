package tr.gov.tubitak.uekae.esya.api.xades.example.sign;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;
import tr.gov.tubitak.uekae.esya.api.xades.example.XadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.xades.example.validation.XadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;


/**
 * BES signing sample
 */
public class Bes extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "bes.xml";

    @Test
    public void testCreateEnveloping() throws Exception{

        // create context with working directory
        Context context = createContext();

        // create signature according to context,
        // with default type (XADES_BES)
        XMLSignature signature = new XMLSignature(context);

        // add document as reference, but do not embed it
        // into the signature (embed=false)
        signature.addDocument("./sample.txt", "text/plain", false);

        // false-true gets non-qualified certificates while true-false gets qualified ones
        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        //This function selects SIGNATURE_RSA_SHA256 algorithm for RSA keys and convenient algorithm for EC. Check the SmartCardManagerBase class getSignerBase() function for more details.
        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

        // add certificate to show who signed the document
        signature.addKeyInfo(cert);

        // now sign it by using smart card
        signature.sign(signer);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);
    }

    @Test
    public void testCreateEnveloping_TwoSteps() throws Exception
    {
        Context context = createContext();
        XMLSignature signature = new XMLSignature(context);
        signature.addDocument("./sample.txt", "text/plain", false);

        ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

        signature.addKeyInfo(cert);
        signature.setSigningTime(Calendar.getInstance());

        BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

        // You can get the parameters from the signer.
        //byte[] dtbs = signature.initAddingSignature(SignatureAlg.fromName(signer.getSignatureAlgorithmStr()), signer.getAlgorithmParameterSpec());

        byte[] dtbs = signature.initAddingSignature(SignatureAlg.RSA_SHA256, null);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        signature.write(bos);
        bos.close();

        byte[] signatureValue = signer.sign(dtbs);
        byte[] unfinishedBytes = bos.toByteArray();

        finishSigning(unfinishedBytes, context, signatureValue);

        // read and validate
        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);
    }

    private void finishSigning(byte[] bsdBytes, Context context, byte [] signatureValue) throws Exception
    {
        InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
        XMLSignature signature = XMLSignature.parse(xmlDocument, context);

        signature.finishAddingSignature(signatureValue);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();
    }
}
