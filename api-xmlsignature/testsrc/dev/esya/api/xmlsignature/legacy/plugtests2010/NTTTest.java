package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class NTTTest extends PT2010BaseTest
{
    protected final static String BASE_NTT_BES = BASELOC +"XAdES-BES.SCOK\\NTT\\";
    protected final static String BASE_NTT_T = BASELOC +"XAdES-T.SCOK\\NTT\\";
    protected final static String BASE_NTT_A = BASELOC +"XAdES-A.SCOK\\NTT\\";

    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_NTT_BES, null, true);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_NTT_BES, null, true);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_NTT_T, null, true);
    }

    // A
    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_NTT_A, null, null, true);
    }

    public void testA8() throws Exception {
        validate("Signature-X-A-8.xml", BASE_NTT_A, null, null, true);
    }


}
