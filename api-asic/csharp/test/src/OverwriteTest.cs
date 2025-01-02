using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class OverwriteTest : ASiCBaseTest
    {
        [Test]
        public void update_CAdES()
        {
            SignaturePackage sp = read(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES);
            String fileName = getFileName(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES)+"-upgraded.asice";
            FileInfo toUpgrade = new FileInfo(fileName);

            // create a copy to update package
            sp.write(new FileStream(fileName, FileMode.Create));

            // add signature
            SignaturePackage sp2 = SignaturePackageFactory.readPackage(/*new Context()*/createContext(), toUpgrade);
            SignatureContainer sc = sp2.createContainer();
            Signature s = sc.createSignature(CERTIFICATE);
            s.addContent(sp.getDatas()[0], false);
            s.sign(SIGNER);

            // write onto read file!
            sp2.write();

            // read again to verify
            SignaturePackage sp3 = SignaturePackageFactory.readPackage(/*new Context()*/createContext(), new FileInfo(fileName));
            PackageValidationResult pvr = sp3.verifyAll();
            Console.WriteLine(pvr);

            Assert.True(pvr.getResultType() == PackageValidationResultType.ALL_VALID);
        }
    }
}
