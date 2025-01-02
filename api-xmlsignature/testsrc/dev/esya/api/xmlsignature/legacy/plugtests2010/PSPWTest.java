package dev.esya.api.xmlsignature.legacy.plugtests2010;


/**
 * @author ayetgin
 */
public class PSPWTest extends PT2010BaseTest
{
    protected final static String BASE_PSPW_BES = BASELOC +"XAdES-BES.SCOK\\PSPW\\";
    protected final static String BASE_PSPW_EPES = BASELOC +"XAdES-EPES.SCOK\\PSPW\\";
    protected final static String BASE_PSPW_T = BASELOC +"XAdES-T.SCOK\\PSPW\\";
    protected final static String BASE_PSPW_C = BASELOC +"XAdES-C.SCOK\\PSPW\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_PSPW_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_PSPW_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_PSPW_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_PSPW_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_PSPW_BES, null, true);
    }

    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_PSPW_BES, null, true);
    }

    public void test11() throws Exception {
        validate("Signature-X-BES-11.xml", BASE_PSPW_BES, null, true);
    }

    // epes
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_PSPW_EPES, OFFLINE_RESOLVER, true);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_PSPW_T, null, true);
    }

    // c incorrect date format!
    /*
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_PSPW_C, null, CERT_VAL_POLICY_CRL, true);
    } */

}
