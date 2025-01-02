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
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    [TestFixture]
    class A01SignWithDifferentCERPRIVATEKEY
    {
        private static ECertificate CERTIFICATE;
        //private static BaseSigner BASESIGNER;
        //private static PrivateKey PRIVATEKEY;
        private static String BASEDIR;
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
            PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

            caseNum=0;
        }

        [SetUp]
        public void setUp()
        {
            /*switch (caseNum)
            {
                case 0: SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME; break;
                case 1: SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME; break;
                case 2: SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
                case 3: SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME; break;
                default: SIGNATUREFILENAME = UnitTestParameters.ERROR_SIG_FILE_NAME; break;
            }
            caseNum++;*/
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
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                bpg.signWithDifferentBaseSigner(signature);
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            } catch (XMLSignatureException e) {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (System.NullReferenceException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_createEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIG_FILE_NAME;
            try
            {
                XmlDocument envelopeDoc = newEnvelope();
                Context context = context = new Context(BASEDIR);
                context.Document = envelopeDoc;
                XMLSignature signature = new XMLSignature(context, false);
                envelopeDoc.DocumentElement.AppendChild(signature.Element);
                signature.addDocument("#data1", "text/xml", false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithDifferentBaseSigner(signature);
                envelopeDoc.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (System.NullReferenceException e)
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
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithDifferentBaseSigner(signature);
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            } catch (XMLSignatureException e) {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (System.NullReferenceException e)
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
        }
    }
}
