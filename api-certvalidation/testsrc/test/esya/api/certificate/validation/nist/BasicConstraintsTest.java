package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.6 Verifying Basic Constraints
 *
 * @author ayetgin
 */
public class BasicConstraintsTest extends BaseNISTTest {

    //4.6.1
    @Test
    public void testInvalidMissingBasicConstraintsTest1() throws Exception {
        test("InvalidMissingbasicConstraintsTest1.xml", "InvalidMissingbasicConstraintsTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.2
    @Test
    public void testInvalidCAFalseTest2() throws Exception {
        test("InvalidcAFalseTest2.xml", "InvalidcAFalseTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.3
    @Test
    public void testInvalidCAFalseTest3() throws Exception {
        test("InvalidcAFalseTest3.xml", "InvalidcAFalseTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.4
    @Test
    public void testValidBasicConstraintsNotCriticalTest4() throws Exception {
        test("ValidbasicConstraintsNotCriticalTest4.xml", "ValidbasicConstraintsNotCriticalTest4EE.crt", CertificateStatus.VALID);
    }

    //4.6.5
    @Test
    public void testInvalidPathLenConstraintTest5() throws Exception {
        test("InvalidpathLenConstraintTest5.xml", "InvalidpathLenConstraintTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.6
    @Test
    public void testInvalidPathLenConstraintTest6() throws Exception {
        test("InvalidpathLenConstraintTest5.xml", "InvalidpathLenConstraintTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.7
    @Test
    public void testValidPathLenConstraintTest7() throws Exception {
        test("ValidpathLenConstraintTest7.xml", "ValidpathLenConstraintTest7EE.crt", CertificateStatus.VALID);
    }

    //4.6.8
    @Test
    public void testValidPathLenConstraintTest8() throws Exception {
        test("ValidpathLenConstraintTest7.xml", "ValidpathLenConstraintTest8EE.crt", CertificateStatus.VALID);
    }

    //4.6.9
    @Test
    public void testInvalidPathLenConstraintTest9() throws Exception {
        test("InvalidpathLenConstraintTest9.xml", "InvalidpathLenConstraintTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.10
    @Test
    public void testInvalidPathLenConstraintTest10() throws Exception {
        test("InvalidpathLenConstraintTest9.xml", "InvalidpathLenConstraintTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.11
    @Test
    public void testInvalidPathLenConstraintTest11() throws Exception {
        test("InvalidpathLenConstraintTest11.xml", "InvalidpathLenConstraintTest11EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.12
    @Test
    public void testInvalidPathLenConstraintTest12() throws Exception {
        test("InvalidpathLenConstraintTest11.xml", "InvalidpathLenConstraintTest12EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.13
    @Test
    public void testValidPathLenConstraintTest13() throws Exception {
        test("ValidpathLenConstraintTest13.xml", "ValidpathLenConstraintTest13EE.crt", CertificateStatus.VALID);
    }

    //4.6.14
    @Test
    public void testValidPathLenConstraintTest14() throws Exception {
        test("ValidpathLenConstraintTest13.xml", "ValidpathLenConstraintTest14EE.crt", CertificateStatus.VALID);
    }

    //4.6.15
    @Test
    public void testValidSelfIssuedPathLenConstraintTest15() throws Exception {
        test("ValidpathLenConstraintTest7.xml", "ValidSelfIssuedpathLenConstraintTest15EE.crt", CertificateStatus.VALID);
    }

    //4.6.16
    @Test
    public void testInvalidSelfIssuedPathLenConstraintTest16() throws Exception {
        test("InvalidSelfIssuedpathLenConstraintTest16.xml", "InvalidSelfIssuedpathLenConstraintTest16EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.6.17
    @Test
    public void testValidSelfIssuedPathLenConstraintTest17() throws Exception {
        test("ValidSelfIssuedpathLenConstraintTest17.xml", "ValidSelfIssuedpathLenConstraintTest17EE.crt", CertificateStatus.VALID);
    }

}
