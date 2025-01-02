package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.3 Verifying Name Chaining
 *
 * @author ayetgin
 */
public class NameChainingTest extends BaseNISTTest {

    // 4.3.1
    @Test
    public void testInvalidNameChainingTest1() throws Exception {
        test("ValidCertificatePathTest1.xml", "InvalidNameChainingTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.3.2
    @Test
    public void testInvalidNameChainingOrderTest2() throws Exception {
        test("InvalidNameChainingOrderTest2.xml", "InvalidNameChainingOrderTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.3.3
    @Test
    public void testValidNameChainingWhitespaceTest3() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidNameChainingWhitespaceTest3EE.crt", CertificateStatus.VALID);
    }

    // 4.3.4
    @Test
    public void testValidNameChainingWhitespaceTest4() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidNameChainingWhitespaceTest4EE.crt", CertificateStatus.VALID);
    }

    // 4.3.5
    @Test
    public void testValidNameChainingCapitalizationTest5() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidNameChainingCapitalizationTest5EE.crt", CertificateStatus.VALID);
    }

    // 4.3.6
    @Test
    public void testValidNameChainingUIDsTest6() throws Exception {
        test("ValidNameUIDsTest6.xml", "ValidNameUIDsTest6EE.crt", CertificateStatus.VALID);
    }

    // 4.3.7
    @Test
    public void testValidRFC3280MandatoryAttributeTypesTest7() throws Exception {
        test("ValidRFC3280MandatoryAttributeTypesTest7.xml", "ValidRFC3280MandatoryAttributeTypesTest7EE.crt", CertificateStatus.VALID);
    }

    // 4.3.8
    @Test
    public void testValidRFC3280OptionalAttributeTypesTest8() throws Exception {
        test("ValidRFC3280OptionalAttributeTypesTest8.xml", "ValidRFC3280OptionalAttributeTypesTest8EE.crt", CertificateStatus.VALID);
    }

    // 4.3.9
    @Test
    public void testValidUTF8Ma3CertificateEncodedNamesTest9() throws Exception {
        test("ValidUTF8StringEncodedNamesTest9.xml", "ValidUTF8StringEncodedNamesTest9EE.crt", CertificateStatus.VALID);
    }

    // 4.3.10
    @Test
    public void testValidRolloverFromPrintableMa3CertificateToUTF8Ma3CertificateTest10() throws Exception {
        test("ValidRolloverfromPrintableStringtoUTF8StringTest10.xml", "ValidRolloverfromPrintableStringtoUTF8StringTest10EE.crt", CertificateStatus.VALID);
    }

    // 4.3.11
    @Test
    public void testValidUTF8Ma3CertificateCaseInsensitiveMatchTest11() throws Exception {
        test("ValidUTF8Ma3CertificateCaseInsensitiveMatchTest11.xml", "ValidUTF8StringCaseInsensitiveMatchTest11EE.crt", CertificateStatus.VALID);
    }


}
