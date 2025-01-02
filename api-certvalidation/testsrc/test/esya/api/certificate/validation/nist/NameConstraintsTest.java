package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.13 Name Constraints
 *
 * @author ayetgin
 */
public class NameConstraintsTest extends BaseNISTTest {

    // 4.13.1
    @Test
    public void testValidDNnameConstraintsTest1() throws Exception {
        test("ValidDNnameConstraintsTest1.xml", "ValidDNnameConstraintsTest1EE.crt", CertificateStatus.VALID);
    }

    // 4.13.2
    @Test
    public void testInvalidDNnameConstraintsTest2() throws Exception {
        test("InvalidDNnameConstraintsTest2.xml", "InvalidDNnameConstraintsTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.3
    @Test
    public void testInvalidDNnameConstraintsTest3() throws Exception {
        test("InvalidDNnameConstraintsTest3.xml", "InvalidDNnameConstraintsTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.4
    @Test
    public void testValidDNnameConstraintsTest4() throws Exception {
        test("ValidDNnameConstraintsTest4.xml", "ValidDNnameConstraintsTest4EE.crt", CertificateStatus.VALID);
    }

    // 4.13.5
    @Test
    public void testValidDNnameConstraintsTest5() throws Exception {
        test("ValidDNnameConstraintsTest5.xml", "ValidDNnameConstraintsTest5EE.crt", CertificateStatus.VALID);
    }

    // 4.13.6
    @Test
    public void testValidDNnameConstraintsTest6() throws Exception {
        test("ValidDNnameConstraintsTest6.xml", "ValidDNnameConstraintsTest6EE.crt", CertificateStatus.VALID);
    }

    // 4.13.7
    @Test
    public void testInvalidDNnameConstraintsTest7() throws Exception {
        test("InvalidDNnameConstraintsTest7.xml", "InvalidDNnameConstraintsTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.8
    @Test
    public void testInvalidDNnameConstraintsTest8() throws Exception {
        test("InvalidDNnameConstraintsTest8.xml", "InvalidDNnameConstraintsTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.9
    @Test
    public void testInvalidDNnameConstraintsTest9() throws Exception {
        test("InvalidDNnameConstraintsTest9.xml", "InvalidDNnameConstraintsTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.10
    @Test
    public void testInvalidDNnameConstraintsTest10() throws Exception {
        test("InvalidDNnameConstraintsTest10.xml", "InvalidDNnameConstraintsTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.11
    @Test
    public void testValidDNnameConstraintsTest11() throws Exception {
        test("ValidDNnameConstraintsTest11.xml", "ValidDNnameConstraintsTest11EE.crt", CertificateStatus.VALID);
    }

    // 4.13.12
    @Test
    public void testInvalidDNnameConstraintsTest12() throws Exception {
        test("InvalidDNnameConstraintsTest12.xml", "InvalidDNnameConstraintsTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.13
    @Test
    public void testInvalidDNnameConstraintsTest13() throws Exception {
        test("InvalidDNnameConstraintsTest13.xml", "InvalidDNnameConstraintsTest13EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.14
    @Test
    public void testValidDNnameConstraintsTest14() throws Exception {
        test("ValidDNnameConstraintsTest14.xml", "ValidDNnameConstraintsTest14EE.crt", CertificateStatus.VALID);
    }

    // 4.13.15
    @Test
    public void testInvalidDNnameConstraintsTest15() throws Exception {
        test("InvalidDNnameConstraintsTest15.xml", "InvalidDNnameConstraintsTest15EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.16
    @Test
    public void testInvalidDNnameConstraintsTest16() throws Exception {
        test("InvalidDNnameConstraintsTest16.xml", "InvalidDNnameConstraintsTest16EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.17
    @Test
    public void testInvalidDNnameConstraintsTest17() throws Exception {
        test("InvalidDNnameConstraintsTest17.xml", "InvalidDNnameConstraintsTest17EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.18
    @Test
    public void testValidDNnameConstraintsTest18() throws Exception {
        test("ValidDNnameConstraintsTest18.xml", "ValidDNnameConstraintsTest18EE.crt", CertificateStatus.VALID);
    }

    // 4.13.19
    @Test
    public void testValidSelfIssuedDNnameConstraintsTest19() throws Exception {
        test("ValidSelfIssuedDNnameConstraintsTest19.xml", "ValidDNnameConstraintsTest19EE.crt", CertificateStatus.VALID);
    }

    // 4.13.20
    @Test
    public void testInvalidSelfIssuedDNnameConstraintsTest20() throws Exception {
        test("InvalidSelfIssuedDNnameConstraintsTest20.xml", "InvalidDNnameConstraintsTest20EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.21
    @Test
    public void testValidRFC822nameConstraintsTest21() throws Exception {
        test("ValidRFC822nameConstraintsTest21.xml", "ValidRFC822nameConstraintsTest21EE.crt", CertificateStatus.VALID);
    }

    // 4.13.22
    @Test
    public void testInvalidRFC822nameConstraintsTest22() throws Exception {
        test("InvalidRFC822nameConstraintsTest22.xml", "InvalidRFC822nameConstraintsTest22EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.23
    @Test
    public void testValidRFC822nameConstraintsTest23() throws Exception {
        test("ValidRFC822nameConstraintsTest23.xml", "ValidRFC822nameConstraintsTest23EE.crt", CertificateStatus.VALID);
    }

    // 4.13.24
    @Test
    public void testInvalidRFC822nameConstraintsTest24() throws Exception {
        test("InvalidRFC822nameConstraintsTest24.xml", "InvalidRFC822nameConstraintsTest24EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.25
    @Test
    public void testValidRFC822nameConstraintsTest25() throws Exception {
        test("ValidRFC822nameConstraintsTest25.xml", "ValidRFC822nameConstraintsTest25EE.crt", CertificateStatus.VALID);
    }

    // 4.13.26
    @Test
    public void testInvalidRFC822nameConstraintsTest26() throws Exception {
        test("InvalidRFC822nameConstraintsTest26.xml", "InvalidRFC822nameConstraintsTest26EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.27
    @Test
    public void testValidDNandRFC822nameConstraintsTest27() throws Exception {
        test("ValidDNandRFC822nameConstraintsTest27.xml", "ValidDNandRFC822nameConstraintsTest27EE.crt", CertificateStatus.VALID);
    }

    // 4.13.28
    @Test
    public void testInvalidDNandRFC822nameConstraintsTest28() throws Exception {
        test("InvalidDNandRFC822nameConstraintsTest28.xml", "InvalidDNandRFC822nameConstraintsTest28EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.29
    @Test
    public void testInvalidDNandRFC822nameConstraintsTest29() throws Exception {
        test("InvalidDNandRFC822nameConstraintsTest29.xml", "InvalidDNandRFC822nameConstraintsTest29EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.30
    @Test
    public void testValidDNSnameConstraintsTest30() throws Exception {
        test("ValidDNSnameConstraintsTest30.xml", "ValidDNSnameConstraintsTest30EE.crt", CertificateStatus.VALID);
    }

    // 4.13.31
    @Test
    public void testInvalidDNSnameConstraintsTest31() throws Exception {
        test("InvalidDNSnameConstraintsTest31.xml", "InvalidDNSnameConstraintsTest31EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.32
    @Test
    public void testValidDNSnameConstraintsTest32() throws Exception {
        test("ValidDNSnameConstraintsTest32.xml", "ValidDNSnameConstraintsTest32EE.crt", CertificateStatus.VALID);
    }

    // 4.13.33
    @Test
    public void testInvalidDNSnameConstraintsTest33() throws Exception {
        test("InvalidDNSnameConstraintsTest33.xml", "InvalidDNSnameConstraintsTest33EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.34
    @Test
    public void testValidURInameConstraintsTest34() throws Exception {
        test("ValidURInameConstraintsTest34.xml", "ValidURInameConstraintsTest34EE.crt", CertificateStatus.VALID);
    }

    // 4.13.35
    @Test
    public void testInvalidURInameConstraintsTest35() throws Exception {
        test("InvalidURInameConstraintsTest35.xml", "InvalidURInameConstraintsTest35EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.36
    @Test
    public void testValidURInameConstraintsTest36() throws Exception {
        test("ValidURInameConstraintsTest36.xml", "ValidURInameConstraintsTest36EE.crt", CertificateStatus.VALID);
    }

    // 4.13.37
    @Test
    public void testInvalidURInameConstraintsTest37() throws Exception {
        test("InvalidURInameConstraintsTest37.xml", "InvalidURInameConstraintsTest37EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.13.38
    @Test
    public void testInvalidDNSnameConstraintsTest38() throws Exception {
        test("InvalidDNSnameConstraintsTest38.xml", "InvalidDNSnameConstraintsTest38EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}
