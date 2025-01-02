using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace test.esya.api.xmlsignature.creation.ec
{
    [TestFixture]
    public class ESX_EC : XMLSignatureTestBase
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        [SetUp]
        public void setUp()
        {
            signatureBytes = new MemoryStream();
        }

        [Test, TestCaseSource("TestCases")]
        public void testCreateEnveloping(SignatureAlg signatureAlg)
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getECSignerCertificate());
            signature.sign(getECSignerInterface(signatureAlg));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1);

            signature.write(signatureBytes);
            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void testCreateEnveloped(SignatureAlg signatureAlg)
        {
            XmlDocument envelopeDoc = newEnvelope();

            Context context = CreateContext();
            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getECSignerCertificate());
            signature.sign(getECSignerInterface(signatureAlg));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1);

            envelopeDoc.Save(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void testCreateDetached(SignatureAlg signatureAlg)
        {
            Context context = CreateContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(BASE_DIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getECSignerCertificate());
            signature.sign(getECSignerInterface(signatureAlg));

            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_X_Type1);

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }
    }
}
