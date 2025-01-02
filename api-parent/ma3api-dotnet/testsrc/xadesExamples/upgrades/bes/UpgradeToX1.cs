using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;
using SignatureType = tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

namespace xmlsig.samples.upgrades.bes
{
    /**
     * Provides upgrade function from BES to X1
     */

    [TestFixture]
    public class UpgradeToX1 : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "x1_from_bes.xml";

        /**
         * Upgrades BES to X1. BES needs to be provided before upgrade process.
         * It can be created in formats.BES.
         */

        [Test]
        public void upgradeBESToX1()
        {
            // create context with working dir
            Context context = createContext();

            // read signature to be upgraded
            XMLSignature signature = XMLSignature.parse(
                new FileDocument(new FileInfo(getTestDataFolder() + "bes.xml")), context);

            // upgrade to X1
            signature.upgrade(SignatureType.ES_X_Type1);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.OpenOrCreate);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }
    }
}