package xmlsig.samples.upgrades.bes;

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
 * Provides upgrade function from BES to X1
 * @author: suleyman.uslu
 */
public class UpgradeToX1 extends SampleBase {

    public static final String SIGNATURE_FILENAME = "x1_from_bes.xml";

    /**
     * Upgrades BES to X1. BES needs to be provided before upgrade process.
     * It can be created in formats.BES.
     * @throws Exception
     */
    @Test
    public void upgradeBESToX1() throws Exception {

        // create context with working directory
        Context context = new Context(BASE_DIR);

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR + "bes.xml")),context);

        // upgrade to X1
        signature.upgrade(SignatureType.ES_X_Type1);

        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
