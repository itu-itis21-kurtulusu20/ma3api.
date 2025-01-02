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
public class ASiC_S_CAdES_ValidationTest
{

    String root = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-S_CSSC_C.SCOK\\";

    public void validate(String company, String file) throws Exception
    {
        Context c = new Context();
        File f = new File(root + company + "\\"+file);
        System.out.println("Validate "+company+"/"+file);
        SignaturePackage sp = SignaturePackageFactory.readPackage(c, f);
        PackageValidationResult pvr = sp.verifyAll();
        System.out.println(pvr);
    }

    @Test
    public void validateCRY() throws Exception {
        validate("CRY", "Container-ASiC-S_CSSC_C-1.asics");
        validate("CRY", "Container-ASiC-S_CSSC_C-2.zip");
        validate("CRY", "Container-ASiC-S_CSSC_C-4.asics");
        validate("CRY", "Container-ASiC-S_CSSC_C-5.asics");
        validate("CRY", "Container-ASiC-S_CSSC_C-6.asics");
    }

    // todo İmzacı Sertifikası Özelliği V2 Kontrolcüsü
    // Özellikteki issuer serial alanı, signerinfo daki signeridentifier alanı
    // ile eşleşmiyor.
    @Test
    public void validateDIC() throws Exception {
        validate("DIC", "Container-ASiC-S_CSSC_C-1.asics");
        validate("DIC", "Container-ASiC-S_CSSC_C-2.zip");
        validate("DIC", "Container-ASiC-S_CSSC_C-3.asics");
        validate("DIC", "Container-ASiC-S_CSSC_C-4.asics");
        validate("DIC", "Container-ASiC-S_CSSC_C-5.asics");
        validate("DIC", "Container-ASiC-S_CSSC_C-6.asics");
    }

    @Test
    public void validateMIC() throws Exception {
        validate("MIC", "Container-ASiC-S_CSSC_C-1.asics");
        validate("MIC", "Container-ASiC-S_CSSC_C-2.zip");
        validate("MIC", "Container-ASiC-S_CSSC_C-3.asics");
        validate("MIC", "Container-ASiC-S_CSSC_C-4.asics");
        validate("MIC", "Container-ASiC-S_CSSC_C-5.asics");
        validate("MIC", "Container-ASiC-S_CSSC_C-6.asics");
    }

    @Test
    public void validateMIT() throws Exception {
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
    @Test
    public void validatePOL() throws Exception {
        validate("POL", "Container-ASiC-S_CSSC_C-1.asics");
        validate("POL", "Container-ASiC-S_CSSC_C-2.zip");
        validate("POL", "Container-ASiC-S_CSSC_C-3.asics");
        validate("POL", "Container-ASiC-S_CSSC_C-4.asics");
        validate("POL", "Container-ASiC-S_CSSC_C-5.asics");
        validate("POL", "Container-ASiC-S_CSSC_C-6.asics");
    }

}
