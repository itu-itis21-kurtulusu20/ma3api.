package test.esya.api.certificate.validation.nist;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;

/**
 * 4.7 Key Usage
 *
 * @author ayetgin
 */
public class KeyUsageTest extends BaseNISTTest {

    //4.7.1
    @Test
    public void testInvalidKeyUsageCriticalKeyCertSignFalseTest1() throws Exception {
        test("InvalidkeyUsageCriticalkeyCertSignFalseTest1.xml", "InvalidkeyUsageCriticalkeyCertSignFalseTest1EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.7.2
    @Test
    public void testInvalidKeyUsageNotCriticalKeyCertSignFalseTest2() throws Exception {
        test("InvalidkeyUsageNotCriticalkeyCertSignFalseTest2.xml", "InvalidkeyUsageNotCriticalkeyCertSignFalseTest2EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.7.3
    @Test
    public void testValidKeyUsageNotCriticalTest3() throws Exception {
        test("ValidkeyUsageNotCriticalTest3.xml", "ValidkeyUsageNotCriticalTest3EE.crt", CertificateStatus.VALID);
    }

    //4.7.4
    @Test
    public void testInvalidKeyUsageCriticalCRLSignFalseTest4() throws Exception {
        test("InvalidkeyUsageCriticalcRLSignFalseTest4.xml", "InvalidkeyUsageCriticalcRLSignFalseTest4EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

    //4.7.5
    @Test
    public void testInvalidKeyUsageNotCriticalCRLSignFalseTest5() throws Exception {
        test("InvalidkeyUsageNotCriticalcRLSignFalseTest5.xml", "InvalidkeyUsageNotCriticalcRLSignFalseTest5EE.crt", CertificateStatus.PATH_VALIDATION_FAILURE);
    }

}
