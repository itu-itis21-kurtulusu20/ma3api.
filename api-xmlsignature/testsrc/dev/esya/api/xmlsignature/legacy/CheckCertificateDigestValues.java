package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.signerHelpers.ParameterGetterFactory;
import org.junit.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
import dev.esya.api.xmlsignature.legacy.signerHelpers.BaseParameterGetter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 18.12.2012
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
public class CheckCertificateDigestValues {

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

    private boolean checkCertificate(ECertificate certificate, CertID certID) throws NoSuchAlgorithmException {
        String digestAlg = certID.getDigestMethod().getAlgorithm().getName();
        byte [] digestValue = certID.getDigestValue();
        String issuerName = certID.getX509IssuerName();
        BigInteger serialNumber = certID.getX509SerialNumber();

        BigInteger certSerialNumber = certificate.getSerialNumber();
        String certIssuerName = certificate.getIssuer().stringValue();

        MessageDigest digester = MessageDigest.getInstance(digestAlg);
        byte [] digested =digester.digest(certificate.getEncoded());

        if(!Arrays.equals(digested, digestValue))
        {
            return false;
        }

        if(!issuerName.equals(certIssuerName))
        {
            return false;
        }

        if(!serialNumber.equals(certSerialNumber))
        {
            return false;
        }

        return true;
    }

    public boolean checkCertificateValues(String baseDir, String fileName) throws ESYAException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, XMLSignatureException {
        XMLSignature signature = XMLSignature.parse(
                new FileDocument(new File(baseDir+fileName)),
                new Context(baseDir)) ;

        int certCountInSig = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCertificateValues().getCertificateCount();
        List<ECertificate> certificatesInSig = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCertificateValues().getAllCertificates();

        int certRefCountInSig = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCompleteCertificateRefs().getCertificateReferenceCount();
        CompleteCertificateRefs certificateRefs = signature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties().getCompleteCertificateRefs();

        if(certCountInSig!=certRefCountInSig)
        {
            return false;
        }

        for(int i=0;i<certRefCountInSig;i++)
        {
            boolean match = false;
            for(int j=0;j<certCountInSig;j++)
            {
                if(checkCertificate(certificatesInSig.get(j),certificateRefs.getCertificateReference(i)))
                {
                    match = true;
                    break;
                }
            }
            if(!match)
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
            boolean testResult = checkCertificateValues(BASEDIR, SIGNATUREFILENAME);
            Assert.assertEquals("CertificateValuesCheckResult", true, testResult);
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
