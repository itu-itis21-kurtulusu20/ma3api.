using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.upgrades.x1
{
    /**
     * Provides upgrade function from X1 to XL
     */

    [TestFixture]
    public class UpgradeToXL : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "xl_from_x1.xml";

        /**
         * Upgrades X1 to XL. X1 needs to be provided before upgrade process.
         * It can be created in formats.X1.
         */

        [Test]
        public void upgradeX1ToXL()
        {
            // create context with working dir
            Context context = createContext();

            // read signature to be upgraded
            XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(getTestDataFolder() + "x1.xml")),
                context);

            // upgrade to XL
            signature.upgrade(api.signature.SignatureType.ES_XL);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.OpenOrCreate);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }
    }
}