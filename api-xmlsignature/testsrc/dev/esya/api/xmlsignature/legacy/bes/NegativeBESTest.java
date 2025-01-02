package dev.esya.api.xmlsignature.legacy.bes;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

/**
 * @author ahmety
 *         date: Sep 18, 2009
 */
public class NegativeBESTest extends XMLBaseTest
{
    protected final static String BASE_ETSI_BES = BASE_XAdES +"BES\\ETSI\\";

    public void testDataObjectFormatWrongReference() throws Exception {
        validate("Signature-X-BESN-1.xml", BASE_ETSI_BES, null, false);
    }

    public void testDataObjectFormatMimeMismatch() throws Exception {
        validate("Signature-X-BESN-2.xml", BASE_ETSI_BES, null, false);
    }

    public void testDataObjectFormatEncodingMismatch() throws Exception {
        validate("Signature-X-BESN-3.xml", BASE_ETSI_BES, null, false);
    }

    public void testDataObjectFormatInvalidCertDigest() throws Exception {
        validate("Signature-X-BESN-4.xml", BASE_ETSI_BES, null, false);
    }

}
