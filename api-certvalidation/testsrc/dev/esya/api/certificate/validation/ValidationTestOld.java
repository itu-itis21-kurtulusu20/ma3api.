package dev.esya.api.certificate.validation;

import org.junit.Ignore;
import org.junit.Test;
import test.esya.api.certificate.validation.nist.BaseNISTTest;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

@Ignore("Certificates expiration time")
public class ValidationTestOld extends BaseNISTTest {

    // 4.15.5
    // removeFromCRL
    @Test
    public void testValiddeltaCRLTest5() throws Exception {
        test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest5EE.crt", CertificateStatus.VALID);
    }

    // 4.15.7
    //removeFromCRL
    @Test
    public void testValiddeltaCRLTest7() throws Exception {
        test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest7EE.crt", CertificateStatus.VALID);
    }

    // 4.14.25
    @Test
    public void testValidIDPWithIndirectCRLTest25() throws Exception {
        test("ValidIDPwithindirectCRLTest24.xml", "ValidIDPwithindirectCRLTest25EE.crt", CertificateStatus.VALID);
    }

    // 4.14.33
    @Test
    public void testValidCRLIssuerTest33() throws Exception {
        test("InvalidcRLIssuerTest31.xml", "ValidcRLIssuerTest33EE.crt", CertificateStatus.VALID);
    }

}
