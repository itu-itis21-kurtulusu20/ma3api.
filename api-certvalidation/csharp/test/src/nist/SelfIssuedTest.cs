using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;

/**
 *      4.5 Verifying Paths with Self-Issued Certificates
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class SelfIssuedTest : BaseNISTTest
    {
        [Test]
        public void testValidBasicSelfIssuedOldWithNewTest1()
        {
            test("ValidBasicSelfIssuedOldWithNewTest1.xml", "ValidBasicSelfIssuedOldWithNewTest1EE.crt", CertificateStatus.VALID);
        }

        [Test]
        public void testInvalidBasicSelfIssuedOldWithNewTest2()
        {
            test("ValidBasicSelfIssuedOldWithNewTest1.xml", "InvalidBasicSelfIssuedOldWithNewTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        [Test]
        public void testValidBasicSelfIssuedNewWithOldTest3()
        {
            test("ValidBasicSelfIssuedNewWithOldTest3.xml", "ValidBasicSelfIssuedNewWithOldTest3EE.crt", CertificateStatus.VALID);
        }

        [Test]
        public void testValidBasicSelfIssuedNewWithOldTest4()
        {
            test("ValidBasicSelfIssuedNewWithOldTest3.xml", "ValidBasicSelfIssuedNewWithOldTest4EE.crt", CertificateStatus.VALID);
        }

        [Test]
        public void testInvalidBasicSelfIssuedNewWithOldTest5()
        {
            test("ValidBasicSelfIssuedNewWithOldTest3.xml", "InvalidBasicSelfIssuedNewWithOldTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        [Test]
        public void testValidBasicSelfIssuedCRLSigningKeyTest6()
        {
            test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "ValidBasicSelfIssuedCRLSigningKeyTest6EE.crt", CertificateStatus.VALID);
        }

        [Test]
        public void testInvalidBasicSelfIssuedCRLSigningKeyTest7()
        {
            test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "InvalidBasicSelfIssuedCRLSigningKeyTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        [Test]
        public void testInvalidBasicSelfIssuedCRLSigningKeyTest8()
        {
            test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "InvalidBasicSelfIssuedCRLSigningKeyTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
