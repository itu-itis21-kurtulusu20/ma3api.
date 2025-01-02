package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class SSCTest extends PT2010BaseTest
{
    protected final static String BASE_SSC_BES = BASELOC +"XAdES-BES.SCOK\\SSC\\";
    protected final static String BASE_SSC_EPES = BASELOC +"XAdES-EPES.SCOK\\SSC\\";
    protected final static String BASE_SSC_T = BASELOC +"XAdES-T.SCOK\\SSC\\";
    protected final static String BASE_SSC_C = BASELOC +"XAdES-C.SCOK\\SSC\\";
    protected final static String BASE_SSC_XL = BASELOC +"XAdES-XL.SCOK\\SSC\\";
    protected final static String BASE_SSC_A = BASELOC +"XAdES-A.SCOK\\SSC\\";

    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_SSC_BES, null, true);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_SSC_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_SSC_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_SSC_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_SSC_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_SSC_BES, null, true);
    }
    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_SSC_BES, null, true);
    }

    public void test11() throws Exception {
        validate("Signature-X-BES-11.xml", BASE_SSC_BES, null, true);
    }

    // epes
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_SSC_EPES, OFFLINE_RESOLVER, true);
    }

    
    public void testEPES2() throws Exception {
        validate("Signature-X-EPES-2.xml", BASE_SSC_EPES, OFFLINE_RESOLVER, true);
    } 

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_SSC_T, null, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_SSC_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_SSC_C, null, true);
    }

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_SSC_C, null, true);
    } */


    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_SSC_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_SSC_XL, null, true);
    }

    // A

    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_SSC_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_SSC_A, null, null, true);
    }

    public void testA5() throws Exception {
        validate("Signature-X-A-5.xml", BASE_SSC_A, null, null, true);
    }


    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_SSC_A, null, null, true);
    }

    public void testA8() throws Exception {
        validate("Signature-X-A-8.xml", BASE_SSC_A, null, null, true);
    }

    public void testA9() throws Exception {
        validate("Signature-X-A-9.xml", BASE_SSC_A, null, null, true);
    }
}
