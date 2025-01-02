using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    public class ValidationTest : ASiCBaseTest
    {
        public void validate(String fileName)
        {
            SignaturePackage signaturePackage = SignaturePackageFactory.readPackage(createContext(), new FileInfo(fileName));

            // validate
            PackageValidationResult pvr = signaturePackage.verifyAll();

            // output results
            Console.WriteLine(pvr);
        }

                                /** NET **/
        
        /** generation **/
        [Test]
        public void nce()
        {
            validate(outDir + "ASiC_E-CAdES-ES_BES.asice");
        }

        [Test]
        public void ncs()
        {
            validate(outDir + "ASiC_S-CAdES-ES_BES.asics");
        }

        [Test]
        public void nxe()
        {
            validate(outDir + "ASiC_E-XAdES-ES_BES.asice");
        }

        [Test]
        public void nxs()
        {
            validate(outDir + "ASiC_S-XAdES-ES_BES.asics");
        }

        /** overwrite **/
        [Test]
        public void now()
        {
            validate(outDir + "ASiC_E-CAdES-ES_BES.asice-upgraded.asice");
        }

                                /** JAVA **/
        
        /** generation **/
        [Test]
        public void jce()
        {
            validate(outDir + "../api-asic/created/ASiC_E-CAdES-ES_BES.asice");
        }

        [Test]
        public void jcs()
        {
            validate(outDir + "../api-asic/created/ASiC_S-CAdES-ES_BES.asics");
        }

        [Test]
        public void jxe()
        {
            validate(outDir + "../api-asic/created/ASiC_E-XAdES-ES_BES.asice");
        }

        [Test]
        public void jxs()
        {
            validate(outDir + "../api-asic/created/ASiC_S-XAdES-ES_BES.asics");
        }

        /** overwrite **/
        [Test]
        public void jow()
        {
            validate(outDir + "../api-asic/created/ASiC_E-CAdES-ES_BES.asice-upgraded.asice");
        }

        /** extend **/
        [Test]
        public void jcx()
        {
            validate(outDir + "../api-asic/created/ASiC_E-CAdES-ES_BES.asiceextended.asice");
        }

        [Test]
        public void jxx()
        {
            validate(outDir + "../api-asic/created/ASiC_E-XAdES-ES_BES.asiceextended.asice");
        }

        /** upgrade **/
        [Test]
        public void jcu()
        {
            validate(outDir + "../api-asic/created/ASiC_E-CAdES-ES_T.asice");
        }

        [Test]
        public void jxu()
        {
            validate(outDir + "../api-asic/created/ASiC_E-XAdES-ES_T.asice");
        }
        
    }
}
