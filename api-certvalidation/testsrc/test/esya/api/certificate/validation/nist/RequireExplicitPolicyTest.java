package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.9 Require Explicit Policy
 *
 * @author ayetgin
 */
public class RequireExplicitPolicyTest extends BaseNISTTest {

    // 4.9.1
    @Test
    public void testValidRequireExplicitPolicyTest1() throws Exception {
        test("ValidRequireExplicitPolicyTest1.xml", "ValidRequireExplicitPolicyTest1EE.crt", CertificateStatus.VALID);
    }

    // 4.9.2
    @Test
    public void testValidRequireExplicitPolicyTest2() throws Exception {
        test("ValidRequireExplicitPolicyTest2.xml", "ValidRequireExplicitPolicyTest2EE.crt", CertificateStatus.VALID);
    }

    // 4.9.3
    @Test
    public void testInvalidRequireExplicitPolicyTest3() throws Exception {
        test("InvalidRequireExplicitPolicyTest3.xml", "InvalidRequireExplicitPolicyTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.9.4
    @Test
    public void testValidRequireExplicitPolicyTest4() throws Exception {
        test("ValidRequireExplicitPolicyTest4.xml", "ValidRequireExplicitPolicyTest4EE.crt", CertificateStatus.VALID);
    }

    // 4.9.5
    @Test
    public void testInvalidRequireExplicitPolicyTest5() throws Exception {
        test("InvalidRequireExplicitPolicyTest5.xml", "InvalidRequireExplicitPolicyTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.9.6
    @Test
    public void testValidSelfIssuedRequireExplicitPolicyTest6() throws Exception {
        test("ValidSelfIssuedRequireExplicitPolicyTest6.xml", "ValidSelfIssuedRequireExplicitPolicyTest6EE.crt", CertificateStatus.VALID);
    }

    // 4.9.7
    @Test
    public void testInvalidSelfIssuedRequireExplicitPolicyTest7() throws Exception {
        test("InvalidSelfIssuedRequireExplicitPolicyTest7.xml", "InvalidSelfIssuedRequireExplicitPolicyTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.9.8
    @Test
    public void testInvalidSelfIssuedRequireExplicitPolicyTest8() throws Exception {
        test("InvalidSelfIssuedRequireExplicitPolicyTest8.xml", "InvalidSelfIssuedRequireExplicitPolicyTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}
