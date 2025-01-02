using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.example.util;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.sign
{
    /**
     * XL signing sample
     */

    [TestFixture]
    public class XL : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "xl.xml";

        /**
         * Creates detached XL
         */

        [Test]
        public void createXL()
        {
            // create context with working directory
            Context context = createContext();

            // create signature according to context,
            // with default type (XADES_BES)
            XMLSignature signature = new XMLSignature(context);

            // add document as reference, but do not embed it
            // into the signature (embed=false)
            signature.addDocument("./sample.txt", "text/plain", false);

            // add certificate to show who signed the document
            // arrange the parameters whether the certificate is qualified or not
            ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());
            signature.addKeyInfo(cert);

            // now sign it by using smart card
            // specifiy the PIN before sign
            signature.sign(SmartCardManager.getInstance().getSigner(getPin(), cert));

            // upgrade to XL
            signature.upgrade(api.signature.SignatureType.ES_XL);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }

        [Test]
        public void createXLInTwoSteps()
        {
            Context context = createContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument("./sample.txt", "text/plain", false);

            ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

            signature.addKeyInfo(cert);

            byte[] dtbs = signature.initAddingSignature(SignatureAlg.RSA_SHA256, null);

            MemoryStream memoryStream = new MemoryStream();
            signature.write(memoryStream);
            memoryStream.Close();

            BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

            byte[] signatureValue = signer.sign(dtbs);
            finishSigning(memoryStream.ToArray(), context, signatureValue);

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }

        private void finishSigning(byte[] bsdBytes, Context context, byte[] signatureValue)
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
            XMLSignature signature = XMLSignature.parse(xmlDocument, context);

            signature.finishAddingSignature(signatureValue);
            signature.upgrade(tr.gov.tubitak.uekae.esya.api.signature.SignatureType.ES_XL);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signature.write(fileStream);
            fileStream.Close();
        }
    }
}