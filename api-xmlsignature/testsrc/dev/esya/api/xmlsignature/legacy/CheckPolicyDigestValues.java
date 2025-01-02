package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyId;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 01.02.2013
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class CheckPolicyDigestValues {

    private static String BASEDIR;
    private static String SIGNATUREFILENAME;

    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        BASEDIR = bpg.getBaseDir();
    }


    @Before
    public void setUp()
    {
    }

    private boolean checkPolicyDigestValue(String baseDir, String fileName) throws XMLSignatureException, IOException, NoSuchAlgorithmException
    {
        Context context = new Context(baseDir);
        XMLSignature signature = XMLSignature.parse(new FileDocument(new File(baseDir+fileName)),context) ;

        SignaturePolicyId policyID = signature.getQualifyingProperties().getSignedSignatureProperties().getSignaturePolicyIdentifier().getSignaturePolicyId();

        String digestAlg = policyID.getDigestMethod().getAlgorithm().getName();
        byte [] digestValue = policyID.getDigestValue();

        String policyFilePath = policyID.getPolicyQualifiers().get(0).getURI();
        Document resolvedDoc = Resolver.resolve(policyFilePath,signature.getContext());
        Transforms transformer = policyID.getTransforms();
        if(transformer!=null)
        {
            resolvedDoc = transformer.apply(resolvedDoc);
        }

        MessageDigest digester = MessageDigest.getInstance(digestAlg);
        byte [] digested = digester.digest(resolvedDoc.getBytes());

        boolean testResult = Arrays.equals(digestValue, digested);

        return testResult;
    }

    @Test
     public void checkPolicyDigestValueForP2Enveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE2_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP2Enveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE2_ENVELOPED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP2Detached()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE2_DETACHED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP3Enveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE3_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP3Enveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE3_ENVELOPED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP3Detached()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE3_DETACHED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP4Enveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP4Enveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkPolicyDigestValueForP4Detached()
    {
        SIGNATUREFILENAME = UnitTestParameters.PROFILE4_DETACHED_SIG_FILE_NAME;
        try {
            boolean testResult = checkPolicyDigestValue(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("SignedPropertiesDigestValueCheck", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
