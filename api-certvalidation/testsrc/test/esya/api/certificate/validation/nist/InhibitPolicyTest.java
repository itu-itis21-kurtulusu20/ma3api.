package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * 4.11 Inhibit Policy Mapping
 *
 * @author ayetgin
 */
public class InhibitPolicyTest extends BaseNISTTest {

    // 4.11.1
    @Test
    public void testInvalidInhibitPolicyMappingTest1() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest1.xml", "InvalidInhibitPolicyMappingTest1EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.2
    @Test
    public void testValidInhibitPolicyMappingTest2() throws Exception {
        TestResult tr = sertifikaDogrula("ValidInhibitPolicyMappingTest2.xml", "ValidInhibitPolicyMappingTest2EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.11.3
    @Test
    public void testInvalidInhibitPolicyMappingTest3() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest3.xml", "InvalidInhibitPolicyMappingTest3EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.4
    @Test
    public void testValidInhibitPolicyMappingTest4() throws Exception {
        TestResult tr = sertifikaDogrula("ValidInhibitPolicyMappingTest4.xml", "ValidInhibitPolicyMappingTest4EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.11.5
    @Test
    public void testInvalidInhibitPolicyMappingTest5() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest5.xml", "InvalidInhibitPolicyMappingTest5EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.6
    @Test
    public void testInvalidInhibitPolicyMappingTest6() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest6.xml", "InvalidInhibitPolicyMappingTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.7
    @Test
    public void testValidSelfIssuedInhibitPolicyMappingTest7() throws Exception {
        TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitPolicyMappingTest7.xml", "ValidSelfIssuedInhibitPolicyMappingTest7EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.11.8
    @Test
    public void testInvalidSelfIssuedInhibitPolicyMappingTest8() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest8.xml", "InvalidSelfIssuedInhibitPolicyMappingTest8EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.9
    @Test
    public void testInvalidSelfIssuedInhibitPolicyMappingTest9() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest9.xml", "InvalidSelfIssuedInhibitPolicyMappingTest9EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.10
    @Test
    public void testInvalidSelfIssuedInhibitPolicyMappingTest10() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest10.xml", "InvalidSelfIssuedInhibitPolicyMappingTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.11.11
    @Test
    public void testInvalidSelfIssuedInhibitPolicyMappingTest11() throws Exception {
        TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest11.xml", "InvalidSelfIssuedInhibitPolicyMappingTest11EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }


}
