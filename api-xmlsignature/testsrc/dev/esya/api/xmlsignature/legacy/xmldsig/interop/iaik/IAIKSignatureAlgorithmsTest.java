package dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

/**
 * @author ahmety
 * date: Jul 27, 2009
 */
public class IAIKSignatureAlgorithmsTest extends XMLBaseTest
{
    static String IAIK_CORE_LOC = IAIKLOC + "signatureAlgorithms\\signatures\\";

    public void testDSASignature() throws Exception
    {
        validate("dSASignature.xml", IAIK_CORE_LOC, null, true);
    }

    public void testRSASignature() throws Exception
    {
        validate("rSASignature.xml", IAIK_CORE_LOC, null, true);
    }

    public void testHMACShortSignature() throws Exception
    {
        validate("hMACShortSignature.xml", IAIK_CORE_LOC, null, "secret".getBytes("ASCII"), true);
    }

    public void testHMACSignature() throws Exception
    {
        validate("hMACSignature.xml", IAIK_CORE_LOC, null, "secret".getBytes("ASCII"), true);
    }



}
