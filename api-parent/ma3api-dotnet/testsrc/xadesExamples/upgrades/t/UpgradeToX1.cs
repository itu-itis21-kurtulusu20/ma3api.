using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.upgrades.t
{
    /**
     * Provides upgrade function from T to X1
     */

    [TestFixture]
    public class UpgradeToX1 : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "x1_from_t.xml";

        /**
         * Upgrades T to X1. T needs to be provided before upgrade process.
         * It can be created in formats.T.
         */

        [Test]
        public void upgradeTToX1()
        {
            // create context with working dir
            Context context = createContext();

            // read signature to be upgraded
            XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(getTestDataFolder() + "t.xml")),
                context);

            // upgrade to X1
            signature.upgrade(api.signature.SignatureType.ES_X_Type1);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.OpenOrCreate);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }
    }
}