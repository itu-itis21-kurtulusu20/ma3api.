package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CRLReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.OCSPReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 17.12.2012
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class CheckCompleteRevocationRefsAndValues {

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
            case 0: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
            case 1: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
            case 2 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
            case 3 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
            case 4 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
            case 5 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
            default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
        }
        caseNum++;*/
    }

    private boolean checkCRLRefs(CRLReference crlReference, ECRL crl) throws NoSuchAlgorithmException, ESYAException
    {
        String digestAlg = crlReference.getDigestMethod().getAlgorithm().getName();
        byte [] crlDigestValueInSig = crlReference.getDigestValue();
        String crlIssuerInSig = crlReference.getCRLIdentifier().getIssuer();
        Date issueTimeInSig = crlReference.getCRLIdentifier().getIssueTime().toGregorianCalendar().getTime();
        BigInteger crlNumberInSig = crlReference.getCRLIdentifier().getNumber();

        byte [] encodedCRL = crl.getEncoded();
        BigInteger crlNumber = crl.getCRLNumber();
        String crlIssuer = crl.getIssuer().stringValue();
        Date issueTime = crl.getThisUpdate().getTime();

        MessageDigest digester = MessageDigest.getInstance(digestAlg);
        byte [] crlDigestValue = digester.digest(encodedCRL);

        if(!Arrays.equals(crlDigestValueInSig,crlDigestValue))
        {
            return false;
        }

        if(!crlIssuerInSig.equals(crlIssuer))
        {
            return false;
        }

        if(!crlNumberInSig.equals(crlNumber))
        {
            return false;
        }

        if(!issueTimeInSig.equals(issueTime))
        {
            return false;
        }

        return true;
    }

    private boolean checkOCSPRefs(OCSPReference ocspReference, EOCSPResponse ocspResponse) throws NoSuchAlgorithmException, ESYAException
    {
        String digestAlg = ocspReference.getDigestAlgAndValue().getDigestMethod().getAlgorithm().getName();
        byte [] ocspDigestValueInSig = ocspReference.getDigestAlgAndValue().getDigestValue();
        byte [] responderIDInSig = ocspReference.getOCSPIdentifier().getResponderID().getByKey();
        Date ocspProducedAtInSig = ocspReference.getOCSPIdentifier().getProducedAt().toGregorianCalendar().getTime();

        byte [] responseBytes = ocspResponse.getEncoded();
        Date ocspProducedAt = ocspResponse.getBasicOCSPResponse().getProducedAt().getTime();
        byte [] responderID = ocspResponse.getBasicOCSPResponse().getTbsResponseData().getResponderIdByKey();

        MessageDigest digester = MessageDigest.getInstance(digestAlg);
        byte [] ocspDigestValue = digester.digest(responseBytes);

        if(!Arrays.equals(ocspDigestValueInSig,ocspDigestValue))
        {
            return false;
        }

        if(!Arrays.equals(responderIDInSig,responderID))
        {
            return false;
        }

        long timeDifference = ocspProducedAt.getTime() - ocspProducedAtInSig.getTime();
        long dayDiff = timeDifference/(24*60*60*1000);
        timeDifference-=(dayDiff*24*60*60*1000);
        long hourDiff = timeDifference/(60*60*1000);
        timeDifference-=(hourDiff*60*60*1000);
        long minDiff = timeDifference/(60*1000);
        timeDifference-=(minDiff*60*1000);
        long secDiff = timeDifference/(1000);
        timeDifference-=(secDiff*1000);
        if(dayDiff!=0 || hourDiff!=0 || minDiff!=0 || secDiff!=0)
        {
            return false;
        }

//        if(!ocspProducedAtInSig.equals(ocspProducedAt))
//        {
//            return false;
//        }

        return true;
    }

    public boolean checkCompleteRevocationRefs(String baseDir, String fileName) throws ESYAException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, XMLSignatureException {
        XMLSignature signature = XMLSignature.parse(
                new FileDocument(new File(baseDir+fileName)),
                new Context(baseDir)) ;


        CompleteRevocationRefs completeRevocationRefs = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCompleteRevocationRefs();

        RevocationValues revocationValues = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getRevocationValues();

        int crlRefsCount = completeRevocationRefs.getCRLReferenceCount();

        for(int i=0;i<crlRefsCount;i++)
        {
             if(!checkCRLRefs(completeRevocationRefs.getCRLReference(i),revocationValues.getCRL(i)))
             {
                 return false;
             }
        }

        int ocspRefsCount =completeRevocationRefs.getOCSPReferenceCount();
        for(int i=0;i<ocspRefsCount;i++)
        {
            if(!checkOCSPRefs(completeRevocationRefs.getOCSPReference(i), revocationValues.getOCSPResponse(i)))
            {
                return false;
            }
        }

        return true;
    }

    @Test
    public void checkSignatureValueForESXLEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
    public void checkSignatureValueForESXLEnveloped()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
        try {
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
    public void checkSignatureValueForESXLDetached()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
        try {
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
    public void checkSignatureValueForESAEnveloping()
    {
        SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
        try {
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
            boolean testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CompleteRevocationRefsCheckResult", true, testResult);
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
