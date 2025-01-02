package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.helper.XmlSignatureTestHelper;
import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 04.01.2013
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class UpgradeESTWithTestLicense {

    private static String BASEDIR;
    private static String ROOTDIR;
    private static String SIGNATUREFILENAME;
    private static String SIGNATUREFILE_TOBE_UPGRADED;

    private static BaseParameterGetter bpg;

    private static int caseNum;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();

        BASEDIR = bpg.getBaseDir();
        ROOTDIR = bpg.getRootDir();
        try {
            XmlSignatureTestHelper.getInstance().loadTestLicense();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Before
    public void setUp() throws FileNotFoundException, ESYAException
    {
    }

    @Test
    public void upgradeESTEnveloping()
    {
        SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE;
        SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_UPGRADED_WITH_TEST_LICENSE;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR)) ;
            signature.upgradeToXAdES_T();
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
            assertTrue(false);
        } catch (XMLSignatureException e) {
            String message = e.getMessage();
            if(message.contains("SignatureTimeStamp"))
            {
                assertTrue(true);
            }
            else{
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                assertTrue(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        }
    }

    @Test
    public void upgradeESTEnveloped()
    {
        SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE;
        SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_UPGRADED_WITH_TEST_LICENSE;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR)) ;
            signature.upgradeToXAdES_T();
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
            assertTrue(false);
        } catch (XMLSignatureException e) {
            String message = e.getMessage();
            if(message.contains("SignatureTimeStamp"))
            {
                assertTrue(true);
            }
            else{
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                assertTrue(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
        }
    }

    @Test
    public void upgradeESTDetached()
    {
        SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_DETACHED_SIGNED_WITH_FREE_LICENSE;
        SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_UPGRADED_WITH_TEST_LICENSE;;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR)) ;
            signature.upgradeToXAdES_T();
            signature.write(new FileOutputStream(BASEDIR+SIGNATUREFILENAME));
            assertTrue(false);
        } catch (XMLSignatureException e) {
            String message = e.getMessage();
            if(message.contains("SignatureTimeStamp"))
            {
                assertTrue(true);
            }
            else{
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                assertTrue(false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            assertTrue(false);
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
