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
    class A00SignWithRevoked
    {
        private static ECertificate CERTIFICATE;
        private static String BASEDIR;
        private static String SIGNATUREFILENAME;
        private static String PLAINFILENAME;
        private static String PLAINFILEMIMETYPE;
        
        private const String ENVELOPE_XML =
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
            CERTIFICATE = bpg.getCertificateRevoked();
            //BASESIGNER = bpg.getBaseSignerRevoked();
            //PRIVATEKEY = bpg.getPrivateKeyRevoked();
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
                case 13: SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME; break;
                case 14 : SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME; break;
                case 15 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME; break;
                case 16 : SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME; break;
                case 17 : SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME; break;
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
        public void a_createBESEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void b_createBESEnvelopedWithRevokedCertificate()
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
                bpg.signWithBaseSignerRevoked(signature);
                envelopeDoc.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(true);
            }
            catch (System.NullReferenceException e)
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
        public void c_createBESDetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.BES_DETACHED_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }


        [Test]
        public void d_createESTEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                System.Console.WriteLine(e.Message);
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            } catch (FileNotFoundException e) {
                System.Console.WriteLine(e.Message);
                SupportClass.WriteStackTrace(e, Console.Error);
                Assert.True(false);
            }
        }

        [Test]
        public void e_createESTEnvelopedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_ENVELOPED_SIG_FILE_NAME;
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
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                envelopeDoc.Save(BASEDIR + SIGNATUREFILENAME);
                Assert.True(true);
            }
            catch (System.NullReferenceException e)
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
        public void f_createESTDetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.EST_DETACHED_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.write(new FileStream(BASEDIR+SIGNATUREFILENAME, FileMode.Create));
                Assert.True(true);
            } catch (XMLSignatureException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            } catch (FileNotFoundException e) {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void g_createESCEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void h_createESCEnvelopedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_ENVELOPED_SIG_FILE_NAME;
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
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_C();
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
                Assert.True(true);
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
        public void i_createESCDetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESC_DETACHED_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void j_createESXEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void k_createESXEnvelopedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_ENVELOPED_SIG_FILE_NAME;
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
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
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
                Assert.True(true);
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
        public void l_createESXDetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESX_DETACHED_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void m_createESXLEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void n_createESXLEnvelopedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_ENVELOPED_SIG_FILE_NAME;
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
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
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
                Assert.True(true);
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
        public void o_createESXLDetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESXL_DETACHED_SIG_FILE_NAME;
            try
            {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (System.NullReferenceException e)
            {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        
        [Test]
        public void p_createESAEnvelopingWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPING_SIG_FILE_NAME;
            try {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, true);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.upgradeToXAdES_A();
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
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
        }

        [Test]
        public void r_createESAEnvelopedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_ENVELOPED_SIG_FILE_NAME;
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
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.upgradeToXAdES_A();
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
                Assert.True(true);
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
        public void s_createESADetachedWithRevokedCertificate()
        {
            SIGNATUREFILENAME = UnitTestParameters.ESA_DETACHED_SIG_FILE_NAME;
            try
            {
                Context context = new Context(BASEDIR);
                XMLSignature signature = new XMLSignature(context);
                signature.addDocument(BASEDIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
                signature.addKeyInfo(CERTIFICATE);
                //signature.sign(BASESIGNER);
                bpg.signWithBaseSignerRevoked(signature);
                signature.upgradeToXAdES_T();
                signature.upgradeToXAdES_C();
                signature.upgradeToXAdES_X1();
                signature.upgradeToXAdES_XL();
                signature.upgradeToXAdES_A();
                signature.write(new FileStream(BASEDIR + SIGNATUREFILENAME, FileMode.Create));
                Assert.True(false);
            }
            catch (XMLSignatureException e)
            {
                Assert.True(true);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (FileNotFoundException e)
            {
                Assert.True(false);
                SupportClass.WriteStackTrace(e, Console.Error);
            }
            catch (System.NullReferenceException e)
            {
                Assert.True(true);
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
