package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class LUXTest extends PT2010BaseTest
{
    protected final static String BASE_LUX_BES = BASELOC +"XAdES-BES.SCOK\\LUX\\";
    protected final static String BASE_LUX_T = BASELOC +"XAdES-T.SCOK\\LUX\\";

    // todo signingtime belli değil    
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_LUX_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_LUX_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_LUX_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_LUX_BES, null, true);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_LUX_T, null, true);
    }

}
