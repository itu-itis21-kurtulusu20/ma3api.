using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.example.upgrades.x2
{
    /**
     * Provides upgrade function from X2 to XL
     */

    [TestFixture]
    public class UpgradeToXL : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "xl_from_x2.xml";

        /**
         * Upgrades X2 to XL. X2 needs to be provided before upgrade process.
         * It can be created in formats.X2.
         */

        [Test]
        public void upgradeX2ToXL()
        {
            // create context with working dir
            Context context = createContext();

            // read signature to be upgraded
            XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(getTestDataFolder() + "x2.xml")),
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