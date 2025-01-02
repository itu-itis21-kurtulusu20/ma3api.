using System;
using System.IO;
using System.Security.Cryptography.X509Certificates;
using System.Xml;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using NUnit.Framework;
using test.esya.api.xmlsignature;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test.signerHelpers;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.help;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.signerHelpers;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.test
{
    class AA0SignEDefter : XMLSignatureTestBase
    {
        private static ECertificate CERTIFICATE;
        private static String SIGNATUREFILENAME;
        private static String PLAINFILENAME;
        private static String PLAINFILEMIMETYPE;
        private static String SIGNATURE_FILE_TOBE_UPGRADED;
        private static String SIGNATUREOUTPUTFOLDER;

        private static BaseParameterGetter bpg;

        [TestFixtureSetUp]
        public static void initialize()
        {
            bpg = ParameterGetterFactory.getParameterGetter();
            CERTIFICATE = bpg.getCertificate();
            PLAINFILENAME =UnitTestParameters.PLAINFILENAME;
            PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

            XmlSignatureTestHelper testHelper = XmlSignatureTestHelper.getInstance();
            SIGNATUREOUTPUTFOLDER = testHelper.getSignatureOutputDir();
        }

        // create sample envelope xml
        // that will contain signature inside
        private XmlDocument newEnvelope()
        {
            XmlDocument xmlDocument = new XmlDocument();
            xmlDocument.PreserveWhitespace = true;
            XmlReader reader = XmlReader.Create(SIGNATUREOUTPUTFOLDER + UnitTestParameters.TEST_EDEFTER_FILE_NAME);
            xmlDocument.Load(reader);
            reader.Close();
            return xmlDocument;   
        }

        [Test]
        public void a_createEnvelopedBesTransformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;
           
            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext();
            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            signature.SigningTime = DateTime.Now;
            envelopeDoc.DocumentElement.AppendChild(signature.Element);

            Transforms transform = new Transforms(context);
            transform.addTransform(new Transform(context,TransformType.ENVELOPED.Url));
            X509Certificate2 msCert = CERTIFICATE.asX509Certificate2();

            signature.addDocument("", "text/xml", transform, DigestMethod.SHA_256, false);
            signature.addKeyInfo(msCert.PublicKey.Key);
            signature.addKeyInfo(CERTIFICATE);

            KeyInfo keyInfo = signature.createOrGetKeyInfo();
            int elementCount = keyInfo.ElementCount;
            for (int i = 0; i < elementCount; i++)
            {
                KeyInfoElement kiElement = keyInfo.get(i);
                if(kiElement.GetType().IsAssignableFrom(typeof(X509Data)))
                {
                    X509Data x509Data = (X509Data) kiElement;
                    X509SubjectName x509SubjectName = new X509SubjectName(context,CERTIFICATE.getSubject().stringValue());
                    x509Data.add(x509SubjectName);
                    break;
                }
            }

            bpg.signWithBaseSigner(signature);
            envelopeDoc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
           
        }

        [Test]
        public void b_createEnvelopedEstTransformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_EST_ENVELOPED_SIG_FILE_NAME;
            SIGNATURE_FILE_TOBE_UPGRADED = UnitTestParameters.TEST_EDEFTER_BES_ENVELOPED_SIG_FILE_NAME;

            Context context = CreateContext();

            XmlDocument doc = new XmlDocument();
            doc.PreserveWhitespace = true;
            doc.Load(SIGNATUREOUTPUTFOLDER + SIGNATURE_FILE_TOBE_UPGRADED);

            XmlNode signatureElement = doc.GetElementsByTagName("ds:Signature").Item(0);

            XMLSignature signature = new XMLSignature((XmlElement)signatureElement, context);

            signature.upgradeToXAdES_T();

            doc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
        }

        [Test]
        public void c_createEnvelopedEscTransformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESC_ENVELOPED_SIG_FILE_NAME;
            SIGNATURE_FILE_TOBE_UPGRADED = UnitTestParameters.TEST_EDEFTER_EST_ENVELOPED_SIG_FILE_NAME;

            Context context = CreateContext();

            XmlDocument doc = new XmlDocument();
            doc.PreserveWhitespace = true;
            doc.Load(SIGNATUREOUTPUTFOLDER + SIGNATURE_FILE_TOBE_UPGRADED);

            XmlNode signatureElement = doc.GetElementsByTagName("ds:Signature").Item(0);

            XMLSignature signature = new XMLSignature((XmlElement)signatureElement, context);

            signature.upgradeToXAdES_C();

            doc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
        }

        [Test]
        public void d_createEnvelopedEsxTransformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESX_ENVELOPED_SIG_FILE_NAME;
            SIGNATURE_FILE_TOBE_UPGRADED = UnitTestParameters.TEST_EDEFTER_ESC_ENVELOPED_SIG_FILE_NAME;

            Context context = CreateContext();

            XmlDocument doc = new XmlDocument();
            doc.PreserveWhitespace = true;
            doc.Load(SIGNATUREOUTPUTFOLDER + SIGNATURE_FILE_TOBE_UPGRADED);

            XmlNode signatureElement = doc.GetElementsByTagName("ds:Signature").Item(0);

            XMLSignature signature = new XMLSignature((XmlElement)signatureElement, context);

            signature.upgradeToXAdES_X1();

            doc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
        }

        [Test]
        public void e_createEnvelopedEsxlTranformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESXL_ENVELOPED_SIG_FILE_NAME;
            SIGNATURE_FILE_TOBE_UPGRADED = UnitTestParameters.TEST_EDEFTER_ESX_ENVELOPED_SIG_FILE_NAME;

            Context context = CreateContext();

            XmlDocument doc = new XmlDocument();
            doc.PreserveWhitespace = true;
            doc.Load(SIGNATUREOUTPUTFOLDER + SIGNATURE_FILE_TOBE_UPGRADED);

            XmlNode signatureElement = doc.GetElementsByTagName("ds:Signature").Item(0);

            XMLSignature signature = new XMLSignature((XmlElement)signatureElement, context);

            signature.upgradeToXAdES_XL();

            doc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
        }

        [Test]
        public void f_createEnvelopedEsaTransformedEDefter()
        {
            SIGNATUREFILENAME = UnitTestParameters.TEST_EDEFTER_ESA_ENVELOPED_SIG_FILE_NAME;
            SIGNATURE_FILE_TOBE_UPGRADED = UnitTestParameters.TEST_EDEFTER_ESXL_ENVELOPED_SIG_FILE_NAME;

            Context context = CreateContext();

            XmlDocument doc = new XmlDocument();
            doc.PreserveWhitespace = true;
            doc.Load(SIGNATUREOUTPUTFOLDER + SIGNATURE_FILE_TOBE_UPGRADED);

            XmlNode signatureElement = doc.GetElementsByTagName("ds:Signature").Item(0);

            XMLSignature signature = new XMLSignature((XmlElement)signatureElement, context);

            signature.upgradeToXAdES_A();

            doc.Save(SIGNATUREOUTPUTFOLDER + SIGNATUREFILENAME);
        }
        
    }
}
