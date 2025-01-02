using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckCompleteCertRefs
    {
        //private static ECertificate CERTIFICATE;
        //private static BaseSigner BASESIGNER;
        //private static PrivateKey PRIVATEKEY;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            //CERTIFICATE = bpg.getCertificate();
            //BASESIGNER = bpg.getBaseSigner();
            //PRIVATEKEY = bpg.getPrivateKey();
            BASEDIR = bpg.getBaseDir();

            caseNum=0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch (caseNum)
            {
                case 0 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME; break;
                case 1 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME; break;
                case 2 : SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME; break;
                case 3 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME; break;
                case 4 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME; break;
                case 5 : SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME; break;
                case 6 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
                case 7: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 8 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 9 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 10 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 11 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        private String getDigestMethodString(DigestAlg digestAlg)
        {
            String rtr;
            if(digestAlg == DigestAlg.SHA1)
            {
                rtr = "SHA-1";
            }
            else if(digestAlg == DigestAlg.SHA224)
            {
                rtr = "SHA-224";
            }
            else if(digestAlg == DigestAlg.SHA256)
            {
                rtr = "SHA-256";
            }
            else if(digestAlg == DigestAlg.SHA384)
            {
                rtr = "SHA-384";
            }
            else if(digestAlg == DigestAlg.SHA512)
            {
                rtr = "SHA-512";
            }
            else
            {
                rtr = "Unknown";
            }
            return rtr;
        }

        private bool compareCertificates(ECertificate certificate1, CertID certIDInSignature)
        {
            try
            {
                BigInteger issuerCertSerialNumber = certificate1.getSerialNumber();
                String issuerCertIssuerName = certificate1.getIssuer().stringValue();

                DigestAlg digestAlg = certIDInSignature.DigestMethod.Algorithm;
                byte [] digestValue = certIDInSignature.DigestValue;
                BigInteger serialNumber = certIDInSignature.X509SerialNumber;
                String issuerName = certIDInSignature.X509IssuerName;

                //MessageDigest digester = MessageDigest.getInstance(digestAlg);
                //byte [] digested =digester.digest(certificate1.getEncoded());
                byte[] digested = DigestUtil.digest(digestAlg, certificate1.getEncoded());

                if (!ArrayUtil.Equals(digested, digestValue))
                {
                    return false;
                }

                if(!issuerCertSerialNumber.Equals(serialNumber))
                {
                    return false;
                }

                if(!issuerCertIssuerName.Equals(issuerName))
                {
                    return false;
                }
                return true; 
            }catch(NoSuchAlgorithmException e)
            {
                throw e;
            }
            
        }

        private bool checkIssuerCertRef(ECertificate issuerCertificate, CertID certIDInSignature)
        {

            if(!issuerCertificate.isCACertificate())
            {
                return false;
            }

            if(!compareCertificates(issuerCertificate,certIDInSignature))
            {
                return false;
            }

            return true;
        }

        private bool checkCRLCertRef(ECertificate issuerCert, ECertificate signingCertificate, CertID certIDInSignature)
        {
            try 
	        {	        
		        OCSPResponseFinder finder = new OCSPResponseFinderFromAIA();
                EOCSPResponse response = finder.findOCSPResponse(signingCertificate,issuerCert);
                ECertificate ocspCertificate = response.getBasicOCSPResponse().getCertificate(0);

                return compareCertificates(ocspCertificate,certIDInSignature);
	        }
            catch(NoSuchAlgorithmException e)
            {
                throw e;
            }
	        catch (ESYAException e)
	        {
		
		        throw e;
	        }
        }

        public bool checkCompleteCertRefs(String baseDir, String fileName)
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir+fileName)),
                    new Context(baseDir)) ;

                bool testResult = true;

                ECertificate signingCertificate = signature.SigningCertificate;
                CertificateFinder finder = new CertificateFinderFromHTTP();
                IList<ECertificate> issuerCerts = finder.findCertificate(signingCertificate);
                ECertificate issuerCert = issuerCerts[0];

                CompleteCertificateRefs completeCertificateRefs = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.CompleteCertificateRefs;
                int certRefsCount = completeCertificateRefs.CertificateReferenceCount;

                bool issuerCheckResult = checkIssuerCertRef(issuerCert,completeCertificateRefs.getCertificateReference(0));
                if(!issuerCheckResult)
                {
                    return false;
                }

                bool crlCheckResult = checkCRLCertRef(issuerCert,signingCertificate,completeCertificateRefs.getCertificateReference(1));
                if(!crlCheckResult)
                {
                    return false;
                }
                /*for(int i=0;i<certRefsCount;i++)
                {
                    CertID certID = completeCertificateRefs.getCertificateReference(i);
                    String digestMethod = getDigestMethodString(certID.getDigestMethod().getAlgorithm());
                    byte [] digestValue = certID.getDigestValue();
                    BigInteger serialNumber = certID.getX509SerialNumber();
                } */

                return testResult;
            }catch(NoSuchAlgorithmException e)
            {
                throw e;
            }
	        catch (Exception e)
	        {
		
		        throw e;
	        }
            
        }

        [Test]
        public void a_checkSignatureValueForESCEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (CryptoException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (NoSuchAlgorithmException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (ESYAException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (InvalidKeyException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (SignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (IOException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_checkSignatureValueForESCEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void c_checkSignatureValueForESCDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void d_checkSignatureValueForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void e_checkSignatureValueForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void f_checkSignatureValueForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void g_checkSignatureValueForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void h_checkSignatureValueForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void i_checkSignatureValueForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void j_checkSignatureValueForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void k_checkSignatureValueForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void l_checkSignatureValueForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkCompleteCertRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteCertRefsCheckResult");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (CryptoException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (NoSuchAlgorithmException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (ESYAException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (InvalidKeyException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (SignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (IOException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [TearDown]
        public void tearDown()
        {

        }

        [TestFixtureTearDown]
        public static void finish()
        {
            //bpg.logoutFromSmartCard();
        }
    }
}
