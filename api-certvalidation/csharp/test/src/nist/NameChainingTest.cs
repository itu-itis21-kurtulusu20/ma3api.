using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;

/**
 *      4.3 Verifying Name Chaining
 *
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.nist
{
    [TestFixture]
    public class NameChainingTest : BaseNISTTest
    {
        // 4.3.1
        [Test]
        public void testInvalidNameChainingTest1()
        {
            test("ValidCertificatePathTest1.xml", "InvalidNameChainingTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.3.2
        [Test]
        public void testInvalidNameChainingOrderTest2()
        {
            test("InvalidNameChainingOrderTest2.xml", "InvalidNameChainingOrderTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
        }

        // 4.3.3
        [Test]
        public void testValidNameChainingWhitespaceTest3()
        {
            test("ValidCertificatePathTest1.xml", "ValidNameChainingWhitespaceTest3EE.crt", CertificateStatus.VALID);
        }

        // 4.3.4
        [Test]
        public void testValidNameChainingWhitespaceTest4()
        {
            test("ValidCertificatePathTest1.xml", "ValidNameChainingWhitespaceTest4EE.crt", CertificateStatus.VALID);
        }

        // 4.3.5
        [Test]
        public void testValidNameChainingCapitalizationTest5()
        {
            test("ValidCertificatePathTest1.xml", "ValidNameChainingCapitalizationTest5EE.crt", CertificateStatus.VALID);
        }

        // 4.3.6
        [Test]
        public void testValidNameChainingUIDsTest6()
        {
            test("ValidNameUIDsTest6.xml", "ValidNameUIDsTest6EE.crt", CertificateStatus.VALID);
        }

        // 4.3.7
        [Test]
        public void testValidRFC3280MandatoryAttributeTypesTest7()
        {
            test("ValidRFC3280MandatoryAttributeTypesTest7.xml", "ValidRFC3280MandatoryAttributeTypesTest7EE.crt", CertificateStatus.VALID);
        }

        // 4.3.8
        [Test]
        public void testValidRFC3280OptionalAttributeTypesTest8()
        {
            test("ValidRFC3280OptionalAttributeTypesTest8.xml", "ValidRFC3280OptionalAttributeTypesTest8EE.crt", CertificateStatus.VALID);
        }

        // 4.3.9
        [Test]
        public void testValidUTF8Ma3CertificateEncodedNamesTest9()
        {
            test("ValidUTF8StringEncodedNamesTest9.xml", "ValidUTF8StringEncodedNamesTest9EE.crt", CertificateStatus.VALID);
        }

        // 4.3.10
        [Test]
        public void testValidRolloverFromPrintableMa3CertificateToUTF8Ma3CertificateTest10()
        {
            test("ValidRolloverfromPrintableStringtoUTF8StringTest10.xml", "ValidRolloverfromPrintableStringtoUTF8StringTest10EE.crt", CertificateStatus.VALID);
        }

        // 4.3.11
        [Test]
        public void testValidUTF8Ma3CertificateCaseInsensitiveMatchTest11()
        {
            test("ValidUTF8Ma3CertificateCaseInsensitiveMatchTest11.xml", "ValidUTF8StringCaseInsensitiveMatchTest11EE.crt", CertificateStatus.VALID);
        }

    }
}
