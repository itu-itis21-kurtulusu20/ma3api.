package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * 4.12 Inhibit Any Policy
 *
 * @author ayetgin
 */
public class InhibitAnyPolicyTest extends BaseNISTTest {

    // 4.12.1
    @Test
    public void testInvalidInhibitAnyPolicyTest1() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest1.xml", "InvalidInhibitAnyPolicyTest1EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.2
    @Test
    public void testValidInhibitAnyPolicyTest2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidInhibitAnyPolicyTest2.xml", "ValidInhibitAnyPolicyTest2EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.12.3.1
    @Test
    public void testInhibitAnyPolicyTest3_1() throws Exception {
        TestResult tr = sertifikaDogrula("InhibitAnyPolicyTest3_1.xml", "InhibitAnyPolicyTest3EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.12.3.2
    @Test
    public void testInhibitAnyPolicyTest3_2() throws Exception {
        TestResult tr = sertifikaDogrula("InhibitAnyPolicyTest3_2.xml", "InhibitAnyPolicyTest3EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.4
    @Test
    public void testInvalidInhibitAnyPolicyTest4() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest4.xml", "InvalidInhibitAnyPolicyTest4EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.5
    @Test
    public void testInvalidInhibitAnyPolicyTest5() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest5.xml", "InvalidInhibitAnyPolicyTest5EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.6
    @Test
    public void testInvalidInhibitAnyPolicyTest6() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest6.xml", "InvalidInhibitAnyPolicyTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.7
    @Test
    public void testValidSelfIssuedInhibitAnyPolicyTest7() throws Exception {
        TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitAnyPolicyTest7.xml", "ValidSelfIssuedInhibitAnyPolicyTest7EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.12.8
    @Test
    public void testInvalidSelfIssuedInhibitAnyPolicyTest8() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitAnyPolicyTest8.xml", "InvalidSelfIssuedInhibitAnyPolicyTest8EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.12.9
    @Test
    public void testValidSelfIssuedInhibitAnyPolicyTest9() throws Exception {
        TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitAnyPolicyTest9.xml", "ValidSelfIssuedInhibitAnyPolicyTest9EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.12.10
    @Test
    public void testInvalidSelfIssuedInhibitAnyPolicyTest10() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitAnyPolicyTest10.xml", "InvalidSelfIssuedInhibitAnyPolicyTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }


}
