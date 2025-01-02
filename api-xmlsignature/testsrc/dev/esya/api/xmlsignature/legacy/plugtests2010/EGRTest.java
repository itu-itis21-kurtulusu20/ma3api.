package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class EGRTest extends PT2010BaseTest
{
    protected final static String BASE_EGR_BES = BASELOC +"XAdES-BES.SCOK\\EGR\\";
    protected final static String BASE_EGR_EPES = BASELOC +"XAdES-EPES.SCOK\\EGR\\";
    protected final static String BASE_EGR_T = BASELOC +"XAdES-T.SCOK\\EGR\\";
    protected final static String BASE_EGR_C = BASELOC +"XAdES-C.SCOK\\EGR\\";
    protected final static String BASE_EGR_X = BASELOC +"XAdES-X.SCOK\\EGR\\";
    protected final static String BASE_EGR_XL = BASELOC +"XAdES-XL.SCOK\\EGR\\";
    protected final static String BASE_EGR_A = BASELOC +"XAdES-A.SCOK\\EGR\\";

    // todo:  http://www.egroup.hu/pki/policies/egts/ETSI_plugtest_20101025_CRL.xml
    public void test2() throws Exception {
        validate("Signature-X-BES-2.xml", BASE_EGR_BES, OFFLINE_RESOLVER, true);
    }

    // epes
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_EGR_EPES, OFFLINE_RESOLVER, true);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_EGR_T, OFFLINE_RESOLVER, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_EGR_C, OFFLINE_RESOLVER, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_EGR_C, OFFLINE_RESOLVER, true);
    }

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_EGR_C, null, true);
    } */

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_EGR_X, OFFLINE_RESOLVER, CERT_VAL_POLICY_CRL, true);
    }

    public void testX2() throws Exception {
        validate("Signature-X-X-2.xml", BASE_EGR_X, OFFLINE_RESOLVER, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_EGR_X, OFFLINE_RESOLVER, true);
    }

    public void testX4() throws Exception {
        validate("Signature-X-X-4.xml", BASE_EGR_X, OFFLINE_RESOLVER, true);
    }

    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_EGR_XL, OFFLINE_RESOLVER, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL2() throws Exception {
        validate("Signature-X-XL-2.xml", BASE_EGR_XL, OFFLINE_RESOLVER, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_EGR_XL, OFFLINE_RESOLVER, true);
    }

    public void testXL4() throws Exception {
        validate("Signature-X-XL-4.xml", BASE_EGR_XL, OFFLINE_RESOLVER, true);
    }

    public void testA1() throws Exception {
        validate("Signature-X-A-1.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }

    public void testA2() throws Exception {
        validate("Signature-X-A-2.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }

    public void testA3() throws Exception {
        validate("Signature-X-A-3.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }

    public void testA4() throws Exception {
        validate("Signature-X-A-4.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }

    public void testA5() throws Exception {
        validate("Signature-X-A-5.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }

    public void testA6() throws Exception {
        validate("Signature-X-A-6.xml", BASE_EGR_A, OFFLINE_RESOLVER, null, true);
    }


}
