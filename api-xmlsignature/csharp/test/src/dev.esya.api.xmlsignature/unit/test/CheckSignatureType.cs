using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class CheckSignatureType
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
                case 13 : SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 14 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 15 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 16 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 17 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
                case 18 : SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        [Test]
        public void a_checkTypeForBESEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR+SIGNATUREFILENAME)),
                        new Context(BASEDIR)) ;
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_BES, type, "Signature Type");
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_checkTypeForBESEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_BES, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void c_checkTypeForBESDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_BES, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void d_checkTypeForESTEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_T, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void e_checkTypeForESTEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_T, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void f_checkTypeForESTDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_T, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void g_checkTypeForESCEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_C, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void h_checkTypeForESCEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_C, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void i_checkTypeForESCDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_C, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void j_checkTypeForESXEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void k_checkTypeForESXEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void l_checkTypeForESXDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void m_checkTypeForESXLEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X_L, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void n_checkTypeForESXLEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X_L, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void o_checkTypeForESXLDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_X_L, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void p_checkTypeForESAEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_A, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void r_checkTypeForESAEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_A, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void s_checkTypeForESADetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_A, type, "Signature Type");
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void t_checkTypeForBESEDefterEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILENAME)),
                        new Context(BASEDIR));
                SignatureType type = signature.SignatureType;
                Assert.AreEqual(SignatureType.XAdES_BES, type, "Signature Type");
            }
            catch (XMLSignatureException e)
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
