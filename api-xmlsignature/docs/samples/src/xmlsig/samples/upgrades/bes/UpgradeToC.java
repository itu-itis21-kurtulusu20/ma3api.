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
 * Provides upgrade function from BES to C
 * @author: suleyman.uslu
 */
public class UpgradeToC extends SampleBase {

    public static final String SIGNATURE_FILENAME = "c_from_bes.xml";

    /**
     * Upgrades BES to C. BES needs to be provided before upgrade process.
     * It can be created in formats.BES.
     * @throws Exception
     */
    @Test
    public void upgradeBESToC() throws Exception {

        // create context with working directory
        Context context = new Context(BASE_DIR);

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR + "bes.xml")),context);

        // upgrade to C
        signature.upgrade(SignatureType.ES_C);

        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
