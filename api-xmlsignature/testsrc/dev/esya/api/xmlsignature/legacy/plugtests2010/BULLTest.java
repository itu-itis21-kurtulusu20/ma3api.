package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class BULLTest extends PT2010BaseTest
{
    protected final static String BASE_BULL_BES = BASELOC +"XAdES-BES.SCOK\\BULL\\";
    protected final static String BASE_BULL_EPES = BASELOC +"XAdES-EPES.SCOK\\BULL\\";
    protected final static String BASE_BULL_T = BASELOC +"XAdES-T.SCOK\\BULL\\";
    protected final static String BASE_BULL_C = BASELOC +"XAdES-C.SCOK\\BULL\\";
    protected final static String BASE_BULL_X = BASELOC +"XAdES-X.SCOK\\BULL\\";
    protected final static String BASE_BULL_XL = BASELOC +"XAdES-XL.SCOK\\BULL\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_BULL_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_BULL_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_BULL_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_BULL_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_BULL_BES, null, true);
    }

    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_BULL_BES, null, true);
    }

    // todo below cant resolve policy!
    /*
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_BULL_EPES, null, true);
    }

    public void testEPES2() throws Exception {
        validate("Signature-X-EPES-2.xml", BASE_BULL_EPES, OFFLINE_RESOLVER, true);
    }

    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_BULL_T, null, true);
    }

    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_BULL_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_BULL_C, null, true);
    }

    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_BULL_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_BULL_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_BULL_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_BULL_XL, null, true);
    }*/
}
