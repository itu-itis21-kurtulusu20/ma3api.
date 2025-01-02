using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.example.util;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.multiple
{
    /**
     * Parallel signature enveloped sample
     */

    [TestFixture]
    public class ParallelEnveloped : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "parallel_enveloped.xml";
        private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
        private RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

        /**
         * Creates two signatures in a document, that signs same inner data
         */

        [Test]
        public void createParallelEnveloped()
        {
            Context context = createContext();

            SignedDocument signatures = new SignedDocument(context);

            Document doc = Resolver.resolve("./sample.xml", context);
            string fragment = signatures.addDocument(doc);

            XMLSignature signature1 = signatures.createSignature();

            // add document as inner reference
            signature1.addDocument("#" + fragment, "text/xml", false);

            // add certificate to show who signed the document
            // arrange the parameters whether the certificate is qualified or not
            ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
            signature1.addKeyInfo(cert);

            // now sign it by using smart card
            // specifiy the PIN before sign
            signature1.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

            XMLSignature signature2 = signatures.createSignature();

            // add document as inner reference
            signature2.addDocument("#" + fragment, "text/plain", false);

            // add certificate to show who signed the document
            signature2.addKeyInfo(cert);

            // now sign it by using smart card
            // specifiy the PIN before sign
            signature2.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

            // write combined document
            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signatures.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validateParallel(SIGNATURE_FILENAME);
        }

        [Test]
        public void createParallelEnveloped_TwoSteps()
        {
            Context context = createContext();

            SignedDocument signedDocument1 = new SignedDocument(context);

            Document doc = Resolver.resolve("./sample.xml", context);
            string fragment = signedDocument1.addDocument(doc);

            XMLSignature signature1 = signedDocument1.createSignature();

            // add document as inner reference
            signature1.addDocument("#" + fragment, "text/xml", false);

            // add certificate to show who signed the document
            // arrange the parameters whether the certificate is qualified or not
            ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
            signature1.addKeyInfo(cert);

            byte[] dtbs1 = signature1.initAddingSignature(signatureAlg, rsapssParams);
            // now sign it by using smart card
            // specifiy the PIN before sign
            BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert, signatureAlg.getName(), rsapssParams);
            byte[] signatureValue1 = signer.sign(dtbs1);

            MemoryStream signatureBytes = new MemoryStream();
            signedDocument1.write(signatureBytes);

            byte[] bsdBytes1 = signatureBytes.ToArray();
            finishSigning(bsdBytes1, context, signatureValue1);

            Document doc2 = Resolver.resolve(getTestDataFolder() + SIGNATURE_FILENAME, context);
            SignedDocument signedDocument2 = new SignedDocument(doc2, context);

            XMLSignature signature2 = signedDocument2.createSignature();

            // add document as inner reference
            signature2.addDocument("#" + fragment, "text/plain", false);

            // add certificate to show who signed the document
            signature2.addKeyInfo(cert);

            // now sign it by using smart card
            byte[] dtbs2 = signature2.initAddingSignature(signatureAlg, rsapssParams);
            byte[] signatureValue2 = signer.sign(dtbs2);

            signatureBytes = new MemoryStream();
            signedDocument2.write(signatureBytes);

            byte[] bsdBytes2 = signatureBytes.ToArray();

            finishSigning(bsdBytes2, context, signatureValue2);

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validateParallel(SIGNATURE_FILENAME);
        }

        private void finishSigning(byte[] bsdBytes, Context context, byte[] signature)
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);

            SignedDocument signedDocument = new SignedDocument(xmlDocument, context);

            signedDocument.finishAddingSignature(signature);

            // write combined document
            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signedDocument.write(fileStream);
            fileStream.Close();
        }
    }
}