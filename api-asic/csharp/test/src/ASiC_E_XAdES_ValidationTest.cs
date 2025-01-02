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
    public class ASiC_E_XAdES_ValidationTest
    {
        string root = "C:\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-E_CSSC_X.SCOK\\";

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

        /* todo (X) XML İmza Kontrolü (imzacı sertifikası yerine kok sertifikayı buluyor)
            Tutarsız imza değeri. */
        [Test]
        public void validateCRY()
        {
            validate("CRY", "Container-ASiC-E_CSSC_X-5.asice");
        }

        // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
        [Test]
        public void validateDIC()
        {
            validate("DIC", "Container-ASiC-E_CSSC_X-1.asice");
            validate("DIC", "Container-ASiC-E_CSSC_X-2.zip");
            validate("DIC", "Container-ASiC-E_CSSC_X-3.asice");
            validate("DIC", "Container-ASiC-E_CSSC_X-4.asice");
            validate("DIC", "Container-ASiC-E_CSSC_X-5.asice");
            validate("DIC", "Container-ASiC-E_CSSC_X-6.asice");
            validate("DIC", "Container-ASiC-E_CSSC_X-7.asice");
        }

        // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
        [Test]
        public void validateMIC()
        {
            validate("MIC", "Container-ASiC-E_CSSC_X-1.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-2.zip");
            validate("MIC", "Container-ASiC-E_CSSC_X-3.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-4.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-5.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-6.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-7.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-8.asice");
            validate("MIC", "Container-ASiC-E_CSSC_X-9.asice");
        }

        /*  todo (X) XML İmza Kontrolü
            Tutarsız imza değeri.
             Unknown file in archive: META-INF/relations.xml
             */
        [Test]
        public void validateMIT()
        {
            validate("MIT", "Container-ASiC-E_CSSC_X-1.asice");
            validate("MIT", "Container-ASiC-E_CSSC_X-2.zip");
            validate("MIT", "Container-ASiC-E_CSSC_X-3.asice");
            validate("MIT", "Container-ASiC-E_CSSC_X-4.asice");
            validate("MIT", "Container-ASiC-E_CSSC_X-5.asice");
            validate("MIT", "Container-ASiC-E_CSSC_X-7.asice");
            validate("MIT", "Container-ASiC-E_CSSC_X-9.asice");
        }

        // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
        [Test]
        public void validateNTT()
        {
            validate("NTT", "Container-ASiC-E_CSSC_X-1.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-2.zip");
            validate("NTT", "Container-ASiC-E_CSSC_X-3.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-4.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-5.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-6.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-7.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-8.asice");
            validate("NTT", "Container-ASiC-E_CSSC_X-9.asice");
        }

        // Caused by: javax.xml.transform.TransformerException: java.io.FileNotFoundException:
        // E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)
        // todo Zaman Damgası Sertifikası Kontrol ediliyor mu?
        [Test]
        public void validatePOL()
        {
            validate("POL", "Container-ASiC-E_CSSC_X-1.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-2.zip");
            validate("POL", "Container-ASiC-E_CSSC_X-3.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-4.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-5.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-6.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-7.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-8.asice");
            validate("POL", "Container-ASiC-E_CSSC_X-9.asice");
        }

    }
}