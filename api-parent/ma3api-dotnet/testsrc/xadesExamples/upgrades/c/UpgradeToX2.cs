using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.xades.example;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.example.validation;
using SignatureType = tr.gov.tubitak.uekae.esya.api.signature.SignatureType;

namespace xmlsig.samples.upgrades.c
{
    /**
     * Provides upgrade function from C to X2
     */

    [TestFixture]
    public class UpgradeToX2 : XadesSampleBase
    {
        public static readonly string SIGNATURE_FILENAME = "x2_from_c.xml";

        /**
         * Upgrades C to X2. C needs to be provided before upgrade process.
         * It can be created in formats.C.
         */

        [Test]
        public void upgradeCToX2()
        {
            // create context with working dir
            Context context = createContext();

            // read signature to be upgraded
            XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(getTestDataFolder() + "c.xml")),
                context);

            // upgrade to X2
            signature.upgrade(SignatureType.ES_X_Type2);

            FileStream fileStream = new FileStream(getTestDataFolder() + SIGNATURE_FILENAME, FileMode.OpenOrCreate);
            signature.write(fileStream);
            fileStream.Close();

            XadesSignatureValidation signatureValidation = new XadesSignatureValidation();
            signatureValidation.validate(SIGNATURE_FILENAME);
        }
    }
}