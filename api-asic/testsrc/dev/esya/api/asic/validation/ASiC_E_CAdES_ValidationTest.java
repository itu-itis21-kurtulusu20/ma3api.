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
public class ASiC_E_CAdES_ValidationTest
{

    String root = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-E_CSSC_C.SCOK\\";

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
        validate("CRY", "Container-ASiC-E_CSSC_C-1.asice");
        validate("CRY", "Container-ASiC-E_CSSC_C-2.zip");
        validate("CRY", "Container-ASiC-E_CSSC_C-4.asice");
        validate("CRY", "Container-ASiC-E_CSSC_C-5.asice");
        validate("CRY", "Container-ASiC-E_CSSC_C-6.asice");
    }

    // todo İmzacı Sertifikası Özelliği V2 Kontrolcüsü
    // Özellikteki issuer serial alanı, signerinfo daki signeridentifier alanı
    // ile eşleşmiyor.
    @Test
    public void validateDIC() throws Exception {
        validate("DIC", "Container-ASiC-E_CSSC_C-1.asice");
        validate("DIC", "Container-ASiC-E_CSSC_C-2.zip");
        validate("DIC", "Container-ASiC-E_CSSC_C-3.asice");
        validate("DIC", "Container-ASiC-E_CSSC_C-4.asice");
        validate("DIC", "Container-ASiC-E_CSSC_C-5.asice");
        validate("DIC", "Container-ASiC-E_CSSC_C-6.asice");
    }

    @Test
    public void validateMIC() throws Exception {
        validate("MIC", "Container-ASiC-E_CSSC_C-1.asice");
        validate("MIC", "Container-ASiC-E_CSSC_C-2.zip");
        validate("MIC", "Container-ASiC-E_CSSC_C-3.asice");
        validate("MIC", "Container-ASiC-E_CSSC_C-4.asice");
        validate("MIC", "Container-ASiC-E_CSSC_C-5.asice");
        validate("MIC", "Container-ASiC-E_CSSC_C-6.asice");
    }

    @Test
    public void validateMIT() throws Exception {
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
    @Test
    public void validatePOL() throws Exception {
        validate("POL", "Container-ASiC-E_CSSC_C-1.asice");
        validate("POL", "Container-ASiC-E_CSSC_C-2.zip");
        validate("POL", "Container-ASiC-E_CSSC_C-3.asice");
        validate("POL", "Container-ASiC-E_CSSC_C-4.asice");
        validate("POL", "Container-ASiC-E_CSSC_C-5.asice");
        validate("POL", "Container-ASiC-E_CSSC_C-6.asice");
    }

}
