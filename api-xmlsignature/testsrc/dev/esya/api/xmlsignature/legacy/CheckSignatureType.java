package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 29.11.2012
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class CheckSignatureType {

    private static String BASEDIR;
    private static String SIGNATUREFILENAME;

    private static int caseNum;
    private static BaseParameterGetter bpg;

    @BeforeClass
    public static void initialize()
    {
        bpg = ParameterGetterFactory.getParameterGetter();
        BASEDIR = bpg.getBaseDir();

        caseNum=0;
    }

    @Before
    public void setUp()
    {
        /*switch (caseNum)
        {
            case 0 : SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME; break;
            case 1 : SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME; break;
            case 2 : SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
            case 3 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME; break;
            case 4 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME; break;
            case 5 : SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME; break;
            case 6 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME; break;
            case 7 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME; break;
            case 8 : SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME; break;
            case 9 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME; break;
            case 10 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME; break;
            case 11 : SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME; break;
            case 12 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
            case 13: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
            case 14 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
            case 15 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
            case 16 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
            case 17 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
            case 18 : SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME; break;
            default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
        }
        caseNum++;*/
    }

    @Test
    public void checkTypeForBESEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_BES,type);
        } catch (XMLSignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.assertTrue(false);
        }
    }

    @Test
    public void checkTypeForBESEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_BES,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForBESDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_BES,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESTEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_T,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESTEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_T,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESTDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_T,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESCEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_C,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESCEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_C,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESCDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_C,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXLEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X_L,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXLEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X_L,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESXLDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_X_L,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESAEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_A,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESAEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_A,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForESADetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_A,type);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkTypeForBESEDefterEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
        try {
            XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new File(BASEDIR+SIGNATUREFILENAME)),
                    new Context(BASEDIR)) ;
            SignatureType type = signature.getSignatureType();
            Assert.assertEquals("Signature Type",SignatureType.XAdES_BES,type);
        } catch (XMLSignatureException e) {
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
