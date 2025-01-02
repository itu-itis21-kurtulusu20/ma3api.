using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
/**
 * 4.2 Validity Periods
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class ValidityPeriodsTest : BaseNISTTest
    {
        // 4.2.1
        [Test]
        public void testInvalidCANotBeforeDateTest1()
        {
            test("InvalidCAnotBeforeDateTest1.xml", "InvalidCAnotBeforeDateTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.2.2
        [Test]
        public void testInvalidNotBeforeDateTest2()
        {
            test("ValidCertificatePathTest1.xml", "InvalidEEnotBeforeDateTest2EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        }

        // 4.2.3
        [Test]
        public void testValidpre2000UTCNotBeforeDateTest3()
        {
            test("ValidCertificatePathTest1.xml", "Validpre2000UTCnotBeforeDateTest3EE.crt", CertificateStatus.VALID);
        }

        // 4.2.4
        [Test]
        public void testValidGeneralizedTimeNotBeforeDateTest4()
        {
            test("ValidCertificatePathTest1.xml", "ValidGeneralizedTimenotBeforeDateTest4EE.crt", CertificateStatus.VALID);
        }

        // 4.2.5
        [Test]
        public void testInvalidCANotAfterDateTest5()
        {
            test("InvalidCAnotAfterDateTest5.xml", "InvalidCAnotAfterDateTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.2.6
        [Test]
        public void testInvalidNotAfterDateTest6()
        {
            test("ValidCertificatePathTest1.xml", "InvalidEEnotAfterDateTest6EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        }

        // 4.2.7
        [Test]
        public void testInvalidpre2000UTCNotAfterDateTest7()
        {
            test("ValidCertificatePathTest1.xml", "Invalidpre2000UTCEEnotAfterDateTest7EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        }

        // 4.2.8
        [Test]
        public void testValidGeneralizedTimeNotAfterDateTest8()
        {
            test("ValidCertificatePathTest1.xml", "ValidGeneralizedTimeNotAfterDateTest8EE.crt", CertificateStatus.VALID);
        }

    }
}
