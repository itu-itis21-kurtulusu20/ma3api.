package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.15 Delta-CRLs
 *
 * @author ayetgin
 */
public class DeltaCRLTest extends BaseNISTTest {

    // 4.15.1
    @Test
    public void testInvaliddeltaCRLIndicatorNoBaseTest1() throws Exception {
        test("deltaCRLIndicatorNoBaseTest1EE.xml", "InvaliddeltaCRLIndicatorNoBaseTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.15.2
    @Test
    public void testValiddeltaCRLTest2() throws Exception {
        test("validdeltaCRLTest2EE.xml", "ValiddeltaCRLTest2EE.crt", CertificateStatus.VALID);
    }

    // 4.15.3
    @Test
    public void testInvaliddeltaCRLTest3() throws Exception {
        test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest3EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.15.4
    @Test
    public void testInvaliddeltaCRLTest4() throws Exception {
        test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest4EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.15.6
    //The end entity certificate is listed as on hold on the
    //complete CRL and the delta-CRL indicates that it has been revoked.
    @Test
    public void testInvaliddeltaCRLTest6() throws Exception {
        test("validdeltaCRLTest2EE.xml", "InvaliddeltaCRLTest6EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.15.8
    @Test
    public void testValiddeltaCRLTest8() throws Exception {
        test("ValiddeltaCRLTest8.xml", "ValiddeltaCRLTest8EE.crt", CertificateStatus.VALID);
    }

    // 4.15.9
    @Test
    public void testInvaliddeltaCRLTest9() throws Exception {
        test("ValiddeltaCRLTest8.xml", "InvaliddeltaCRLTest9EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    // 4.15.10
    @Test
    public void testInvaliddeltaCRLTest10() throws Exception {
        test("InvaliddeltaCRLTest10.xml", "InvaliddeltaCRLTest10EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}