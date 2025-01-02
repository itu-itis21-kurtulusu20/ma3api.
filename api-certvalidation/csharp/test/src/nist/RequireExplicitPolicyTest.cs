using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.9 Require Explicit Policy
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class RequireExplicitPolicyTest : BaseNISTTest
    {
        // 4.9.1
        [Test]
        public void testValidRequireExplicitPolicyTest1()
        {
            test("ValidRequireExplicitPolicyTest1.xml", "ValidRequireExplicitPolicyTest1EE.crt", CertificateStatus.VALID);
        }

        // 4.9.2
        [Test]
        public void testValidRequireExplicitPolicyTest2()
        {
            test("ValidRequireExplicitPolicyTest2.xml", "ValidRequireExplicitPolicyTest2EE.crt", CertificateStatus.VALID);
        }

        // 4.9.3
        [Test]
        public void testInvalidRequireExplicitPolicyTest3()
        {
            test("InvalidRequireExplicitPolicyTest3.xml", "InvalidRequireExplicitPolicyTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.9.4
        [Test]
        public void testValidRequireExplicitPolicyTest4()
        {
            test("ValidRequireExplicitPolicyTest4.xml", "ValidRequireExplicitPolicyTest4EE.crt", CertificateStatus.VALID);
        }

        // 4.9.5
        [Test]
        public void testInvalidRequireExplicitPolicyTest5()
        {
            test("InvalidRequireExplicitPolicyTest5.xml", "InvalidRequireExplicitPolicyTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.9.6
        [Test]
        public void testValidSelfIssuedRequireExplicitPolicyTest6()
        {
            test("ValidSelfIssuedRequireExplicitPolicyTest6.xml", "ValidSelfIssuedRequireExplicitPolicyTest6EE.crt", CertificateStatus.VALID);
        }

        // 4.9.7
        [Test]
        public void testInvalidSelfIssuedRequireExplicitPolicyTest7()
        {
            test("InvalidSelfIssuedRequireExplicitPolicyTest7.xml", "InvalidSelfIssuedRequireExplicitPolicyTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.9.8
        [Test]
        public void testInvalidSelfIssuedRequireExplicitPolicyTest8()
        {
            test("InvalidSelfIssuedRequireExplicitPolicyTest8.xml", "InvalidSelfIssuedRequireExplicitPolicyTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
