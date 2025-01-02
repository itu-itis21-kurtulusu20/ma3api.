using System;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Xml;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckSigAndRefsTimeStamp
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
                case 0: SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME; break;
                case 1: SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME; break;
                case 2: SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME; break;
                case 3: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
                case 4: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 5: SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 6: SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 7: SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 8: SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        private bool checkEncapsulatedTS(EncapsulatedTimeStamp encapsulatedTS, byte [] contentToTSed)
        {
            try
            {
               // String str = Convert.ToString(contentToTSed);
               // File.AppendAllText("C:\\testdeneme.txt","Content To Be Timestamped:\n");
               // File.AppendAllText("C:\\testdeneme.txt", str + "\n");
                String strEncoded = Convert.ToBase64String(contentToTSed);
		        DigestAlg digestAlg = encapsulatedTS.DigestAlgorithm;
                byte [] digestValue = encapsulatedTS.DigestValue;
                String str1 = Convert.ToBase64String(digestValue);
                //File.AppendAllText("C:\\testdeneme.txt","Digest Value taken from EncapsulatedTimeStamp:\n");
                //File.AppendAllText("C:\\testdeneme.txt",str1+"\n");
                DateTime time = encapsulatedTS.Time;
                int certSize = encapsulatedTS.SignedData.getCertificates().Count;
                for(int i=0;i<certSize;i++)
                {
                    ECertificate certificate = encapsulatedTS.SignedData.getCertificates()[i];
                    System.Console.WriteLine("Issuer: "+certificate.getIssuer().stringValue());
                    System.Console.WriteLine("Subject: "+certificate.getSubject().stringValue());
                    System.Console.WriteLine("Serial Number: "+certificate.getSerialNumberHex());
                    bool isTimeStamping = certificate.isTimeStampingCertificate();
                    bool isCA = certificate.isCACertificate();
                    if(!isTimeStamping && !isCA)
                    {
                        return false;
                    }
                }

                if(!encapsulatedTS.TimeStamp)
                {
                    return false;
                }

                //MessageDigest digester = MessageDigest.getInstance(digestAlg);
                //byte [] digested =digester.digest(contentToTSed);
                byte[] digested = DigestUtil.digest(digestAlg, contentToTSed);
              //  String str2 = Convert.ToBase64String(digested);
               // File.AppendAllText("C:\\testdeneme.txt","Digest Value taken from Content To Be Timestamped:\n");
               // File.AppendAllText("C:\\testdeneme.txt", str2 + "\n");

                if (!ArrayUtil.Equals(digested, digestValue))
                {
                    return false;
                }

                return true;
	        }
	        catch (XMLSignatureException e)
	        {
		        throw e;
	        }catch(NoSuchAlgorithmException e)
	        {
	            throw e;
	        }
        }

        private bool checkTimeStamp(SigAndRefsTimeStamp timeStamp, XMLSignature signature)
        {
            try
            {
		        byte [] contentToTSed = timeStamp.getContentForTimeStamp(signature);
                int uzunluk = contentToTSed.Length;
	            String str1 = Convert.ToBase64String(contentToTSed);
                //String c14nMethod =  timeStamp.getCanonicalizationMethod().name();
                int encapsulatedTSCount =  timeStamp.EncapsulatedTimeStampCount;
                for(int i=0;i<encapsulatedTSCount;i++)
                {
                    if(!checkEncapsulatedTS(timeStamp.getEncapsulatedTimeStamp(i),contentToTSed))
                    {
                        return false;
                    }
                }

                return true;
	        }
	        catch (XMLSignatureException e)
	        {
		        throw e;
	        }catch(NoSuchAlgorithmException e)
	        {
	            throw e;
	        }
        }

        public bool checkSigAndRefsTimeStamp(String baseDir, String fileName)
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(baseDir + fileName)),
                    new Context(baseDir));


                int timeStampCount = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.SigAndRefsTimeStampCount;

                for (int i = 0; i < timeStampCount; i++)
                {
                    if (!checkTimeStamp(signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.getSigAndRefsTimeStamp(i), signature))
                    {
                        return false;
                    }
                }

                return true;
            }
            catch (ESYAException e)
            {
                throw e;
            }catch(NoSuchAlgorithmException e)
            {
                throw e;
            }catch(Exception e)
            {
                throw e;
            }
        }

        [Test]
        public void a_checkSigAndRefsTimeStampForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void b_checkSigAndRefsTimeStampForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void c_checkSigAndRefsTimeStampForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void d_checkSigAndRefsTimeStampForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void e_checkSigAndRefsTimeStampForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void f_checkSigAndRefsTimeStampForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void g_checkSigAndRefsTimeStampForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void h_checkSigAndRefsTimeStampForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
        public void i_checkSigAndRefsTimeStampForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkSigAndRefsTimeStamp(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "SigAndRefsTimeStampCheckResult");
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
