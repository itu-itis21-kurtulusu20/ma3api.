package dev.esya.api.pades.pdfbox;

import dev.esya.api.pades.pdfbox.settings.PAdESTestSettings;
import dev.esya.api.pades.pdfbox.settings.UGPAdESTestSettings;
import org.junit.Assert;
import test.esya.api.cmssignature.testconstants.TestConstants;
import test.esya.api.cmssignature.testconstants.UGTestConstants;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.signature.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by orcun.ertugrul on 07-Dec-17.
 */
public class PAdESBaseTest
{
    protected String SIGNATURE_FOLDER = "C:\\a\\PAdES\\";

    protected TestConstants settings = new UGTestConstants();

    protected PAdESTestSettings testSettings = new UGPAdESTestSettings();

    protected ByteArrayOutputStream signatureBytes = new ByteArrayOutputStream();


    public ContainerValidationResult validate(PAdESContext context) throws Exception{
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signatureBytes.toByteArray());
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, inputStream, context);

        ContainerValidationResult validationResult = signatureContainer.verifyAll();

        // close the container
        signatureContainer.close();

        return validationResult;
    }


    public void checkIsValid(PAdESContext context) throws Exception{
        ContainerValidationResult validationResult = validate(context);

        if( validationResult.getResultType() != ContainerValidationResultType.ALL_VALID ){
            System.out.println(validationResult.toString());
            assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
        }
    }

    protected void checkType(int index, SignatureType signatureType, PAdESContext context) throws Exception
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signatureBytes.toByteArray());
        SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.PAdES, inputStream, context);

        // get signature type before closing the container
        SignatureType newSignatureType = signatureContainer.getSignatures().get(index).getSignatureType();

        // close the container
        signatureContainer.close();

        Assert.assertEquals(signatureType, newSignatureType);
    }



}
