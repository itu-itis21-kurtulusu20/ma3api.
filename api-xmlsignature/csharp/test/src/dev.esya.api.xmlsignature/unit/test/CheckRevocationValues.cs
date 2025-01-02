using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckRevocationValues
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

        private bool checkCRLRefs(List<ECertificate> certsInSignature, ECRL crl)
        {
            try 
	        {	        
		        for(int i=0;i<certsInSignature.Count;i++)
                {
                    if(crl.indexOf(certsInSignature[i])>=0)
                    {
                        return false;   //certificate is revoked when signing
                    }
                }

                return true;
	        }
	        catch (NoSuchAlgorithmException e)
	        {
        		
		        throw e;
	        }catch(ESYAException e)
	        {
	            throw e;
	        }
        }

        private bool checkOCSPRefs(List<ECertificate> certsInSignature, EOCSPResponse ocspResponse)
        {
            try 
	        {	        
		        for(int i=0;i<certsInSignature.Count;i++)
                {
                    for(int j=0;j<ocspResponse.getSingleResponseCount();j++)
                    {
                        if(ocspResponse.getSingleResponse(j).getCertificateStatus()==2 && ocspResponse.getSingleResponse(j).getCertID().getSerialNumber().Equals(certsInSignature[i].getSerialNumber()))
                        {
                            return false;   //certificate is revoked when signing
                        }
                    }
                }

                return true;
	        }
	        catch (NoSuchAlgorithmException e)
	        {
        		
		        throw e;
	        }catch(ESYAException e)
	        {
	            throw e;
	        }
        }

        public bool checkRevocationValues(String baseDir, String fileName)
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir+fileName)),
                    new Context(baseDir)) ;

                SigningCertificate signingCertificate = (SigningCertificate)signature.QualifyingProperties.SignedSignatureProperties.SigningCertificate;

                List<ECertificate> certsInSignature = new List<ECertificate>();
                KeyInfo keyInfo = signature.KeyInfo;
                for(int i = 0;i<keyInfo.ElementCount;i++)
                {
                    X509Data data = (X509Data)keyInfo.get(i);
                    for(int j=0;j<data.ElementCount;j++)
                    {
                        X509DataElement xde = data.get(j);
                        if(xde.GetType() == typeof (X509Certificate))
                        {
                            X509Certificate crt = (X509Certificate)xde;
                            certsInSignature.Add(new ECertificate(crt.CertificateBytes));
                        }
                    }
                }

                RevocationValues revocationValues = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.RevocationValues;

                int crlCount = revocationValues.CRLValueCount;

                for(int i=0;i<crlCount;i++)
                {
                    if(!checkCRLRefs(certsInSignature,revocationValues.getCRL(i)))
                    {
                        return false;
                    }
                }

                int ocspCount =revocationValues.OCSPValueCount;
                for(int i=0;i<ocspCount;i++)
                {
                    if(!checkOCSPRefs(certsInSignature, revocationValues.getOCSPResponse(i)))
                    {
                        return false;
                    }
                }

                return true;
            }
            catch (NoSuchAlgorithmException e)
            {
                throw e;
            }catch(ESYAException e)
            {
                throw e;
            }catch(Exception e)
            {
                throw e;
            }
        }

        [Test]
        public void a_checkSignatureValueForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
        public void b_checkSignatureValueForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
        public void c_checkSignatureValueForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
        public void d_checkSignatureValueForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
        public void e_checkSignatureValueForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
        public void f_checkSignatureValueForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkRevocationValues(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "RevocationValuesCheckResult");
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
