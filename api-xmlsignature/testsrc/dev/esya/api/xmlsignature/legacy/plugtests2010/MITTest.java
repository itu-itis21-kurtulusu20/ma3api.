package dev.esya.api.xmlsignature.legacy.plugtests2010;


/**
 * @author ayetgin
 */
public class MITTest extends PT2010BaseTest
{
    protected final static String BASE_MIT_BES = BASELOC +"XAdES-BES.SCOK\\MIT\\";
    protected final static String BASE_MIT_T = BASELOC +"XAdES-T.SCOK\\MIT\\";
    protected final static String BASE_MIT_XL = BASELOC +"XAdES-XL.SCOK\\MIT\\";
    protected final static String BASE_MIT_A = BASELOC +"XAdES-A.SCOK\\MIT\\";

    public void test3() throws Exception {
        validate("Signature-X-BES-3.xml", BASE_MIT_BES, null, true);
    }

     // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_MIT_T, null, true);
    }

    public void testT141_1() throws Exception {
        validate("Signature-X141-T-1.xml", BASE_MIT_T, null, CERT_VAL_POLICY_CRL, true);
    }

     // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_MIT_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_MIT_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_MIT_XL, null, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_MIT_XL, null, true);
    }

    // A
    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_MIT_A, null, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_MIT_A, null, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_MIT_A, null, null, true);
    }

    public void testA5() throws Exception {
        validate("Signature-X-A-5.xml", BASE_MIT_A, null, null, true);
    }

    public void testA6() throws Exception {
        validate("Signature-X-A-6.xml", BASE_MIT_A, null, null, true);
    }

    public void test141A2() throws Exception {
        validate("Signature-X141-A-2.xml", BASE_MIT_A, null, null, true);
    }

    public void test141A3() throws Exception {
        validate("Signature-X141-A-3.xml", BASE_MIT_A, null, null, true);
    }

    public void test141A5() throws Exception {
        validate("Signature-X141-A-5.xml", BASE_MIT_A, null, null, true);
    }

}
