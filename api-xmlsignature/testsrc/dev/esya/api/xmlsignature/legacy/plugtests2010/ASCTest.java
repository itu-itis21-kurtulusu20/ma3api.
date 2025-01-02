package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class ASCTest extends PT2010BaseTest
{
    protected final static String BASE_ASC_BES = BASELOC +"XAdES-BES.SCOK\\ASC\\";
    protected final static String BASE_ASC_EPES = BASELOC +"XAdES-EPES.SCOK\\ASC\\";
    protected final static String BASE_ASC_T = BASELOC +"XAdES-T.SCOK\\ASC\\";
    protected final static String BASE_ASC_C = BASELOC +"XAdES-C.SCOK\\ASC\\";
    protected final static String BASE_ASC_X = BASELOC +"XAdES-X.SCOK\\ASC\\";
    protected final static String BASE_ASC_XL = BASELOC +"XAdES-XL.SCOK\\ASC\\";
    protected final static String BASE_ASC_A = BASELOC +"XAdES-A.SCOK\\ASC\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_ASC_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_ASC_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_ASC_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_ASC_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_ASC_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_ASC_BES, null, true);
    }
    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_ASC_BES, null, true);
    }

    public void test9() throws Exception {
        validate("Signature-X-BES-9.xml", BASE_ASC_BES, null, true);
    }

    public void test10() throws Exception {
        validate("Signature-X-BES-10.xml", BASE_ASC_BES, null, true);
    }

    public void test11() throws Exception {
        validate("Signature-X-BES-11.xml", BASE_ASC_BES, null, true);
    }
    
    public void test15() throws Exception {
        validate("Signature-X-BES-15.xml", BASE_ASC_BES, null, true);
    }

    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_ASC_EPES, OFFLINE_RESOLVER, false);
    }

    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_ASC_T, null, true);
    }

    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_ASC_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_ASC_C, null, true);
    }

    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_ASC_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_ASC_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_ASC_X, null, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_ASC_X, null, true);
    }

    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_ASC_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_ASC_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_ASC_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_ASC_XL, null, true);
    }


    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_ASC_A, null, null, true);
    }

    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_ASC_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_ASC_A, null, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_ASC_A, null, null, true);
    }


    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_ASC_A, null, null, true);
    }

    public void testA9() throws Exception {
        validate("Signature-X-A-9.xml", BASE_ASC_A, null, null, true);
    }

}
