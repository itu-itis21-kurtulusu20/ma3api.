package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 27.11.2012
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class SignValidator {

    private static IResolver POLICY_RESOLVER;
    private static List<String> signatureFiles;
    private static ECertificate CERTIFICATE;
    private static ECertificate CERTIFICATEWRONG;
    private static String BASEDIR;

    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        CERTIFICATE = bpg.getCertificate();
        BASEDIR = bpg.getBaseDir();
        POLICY_RESOLVER = bpg.getPolicyResolver();
        signatureFiles = new ArrayList<String>();

    }

    @Before
    public void setUp()
    {
        signatureFiles.clear();
        signatureFiles.add(UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.BES_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.EST_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.PARALLEL_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PARALLEL_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.COUNTER_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.COUNTER_ON_PARALLEL_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.CREATED_COUNTER_ON_PARALLEL_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.PROFILE2_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE2_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE2_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.PROFILE3_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE3_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE3_DETACHED_SIG_FILE_NAME);

        signatureFiles.add(UnitTestParameters.PROFILE4_ENVELOPING_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE4_ENVELOPED_SIG_FILE_NAME);
        signatureFiles.add(UnitTestParameters.PROFILE4_DETACHED_SIG_FILE_NAME);
    }


    private boolean validate(String fileName) throws  Exception
    {
        Context context = new Context(BASEDIR);
        context.addExternalResolver(POLICY_RESOLVER);

        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(BASEDIR+fileName)), context);

        // no params, use the certificate in key info
        ValidationResult result = signature.verify();
        System.out.println(result.toXml());
        Assert.assertTrue("Cant verify " + fileName,
                result.getType() == ValidationResultType.VALID);

        if (result.getType() != ValidationResultType.VALID)
        {
            return false;
        }

        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            List<XMLSignature> counterSignatures = usp.getAllCounterSignatures();
            for (XMLSignature counterSignature : counterSignatures){
                ValidationResult counterResult = signature.verify();

                System.out.println(counterResult.toXml());

                Assert.assertTrue("Cant verify counter signature" + fileName + " : "+counterSignature.getId(),
                        counterResult.getType() == ValidationResultType.VALID);

                if (counterResult.getType() != ValidationResultType.VALID)
                {
                    return false;
                }

            }
        }
        return true;
    }

    @Test
    public void validateSignature() {
        boolean testResult = true;
        for (String signatureFile : signatureFiles)
        {
            if((new File(BASEDIR+signatureFile)).exists()){
                try
                {
                    testResult = validate(signatureFile);
                    if(!testResult){
                        break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }
            }
            else{
                continue;
            }
        }
        Assert.assertTrue(testResult);
    }


    private boolean validateWithCertificate(String fileName, ECertificate certificate) throws  Exception {

        XMLSignature signature = XMLSignature.parse(
                new FileDocument(new File(BASEDIR+fileName)),
                new Context(BASEDIR)) ;

        // no params, use the certificate in key info
        ValidationResult result = signature.verify(certificate);
        System.out.println(result.toXml());
        Assert.assertTrue("Cant verify " + fileName,
                result.getType() == ValidationResultType.VALID);

        if (result.getType() != ValidationResultType.VALID)
        {
            return false;
        }

        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
        if (usp!=null){
            List<XMLSignature> counterSignatures = usp.getAllCounterSignatures();
            for (XMLSignature counterSignature : counterSignatures){
                ValidationResult counterResult = signature.verify();

                System.out.println(counterResult.toXml());

                Assert.assertTrue("Cant verify counter signature" + fileName + " : "+counterSignature.getId(),
                        counterResult.getType() == ValidationResultType.VALID);

                if (counterResult.getType() != ValidationResultType.VALID)
                {
                    return false;
                }

            }
        }
        return true;
    }

    //@Test
    public void validateSignaturesWithCertificate()
    {
        boolean testResult = true;
        for (String signatureFile : signatureFiles)
        {
            if((new File(BASEDIR + signatureFile)).exists()){
                try
                {
                    testResult = validateWithCertificate(signatureFile,CERTIFICATE);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }
            }
            else{
                continue;
            }
        }
        Assert.assertTrue(testResult);
    }

    //@Test
    public void validateSignaturesWithWrongCertificate()
    {
        boolean testResult = false;
        for (String signatureFile : signatureFiles)
        {
            if((new File(BASEDIR + signatureFile)).exists()){
                try
                {
                    testResult = validateWithCertificate(signatureFile,CERTIFICATEWRONG);
                    if(testResult){
                        break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        Assert.assertFalse(testResult);
    }

    @After
    public void tearDown()
    {

    }

    @AfterClass
    public static void finish()
    {

    }
}
