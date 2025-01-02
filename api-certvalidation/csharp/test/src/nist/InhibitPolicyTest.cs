using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.11 Inhibit Policy Mapping
 * 
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    class InhibitPolicyTest : BaseNISTTest
    {
        // 4.11.1
        [Test]
        public void testInvalidInhibitPolicyMappingTest1()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest1.xml", "InvalidInhibitPolicyMappingTest1EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.2
        [Test]
        public void testValidInhibitPolicyMappingTest2()
        {
            TestResult tr = sertifikaDogrula("ValidInhibitPolicyMappingTest2.xml", "ValidInhibitPolicyMappingTest2EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.11.3
        [Test]
        public void testInvalidInhibitPolicyMappingTest3()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest3.xml", "InvalidInhibitPolicyMappingTest3EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.4
        [Test]
        public void testValidInhibitPolicyMappingTest4()
        {
            TestResult tr = sertifikaDogrula("ValidInhibitPolicyMappingTest4.xml", "ValidInhibitPolicyMappingTest4EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_2));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.11.5
        [Test]
        public void testInvalidInhibitPolicyMappingTest5()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest5.xml", "InvalidInhibitPolicyMappingTest5EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.6
        [Test]
        public void testInvalidInhibitPolicyMappingTest6()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitPolicyMappingTest6.xml", "InvalidInhibitPolicyMappingTest6EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.7
        [Test]
        public void testValidSelfIssuedInhibitPolicyMappingTest7()
        {
            TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitPolicyMappingTest7.xml", "ValidSelfIssuedInhibitPolicyMappingTest7EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.11.8
        [Test]
        public void testInvalidSelfIssuedInhibitPolicyMappingTest8()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest8.xml", "InvalidSelfIssuedInhibitPolicyMappingTest8EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.9
        [Test]
        public void testInvalidSelfIssuedInhibitPolicyMappingTest9()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest9.xml", "InvalidSelfIssuedInhibitPolicyMappingTest9EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.10
        [Test]
        public void testInvalidSelfIssuedInhibitPolicyMappingTest10()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest10.xml", "InvalidSelfIssuedInhibitPolicyMappingTest10EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.11.11
        [Test]
        public void testInvalidSelfIssuedInhibitPolicyMappingTest11()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitPolicyMappingTest11.xml", "InvalidSelfIssuedInhibitPolicyMappingTest11EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

    }
}
