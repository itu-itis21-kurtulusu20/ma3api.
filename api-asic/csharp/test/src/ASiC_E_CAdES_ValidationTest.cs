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
    public class ASiC_E_CAdES_ValidationTest
    {
        string root = "C:\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-E_CSSC_C.SCOK\\";

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
            validate("CRY", "Container-ASiC-E_CSSC_C-1.asice");
            validate("CRY", "Container-ASiC-E_CSSC_C-2.zip");
            validate("CRY", "Container-ASiC-E_CSSC_C-4.asice");
            validate("CRY", "Container-ASiC-E_CSSC_C-5.asice");
            validate("CRY", "Container-ASiC-E_CSSC_C-6.asice");
        }

        // todo İmzacı Sertifikası Özelliği V2 Kontrolcüsü
        // Özellikteki issuer serial alanı, signerinfo daki signeridentifier alanı
        // ile eşleşmiyor.
        [Test]
        public void validateDIC()
        {
            validate("DIC", "Container-ASiC-E_CSSC_C-1.asice");
            validate("DIC", "Container-ASiC-E_CSSC_C-2.zip");
            validate("DIC", "Container-ASiC-E_CSSC_C-3.asice");
            validate("DIC", "Container-ASiC-E_CSSC_C-4.asice");
            validate("DIC", "Container-ASiC-E_CSSC_C-5.asice");
            validate("DIC", "Container-ASiC-E_CSSC_C-6.asice");
        }

        [Test]
        public void validateMIC()
        {
            validate("MIC", "Container-ASiC-E_CSSC_C-1.asice");
            validate("MIC", "Container-ASiC-E_CSSC_C-2.zip");
            validate("MIC", "Container-ASiC-E_CSSC_C-3.asice");
            validate("MIC", "Container-ASiC-E_CSSC_C-4.asice");
            validate("MIC", "Container-ASiC-E_CSSC_C-5.asice");
            validate("MIC", "Container-ASiC-E_CSSC_C-6.asice");
        }

        [Test]
        public void validateMIT()
        {
            validate("MIT", "Container-ASiC-E_CSSC_C-1.asice");
            validate("MIT", "Container-ASiC-E_CSSC_C-2.zip");
            validate("MIT", "Container-ASiC-E_CSSC_C-3.asice");
            validate("MIT", "Container-ASiC-E_CSSC_C-4.asice");
            validate("MIT", "Container-ASiC-E_CSSC_C-5.asice");
            validate("MIT", "Container-ASiC-E_CSSC_C-6.asice");
        }

        // todo Zaman Damgası Sertifikası Kontrolcüsü
        // Sertifika Zinciri Sorunlu. Güvendiğiniz bir sertifika zinciri
        // oluşturulamadı. Sertifikanın kök sertifikası güvenilir
        // sertifikalarınızdan biri olmayabilir.
        [Test]
        public void validatePOL() 
        {
            validate("POL", "Container-ASiC-E_CSSC_C-1.asice");
            validate("POL", "Container-ASiC-E_CSSC_C-2.zip");
            validate("POL", "Container-ASiC-E_CSSC_C-3.asice");
            validate("POL", "Container-ASiC-E_CSSC_C-4.asice");
            validate("POL", "Container-ASiC-E_CSSC_C-5.asice");
            validate("POL", "Container-ASiC-E_CSSC_C-6.asice");
        }

    }
}