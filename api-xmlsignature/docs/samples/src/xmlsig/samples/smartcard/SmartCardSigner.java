package xmlsig.samples.smartcard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import xmlsig.samples.utils.JSmartCardManager;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Check smart card documentation for more smart card usage scenarios
 * and info.
 *
 * @author suleyman.uslu
 */
@RunWith(JUnit4.class)
public class SmartCardSigner extends SampleBase
{

    public static final String SIGNATURE_FILENAME = "smartcard.xml";

    /**
     * Creates detached BES with smart card. Do not forget to enter
     * your smart card's pin.
     * @throws Exception
     */
    @Test
    public void createDetachedBesWithSmartCard() throws Exception
    {
        try {
            // create context with working directory
            Context context = new Context(BASE_DIR);

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document as reference, but do not embed it
            // into the signature (embed=false)
            signature.addDocument("./sample.txt", "text/plain", false);

            signature.getSignedInfo().setSignatureMethod(SignatureMethod.RSA_SHA256);

            // false-true gets non-qualified certificates while true-false gets qualified ones
            X509Certificate cert = JSmartCardManager.getInstance().getSignatureCertificate(true);

            // add certificate to show who signed the document
            signature.addKeyInfo(new ECertificate(cert.getEncoded()));

            // now sign it by using smart card
            signature.sign(JSmartCardManager.getInstance().getSigner(PIN, cert));

            signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
        }
        catch (XMLSignatureException x){
            // cannot create signature
            x.printStackTrace();
        }
        catch (IOException x){
            // probably couldn't write to the file
            x.printStackTrace();
        }
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
