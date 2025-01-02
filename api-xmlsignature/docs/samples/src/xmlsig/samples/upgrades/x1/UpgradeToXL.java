package xmlsig.samples.upgrades.x1;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Provides upgrade function from X1 to XL
 * @author: suleyman.uslu
 */
public class UpgradeToXL extends SampleBase {

    public static final String SIGNATURE_FILENAME = "xl_from_x1.xml";

    /**
     * Upgrades X1 to XL. X1 signature needs to be provided before upgrade process.
     * It can be created in formats.X1.
     * @throws Exception
     */
    @Test
    public void upgradeX1ToXL() throws Exception {

        // create context with working directory
        Context context = new Context(BASE_DIR);

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR + "x1.xml")),context);

        // upgrade to XL
        signature.upgrade(SignatureType.ES_XL);

        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
