package xmlsig.samples.validation;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import xmlsig.samples.utils.SampleBase;

import java.io.File;
import java.util.List;

/**
 * Provides validating functions for xml signatures.
 * @author suleyman.uslu
 */
@RunWith(JUnit4.class)
public class Validation  extends SampleBase
{
    /**
     * Generic validate function. Validates known types of xml signature.
     * @param fileName name of the signature file to be validated
     * @throws Exception
     */
    public static void validate(String fileName) throws  Exception {

        Context context = new Context(BASE_DIR);

        // add external resolver to resolve policies
        context.addExternalResolver(POLICY_RESOLVER);

        // read signature
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASE_DIR+fileName)), context);

        // no parameters, use the certificate in key info
        ValidationResult result = signature.verify();
        System.out.println(result.toXml());
        Assert.assertTrue("Cant verify " + fileName,
                result.getType() == ValidationResultType.VALID);

        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            List<XMLSignature> counterSignatures = usp.getAllCounterSignatures();
            for (XMLSignature counterSignature : counterSignatures){
                ValidationResult counterResult = signature.verify();

                System.out.println(counterResult.toXml());

                Assert.assertTrue("Cant verify counter signature" + fileName + " : "+counterSignature.getId(),
                                  counterResult.getType() == ValidationResultType.VALID);

            }
        }
    }
}
