package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class TWTest extends PT2010BaseTest
{
    protected final static String BASE_TW_T     = BASELOC +"XAdES-T.SCOK\\TW\\";
    protected final static String BASE_TW_A     = BASELOC +"XAdES-A.SCOK\\TW\\";

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_TW_T, null, true);
    }

    // a
    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_TW_A, null, true);
    }

    public void testA9() throws Exception {
        validate("Signature-X-A-9.xml", BASE_TW_A, null, true);
    }

}
