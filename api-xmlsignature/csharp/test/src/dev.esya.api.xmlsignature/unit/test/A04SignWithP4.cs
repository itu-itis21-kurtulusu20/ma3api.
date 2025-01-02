using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;
using NUnit.Framework;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class SignWithP4
    {
        private static IResolver POLICY_RESOLVER;
        private static String POLICY_FILE_PATH;
        private static ECertificate CERTIFICATE;
        private static String BASEDIR;
        private static String ROOTDIR;
        private static String SIGNATUREFILENAME;
        private static String PLAINFILENAME;
        private static String PLAINFILEMIMETYPE;
        private const String ENVELOPE_XML = "<envelope>\n" + "  <data id=\"data1\">\n" + "    <item>Item 1</item>\n" + "    <item>Item 2</item>\n" + "    <item>Item 3</item>\n" + "  </data>\n" + "</envelope>\n";

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            CERTIFICATE = bpg.getCertificate();
            BASEDIR = bpg.getBaseDir();
            ROOTDIR = bpg.getRootDir();
            POLICY_RESOLVER = bpg.getPolicyResolver();
            PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

            caseNum = 0;
        }

        [SetUp]
        public void setUp()
        {
        }

        // create sample envelope xml
        // that will contain signature inside
        private XmlDocument newEnvelope()
        {
            try
            {
                XmlDocument xmlDocument = new XmlDocument();
                xmlDocument.PreserveWhitespace = true;
                xmlDocument.LoadXml(ENVELOPE_XML);
                //xmlDocument.Load(new System.IO.MemoryStream(SupportClass.ToByteArray(ENVELOPE_XML)));
                return xmlDocument;
            }
            catch (System.Exception x)
            {
                // we shouldnt be here if ENVELOPE_XML is valid
                SupportClass.WriteStackTrace(x, Console.Error);
            }
            throw new ESYAException("Cant construct envelope xml ");
        }


        [Test]
        public void a_createEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPING_SIG_FILE_NAME;
            POLICY_FILE_PATH = ROOTDIR + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME4;
            try
            {
                Context context = new Context(BASEDIR);
                context.addExternalResolver(POLICY_RESOLVER);
                XMLSignature signature = new XMLSignature(context);
                signature.setPolicyIdentifier(UnitTestParameters.OID_POLICY_P4, "Uzun Dönemli ve ÇİSDuP Kontrollü Güvenli Elektronik İmza Politikası", POLICY_FILE_PATH);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                bpg.signWithBaseSigner(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (XMLSignatureRuntimeException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_createEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_ENVELOPED_SIG_FILE_NAME;
            POLICY_FILE_PATH = ROOTDIR + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME4;
            try
            {
                XmlDocument envelopeDoc = newEnvelope();
                Context context = context = new Context(BASEDIR);
                context.addExternalResolver(POLICY_RESOLVER);
                context.Document = envelopeDoc;
                XMLSignature signature = new XMLSignature(context, false);
                signature.setPolicyIdentifier(UnitTestParameters.OID_POLICY_P4, "Uzun Dönemli ve ÇİSDuP Kontrollü Güvenli Elektronik İmza Politikası", POLICY_FILE_PATH);
                envelopeDoc.DocumentElement.AppendChild(signature.Element);
                signature.addDocument("#data1", "text/xml", false);
                signature.addKeyInfo(CERTIFICATE);
                bpg.signWithBaseSigner(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                envelopeDoc.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(true);
            }
            catch (NullReferenceException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (XMLSignatureException e)
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
        }

        [Test]
        public void c_createDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.PROFILE4_DETACHED_SIG_FILE_NAME;
            POLICY_FILE_PATH = ROOTDIR + "\\docs\\config\\policy\\" + UnitTestParameters.POLICY_FILE_NAME4;
            try
            {
                Context context = new Context(BASEDIR);
                context.addExternalResolver(POLICY_RESOLVER);
                XMLSignature signature = new XMLSignature(context);
                signature.setPolicyIdentifier(UnitTestParameters.OID_POLICY_P4, "Uzun Dönemli ve ÇİSDuP Kontrollü Güvenli Elektronik İmza Politikası", POLICY_FILE_PATH);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            }
            catch (XMLSignatureException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
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
