package xmlsig.samples.attributes;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.IndividualDataObjectsTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.Include;
import xmlsig.samples.utils.SampleBase;
import xmlsig.samples.validation.Validation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Adds IndividualDataObjectsTimestamp attribute to BES
 * @author: suleyman.uslu
 */
public class IndividualDataObjectsTimeStampAttribute extends SampleBase {

    public static final String SIGNATURE_FILENAME = "individual_data_objects_timestamp.xml";

    /**
     * Creates BES with IndividualDataObjectsTimestamp attribute
     * @throws Exception
     */
    @Test
    public void createBESWithIndividualDataObjectsTimeStamp() throws Exception
    {
        try {
            // create context with working directory
            Context context = new Context(BASE_DIR);

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document into the signature and get the reference
            String ref1 = "#" + signature.addDocument("./sample.txt", "text/plain", true);

            // add another object
            String objId2 = signature.addPlainObject("Test Data 1", "text/plain", null);
            String ref2 = "#" + signature.addDocument("#"+objId2, null, false);

            // add certificate to show who signed the document
            signature.addKeyInfo(CERTIFICATE);

            // create new individual data objects timestamp structure
            IndividualDataObjectsTimeStamp timestamp = new IndividualDataObjectsTimeStamp(context);

            // add objects to timestamp structure
            timestamp.addInclude(new Include(context, ref1, Boolean.TRUE));
            timestamp.addInclude(new Include(context, ref2, Boolean.TRUE));

            // get encapsulated timestamp to individual data objects timestamp
            timestamp.addEncapsulatedTimeStamp(signature);

            // add individual data objects timestamp to signature
            signature.getQualifyingProperties().getSignedProperties().createOrGetSignedDataObjectProperties().
                    addIndividualDataObjectsTimeStamp(timestamp);

            // optional - add timestamp validation data
            signature.addTimeStampValidationData(timestamp, Calendar.getInstance());

            // now sign it by using private key
            signature.sign(PRIVATE_KEY);

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
