package dev.esya.api.xmlsignature.legacy.plugtests2010;

/**
 * @author ayetgin
 */
public class CERTest extends PT2010BaseTest
{
    protected final static String BASE_CER_BES = BASELOC +"XAdES-BES.SCOK\\CER\\";
    protected final static String BASE_CER_T = BASELOC +"XAdES-T.SCOK\\CER\\";
    protected final static String BASE_CER_X = BASELOC +"XAdES-X.SCOK\\CER\\";

    public void test1() throws Exception {
        validate("Signature-X-BES-1.xml", BASE_CER_BES, null, true);
    }

    public void testT1() throws Exception {
        validate("Signature-X-T-1.xml", BASE_CER_T, null, true);
    }

    public void testX1() throws Exception {
        validate("Signature-X-X-1.xml", BASE_CER_X, null, CERT_VAL_POLICY_CRL, true);
    }

    
}
