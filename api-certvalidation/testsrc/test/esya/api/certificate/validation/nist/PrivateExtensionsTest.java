package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.16 Private Certificate Extensions
 *
 * @author ayetgin
 */
public class PrivateExtensionsTest extends BaseNISTTest {

    // 4.16.1
    @Test
    public void testValidUnknownNotCriticalCertificateExtensionTest1() throws Exception {
        test("ValidUnknownNotCriticalCertificateExtensionTest1EE.xml", "ValidUnknownNotCriticalCertificateExtensionTest1EE.crt", CertificateStatus.VALID);
    }

    // 4.16.2
    @Test
    public void testInvalidUnknownCriticalCertificateExtensionTest2() throws Exception {
        test("ValidUnknownNotCriticalCertificateExtensionTest1EE.xml", "InvalidUnknownCriticalCertificateExtensionTest2EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
    }


}
