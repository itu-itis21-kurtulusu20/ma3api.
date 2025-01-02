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
 * Provides upgrade function from BES to T
 * @author: suleyman.uslu
 */
public class UpgradeToT extends SampleBase {

    public static final String SIGNATURE_FILENAME = "t_from_bes.xml";

    /**
     * Upgrades BES to T. BES needs to be provided before upgrade process.
     * It can be created in formats.BES.
     * @throws Exception
     */
    @Test
    public void upgradeBESToT() throws Exception {

        // create context with working directory
        Context context = new Context(BASE_DIR);

        // read signature to be upgraded
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR + "bes.xml")),context);

        // upgrade to T
        signature.upgrade(SignatureType.ES_T);

        signature.write(new FileOutputStream(BASE_DIR + SIGNATURE_FILENAME));
    }

    @Test
    public void validate() throws Exception {
        Validation.validate(SIGNATURE_FILENAME);
    }
}
