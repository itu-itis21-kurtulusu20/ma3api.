

using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature
{
    public class Upgrade : BaseTest
    {
        public void upgradeBesToT(String fileNameRead, String fileNameWrite,
                            SignatureType convertToType)
        {
            SignatureContainer sc = readSignatureContainer(fileNameRead);
            Signature signature = sc.getSignatures()[0];
            signature.upgrade(convertToType);
            write(sc, fileNameWrite);

            SignatureContainer read = readSignatureContainer(fileNameWrite);
            Signature readSignature = read.getSignatures()[0];
            Assert.AreEqual(convertToType, readSignature.getSignatureType());
            ContainerValidationResult cvr = read.verifyAll();
            Console.WriteLine(cvr);
            Assert.AreEqual(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        }


        [Test]
        public void upgradeBesToT()
        {
            upgradeBesToT(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_T, SignatureType.ES_T);
        }

        [Test]
        public void upgradeBesToC()
        {
            upgradeBesToT(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_C, SignatureType.ES_C);
        }

        [Test]
        public void upgradeBesToX1()
        {
            upgradeBesToT(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_X1, SignatureType.ES_X_Type1);
        }

        [Test]
        public void upgradeBesToXL()
        {
            upgradeBesToT(FileNames.BES_DETACHED, FileNames.UPGRADED_BES_XL, SignatureType.ES_XL);
        }

        [Test]
        public void upgradeTToC()
        {
            upgradeBesToT(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_C, SignatureType.ES_C);
        }

        [Test]
        public void upgradeTToX1()
        {
            upgradeBesToT(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_X1, SignatureType.ES_X_Type1);
        }

        [Test]
        public void upgradeTToXL()
        {
            upgradeBesToT(FileNames.UPGRADED_BES_T, FileNames.UPGRADED_T_XL, SignatureType.ES_XL);
        }

        [Test]
        public void upgradeCToXL()
        {
            upgradeBesToT(FileNames.UPGRADED_T_C, FileNames.UPGRADED_C_XL, SignatureType.ES_XL);
        }
    }
}
