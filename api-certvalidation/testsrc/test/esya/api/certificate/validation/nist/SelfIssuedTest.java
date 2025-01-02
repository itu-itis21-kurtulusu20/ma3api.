package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.5 Verifying Paths with Self-Issued Certificates
 *
 * @author ayetgin
 */
public class SelfIssuedTest extends BaseNISTTest {

    @Test
    public void testValidBasicSelfIssuedOldWithNewTest1() throws Exception {
        test("ValidBasicSelfIssuedOldWithNewTest1.xml", "ValidBasicSelfIssuedOldWithNewTest1EE.crt", CertificateStatus.VALID);
    }

    @Test
    public void testInvalidBasicSelfIssuedOldWithNewTest2() throws Exception {
        test("ValidBasicSelfIssuedOldWithNewTest1.xml", "InvalidBasicSelfIssuedOldWithNewTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    @Test
    public void testValidBasicSelfIssuedNewWithOldTest3() throws Exception {
        test("ValidBasicSelfIssuedNewWithOldTest3.xml", "ValidBasicSelfIssuedNewWithOldTest3EE.crt", CertificateStatus.VALID);
    }

    @Test
    public void testValidBasicSelfIssuedNewWithOldTest4() throws Exception {
        test("ValidBasicSelfIssuedNewWithOldTest3.xml", "ValidBasicSelfIssuedNewWithOldTest4EE.crt", CertificateStatus.VALID);
    }

    @Test
    public void testInvalidBasicSelfIssuedNewWithOldTest5() throws Exception {
        test("ValidBasicSelfIssuedNewWithOldTest3.xml", "InvalidBasicSelfIssuedNewWithOldTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    @Test
    public void testValidBasicSelfIssuedCRLSigningKeyTest6() throws Exception {
        test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "ValidBasicSelfIssuedCRLSigningKeyTest6EE.crt", CertificateStatus.VALID);
    }

    @Test
    public void testInvalidBasicSelfIssuedCRLSigningKeyTest7() throws Exception {
        test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "InvalidBasicSelfIssuedCRLSigningKeyTest7EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    @Test
    public void testInvalidBasicSelfIssuedCRLSigningKeyTest8() throws Exception {
        test("ValidBasicSelfIssuedCRLSigningKeyTest6.xml", "InvalidBasicSelfIssuedCRLSigningKeyTest8EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }


}
