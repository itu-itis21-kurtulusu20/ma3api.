package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * 4.10 Policy Mappings
 *
 * @author ayetgin
 */
public class PolicyMappingsTest extends BaseNISTTest {

    // 4.10.1.1
    @Test
    public void testValidPolicyMappingTest1_1() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_1.xml", "ValidPolicyMappingTest1EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.1.2
    @Test
    public void testValidPolicyMappingTest1_2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_2.xml", "ValidPolicyMappingTest1EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.1.3
    @Test
    public void testValidPolicyMappingTest1_3() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_3.xml", "ValidPolicyMappingTest1EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.2.1
    @Test
    public void testInvalidPolicyMappingTest2_1() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest2_1.xml", "InvalidPolicyMappingTest2EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.2.2
    @Test
    public void testInvalidPolicyMappingTest2_2() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest2_2.xml", "InvalidPolicyMappingTest2EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.3.1
    @Test
    public void testValidPolicyMappingTest3_1() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest3_1.xml", "ValidPolicyMappingTest3EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.3.2
    @Test
    public void testValidPolicyMappingTest3_2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest3_2.xml", "ValidPolicyMappingTest3EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_2));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.4
    @Test
    public void testInvalidPolicyMappingTest4() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest4.xml", "InvalidPolicyMappingTest4EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.5.1
    @Test
    public void testValidPolicyMappingTest5_1() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest5_1.xml", "ValidPolicyMappingTest5EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.5.2
    @Test
    public void testValidPolicyMappingTest5_2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest5_2.xml", "ValidPolicyMappingTest5EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.6.1
    @Test
    public void testValidPolicyMappingTest6_1() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest6_1.xml", "ValidPolicyMappingTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.6.2
    @Test
    public void testValidPolicyMappingTest6_2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest6_2.xml", "ValidPolicyMappingTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.7
    @Test
    public void testInvalidMappingFromanyPolicyTest7() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidMappingFromanyPolicyTest7.xml", "InvalidMappingFromanyPolicyTest7EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.8
    @Test
    public void testInvalidMappingToanyPolicyTest8() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidMappingToanyPolicyTest8.xml", "InvalidMappingToanyPolicyTest8EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.9
    @Test
    public void testValidPolicyMappingTest9() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest9.xml", "ValidPolicyMappingTest9EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.10
    @Test
    public void testInValidPolicyMappingTest10() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest10.xml", "InvalidPolicyMappingTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.10.11
    @Test
    public void testValidPolicyMappingTest11() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest11.xml", "ValidPolicyMappingTest11EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.12.1
    @Test
    public void testValidPolicyMappingTest12_1() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest12_1.xml", "ValidPolicyMappingTest12EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.12.2
    @Test
    public void testValidPolicyMappingTest12_2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest12_2.xml", "ValidPolicyMappingTest12EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.13
    @Test
    public void testValidPolicyMappingTest13() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest13.xml", "ValidPolicyMappingTest13EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.10.14
    @Test
    public void testValidPolicyMappingTest14() throws Exception {
        TestResult tr = sertifikaDogrula("ValidPolicyMappingTest14.xml", "ValidPolicyMappingTest14EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

}
