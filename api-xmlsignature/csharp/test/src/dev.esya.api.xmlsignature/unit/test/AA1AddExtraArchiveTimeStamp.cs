using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit;
using NUnit.Framework;
using NUnit.Framework.Constraints;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class AA1AddExtraArchiveTimeStamp
    {
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;
        private static String ENVELOPE_XML =
                "<envelope>\n" +
                        "  <data id=\"data1\">\n" +
                        "    <item>Item 1</item>\n"+
                        "    <item>Item 2</item>\n"+
                        "    <item>Item 3</item>\n"+
                        "  </data>\n" +
                        "</envelope>\n";

        private static int caseNum;
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            BASEDIR = bpg.getBaseDir();

            caseNum=0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch (caseNum)
            {
                case 0: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME; break;
                case 1: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME; break;
                case 2: SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
        }

        // create sample envelope xml
        // that will contain signature inside
        private XmlDocument newEnvelope() {
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
        public void a_createEnvelopingAddArchiveTimeStamp()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR+"/xades_esa_enveloping.xml")),
                        new Context(BASEDIR)) ;
                signature.addArchiveTimeStamp();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            } catch (FileNotFoundException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void b_createEnvelopedAddArchiveTimeStamp()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_ENVELOPED_SIG_FILE_NAME;
            try {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + "/xades_esa_enveloped.xml")),
                        new Context(BASEDIR));
                XmlDocument document = signature.Document;

                signature.addArchiveTimeStamp();

                document.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(true);
            } catch (XMLSignatureException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            } catch (ESYAException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            } catch (FileNotFoundException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void c_createDetachedAddArchiveTimeStamp()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ARCHIVE_TS_DETACHED_SIG_FILE_NAME;
            try {
                XMLSignature signature = XMLSignature.parse(
                        new FileDocument(new FileInfo(BASEDIR + "/xades_esa_detached.xml")),
                        new Context(BASEDIR));
                signature.addArchiveTimeStamp();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            } catch (FileNotFoundException e) {
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
