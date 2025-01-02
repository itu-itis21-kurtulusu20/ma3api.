using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    /**
     * @author yavuz.kahveci
     */
    [TestFixture]
    public class GenerationTest : ASiCBaseTest
    {
        public void createBES(PackageType packageType, SignatureFormat format)
        {
            try
            {
                //Config config = new Config();
                //Context c = new Context();
                //c.setConfig(config);

                Context c = createContext();

                SignaturePackage signaturePackage = SignaturePackageFactory.createPackage(c, packageType, format);

                // add into zip
                Signable inPackage = signaturePackage.addData(new SignableFile(dataFile, "text/plain"), "sample.txt");

                SignatureContainer container = signaturePackage.createContainer();
                Signature signature = container.createSignature(CERTIFICATE);

                // pass document in ZIP to signature
                signature.addContent(inPackage, false);

                signature.sign(SIGNER);

                string fileName = getFileName(packageType, format, SignatureType.ES_BES);
                signaturePackage.write(new FileStream(fileName, FileMode.Create));

                // read it back
                signaturePackage = SignaturePackageFactory.readPackage(c, new FileInfo(fileName));

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
        public void createASiC_S_CAdES()
        {
            createBES(PackageType.ASiC_S, SignatureFormat.CAdES);
        }

        [Test]
        public void createASiC_E_CAdES()
        {
            createBES(PackageType.ASiC_E, SignatureFormat.CAdES);
        }

        [Test]
        public void createASiC_S_XAdES()
        {
            createBES(PackageType.ASiC_S, SignatureFormat.XAdES);
        }

        [Test]
        public void createASiC_E_XAdES() 
        {
            createBES(PackageType.ASiC_E, SignatureFormat.XAdES);
        }

        /*public void main()
        {
            createBES(PackageType.ASiC_S, SignatureFormat.CAdES);
        }*/
    }
}
