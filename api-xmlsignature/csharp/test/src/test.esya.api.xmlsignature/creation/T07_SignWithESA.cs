using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace test.esya.api.xmlsignature.creation
{
    [TestFixture]
    class T07_SignWithESA : XMLSignatureTestBase
    {
        [SetUp]
        public void setUp()
        {
            signatureBytes = new MemoryStream();
        }


        [Test]
        public void a_createEnveloping()
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_A);

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test]
        public void b_createEnveloped()
        {
            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext();
            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_A);

            envelopeDoc.Save(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test]
        public void c_createDetached()
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_A);

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }
    }
}
