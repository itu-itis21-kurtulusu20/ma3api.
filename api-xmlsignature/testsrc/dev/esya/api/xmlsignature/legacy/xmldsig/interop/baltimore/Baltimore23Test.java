package dev.esya.api.xmlsignature.legacy.xmldsig.interop.baltimore;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;

/**
 * @author ahmety
 * date: Jul 29, 2009
 */
public class Baltimore23Test extends XMLBaseTest
{

    protected final static String BALTIMORE23LOC = BALTIMORELOC + "merlin-xmldsig-twenty-three\\";

    public void testEnvelopingHmacSha1() throws Exception
    {
        validate("signature-enveloping-hmac-sha1.xml", BALTIMORE23LOC, null, "secret".getBytes("ASCII"), true);
    }

    public void testEnvelopingHmacSha1_40() throws Exception
    {
        validate("signature-enveloping-hmac-sha1-40.xml", BALTIMORE23LOC, null, "secret".getBytes("ASCII"), true);
    }

    public void testEnvelopedDSA() throws Exception
    {
        validate("signature-enveloped-dsa.xml", BALTIMORE23LOC, null, true);
    }

    public void testEnvelopingBase64DSA() throws Exception
    {
        validate("signature-enveloping-b64-dsa.xml", BALTIMORE23LOC, null, true);
    }

    public void testEnvelopingDSA() throws Exception
    {
        validate("signature-enveloping-dsa.xml", BALTIMORE23LOC, null, true);
    }

    public void testEnvelopingRSA() throws Exception
    {
        validate("signature-enveloping-rsa.xml", BALTIMORE23LOC, null, true);
    }

    public void testExternalBase64DSA() throws Exception
    {
        OfflineResolver resolver = new OfflineResolver(
                         "http://www.w3.org/Signature/2002/04/xml-stylesheet.b64",
                         BALTIMORE23LOC +"xml-stylesheet.b64", "text/plain");
        validate("signature-external-b64-dsa.xml", BALTIMORE23LOC, resolver, true);
    }

    public void testExternalDSA() throws Exception
    {
        OfflineResolver resolver = new OfflineResolver(
                        "http://www.w3.org/TR/xml-stylesheet",
                        BASELOC + "org/w3c/www/TR/xml-stylesheet.html", "text/html");
        validate("signature-external-dsa.xml", BALTIMORE23LOC, resolver, true);
    }

    // todo
    public void testExternalDSA2() throws Exception
    {
        validate("signature.xml", BALTIMORE23LOC, OFFLINE_RESOLVER, true);
    }

}
