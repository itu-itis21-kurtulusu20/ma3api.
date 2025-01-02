package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.ArchiveTimeStamp;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 18.12.2012
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public class CheckArchiveTimeStamp {

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
            case 0 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
            case 1 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
            case 2 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
            default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
        }
        caseNum++;*/
    }

    private boolean checkEncapsulatedTS(EncapsulatedTimeStamp encapsulatedTS) throws XMLSignatureException, NoSuchAlgorithmException
    {
        if(!encapsulatedTS.isTimeStamp())
        {
            return false;
        }

        String digestAlg = encapsulatedTS.getDigestAlgorithm().getName();
        byte [] digestValue = encapsulatedTS.getDigestValue();

        byte [] contentByteArry = encapsulatedTS.getSignedData().getEncapsulatedContentInfo().getContent();

//        encapsulatedTS.getElement().getNodeValue()
//        String str = encapsulatedTS.getElement().getFirstChild().getNodeValue();
//        byte [] decodedTSByteArry = Base64.decode(new String(contentByteArry));

//        MessageDigest digester = MessageDigest.getInstance(digestAlg);
//        byte [] tsDigestValue = digester.digest(contentByteArry);
//
//        if(!Arrays.equals(digestValue,tsDigestValue))
//        {
//            return false;
//        }

        return true;
    }

    private boolean checkArchiveTS(ArchiveTimeStamp archiveTS) throws XMLSignatureException, NoSuchAlgorithmException
    {
        int encapsulatedTS = archiveTS.getEncapsulatedTimeStampCount();
        for(int i=0;i<encapsulatedTS;i++)
        {
            if(!checkEncapsulatedTS(archiveTS.getEncapsulatedTimeStamp(i)))
            {
                return false;
            }
        }
        return true;
    }

    public boolean checkArchiveTimeStamp(String baseDir, String fileName) throws ESYAException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, XMLSignatureException {
        XMLSignature signature = XMLSignature.parse(
                new FileDocument(new File(baseDir+fileName)),
                new Context(baseDir)) ;

        int archiveTSCount = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getArchiveTimeStampCount();

        for(int i=0;i<archiveTSCount;i++)
        {
            if(!checkArchiveTS(signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getArchiveTimeStamp(i)))
            {
                return false;
            }
        }

        return true;
    }

    @Test
    public void checkSignatureValueForESAEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("ArchiveTimeStampCheckResult", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CryptoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkSignatureValueForESAEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
        try {
            boolean testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("ArchiveTimeStampCheckResult", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CryptoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void checkSignatureValueForESADetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
        try {
            boolean testResult = checkArchiveTimeStamp(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("ArchiveTimeStampCheckResult", true, testResult);
        } catch (XMLSignatureException e) {
            Assert.assertTrue(false);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CryptoException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ESYAException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
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
