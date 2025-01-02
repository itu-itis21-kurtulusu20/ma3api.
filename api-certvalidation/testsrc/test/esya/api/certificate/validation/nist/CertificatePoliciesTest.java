package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * 4.8 Certificate Policies
 *
 * @author ayetgin
 */
public class CertificatePoliciesTest extends BaseNISTTest {

    //4.8.1.1
    @Test
    public void testAllCertificatesSamePolicyTest1_1() throws Exception {
        //	If the initial-explicit-policy indicator is set
        // 	and the initial-policy-set include any_policy, then the
        // 	path should be validated
        test("AllCertificatesSamePolicyTest1_1.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.VALID);

    }

    //4.8.1.2
    @Test
    public void testAllCertificatesSamePolicyTest1_2() throws Exception {
        //	If the initial-explicit-policy indicator is set
        // 	and the initial-policy-set include NIST-test-policy-1, then the
        // 	path should be validated
        test("AllCertificatesSamePolicyTest1_2.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.VALID);

    }

    //4.8.1.3
    @Test
    public void testAllCertificatesSamePolicyTest1_3() throws Exception {
        //	If the initial-explicit-policy indicator is set
        // 	and the initial-policy-set does not include NIST-test-policy-1, then the
        // 	path should be rejected
        test("AllCertificatesSamePolicyTest1_3.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.8.1.4
    @Test
    public void testAllCertificatesSamePolicyTest1_4() throws Exception {
        //	If the initial-explicit-policy indicator is set
        // 	and the initial-policy-set does include NIST-test-policy-1 and NIST-test-policy-2, then the
        // 	path should be rejected
        test("AllCertificatesSamePolicyTest1_4.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.VALID);
    }

    // 4.8.2.1
    @Test
    public void testAllCertificatesNoPolicyTest1_1() throws Exception {
        test("AllCertificatesNoPolicyTest1_1.xml", "AllCertificatesNoPoliciesTest2EE.crt", CertificateStatus.VALID);
    }

    // 4.8.2.2
    @Test
    public void testAllCertificatesNoPolicyTest1_2() throws Exception {
        test("AllCertificatesNoPolicyTest1_2.xml", "AllCertificatesNoPoliciesTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.3.1
    @Test
    public void testDifferentPoliciesTest3_1() throws Exception {
        test("DifferentPoliciesTest3_1.xml", "DifferentPoliciesTest3EE.crt", CertificateStatus.VALID);
    }

    // 4.8.3.2
    @Test
    public void testDifferentPoliciesTest3_2() throws Exception {
        test("DifferentPoliciesTest3_2.xml", "DifferentPoliciesTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.3.3
    @Test
    public void testDifferentPoliciesTest3_3() throws Exception {
        test("DifferentPoliciesTest3_3.xml", "DifferentPoliciesTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.4
    @Test
    public void testDifferentPoliciesTest4() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest4.xml", "DifferentPoliciesTest4EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.5
    @Test
    public void testDifferentPoliciesTest5() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest5.xml", "DifferentPoliciesTest5EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.6.1
    @Test
    public void testOverlappingPoliciesTest6_1() throws Exception {
        TestResult tr = sertifikaDogrula("OverlappingPoliciesTest6_1.xml", "OverlappingPoliciesTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.6.2
    @Test
    public void testOverlappingPoliciesTest6_2() throws Exception {
        TestResult tr = sertifikaDogrula("OverlappingPoliciesTest6_2.xml", "OverlappingPoliciesTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.6.3
    @Test
    public void testOverlappingPoliciesTest6_3() throws Exception {
        TestResult tr = sertifikaDogrula("OverlappingPoliciesTest6_3.xml", "OverlappingPoliciesTest6EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.7
    @Test
    public void testDifferentPoliciesTest7() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest7.xml", "DifferentPoliciesTest7EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.8
    @Test
    public void testDifferentPoliciesTest8() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest8.xml", "DifferentPoliciesTest8EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.9
    @Test
    public void testDifferentPoliciesTest9() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest9.xml", "DifferentPoliciesTest9EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.10.1
    @Test
    public void testAllCertificatesSamePoliciesTest10_1() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest10_1.xml", "AllCertificatesSamePoliciesTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.10.2
    @Test
    public void testAllCertificatesSamePoliciesTest10_2() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest10_2.xml", "AllCertificatesSamePoliciesTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.10.3
    @Test
    public void testAllCertificatesSamePoliciesTest10_3() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest10_3.xml", "AllCertificatesSamePoliciesTest10EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.11.1
    @Test
    public void testAllCertificatesAnyPolicyTest11_1() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesanyPolicyTest11_1.xml", "AllCertificatesanyPolicyTest11EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(Constants.IMP_ANY_POLICY));
        assertTrue(tr.getUserConstrainedPolicySet().contains(Constants.IMP_ANY_POLICY));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.11.2
    @Test
    public void testAllCertificatesAnyPolicyTest11_2() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesanyPolicyTest11_2.xml", "AllCertificatesanyPolicyTest11EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(Constants.IMP_ANY_POLICY));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.12
    @Test
    public void testDifferentPoliciesTest12() throws Exception {
        TestResult tr = sertifikaDogrula("DifferentPoliciesTest12.xml", "DifferentPoliciesTest12EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().isEmpty());
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.13.1
    @Test
    public void testAllCertificatesSamePoliciesTest13_1() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest13_1.xml", "AllCertificatesSamePoliciesTest13EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_3));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    //4.8.13.2
    @Test
    public void testAllCertificatesSamePoliciesTest13_2() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest13_2.xml", "AllCertificatesSamePoliciesTest13EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_3));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.13.3
    @Test
    public void testAllCertificatesSamePoliciesTest13_3() throws Exception {
        TestResult tr = sertifikaDogrula("AllCertificatesSamePoliciesTest13_3.xml", "AllCertificatesSamePoliciesTest13EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_2));
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_3));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.14.1
    @Test
    public void testAnyPolicyTest14_1() throws Exception {
        TestResult tr = sertifikaDogrula("AnyPolicyTest14_1.xml", "AnyPolicyTest14EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.14.2
    @Test
    public void testAnyPolicyTest14_2() throws Exception {
        TestResult tr = sertifikaDogrula("AnyPolicyTest14_2.xml", "AnyPolicyTest14EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.8.15
    @Test
    public void testUserNoticeQualifierTest15() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest15.xml", "UserNoticeQualifierTest15EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.16
    @Test
    public void testUserNoticeQualifierTest16() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest16.xml", "UserNoticeQualifierTest16EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.17
    @Test
    public void testUserNoticeQualifierTest17() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest17.xml", "UserNoticeQualifierTest17EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.18.1
    @Test
    public void testUserNoticeQualifierTest18_1() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest18_1.xml", "UserNoticeQualifierTest18EE.crt");
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.18.2
    @Test
    public void testUserNoticeQualifierTest18_2() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest18_2.xml", "UserNoticeQualifierTest18EE.crt");
        assertTrue(tr.getValidQualifierSet().size() == 1);
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertFalse(tr.getUserConstrainedPolicySet().isEmpty());
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.19
    @Test
    public void testUserNoticeQualifierTest19() throws Exception {
        TestResult tr = sertifikaDogrula("UserNoticeQualifierTest19.xml", "UserNoticeQualifierTest19EE.crt");
        assertTrue(tr.getValidQualifierSet().size() == 1);
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

    // 4.8.20
    @Test
    public void testCPSPointerQualifierTest20() throws Exception {
        TestResult tr = sertifikaDogrula("CPSPointerQualifierTest20.xml", "CPSPointerQualifierTest20EE.crt");
        assertTrue(tr.getValidQualifierSet().size() == 1);
        assertTrue(tr.getAuthoritiesConstrainedPolicySet().contains(NIST_POLICY_1));
        assertTrue(tr.getUserConstrainedPolicySet().contains(NIST_POLICY_1));
        assertEquals(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
    }

}
