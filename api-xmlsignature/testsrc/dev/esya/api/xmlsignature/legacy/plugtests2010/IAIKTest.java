package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class IAIKTest extends PT2010BaseTest
{

    protected final static String BASE_IAIK_C = BASELOC +"XAdES-C.SCOK\\IAIK\\";
    protected final static String BASE_IAIK_X = BASELOC +"XAdES-X.SCOK\\IAIK\\";
    protected final static String BASE_IAIK_XL = BASELOC +"XAdES-XL.SCOK\\IAIK\\";
    protected final static String BASE_IAIK_A = BASELOC +"XAdES-A.SCOK\\IAIK\\";

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_IAIK_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_IAIK_C, null, true);
    }

    public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_IAIK_C, null, true);
    }

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_IAIK_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_IAIK_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_IAIK_X, null, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_IAIK_X, null, true);
    }

    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_IAIK_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_IAIK_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_IAIK_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_IAIK_XL, null, true);
    }

    // a
    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA5() throws Exception {
        validate("Signature-X-A-5.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA6() throws Exception {
        validate("Signature-X-A-6.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA8() throws Exception {
        validate("Signature-X-A-8.xml", BASE_IAIK_A, null, null, true);
    }

    public void testA9() throws Exception {
        validate("Signature-X-A-9.xml", BASE_IAIK_A, null, null, true);
    }
}
