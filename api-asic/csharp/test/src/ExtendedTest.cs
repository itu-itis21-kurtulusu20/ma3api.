using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class ExtendedTest : ASiCBaseTest
    {
        [Test]
        public void appendContainer_XAdES()
        {
            SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.XAdES, SignatureType.ES_BES);
            Signature existing = sp.getContainers()[0].getSignatures()[0];
            SignatureContainer sc = sp.createContainer();
            Signature s = sc.createSignature(CERTIFICATE);
            // get signable from signature
            s.addContent(existing.getContents()[0], false);
            s.sign(SIGNER);
            String fileName = getFileName(PackageType.ASiC_E, SignatureFormat.XAdES, SignatureType.ES_BES)+"extended.asice";
            sp.write(new FileStream(fileName,FileMode.Create));

            SignaturePackage sp2 = SignaturePackageFactory.readPackage(new Context(), new FileInfo(fileName));
            PackageValidationResult pvr = sp2.verifyAll();
            Console.WriteLine(pvr);

            Assert.True(pvr.getResultType() == PackageValidationResultType.ALL_VALID);

        }

        [Test]
        public void appendContainer_CAdES()
        {
            SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES);
            SignatureContainer sc = sp.createContainer();
            Signature s = sc.createSignature(CERTIFICATE);
            // get signable from package
            s.addContent(sp.getDatas()[0], false);
            s.sign(SIGNER);
            String fileName = getFileName(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES)+"extended.asice";
            sp.write(new FileStream(fileName, FileMode.Create));

            SignaturePackage sp2 = SignaturePackageFactory.readPackage(new Context(), new FileInfo(fileName));
            PackageValidationResult pvr = sp2.verifyAll();
            Console.WriteLine(pvr);

            Assert.True(pvr.getResultType() == PackageValidationResultType.ALL_VALID);
        }
    }
}
