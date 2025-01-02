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
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]    
    class CheckCompleteRevocationRefsAndValues
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

        private bool checkCRLRefs(CRLReference crlReference, ECRL crl)
        {
            try
            {
                DigestAlg digestAlg = crlReference.DigestMethod.Algorithm;
                byte [] crlDigestValueInSig = crlReference.DigestValue;
                String crlIssuerInSig = crlReference.CRLIdentifier.Issuer;
                DateTime issueTimeInSig = crlReference.CRLIdentifier.IssueTime;
                BigInteger crlNumberInSig = crlReference.CRLIdentifier.Number;

                byte [] encodedCRL = crl.getEncoded();
                BigInteger crlNumber = crl.getCRLNumber();
                String crlIssuer = crl.getIssuer().stringValue();
                var thisUpdate = crl.getThisUpdate();
                if (thisUpdate != null)
                {
                    DateTime issueTime = (DateTime)thisUpdate;

                    //MessageDigest digester = MessageDigest.getInstance(digestAlg);
                    //byte [] crlDigestValue = digester.digest(encodedCRL);
                    byte[] crlDigestValue = DigestUtil.digest(digestAlg, encodedCRL);

                    if (!ArrayUtil.Equals(crlDigestValueInSig, crlDigestValue))
                    {
                        return false;
                    }
                    if(!crlIssuerInSig.Equals(crlIssuer))
                    {
                        return false;
                    }

                    if(!crlNumberInSig.Equals(crlNumber))
                    {
                        return false;
                    }

                    if(!issueTimeInSig.Equals(issueTime))
                    {
                        return false;
                    }
                }
                else
                {
                    throw new ESYAException("CRL den zaman bilgisi alinamadi");
                }

                return true;
            }
            catch (NoSuchAlgorithmException e)
            {
                
                throw e;
            }
        }

        private bool checkOCSPRefs(OCSPReference ocspReference, EOCSPResponse ocspResponse)
        {
            try
            {
                DigestAlg digestAlg = ocspReference.DigestAlgAndValue.DigestMethod.Algorithm;
                byte [] ocspDigestValueInSig = ocspReference.DigestAlgAndValue.DigestValue;
                byte [] responderIDInSig = ocspReference.OCSPIdentifier.ResponderID.ByKey;
                DateTime ocspProducedAtInSig = ocspReference.OCSPIdentifier.ProducedAt;

                byte [] responseBytes = ocspResponse.getEncoded();
                DateTime? producedAt = ocspResponse.getBasicOCSPResponse().getProducedAt();
                if (producedAt != null)
                {
                    DateTime ocspProducedAt = (DateTime)producedAt;
                    byte [] responderID = ocspResponse.getBasicOCSPResponse().getTbsResponseData().getResponderIdByKey();

                    //MessageDigest digester = MessageDigest.getInstance(digestAlg);
                    //byte [] ocspDigestValue = digester.digest(responseBytes);
                    byte[] ocspDigestValue = DigestUtil.digest(digestAlg, responseBytes);

                    if (!ArrayUtil.Equals(ocspDigestValueInSig, ocspDigestValue))
                    {
                        return false;
                    }

                    if (!ArrayUtil.Equals(responderIDInSig, responderID))
                    {
                        return false;
                    }

                    TimeSpan timeSpan = ocspProducedAtInSig.Subtract(ocspProducedAt);
                    if (timeSpan.Seconds != 0 || timeSpan.Minutes != 0 || timeSpan.Hours != 0 || timeSpan.Days != 0 )
                    {
                        return false;
                    }
                    //if(!ocspProducedAtInSig.Equals(ocspProducedAt))
                    //{
                    //    return false;
                    //}
                }
                else
                {
                    throw new ESYAException("OCSP den zaman bilgisi alinamadi");
                }
                return true;
            }catch(NoSuchAlgorithmException e)
            {
                throw e;
            } catch(ESYAException e)
            {
                throw e;
            }
        }

        public bool checkCompleteRevocationRefs(String baseDir, String fileName)
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir + fileName)),
                    new Context(baseDir));


                CompleteRevocationRefs completeRevocationRefs = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.CompleteRevocationRefs;

                RevocationValues revocationValues = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.RevocationValues;

                int crlRefsCount = completeRevocationRefs.CRLReferenceCount;

                for (int i = 0; i < crlRefsCount; i++)
                {
                    if (!checkCRLRefs(completeRevocationRefs.getCRLReference(i), revocationValues.getCRL(i)))
                    {
                        return false;
                    }
                }

                int ocspRefsCount = completeRevocationRefs.OCSPReferenceCount;
                for (int i = 0; i < ocspRefsCount; i++)
                {
                    if (!checkOCSPRefs(completeRevocationRefs.getOCSPReference(i), revocationValues.getOCSPResponse(i)))
                    {
                        return false;
                    }
                }

                return true;
            } catch(Exception e)
            {
                throw e;
            }
        }

        [Test]
        public void a_checkSignatureValueForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
                bool testResult = checkCompleteRevocationRefs(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "CompleteRevocationRefsCheckResult");
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
