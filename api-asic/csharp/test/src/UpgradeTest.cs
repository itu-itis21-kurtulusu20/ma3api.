using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.config;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class UpgradeTest : ASiCBaseTest
    {
        public void upgrade(PackageType packageType, SignatureFormat format, SignatureType current, SignatureType next)
        {
            try
            {
                Config config = new Config();
                Context c = new Context();
                c.setConfig(config);
                SignaturePackage signaturePackage = SignaturePackageFactory.readPackage(c, new FileInfo(getFileName(packageType, format, current)));
                SignatureContainer sc = signaturePackage.getContainers()[0];
                Signature signature = sc.getSignatures()[0];

                // for adding timestamp convert to ES_T
                //signature.upgrade(SignatureType.ES_T);
                // add timestamp and validation references
                //signature.upgrade(SignatureType.ES_XL);

                signature.upgrade(next);

                signaturePackage.write(new FileStream(getFileName(packageType, format, next), FileMode.Create));

                // validate
                PackageValidationResult pvr = signaturePackage.verifyAll();

                // output results
                Console.WriteLine(pvr);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
                throw;
            }
        }

        [Test]
        public void upgrade_BES_T_X_E()
        {
            upgrade(PackageType.ASiC_E, SignatureFormat.XAdES, SignatureType.ES_BES, SignatureType.ES_T);
        }

        [Test]
        public void upgrade_BES_T_C_E()
        {
            upgrade(PackageType.ASiC_E, SignatureFormat.CAdES, SignatureType.ES_BES, SignatureType.ES_T);
        }
    }
}
