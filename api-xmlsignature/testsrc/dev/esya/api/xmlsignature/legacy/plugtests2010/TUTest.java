package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class TUTest extends PT2010BaseTest
{
    protected final static String BASE_TU_BES   = BASELOC +"XAdES-BES.SCOK\\TU\\";
    protected final static String BASE_TU_EPES  = BASELOC +"XAdES-EPES.SCOK\\TU\\";
    protected final static String BASE_TU_T     = BASELOC +"XAdES-T.SCOK\\TU\\";
    protected final static String BASE_TU_C     = BASELOC +"XAdES-C.SCOK\\TU\\";
    protected final static String BASE_TU_X     = BASELOC +"XAdES-X.SCOK\\TU\\";
    protected final static String BASE_TU_XL    = BASELOC +"XAdES-XL.SCOK\\TU\\";

    // todo signingtime belli değil    
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_TU_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_TU_BES, null, CERT_VAL_POLICY_OCSP, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_TU_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_TU_BES, null, true);
    }

    public void test5() throws Exception {
        validate("Signature-X-BES-5.xml", BASE_TU_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_TU_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_TU_BES, null, true);
    }
    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_TU_BES, null, true);
    }

    public void test9() throws Exception {
        validate("Signature-X-BES-9.xml", BASE_TU_BES, null, true);
    }

    public void test10() throws Exception {
        validate("Signature-X-BES-10.xml", BASE_TU_BES, null, true);
    }

    public void test11() throws Exception {
        validate("Signature-X-BES-11.xml", BASE_TU_BES, null, true);
    }
    
    public void test15() throws Exception {
        validate("Signature-X-BES-15.xml", BASE_TU_BES, null, true);
    }
    
    public void testX141_1() throws Exception {
        validate("Signature-X141-BES-1.xml", BASE_TU_BES, null, true);
    }
    
    public void testX141_2() throws Exception {
        validate("Signature-X141-BES-2.xml", BASE_TU_BES, null, true);
    }
    
    // epes
    // todo signingtime belli değil
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_TU_EPES, OFFLINE_RESOLVER, false);
    }

    public void testEPES2() throws Exception {
        validate("Signature-X-EPES-2.xml", BASE_TU_EPES, OFFLINE_RESOLVER, true);
    } 

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_TU_T, null, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_TU_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_TU_C, null, true);
    }

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_TU_C, null, true);
    } */

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_TU_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_TU_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_TU_X, null, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_TU_X, null, true);
    }

    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_TU_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_TU_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_TU_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_TU_XL, null, true);
    }

}
