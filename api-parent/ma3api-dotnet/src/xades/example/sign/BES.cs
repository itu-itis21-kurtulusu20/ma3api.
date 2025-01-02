using System;
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

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.sign
{
    /**
     * BES signing sample
     */

    [TestFixture]
    public class BES : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "bes.xml";

        [Test]
        public void testCreateEnveloping()
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

            //This function selects SIGNATURE_RSA_SHA256 algorithm for RSA keys and convenient algorithm for EC. Check the SmartCardManager class getSigner() function for more details.
            BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

            // add certificate to show who signed the document
            signature.addKeyInfo(cert);

            // now sign it by using smart card
            signature.sign(signer);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }

        [Test]
        public void testCreateEnveloping_TwoSteps()
        {
            Context context = createContext();
            XMLSignature signature = new XMLSignature(context);
            signature.addDocument("./sample.txt", "text/plain", false);

            ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(isQualified());

            signature.addKeyInfo(cert);

            BaseSigner signer = SmartCardManager.getInstance().getSigner(getPin(), cert);

            // You can get the parameters from the signer.
            //byte[] dtbs = signature.initAddingSignature(SignatureAlg.fromName(signer.getSignatureAlgorithmStr()), signer.getAlgorithmParameterSpec());

            byte[] dtbs = signature.initAddingSignature(SignatureAlg.RSA_SHA256, null);

            MemoryStream memoryStream = new MemoryStream();
            signature.write(memoryStream);
            memoryStream.Close();

            byte[] signatureValue = signer.sign(dtbs);
            byte[] unfinishedBytes = memoryStream.ToArray();

            finishSigning(unfinishedBytes, context, signatureValue);

            // read and validate
            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }

        private void finishSigning(byte[] bsdBytes, Context context, byte[] signatureValue)
        {
            InMemoryDocument xmlDocument = new InMemoryDocument(bsdBytes, null, "application/xml", null);
            XMLSignature signature = XMLSignature.parse(xmlDocument, context);

            signature.finishAddingSignature(signatureValue);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.Create);
            signature.write(fileStream);
            fileStream.Close();
        }
    }
}