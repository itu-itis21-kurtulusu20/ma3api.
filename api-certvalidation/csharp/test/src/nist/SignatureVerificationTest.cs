using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using NUnit.Framework;
/**
 * 4.1 Signature validation
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class SignatureVerificationTest : BaseNISTTest
    {
        //4.1.1
        [Test]
        public void testValidSignaturesTest1()
        {
            test("ValidCertificatePathTest1.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.VALID);
        }

        //4.1.2
        [Test]
        public void testInvalidCASignatureTest2()
        {
            test("InvalidCASignatureTest2.xml", "InvalidCASignatureTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.1.3
        [Test]
        public void testInvalidSignatureTest3()
        {
            test("ValidCertificatePathTest1.xml", "InvalidEESignatureTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.1.4
        [Test]
        public void testValidDSASignatureTest4()
        {
            test("ValidDSASignaturesTest4.xml", "ValidDSASignaturesTest4EE.crt", CertificateStatus.VALID);
        }

        //4.1.5
        [Test]
        public void testValidDSAParameterInheritanceTest5()
        {
            test("ValidDSAParameterInheritanceTest5.xml", "ValidDSAParameterInheritanceTest5EE.crt", CertificateStatus.VALID);
        }

        //4.1.6
        [Test]
        public void testInvalidDSASignatureTest6()
        {
            test("ValidDSASignaturesTest4.xml", "InvalidDSASignatureTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
