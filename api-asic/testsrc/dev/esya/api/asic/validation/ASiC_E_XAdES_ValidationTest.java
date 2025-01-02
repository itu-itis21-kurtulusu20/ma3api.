package dev.esya.api.asic.validation;

import org.junit.Ignore;
import org.junit.Test;

import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackageFactory;

import java.io.File;

/**
 * @author ayetgin
 */
@Ignore("Plugtest")
public class ASiC_E_XAdES_ValidationTest
{

    String root = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-E_CSSC_X.SCOK\\";

    public void validate(String company, String file) throws Exception
    {
        Context c = new Context();
        File f = new File(root + company + "\\"+file);
        System.out.println("Validate "+company+"/"+file);
        SignaturePackage sp = SignaturePackageFactory.readPackage(c, f);
        PackageValidationResult pvr = sp.verifyAll();
        System.out.println(pvr);
    }

    /* todo (X) XML İmza Kontrolü (imzacı sertifikası yerine kok sertifikayı buluyor)
        Tutarsız imza değeri. */
    @Test
    public void validateCRY() throws Exception {
        validate("CRY", "Container-ASiC-E_CSSC_X-5.asice");
    }

    // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
    @Test
    public void validateDIC() throws Exception {
        validate("DIC", "Container-ASiC-E_CSSC_X-1.asice");
        validate("DIC", "Container-ASiC-E_CSSC_X-2.zip");
        validate("DIC", "Container-ASiC-E_CSSC_X-3.asice");
        validate("DIC", "Container-ASiC-E_CSSC_X-4.asice");
        validate("DIC", "Container-ASiC-E_CSSC_X-5.asice");
        validate("DIC", "Container-ASiC-E_CSSC_X-6.asice");
        validate("DIC", "Container-ASiC-E_CSSC_X-7.asice");
    }

    // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
    @Test
    public void validateMIC() throws Exception {
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
    @Test
    public void validateMIT() throws Exception {
        validate("MIT", "Container-ASiC-E_CSSC_X-1.asice");
        validate("MIT", "Container-ASiC-E_CSSC_X-2.zip");
        validate("MIT", "Container-ASiC-E_CSSC_X-3.asice");
        validate("MIT", "Container-ASiC-E_CSSC_X-4.asice");
        validate("MIT", "Container-ASiC-E_CSSC_X-5.asice");
        validate("MIT", "Container-ASiC-E_CSSC_X-7.asice");
        validate("MIT", "Container-ASiC-E_CSSC_X-9.asice");
    }

    // ERROR:  'E:\ahmet\prj\MA3API\Manifest.dtd (The system cannot find the file specified)'
    @Test
    public void validateNTT() throws Exception {
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
    @Test
    public void validatePOL() throws Exception {
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
