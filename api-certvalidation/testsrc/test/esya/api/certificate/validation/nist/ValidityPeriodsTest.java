package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.2 Validity Periods
 *
 * @author ayetgin
 */
public class ValidityPeriodsTest extends BaseNISTTest {

    // 4.2.1
    @Test
    public void testInvalidCANotBeforeDateTest1() throws Exception {
        test("InvalidCAnotBeforeDateTest1.xml", "InvalidCAnotBeforeDateTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.2.2
    @Test
    public void testInvalidNotBeforeDateTest2() throws Exception {
        test("ValidCertificatePathTest1.xml", "InvalidEEnotBeforeDateTest2EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
    }

    // 4.2.3
    @Test
    public void testValidpre2000UTCNotBeforeDateTest3() throws Exception {
        test("ValidCertificatePathTest1.xml", "Validpre2000UTCnotBeforeDateTest3EE.crt", CertificateStatus.VALID);
    }

    // 4.2.4
    @Test
    public void testValidGeneralizedTimeNotBeforeDateTest4() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidGeneralizedTimenotBeforeDateTest4EE.crt", CertificateStatus.VALID);
    }

    // 4.2.5
    @Test
    public void testInvalidCANotAfterDateTest5() throws Exception {
        test("InvalidCAnotAfterDateTest5.xml", "InvalidCAnotAfterDateTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.2.6
    @Test
    public void testInvalidNotAfterDateTest6() throws Exception {
        test("ValidCertificatePathTest1.xml", "InvalidEEnotAfterDateTest6EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
    }

    // 4.2.7
    @Test
    public void testInvalidpre2000UTCNotAfterDateTest7() throws Exception {
        test("ValidCertificatePathTest1.xml", "Invalidpre2000UTCEEnotAfterDateTest7EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
    }

    // 4.2.8
    @Test
    public void testValidGeneralizedTimeNotAfterDateTest8() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidGeneralizedTimeNotAfterDateTest8EE.crt", CertificateStatus.VALID);
    }

}
