using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;


namespace test.esya.api.xmlsignature.creation
{
    [TestFixture]
    class T03_SignWithEPES : XMLSignatureTestBase
    {
        public static readonly int[] OID_POLICY_P2 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 1, 1 };

        public static String POLICY_FILE = "T:/api-parent/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf";

        public static OfflineResolver POLICY_RESOLVER;

        static T03_SignWithEPES()
        {

            POLICY_RESOLVER = new OfflineResolver();
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", POLICY_FILE, "text/plain");
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", POLICY_FILE, "text/plain");
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", POLICY_FILE, "text/plain");
        }

        [SetUp]
        public void setUp()
        {
            signatureBytes = new MemoryStream();
        }


        [Test]
        public void a_createEnveloping()
        {
            Context context = CreateContext();
            context.addExternalResolver(POLICY_RESOLVER);

            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);
            signature.addKeyInfo(getSignerCertificate());

            signature.SigningTime = DateTime.Now;
            signature.setPolicyIdentifier(OID_POLICY_P2,
               "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
               "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
               );

            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgradeToXAdES_T();

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test]
        public void b_createEnveloped()
        {
            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext();
            context.addExternalResolver(POLICY_RESOLVER);

            context.Document = envelopeDoc;
            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getSignerCertificate());

            signature.SigningTime = DateTime.Now;
            signature.setPolicyIdentifier(OID_POLICY_P2,
              "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
              "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
              );

            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgradeToXAdES_T();

            envelopeDoc.Save(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test]
        public void c_createDetached()
        {
            Context context = CreateContext();
            context.addExternalResolver(POLICY_RESOLVER);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getSignerCertificate());

            signature.SigningTime = DateTime.Now;
            signature.setPolicyIdentifier(OID_POLICY_P2,
              "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
              "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
              );

            signature.sign(getSignerInterface(SignatureAlg.RSA_SHA256));

            signature.upgradeToXAdES_T();

            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }
    }
}
