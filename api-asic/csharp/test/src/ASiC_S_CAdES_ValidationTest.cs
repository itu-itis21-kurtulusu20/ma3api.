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
    public class ASiC_S_CAdES_ValidationTest
    {
        string root = "C:\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-S_CSSC_C.SCOK\\";

        public void validate(string company, string file)
        {
            Config config = new Config();
            Context c = new Context();
            c.setConfig(config);
            FileInfo f = new FileInfo(root + company + "\\"+file);
            Console.WriteLine("Validate "+company+"/"+file);
            SignaturePackage sp = SignaturePackageFactory.readPackage(c, f);
            PackageValidationResult pvr = sp.verifyAll();
            Console.WriteLine(pvr.ToString());
        }

        [Test]
        public void validateCRY()
        {
            validate("CRY", "Container-ASiC-S_CSSC_C-1.asics");
            //validate("CRY", "Container-ASiC-S_CSSC_C-2.zip");
            //validate("CRY", "Container-ASiC-S_CSSC_C-4.asics");
            //validate("CRY", "Container-ASiC-S_CSSC_C-5.asics");
            //validate("CRY", "Container-ASiC-S_CSSC_C-6.asics");
        }

        // todo İmzacı Sertifikası Özelliği V2 Kontrolcüsü
        // Özellikteki issuer serial alanı, signerinfo daki signeridentifier alanı
        // ile eşleşmiyor.
        [Test]
        public void validateDIC()
        {
            validate("DIC", "Container-ASiC-S_CSSC_C-1.asics");
            validate("DIC", "Container-ASiC-S_CSSC_C-2.zip");
            validate("DIC", "Container-ASiC-S_CSSC_C-3.asics");
            validate("DIC", "Container-ASiC-S_CSSC_C-4.asics");
            validate("DIC", "Container-ASiC-S_CSSC_C-5.asics");
            validate("DIC", "Container-ASiC-S_CSSC_C-6.asics");
        }

        [Test]
        public void validateMIC() 
        {
            validate("MIC", "Container-ASiC-S_CSSC_C-1.asics");
            validate("MIC", "Container-ASiC-S_CSSC_C-2.zip");
            validate("MIC", "Container-ASiC-S_CSSC_C-3.asics");
            validate("MIC", "Container-ASiC-S_CSSC_C-4.asics");
            validate("MIC", "Container-ASiC-S_CSSC_C-5.asics");
            validate("MIC", "Container-ASiC-S_CSSC_C-6.asics");
        }

        [Test]
        public void validateMIT()
        {
            validate("MIT", "Container-ASiC-S_CSSC_C-1.asics");
            validate("MIT", "Container-ASiC-S_CSSC_C-2.zip");
            validate("MIT", "Container-ASiC-S_CSSC_C-3.asics");
            validate("MIT", "Container-ASiC-S_CSSC_C-4.asics");
            validate("MIT", "Container-ASiC-S_CSSC_C-5.asics");
            validate("MIT", "Container-ASiC-S_CSSC_C-6.asics");
        }

        // todo Zaman Damgası Sertifikası Kontrolcüsü
        // Sertifika Zinciri Sorunlu. Güvendiğiniz bir sertifika zinciri
        // oluşturulamadı. Sertifikanın kök sertifikası güvenilir
        // sertifikalarınızdan biri olmayabilir.
        [Test]
        public void validatePOL()
        {
            validate("POL", "Container-ASiC-S_CSSC_C-1.asics");
            validate("POL", "Container-ASiC-S_CSSC_C-2.zip");
            validate("POL", "Container-ASiC-S_CSSC_C-3.asics");
            validate("POL", "Container-ASiC-S_CSSC_C-4.asics");
            validate("POL", "Container-ASiC-S_CSSC_C-5.asics");
            validate("POL", "Container-ASiC-S_CSSC_C-6.asics");
        }

    }
}