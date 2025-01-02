using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;

/**
 * 4.10 Policy Mappings
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class PolicyMappingsTest : BaseNISTTest
    {
        // 4.10.1.1
        [Test]
        public void testValidPolicyMappingTest1_1()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_1.xml", "ValidPolicyMappingTest1EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.1.2
        [Test]
        public void testValidPolicyMappingTest1_2()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_2.xml", "ValidPolicyMappingTest1EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.1.3
        [Test]
        public void testValidPolicyMappingTest1_3()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest1_3.xml", "ValidPolicyMappingTest1EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.2.1
        [Test]
        public void testInvalidPolicyMappingTest2_1()
        {
            TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest2_1.xml", "InvalidPolicyMappingTest2EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.2.2
        [Test]
        public void testInvalidPolicyMappingTest2_2()
        {
            TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest2_2.xml", "InvalidPolicyMappingTest2EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.3.1
        [Test]
        public void testValidPolicyMappingTest3_1()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest3_1.xml", "ValidPolicyMappingTest3EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.3.2
        [Test]
        public void testValidPolicyMappingTest3_2()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest3_2.xml", "ValidPolicyMappingTest3EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_2));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_2));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.4
        [Test]
        public void testInvalidPolicyMappingTest4()
        {
            TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest4.xml", "InvalidPolicyMappingTest4EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.5.1
        [Test]
        public void testValidPolicyMappingTest5_1()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest5_1.xml", "ValidPolicyMappingTest5EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.5.2
        [Test]
        public void testValidPolicyMappingTest5_2()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest5_2.xml", "ValidPolicyMappingTest5EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.6.1
        [Test]
        public void testValidPolicyMappingTest6_1()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest6_1.xml", "ValidPolicyMappingTest6EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.6.2
        [Test]
        public void testValidPolicyMappingTest6_2()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest6_2.xml", "ValidPolicyMappingTest6EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.7
        [Test]
        public void testInvalidMappingFromanyPolicyTest7()
        {
            TestResult tr = sertifikaDogrula("InvalidMappingFromanyPolicyTest7.xml", "InvalidMappingFromanyPolicyTest7EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.8
        [Test]
        public void testInvalidMappingToanyPolicyTest8()
        {
            TestResult tr = sertifikaDogrula("InvalidMappingToanyPolicyTest8.xml", "InvalidMappingToanyPolicyTest8EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.9
        [Test]
        public void testValidPolicyMappingTest9()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest9.xml", "ValidPolicyMappingTest9EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.10
        [Test]
        public void testInValidPolicyMappingTest10()
        {
            TestResult tr = sertifikaDogrula("InvalidPolicyMappingTest10.xml", "InvalidPolicyMappingTest10EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.10.11
        [Test]
        public void testValidPolicyMappingTest11()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest11.xml", "ValidPolicyMappingTest11EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.12.1
        [Test]
        public void testValidPolicyMappingTest12_1()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest12_1.xml", "ValidPolicyMappingTest12EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_2));
            Assert.False(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.12.2
        [Test]
        public void testValidPolicyMappingTest12_2()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest12_2.xml", "ValidPolicyMappingTest12EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_2));
            Assert.False(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.13
        [Test]
        public void testValidPolicyMappingTest13()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest13.xml", "ValidPolicyMappingTest13EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.10.14
        [Test]
        public void testValidPolicyMappingTest14()
        {
            TestResult tr = sertifikaDogrula("ValidPolicyMappingTest14.xml", "ValidPolicyMappingTest14EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }
    }
}
