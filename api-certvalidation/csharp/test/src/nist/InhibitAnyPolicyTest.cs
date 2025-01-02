using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.12 Inhibit Any Policy
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    class InhibitAnyPolicyTest : BaseNISTTest
    {
        // 4.12.1
        [Test]
        public void testInvalidInhibitAnyPolicyTest1()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest1.xml", "InvalidInhibitAnyPolicyTest1EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.2
        [Test]
        public void testValidInhibitAnyPolicyTest2()
        {
            TestResult tr = sertifikaDogrula("ValidInhibitAnyPolicyTest2.xml", "ValidInhibitAnyPolicyTest2EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.12.3.1
        [Test]
        public void testInhibitAnyPolicyTest3_1()
        {
            TestResult tr = sertifikaDogrula("InhibitAnyPolicyTest3_1.xml", "InhibitAnyPolicyTest3EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.12.3.2
        [Test]
        public void testInhibitAnyPolicyTest3_2()
        {
            TestResult tr = sertifikaDogrula("InhibitAnyPolicyTest3_2.xml", "InhibitAnyPolicyTest3EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.4
        [Test]
        public void testInvalidInhibitAnyPolicyTest4()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest4.xml", "InvalidInhibitAnyPolicyTest4EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.5
        [Test]
        public void testInvalidInhibitAnyPolicyTest5()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest5.xml", "InvalidInhibitAnyPolicyTest5EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.6
        [Test]
        public void testInvalidInhibitAnyPolicyTest6()
        {
            TestResult tr = sertifikaDogrula("InvalidInhibitAnyPolicyTest6.xml", "InvalidInhibitAnyPolicyTest6EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.7
        [Test]
        public void testValidSelfIssuedInhibitAnyPolicyTest7()
        {
            TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitAnyPolicyTest7.xml", "ValidSelfIssuedInhibitAnyPolicyTest7EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.12.8
        [Test]
        public void testInvalidSelfIssuedInhibitAnyPolicyTest8()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitAnyPolicyTest8.xml", "InvalidSelfIssuedInhibitAnyPolicyTest8EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.12.9
        [Test]
        public void testValidSelfIssuedInhibitAnyPolicyTest9()
        {
            TestResult tr = sertifikaDogrula("ValidSelfIssuedInhibitAnyPolicyTest9.xml", "ValidSelfIssuedInhibitAnyPolicyTest9EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.True(tr.getUserConstrainedPolicySet().Contains(NIST_POLICY_1));
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.VALID);
        }

        // 4.12.10
        [Test]
        public void testInvalidSelfIssuedInhibitAnyPolicyTest10()
        {
            TestResult tr = sertifikaDogrula("InvalidSelfIssuedInhibitAnyPolicyTest10.xml", "InvalidSelfIssuedInhibitAnyPolicyTest10EE.crt");
            Assert.True(tr.getAuthoritiesConstrainedPolicySet().Count == 0);
            Assert.True(tr.getUserConstrainedPolicySet().Count == 0);
            Assert.AreEqual(tr.getCertificateStatusInfo().getCertificateStatus(), CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
