using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 *      4.16 Private Certificate Extensions
 * 
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class PrivateExtensionsTest : BaseNISTTest
    {
        // 4.16.1
        [Test]
        public void testValidUnknownNotCriticalCertificateExtensionTest1()
        {
            test("ValidUnknownNotCriticalCertificateExtensionTest1EE.xml", "ValidUnknownNotCriticalCertificateExtensionTest1EE.crt", CertificateStatus.VALID);
        }

        // 4.16.2
        [Test]
        public void testInvalidUnknownCriticalCertificateExtensionTest2()
        {
            test("ValidUnknownNotCriticalCertificateExtensionTest1EE.xml", "InvalidUnknownCriticalCertificateExtensionTest2EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        }

    }
}
