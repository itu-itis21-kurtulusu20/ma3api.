package dev.esya.api.xmlsignature.legacy.plugtests2010;


/**
 * @author ayetgin
 */
public class IBMTest extends PT2010BaseTest
{
    protected final static String BASE_IBM_BES = BASELOC +"XAdES-BES.SCOK\\IBM\\";
    protected final static String BASE_IBM_EPES = BASELOC +"XAdES-EPES.SCOK\\IBM\\";
    protected final static String BASE_IBM_T = BASELOC +"XAdES-T.SCOK\\IBM\\";
    protected final static String BASE_IBM_C = BASELOC +"XAdES-C.SCOK\\IBM\\";
    protected final static String BASE_IBM_X = BASELOC +"XAdES-X.SCOK\\IBM\\";
    protected final static String BASE_IBM_XL = BASELOC +"XAdES-XL.SCOK\\IBM\\";
    protected final static String BASE_IBM_A = BASELOC +"XAdES-A.SCOK\\IBM\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_IBM_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_IBM_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_IBM_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_IBM_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_IBM_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_IBM_BES, null, true);
    }

    /* timestamp digest error
    public void test9() throws Exception {
        validate("Signature-X-BES-9.xml", BASE_IBM_BES, null, true);
    }

    public void test10() throws Exception {
        validate("Signature-X-BES-10.xml", BASE_IBM_BES, null, true);
    }
    */

    /*  policy digest error
    // epes
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_IBM_EPES, OFFLINE_RESOLVER, true);
    }   */

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_IBM_T, null, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_IBM_C, null, CERT_VAL_POLICY_CRL, true);
    }

    // todo name tersten encode edilmiş?
    /*
    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_IBM_C, null, true);
    } */

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_IBM_C, null, true);
    } */

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_IBM_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_IBM_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_IBM_X, null, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_IBM_X, null, true);
    }

    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_IBM_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_IBM_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_IBM_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_IBM_XL, null, true);
    }

    // below are problematic
    /*
    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_IBM_A, null, null, true);
    }

    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_IBM_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_IBM_A, null, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_IBM_A, null, null, true);
    } */

}
