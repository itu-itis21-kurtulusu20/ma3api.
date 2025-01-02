using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

namespace test.esya.api.xmlsignature.creation
{
    [TestFixture]
    class T06_SignWithESXL : XMLSignatureTestBase
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

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL);

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

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL);

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

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL);

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test]
        public void testCreateEnveloping_TwoSteps()
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());
            signature.SigningTime = DateTime.Now;

            byte[] dtbs = signature.initAddingSignature(SignatureAlg.RSA_SHA256, null);

            signature.write(signatureBytes);

            BaseSigner signer = getSignerInterface(SignatureAlg.RSA_SHA256, null);

            byte[] signatureValue = signer.sign(dtbs);
            finishSigning(signatureBytes.ToArray(), context, signatureValue);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        private void finishSigning(byte[] bsdBytes, Context context, byte[] signatureValue)
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
            XMLSignature signature = XMLSignature.parse(xmlDocument, context);

            signature.finishAddingSignature(signatureValue);
            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL);

            signatureBytes = new MemoryStream();
            signature.write(signatureBytes);
        }
    }
}
