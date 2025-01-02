using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

namespace test.esya.api.xmlsignature.creation
{
    [TestFixture]
    class T01_SignWithBES : XMLSignatureTestBase
    {

        public static Object[] TestCases =
        {
            new object[] {SignatureAlg.RSA_SHA1, null},
            new object[] {SignatureAlg.RSA_SHA256, null},
            new object[] {SignatureAlg.RSA_SHA384, null},
            new object[] {SignatureAlg.RSA_SHA512, null},


            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512)}
        };

        [SetUp]
        public void setUp()
        {
            signatureBytes = new MemoryStream();
        }

        [Test, TestCaseSource("TestCases")]
        public void a_createEnveloping(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(signatureAlg, rsaPssParams));
            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void a_createEnveloping_TwoSteps(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());

            byte[] dtbs = signature.initAddingSignature(signatureAlg, rsaPssParams);
            signature.write(signatureBytes);

            BaseSigner signer = getSignerInterface(signatureAlg, rsaPssParams);
            byte[] signatureValue = signer.sign(dtbs);
            finishSigning(signatureBytes.ToArray(), context, signatureValue);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        private void finishSigning(byte[] bsdBytes, Context context, byte[] signatureValue)
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
            XMLSignature signature = XMLSignature.parse(xmlDocument, context);

            signature.finishAddingSignature(signatureValue);

            signatureBytes = new MemoryStream();
            signature.write(signatureBytes);
        }

        [Test, TestCaseSource("TestCases")]
        public void b_createEnveloped(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext();
            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(signatureAlg, rsaPssParams));

            envelopeDoc.Save(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void c_createDetached(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getSignerCertificate());
            signature.sign(getSignerInterface(signatureAlg, rsaPssParams));

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void createCounterSignature(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {

            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);

            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());
            signature.SigningTime = DateTime.Now;
            signature.sign(getSignerInterface(signatureAlg, rsaPssParams));

            // create counter signatures for signature
            XMLSignature counter = signature.createCounterSignature();
            counter.addKeyInfo(getSecondSignerCertificate());
            counter.sign(getSecondSignerInterface(signatureAlg, rsaPssParams));

            // output final document
            signature.write(signatureBytes);
            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }
    }
}
