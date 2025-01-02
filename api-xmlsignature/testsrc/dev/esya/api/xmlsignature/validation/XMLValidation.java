package dev.esya.api.xmlsignature.validation;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import test.esya.api.xmlsignature.validation.XMLValidationUtil;

/**
 * Created by orcun.ertugrul on 17-Aug-17.
 */
public class XMLValidation
{
    @Test
    public void validateYTE() throws Exception {
        BasicConfigurator.configure();
        String signatureFileName = "C:\\Users\\orcun.ertugrul\\Desktop\\YTE\\counter_signature_arasi_10_dkdan_az(force_true_iken_dogrulaniyor).xml";
        XMLValidationUtil.checkSignatureIsValid("C:\\Users\\orcun.ertugrul\\Desktop\\YTE", signatureFileName);
    }
}
