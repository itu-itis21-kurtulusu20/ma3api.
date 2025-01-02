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
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.help;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    class SignBESWithFreeLicense
    {
        private static ECertificate CERTIFICATE;
        private static String BASEDIR;
        private static String ROOTDIR;
        private static String SIGNATUREFILENAME;
        //private static String SIGNATUREFILE_TOBE_UPGRADED;
        private static String PLAINFILENAME;
        private static String PLAINFILEMIMETYPE;
        private const String ENVELOPE_XML = "<envelope>\n" + "  <data id=\"data1\">\n" + "    <item>Item 1</item>\n" + "    <item>Item 2</item>\n" + "    <item>Item 3</item>\n" + "  </data>\n" + "</envelope>\n";
     
        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

            CERTIFICATE = bpg.getCertificate();
            BASEDIR = bpg.getBaseDir();
            ROOTDIR = bpg.getRootDir();

            XmlSignatureTestHelper.getInstance().loadFreeLicense();
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
        public void a_createBESEnveloping()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIGNED_WITH_FREE_LICENSE;
            try
            {
                //loadFreeLicense();
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature);
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            }
            catch (XMLSignatureException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
            catch (FileNotFoundException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
            catch (XMLSignatureRuntimeException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void b_createBESEnveloped()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPED_SIGNED_WITH_FREE_LICENSE;
            try
            {
                //loadFreeLicense();
                XmlDocument envelopeDoc = newEnvelope();
                Context context = context = new Context(BASEDIR);
                context.Document = envelopeDoc;
                XMLSignature signature = new XMLSignature(context, false);
                envelopeDoc.DocumentElement.AppendChild(signature.Element);
                signature.addDocument("#data1", "text/xml", false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature);
                envelopeDoc.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(true);
            }
            catch (NullReferenceException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
            catch (ESYAException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
            catch (FileNotFoundException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void c_createBESDetached()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIGNED_WITH_FREE_LICENSE;
            try
            {
                //loadFreeLicense();
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSigner(signature);
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            }
            catch (XMLSignatureException e)
            {
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
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
        }

    }
}
