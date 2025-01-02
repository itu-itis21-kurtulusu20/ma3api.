using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.help;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    class UpgradeESTWithTestLicense
    {
        private static String BASEDIR;
        private static String ROOTDIR;
        private static String SIGNATUREFILENAME;
        private static String SIGNATUREFILE_TOBE_UPGRADED;

        private static BaseParameterGetter bpg;

        private static int caseNum;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
           
            BASEDIR = bpg.getBaseDir();
            ROOTDIR = bpg.getRootDir();
            XmlSignatureTestHelper.getInstance().loadTestLicense();
        }

        [SetUp]
        public void setUp()
        {
        }
      
        [Test]
        public void a_upgradeESTEnveloping()
        {
            SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE;
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_UPGRADED_WITH_TEST_LICENSE;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR));
                signature.upgradeToXAdES_T();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                string message = e.Message;
                if (message.Contains("SignatureTimeStamp"))
                {
                    Assert.True(true);
                }
                else
                {
                    SupportClass.WriteStackTrace(e, Console.Error);
                    Assert.True(false);
                }
            }
            catch (FileNotFoundException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void b_upgradeESTEnveloped()
        {
            SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE;
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_UPGRADED_WITH_TEST_LICENSE;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR));
                signature.upgradeToXAdES_T();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                string message = e.Message;
                if (message.Contains("SignatureTimeStamp"))
                {
                    Assert.True(true);
                }
                else
                {
                    SupportClass.WriteStackTrace(e, Console.Error);
                    Assert.True(false);
                }
            }
            catch (FileNotFoundException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void c_upgradeESTDetached()
        {
            SIGNATUREFILE_TOBE_UPGRADED = UnitTestParameters.BES_DETACHED_SIGNED_WITH_FREE_LICENSE;
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_UPGRADED_WITH_TEST_LICENSE;
            try
            {
                XMLSignature signature = XMLSignature.parse(
                    new FileDocument(new FileInfo(BASEDIR + SIGNATUREFILE_TOBE_UPGRADED)),
                    new Context(BASEDIR));
                signature.upgradeToXAdES_T();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                string message = e.Message;
                if (message.Contains("SignatureTimeStamp"))
                {
                    Assert.True(true);
                }
                else
                {
                    SupportClass.WriteStackTrace(e, Console.Error);
                    Assert.True(false);
                }
            }
            catch (FileNotFoundException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
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
