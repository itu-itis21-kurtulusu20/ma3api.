package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class MICRTest extends PT2010BaseTest
{
    protected final static String BASE_MICR_BES   = BASELOC +"XAdES-BES.SCOK\\MICR\\";
    protected final static String BASE_MICR_EPES  = BASELOC +"XAdES-EPES.SCOK\\MICR\\";
    protected final static String BASE_MICR_T     = BASELOC +"XAdES-T.SCOK\\MICR\\";
    protected final static String BASE_MICR_C     = BASELOC +"XAdES-C.SCOK\\MICR\\";
    protected final static String BASE_MICR_X     = BASELOC +"XAdES-X.SCOK\\MICR\\";
    protected final static String BASE_MICR_XL    = BASELOC +"XAdES-XL.SCOK\\MICR\\";

    
    // epes
    public void testEPES1() throws Exception {
        validate("Signature-X-EPES-1.xml", BASE_MICR_EPES, null, true);
    }

    // t
    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_MICR_T, null, true);
    }

    // c
    public void testC1() throws Exception {
        validate("Signature-X-C-1.xml", BASE_MICR_C, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testC2() throws Exception {
        validate("Signature-X-C-2.xml", BASE_MICR_C, null, true);
    }

    /*public void testC3() throws Exception {
        validate("Signature-X-C-3.xml", BASE_MICR_C, null, true);
    } */

    // x
    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_MICR_X, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testX3() throws Exception {
        validate("Signature-X-X-3.xml", BASE_MICR_X, null, true);
    }
    // XL
    public void testXL1() throws Exception {
        validate("Signature-X-XL-1.xml", BASE_MICR_XL, null, CERT_VAL_POLICY_CRL, true);
    }

    public void testXL3() throws Exception {
        validate("Signature-X-XL-3.xml", BASE_MICR_XL, null, true);
    }


}
