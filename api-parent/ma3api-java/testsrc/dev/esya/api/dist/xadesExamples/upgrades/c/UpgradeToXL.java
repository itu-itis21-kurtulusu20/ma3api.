package tr.gov.tubitak.uekae.esya.api.xades.example.upgrades.c;

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
 * Provides upgrade function from C to XL
 */
public class UpgradeToXL extends XadesSampleBase {

    public static final String SIGNATURE_FILENAME = "xl_from_c.xml";

    /**
     * Upgrades C to XL. C signature needs to be provided before upgrade process.
     * It can be created in formats.C.
     *
     * @throws Exception
     */
    @Test
    public void upgradeCToXL() throws Exception {

        // create context with working directory
        Context context = createContext();

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(getTestDataFolder() + "c.xml")), context);

        // upgrade to XL
        signature.upgrade(SignatureType.ES_XL);

        FileOutputStream fileOutputStream = new FileOutputStream(getTestDataFolder() + SIGNATURE_FILENAME);
        signature.write(fileOutputStream);
        fileOutputStream.close();

        XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
        signatureValidation.validate(SIGNATURE_FILENAME);

    }
}
