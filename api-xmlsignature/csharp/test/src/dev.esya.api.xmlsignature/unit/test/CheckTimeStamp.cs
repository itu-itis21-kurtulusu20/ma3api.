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
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckTimeStamp
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
                case 0 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME; break;
                case 1 : SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME; break;
                case 2 : SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME; break;
                case 3 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME; break;
                case 4 : SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME; break;
                case 5 : SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME; break;
                case 6 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME; break;
                case 7 : SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME; break;
                case 8 : SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME; break;
                case 9 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME; break;
                case 10: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 11 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 12 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 13 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 14 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        public bool checkTimeStampValue(String baseDir, String fileName) //throws ESYAException, NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException
        {
            try
            {
                XMLSignature signature = XMLSignature.parse(
                            new FileDocument(new FileInfo(baseDir + fileName)),
                            new Context(baseDir));

                SignatureTimeStamp timeStamp = signature.QualifyingProperties.UnsignedProperties.UnsignedSignatureProperties.getSignatureTimeStamp(0);

                int timeStampCount = timeStamp.EncapsulatedTimeStampCount;
                bool testResult = false;
                System.Console.WriteLine(fileName + " " + timeStampCount);
                for (int i = 0; i < timeStampCount; i++)
                {
                    EncapsulatedTimeStamp encTimeStamp = timeStamp.getEncapsulatedTimeStamp(i);
                    DigestAlg alg = encTimeStamp.DigestAlgorithm;
                    byte[] digestValue = encTimeStamp.DigestValue;
                    DateTime cal = encTimeStamp.Time;
                    if(!encTimeStamp.TimeStamp)
                    {
                        return false;
                    }
                }

                return true;
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        [Test]
        public void a_checkSignatureValueForESTEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
            try {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void b_checkSignatureValueForESTEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void c_checkSignatureValueForESTDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void d_checkSignatureValueForESCEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void e_checkSignatureValueForESCEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void f_checkSignatureValueForESCDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void g_checkSignatureValueForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void h_checkSignatureValueForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void i_checkSignatureValueForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void j_checkSignatureValueForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void k_checkSignatureValueForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void l_checkSignatureValueForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void m_checkSignatureValueForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void n_checkSignatureValueForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
        public void o_checkSignatureValueForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                bool testResult = checkTimeStampValue(BASEDIR, SIGNATUREFILENAME);
                Assert.AreEqual(true, testResult, "TimesStampCheckResult");
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
