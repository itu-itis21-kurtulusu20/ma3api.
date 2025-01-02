using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.config;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic
{
    /**
     * @author yavuz.kahveci
     */
    [TestFixture]
    public class ASiC_S_XAdES_ValidationTest
    {
        string root = "C:\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-S_CSSC_X.SCOK\\";

        public void validate(string company, string file)
        {
            try
            {
                Console.WriteLine("/////////////////////////////////////////");
                Config config = new Config();
                Context c = new Context();
                c.setConfig(config);
                FileInfo f = new FileInfo(root + company + "\\" + file);
                Console.WriteLine("Validate " + company + "/" + file);
                SignaturePackage sp = SignaturePackageFactory.readPackage(c, f);
                PackageValidationResult pvr = sp.verifyAll();
                Console.WriteLine(pvr.ToString());
                Console.WriteLine("/////////////////////////////////////////");
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        /* todo (X) XML İmza Kontrolü (imzacı sertifikası yerine kok sertifikayı buluyor)
            Tutarsız imza değeri. */
        [Test]
        public void validateCRY()
        {
            validate("CRY", "Container-ASiC-S_CSSC_X-1.asics");
            validate("CRY", "Container-ASiC-S_CSSC_X-2.zip");
            validate("CRY", "Container-ASiC-S_CSSC_X-4.asics");
            validate("CRY", "Container-ASiC-S_CSSC_X-5.asics");
            validate("CRY", "Container-ASiC-S_CSSC_X-6.asics");
        }

        // todo (X) XML İmza Kontrolü
        //    Tutarsız imza değeri.
        [Test]
        public void validateDIC()
        {
            validate("DIC", "Container-ASiC-S_CSSC_X-1.asics");
            validate("DIC", "Container-ASiC-S_CSSC_X-2.zip");
            validate("DIC", "Container-ASiC-S_CSSC_X-3.asics");
            validate("DIC", "Container-ASiC-S_CSSC_X-4.asics");
            validate("DIC", "Container-ASiC-S_CSSC_X-5.asics");
            validate("DIC", "Container-ASiC-S_CSSC_X-6.asics");
        }

        [Test]
        public void validateEC()
        {
            validate("EC", "Container-ASiC-S_CSSC_X-1.asics");
            validate("EC", "Container-ASiC-S_CSSC_X-2.zip");
            validate("EC", "Container-ASiC-S_CSSC_X-3.asics");
            validate("EC", "Container-ASiC-S_CSSC_X-4.asics");
            validate("EC", "Container-ASiC-S_CSSC_X-5.asics");
            validate("EC", "Container-ASiC-S_CSSC_X-6.asics");
        }

        [Test]
        public void validateMIC()
        {
            validate("MIC", "Container-ASiC-S_CSSC_X-1.asics");
            validate("MIC", "Container-ASiC-S_CSSC_X-2.zip");
            validate("MIC", "Container-ASiC-S_CSSC_X-3.asics");
            validate("MIC", "Container-ASiC-S_CSSC_X-4.asics");
            validate("MIC", "Container-ASiC-S_CSSC_X-5.asics");
            validate("MIC", "Container-ASiC-S_CSSC_X-6.asics");
        }

        /*  todo (X) XML İmza Kontrolü
            Tutarsız imza değeri. */
        [Test]
        public void validateMIT()
        {
            validate("MIT", "Container-ASiC-S_CSSC_X-1.asics");
            validate("MIT", "Container-ASiC-S_CSSC_X-2.zip");
            validate("MIT", "Container-ASiC-S_CSSC_X-3.asics");
            validate("MIT", "Container-ASiC-S_CSSC_X-4.asics");
            validate("MIT", "Container-ASiC-S_CSSC_X-5.asics");
            validate("MIT", "Container-ASiC-S_CSSC_X-6.asics");
        }

        [Test]
        public void validateNTT()
       {
            validate("NTT", "Container-ASiC-S_CSSC_X-1.asics");
            validate("NTT", "Container-ASiC-S_CSSC_X-2.zip");
            validate("NTT", "Container-ASiC-S_CSSC_X-3.asics");
            validate("NTT", "Container-ASiC-S_CSSC_X-4.asics");
            validate("NTT", "Container-ASiC-S_CSSC_X-5.asics");
            validate("NTT", "Container-ASiC-S_CSSC_X-6.asics");
        }

        // todo Zaman Damgası Sertifikası Kontrol ediliyor mu?
        [Test]
        public void validatePOL()
        {
            validate("POL", "Container-ASiC-S_CSSC_X-1.asics");
            validate("POL", "Container-ASiC-S_CSSC_X-2.zip");
            validate("POL", "Container-ASiC-S_CSSC_X-3.asics");
            validate("POL", "Container-ASiC-S_CSSC_X-4.asics");
            validate("POL", "Container-ASiC-S_CSSC_X-5.asics");
            validate("POL", "Container-ASiC-S_CSSC_X-6.asics");
        }

        // todo sertifika bulma, name match vs.
        [Test]
        public void validateSIX()
        {
            validate("SIX", "Container-ASiC-S_CSSC_X-1.asics");
            validate("SIX", "Container-ASiC-S_CSSC_X-2.zip");
            validate("SIX", "Container-ASiC-S_CSSC_X-3.asics");
            validate("SIX", "Container-ASiC-S_CSSC_X-4.asics");
            validate("SIX", "Container-ASiC-S_CSSC_X-5.asics");
            validate("SIX", "Container-ASiC-S_CSSC_X-6.asics");
        }

    }
}