package dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;

/**
 * @author ahmety
 * date: Jul 27, 2009
 */
public class IAIKTransformsTest extends XMLBaseTest
{

    static String IAIK_TRANSFORMS_LOC = IAIKLOC + "transforms\\signatures\\";


    public void testBase64DecodeSignature()
            throws Exception
    {
        validate("base64DecodeSignature.xml", IAIK_TRANSFORMS_LOC, null, true);
    }

    public void testC14nSignature() throws Exception
    {
        validate("c14nSignature.xml", IAIK_TRANSFORMS_LOC, null, true);
    }

    public void testEnvelopedSignature()
            throws Exception
    {
        validate("envelopedSignatureSignature.xml", IAIK_TRANSFORMS_LOC, null, true);
    }

    public void testXPathSignature() throws Exception
    {
        validate("xPathSignature.xml", IAIK_TRANSFORMS_LOC, null, true);
    }

}
