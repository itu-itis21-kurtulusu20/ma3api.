using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using NUnit.Framework;
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    /**
 *      4.4 Basic Certificate Revocation Tests
 *
 * @author ayetgin
 */
    [TestFixture]
    class BasicRevocationTest : BaseNISTTest
    {
        //4.4.1
        [Test]
        public void testMissingCRLTest1()
        {
            test("InvalidMissingCRLTest1.xml", "InvalidMissingCRLTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.2
        [Test]
        public void testInvalidRevokedCATest2()
        {
            test("InvalidRevokedCATest2.xml", "InvalidRevokedCATest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.3
        [Test]
        public void testInvalidRevokedTest3()
        {                            // todo
            test("ValidCertificatePathTest1.xml", "InvalidRevokedEETest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.4
        [Test]
        public void testInvalidBadCRLSignatureTest4()
        {
            test("InvalidBadCRLSignatureTest4.xml", "InvalidBadCRLSignatureTest4EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.5
        [Test]
        public void testInvalidBadCRLIssuerNameTest5()
        {
            test("InvalidBadCRLIssuerNameTest5.xml", "InvalidBadCRLIssuerNameTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.6
        [Test]
        public void testInvalidWrongCRLTest6()
        {
            test("InvalidWrongCRLTest6.xml", "InvalidWrongCRLTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.7
        [Test]
        public void testValidTwoCRLsTest7()
        {
            test("ValidTwoCRLsTest7.xml", "ValidTwoCRLsTest7EE.crt", CertificateStatus.VALID);
        }

        //4.4.8
        [Test]
        public void testInvalidUnknownCRLEntryExtensionTest8()
        {
            test("InvalidUnknownCRLEntryExtensionTest8.xml", "InvalidUnknownCRLEntryExtensionTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.9
        [Test]
        public void testInvalidUnknownCRLExtensionTest9()
        {
            test("InvalidUnknownCRLExtensionTest9.xml", "InvalidUnknownCRLExtensionTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.10
        [Test]
        public void testInvalidUnknownCRLExtensionTest10()
        {
            test("InvalidUnknownCRLExtensionTest9.xml", "InvalidUnknownCRLExtensionTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.11
        [Test]
        public void testInvalidOldCRLNextUpdateTest11()
        {
            test("InvalidOldCRLnextUpdateTest11.xml", "InvalidOldCRLnextUpdateTest11EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.12
        [Test]
        public void testInvalidpre2000CRLNextUpdateTest12()
        {
            test("Invalidpre2000CRLnextUpdateTest12.xml", "Invalidpre2000CRLnextUpdateTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.13
        [Test]
        public void testValidGeneralizedTimeCRLNextUpdateTest13()
        {
            test("testValidGeneralizedTimeCRLNextUpdateTest13.xml", "ValidGeneralizedTimeCRLnextUpdateTest13EE.crt", CertificateStatus.VALID);
        }

        //4.4.14
        [Test]
        public void testValidNegativeSerialNumberTest14()
        {
            test("ValidNegativeSerialNumberTest14.xml", "ValidNegativeSerialNumberTest14EE.crt", CertificateStatus.VALID);
        }

        //4.4.15
        [Test]
        public void testInvalidNegativeSerialNumberTest15()
        {
            test("ValidNegativeSerialNumberTest14.xml", "InvalidNegativeSerialNumberTest15EE.crt", CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        }

        //4.4.16
        [Test]
        public void testValidLongSerialNumberTest16()
        {
            test("ValidLongSerialNumberTest16.xml", "ValidLongSerialNumberTest16EE.crt", CertificateStatus.VALID);
        }

        //4.4.17
        [Test]
        public void testValidLongSerialNumberTest17()
        {
            test("ValidLongSerialNumberTest16.xml", "ValidLongSerialNumberTest17EE.crt", CertificateStatus.VALID);
        }

        //4.4.18
        [Test]
        public void testInvalidLongSerialNumberTest18()
        {
            test("ValidLongSerialNumberTest16.xml", "InvalidLongSerialNumberTest18EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.19
        [Test]
        public void testValidSeparateCertificateAndCRLKeysTest19()
        {
            test("ValidSeparateCertificateAndCRLKeysTest19.xml", "ValidSeparateCertificateandCRLKeysTest19EE.crt", CertificateStatus.VALID);
        }

        //4.4.20
        [Test]
        public void testInvalidSeparateCertificateAndCRLKeysTest20()
        {
            test("ValidSeparateCertificateAndCRLKeysTest19.xml", "InvalidSeparateCertificateandCRLKeysTest20EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        //4.4.21
        [Test]
        public void testInvalidSeparateCertificateAndCRLKeysTest21()
        {
            test("InvalidSeparateCertificateAndCRLKeysTest21.xml", "InvalidSeparateCertificateandCRLKeysTest21EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }
    }
}
