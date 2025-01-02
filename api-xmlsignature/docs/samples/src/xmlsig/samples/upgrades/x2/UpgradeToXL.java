package xmlsig.samples.upgrades.x2;

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
 * Provides upgrade function from X2 to XL
 * @author: suleyman.uslu
 */
public class UpgradeToXL extends SampleBase {

    public static final String SIGNATURE_FILENAME = "xl_from_x2.xml";

    /**
     * Upgrades X2 to XL. X2 signature needs to be provided before upgrade process.
     * It can be created in formats.X2.
     * @throws Exception
     */
    @Test
    public void upgradeX2ToXL() throws Exception {

        // create context with working directory
        Context context = new Context(BASE_DIR);

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR + "x2.xml")),context);

        // upgrade to XL
        signature.upgrade(SignatureType.ES_XL);

        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
