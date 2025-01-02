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
public class ASiC_S_XAdES_ValidationTest
{

    String root = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\birstunas\\ASiC-S_CSSC_X.SCOK\\";

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
        validate("CRY", "Container-ASiC-S_CSSC_X-1.asics");
        validate("CRY", "Container-ASiC-S_CSSC_X-2.zip");
        validate("CRY", "Container-ASiC-S_CSSC_X-4.asics");
        validate("CRY", "Container-ASiC-S_CSSC_X-5.asics");
        validate("CRY", "Container-ASiC-S_CSSC_X-6.asics");
    }

    // todo (X) XML İmza Kontrolü
    //    Tutarsız imza değeri.
    @Test
    public void validateDIC() throws Exception {
        validate("DIC", "Container-ASiC-S_CSSC_X-1.asics");
        validate("DIC", "Container-ASiC-S_CSSC_X-2.zip");
        validate("DIC", "Container-ASiC-S_CSSC_X-3.asics");
        validate("DIC", "Container-ASiC-S_CSSC_X-4.asics");
        validate("DIC", "Container-ASiC-S_CSSC_X-5.asics");
        validate("DIC", "Container-ASiC-S_CSSC_X-6.asics");
    }

    @Test
    public void validateEC() throws Exception {
        validate("EC", "Container-ASiC-S_CSSC_X-1.asics");
        validate("EC", "Container-ASiC-S_CSSC_X-2.zip");
        validate("EC", "Container-ASiC-S_CSSC_X-3.asics");
        validate("EC", "Container-ASiC-S_CSSC_X-4.asics");
        validate("EC", "Container-ASiC-S_CSSC_X-5.asics");
        validate("EC", "Container-ASiC-S_CSSC_X-6.asics");
    }

    @Test
    public void validateMIC() throws Exception {
        validate("MIC", "Container-ASiC-S_CSSC_X-1.asics");
        validate("MIC", "Container-ASiC-S_CSSC_X-2.zip");
        validate("MIC", "Container-ASiC-S_CSSC_X-3.asics");
        validate("MIC", "Container-ASiC-S_CSSC_X-4.asics");
        validate("MIC", "Container-ASiC-S_CSSC_X-5.asics");
        validate("MIC", "Container-ASiC-S_CSSC_X-6.asics");
    }

    /*  todo (X) XML İmza Kontrolü
        Tutarsız imza değeri. */
    @Test
    public void validateMIT() throws Exception {
        validate("MIT", "Container-ASiC-S_CSSC_X-1.asics");
        validate("MIT", "Container-ASiC-S_CSSC_X-2.zip");
        validate("MIT", "Container-ASiC-S_CSSC_X-3.asics");
        validate("MIT", "Container-ASiC-S_CSSC_X-4.asics");
        validate("MIT", "Container-ASiC-S_CSSC_X-5.asics");
        validate("MIT", "Container-ASiC-S_CSSC_X-6.asics");
    }

    @Test
    public void validateNTT() throws Exception {
        validate("NTT", "Container-ASiC-S_CSSC_X-1.asics");
        validate("NTT", "Container-ASiC-S_CSSC_X-2.zip");
        validate("NTT", "Container-ASiC-S_CSSC_X-3.asics");
        validate("NTT", "Container-ASiC-S_CSSC_X-4.asics");
        validate("NTT", "Container-ASiC-S_CSSC_X-5.asics");
        validate("NTT", "Container-ASiC-S_CSSC_X-6.asics");
    }

    // todo Zaman Damgası Sertifikası Kontrol ediliyor mu?
    @Test
    public void validatePOL() throws Exception {
        validate("POL", "Container-ASiC-S_CSSC_X-1.asics");
        validate("POL", "Container-ASiC-S_CSSC_X-2.zip");
        validate("POL", "Container-ASiC-S_CSSC_X-3.asics");
        validate("POL", "Container-ASiC-S_CSSC_X-4.asics");
        validate("POL", "Container-ASiC-S_CSSC_X-5.asics");
        validate("POL", "Container-ASiC-S_CSSC_X-6.asics");
    }

    // todo sertifika bulma, name match vs.
    @Test
    public void validateSIX() throws Exception {
        validate("SIX", "Container-ASiC-S_CSSC_X-1.asics");
        validate("SIX", "Container-ASiC-S_CSSC_X-2.zip");
        validate("SIX", "Container-ASiC-S_CSSC_X-3.asics");
        validate("SIX", "Container-ASiC-S_CSSC_X-4.asics");
        validate("SIX", "Container-ASiC-S_CSSC_X-5.asics");
        validate("SIX", "Container-ASiC-S_CSSC_X-6.asics");
    }

}
