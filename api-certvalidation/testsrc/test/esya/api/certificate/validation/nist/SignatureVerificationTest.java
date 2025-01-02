package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.1 Signature validation
 *
 * @author ayetgin
 */
public class SignatureVerificationTest extends BaseNISTTest {

    //4.1.1
    @Test
    public void testValidSignaturesTest1() throws Exception {
        test("ValidCertificatePathTest1.xml", "ValidCertificatePathTest1EE.crt", CertificateStatus.VALID);
    }

    //4.1.2
    @Test
    public void testInvalidCASignatureTest2() throws Exception {
        test("InvalidCASignatureTest2.xml", "InvalidCASignatureTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.1.3
    @Test
    public void testInvalidSignatureTest3() throws Exception {
        test("ValidCertificatePathTest1.xml", "InvalidEESignatureTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.1.4
    @Test
    public void testValidDSASignatureTest4() throws Exception {
        test("ValidDSASignaturesTest4.xml", "ValidDSASignaturesTest4EE.crt", CertificateStatus.VALID);
    }

    //4.1.5
    @Test
    public void testValidDSAParameterInheritanceTest5() throws Exception {
        test("ValidDSAParameterInheritanceTest5.xml", "ValidDSAParameterInheritanceTest5EE.crt", CertificateStatus.VALID);
    }

    //4.1.6
    @Test
    public void testInvalidDSASignatureTest6() throws Exception {
        test("ValidDSASignaturesTest4.xml", "InvalidDSASignatureTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}
