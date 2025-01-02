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
 * Provides upgrade function from T to X2
 */
public class UpgradeToX2 extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "x2_from_t.xml";

    /**
     * Upgrades T to X2. T signature needs to be provided before upgrade process.
     * It can be created in formats.T.
     *
     * @throws Exception
     */
    @Test
    public void upgradeTToX2() throws Exception {

        // create context with working directory
        Context context = createContext();

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(getTestDataFolder() + "t.xml")), context);

        // upgrade to X2
        signature.upgrade(SignatureType.ES_X_Type2);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);

    }
}
