package tr.gov.tubitak.uekae.esya.api.xades.example.upgrades.bes;

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
 * Provides upgrade function from BES to X2
 */
public class UpgradeToX2 extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "x2_from_bes.xml";

    /**
     * Upgrades BES to X2. BES needs to be provided before upgrade process.
     * It can be created in formats.BES.
     *
     * @throws Exception
     */
    @Test
    public void upgradeBESToX2() throws Exception {

        // create context with working directory
        Context context = createContext();

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(getTestDataFolder() + "bes.xml")), context);

        // upgrade to X2
        signature.upgrade(SignatureType.ES_X_Type2);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);
    }
}
