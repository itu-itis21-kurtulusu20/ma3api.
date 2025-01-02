package dev.esya.api.xmlsignature.legacy.plugtests2010;


/**
 * @author ayetgin
 */
public class FEDTest extends PT2010BaseTest
{
    protected final static String BASE_FED_BES = BASELOC +"XAdES-BES.SCOK\\FED\\";
    protected final static String BASE_FED_EPES = BASELOC +"XAdES-EPES.SCOK\\FED\\";
    protected final static String BASE_FED_T = BASELOC +"XAdES-T.SCOK\\FED\\";
    protected final static String BASE_FED_C = BASELOC +"XAdES-C.SCOK\\FED\\";
    protected final static String BASE_FED_X = BASELOC +"XAdES-X.SCOK\\FED\\";
    protected final static String BASE_FED_XL = BASELOC +"XAdES-XL.SCOK\\FED\\";
    protected final static String BASE_FED_A = BASELOC +"XAdES-A.SCOK\\FED\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_FED_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_FED_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_FED_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_FED_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_FED_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_FED_BES, null, true);
    }
    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_FED_BES, null, true);
    }

    public void test9() throws Exception {
        validate("Signature-X-BES-9.xml", BASE_FED_BES, null, true);
    }
    public void test10() throws Exception {
        validate("Signature-X-BES-10.xml", BASE_FED_BES, null, true);
    }

    // epes  todo signingtime belli değil
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_FED_EPES, null, false);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_FED_T, null, true);
    }

    // todo timestamp crl time
    public void testT141_1() throws Exception {
        validate("Signature-X141-T-1.xml", BASE_FED_T, null, CERT_VAL_POLICY_CRL, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_FED_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_FED_C, null, true);
    }

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_FED_C, null, true);
    } */

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_FED_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_FED_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_FED_X, null, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_FED_X, null, true);
    }

    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_FED_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_FED_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_FED_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_FED_XL, null, true);
    }

    // a
    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_FED_A, null, null, true);
    }

    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_FED_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_FED_A, null, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_FED_A, null, null, true);
    }

    public void testA5() throws Exception {
        validate("Signature-X-A-5.xml", BASE_FED_A, null, null, true);
    }

    public void testA6() throws Exception {
        validate("Signature-X-A-6.xml", BASE_FED_A, null, null, true);
    }
}
