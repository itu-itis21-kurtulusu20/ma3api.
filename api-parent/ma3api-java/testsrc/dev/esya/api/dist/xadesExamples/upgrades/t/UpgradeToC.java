package tr.gov.tubitak.uekae.esya.api.xades.example.upgrades.t;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xades.example.XadesSampleBase;
import tr.gov.tubitak.uekae.esya.api.xades.example.validation.XadesSignatureValidation;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Provides upgrade function from T to C
 */
public class UpgradeToC extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "c_from_t.xml";

    /**
     * Upgrades T to C. T signature needs to be provided before upgrade process.
     * It can be created in formats.T.
     *
     * @throws Exception
     */
    @Test
    public void upgradeTToC() throws Exception {

        // create context with working directory
        Context context = createContext();

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(getTestDataFolder() + "t.xml")), context);

        // upgrade to C
        signature.upgrade(SignatureType.ES_C);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);

    }
}
