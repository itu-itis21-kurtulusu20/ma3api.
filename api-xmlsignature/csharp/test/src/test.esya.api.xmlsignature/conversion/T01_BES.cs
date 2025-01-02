
using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace test.esya.api.xmlsignature.conversion
{
    class T01_BES : XMLSignatureTestBase
    {
        private readonly String DIRECTORY = "T:\\api-xmlsignature\\test-output\\csharp\\ma3\\conversion\\";
        private String PLAINFILENAME = "../../../../docs/samples/signatures/sample.txt";

        [Test]
        public void a_createEnveloping()
        {
            String FILE_PATH = DIRECTORY + "BES_Enveloping.xml";

            Context context = CreateContext(DIRECTORY);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            FileStream fs = new FileStream(FILE_PATH, FileMode.Create);
            signature.write(fs);
            fs.Close();

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, FILE_PATH);
        }

        [Test]
        public void b_createEnveloped()
        {
            String FILE_PATH = DIRECTORY + "BES_Enveloped.xml";

            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext(DIRECTORY);
            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            FileStream fs = new FileStream(FILE_PATH, FileMode.Create);
            envelopeDoc.Save(fs);
            fs.Close();

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, FILE_PATH);
        }

        [Test]
        public void c_createDetached()
        {
            String FILE_PATH = DIRECTORY + "BES_Detached.xml";

            Context context = CreateContext(DIRECTORY);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            FileStream fs = new FileStream(FILE_PATH, FileMode.Create);
            signature.write(fs);
            fs.Close();

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, FILE_PATH);
        }
    }
}
