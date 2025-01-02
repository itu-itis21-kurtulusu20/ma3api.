using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

namespace test.esya.api.xmlsignature.creation.ec
{
    [TestFixture]
    public class EPES_EC : XMLSignatureTestBase
    {
        public static readonly int[] OID_POLICY_P2 = new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 1, 1 };

        public static String POLICY_FILE = "T:/api-parent/resources/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf";

        public static OfflineResolver POLICY_RESOLVER;

        static EPES_EC()
        {
            POLICY_RESOLVER = new OfflineResolver();
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", POLICY_FILE, "text/plain");
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", POLICY_FILE, "text/plain");
            POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", POLICY_FILE, "text/plain");
        }

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
            context.addExternalResolver(POLICY_RESOLVER);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(PLAINFILENAME, PLAINFILEMIMETYPE, true);

            signature.addKeyInfo(getECSignerCertificate());
            signature.SigningTime = DateTime.UtcNow;
            signature.setPolicyIdentifier(OID_POLICY_P2,
                    "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                    "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
            );

            signature.sign(getECSignerInterface(signatureAlg));
            signature.upgradeToXAdES_T();
            signature.write(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void testCreateEnveloped(SignatureAlg signatureAlg)
        {
            XmlDocument envelopeDoc = newEnvelope();
            Context context = CreateContext();
            context.addExternalResolver(POLICY_RESOLVER);
            context.Document = envelopeDoc;

            XMLSignature signature = new XMLSignature(context, false);
            envelopeDoc.DocumentElement.AppendChild(signature.Element);
            signature.addDocument("#data1", "text/xml", false);
            signature.addKeyInfo(getECSignerCertificate());
            signature.SigningTime = DateTime.UtcNow;
            signature.setPolicyIdentifier(OID_POLICY_P2,
                    "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                    "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
            );
            signature.sign(getECSignerInterface(signatureAlg));
            signature.upgradeToXAdES_T();

            envelopeDoc.Save(signatureBytes);

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }

        [Test, TestCaseSource("TestCases")]
        public void testCreateDetached(SignatureAlg signatureAlg)
        {
            Context context = CreateContext();
            context.addExternalResolver(POLICY_RESOLVER);
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument(BASE_DIR + PLAINFILENAME, PLAINFILEMIMETYPE, false);
            signature.addKeyInfo(getECSignerCertificate());
            signature.SigningTime = DateTime.UtcNow;
            signature.setPolicyIdentifier(OID_POLICY_P2,
                    "Kısa Dönemli ve SİL Kontrollü Güvenli Elektronik İmza Politikası",
                    "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf"
            );
            signature.sign(getECSignerInterface(signatureAlg));

            signature.write(signatureBytes);
            signature.upgradeToXAdES_T();

            XMLValidationUtil.checkSignatureIsValid(BASE_DIR, signatureBytes.ToArray());
        }
    }
}
