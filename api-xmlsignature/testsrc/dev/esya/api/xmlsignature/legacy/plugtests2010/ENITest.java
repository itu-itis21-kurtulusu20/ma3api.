package dev.esya.api.xmlsignature.legacy.plugtests2010;


/**
 * @author ayetgin
 */
public class ENITest extends PT2010BaseTest
{
    protected final static String BASE_ENI_BES = BASELOC +"XAdES-BES.SCOK\\ENI\\";
    protected final static String BASE_ENI_T = BASELOC +"XAdES-T.SCOK\\ENI\\";
    protected final static String BASE_ENI_A = BASELOC +"XAdES-A.SCOK\\ENI\\";

    // todo signingtime belli değil
    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_ENI_BES, null, false);
    }

    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_ENI_BES, null, true);
    }

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_ENI_BES, null, true);
    }

    public void test4() throws Exception {
        validate("Signature-X-BES-4.xml", BASE_ENI_BES, null, true);
    }

    public void test6() throws Exception {
        validate("Signature-X-BES-6.xml", BASE_ENI_BES, null, true);
    }

    public void test7() throws Exception {
        validate("Signature-X-BES-7.xml", BASE_ENI_BES, null, true);
    }

    public void test8() throws Exception {
        validate("Signature-X-BES-8.xml", BASE_ENI_BES, null, true);
    }

    public void test11() throws Exception {
        validate("Signature-X-BES-11.xml", BASE_ENI_BES, null, true);
    }
    
    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_ENI_T, null, true);
    }

    // a
    public void testA7() throws Exception {
        validate("Signature-X-A-7.xml", BASE_ENI_A, null, null, true);
    }

    public void testA8() throws Exception {
        validate("Signature-X-A-8.xml", BASE_ENI_A, null, null, true);
    }

    public void testA9() throws Exception {
        validate("Signature-X-A-9.xml", BASE_ENI_A, null, null, true);
    }

}
