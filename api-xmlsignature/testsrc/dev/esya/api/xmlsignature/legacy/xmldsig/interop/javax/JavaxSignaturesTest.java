package dev.esya.api.xmlsignature.legacy.xmldsig.interop.javax;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;

import java.io.File;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * @author ahmety
 * date: Jul 29, 2009
 */
public class JavaxSignaturesTest extends XMLBaseTest
{

    // test with asertion id
    public void testEnvelopingSignature() throws Exception
    {
        FileDocument d = new FileDocument(new File(JAVAXLOC + "envelopingSignature.xml"), MIMETYPE_XML);
        XMLSignature signature = XMLSignature.parse(d, new Context(JAVAXLOC));

        Document doc = signature.getDocument();
        NodeList nl = doc.getElementsByTagName("Assertion");

        Element assertionElement = (Element) nl.item(0);
        String assertionId = assertionElement.getAttributeNS(null, "AssertionID");

        signature.getContext().getIdRegistry().put(assertionId, assertionElement);

        ValidationResult result = signature.verify();
        assertTrue(result.getType()== ValidationResultType.VALID);
    }

    public void testInvalidSignature() throws Exception
    {
        validate("invalid-signature.xml", JAVAXLOC, null, false);
    }

    public void testSignatureXSLT1() throws Exception
    {
        validate("signature1.xml", JAVAXLOC, null, false);
    }

    public void testSignatureXSLT2() throws Exception
    {
        validate("signature2.xml", JAVAXLOC, null, false);
    }

    public void testSignatureXSLT3() throws Exception
    {
        validate("signature3.xml", JAVAXLOC, null, false);
    }

    public void testSignatureExternalC14nAttributes() throws Exception
    {
        validate("signature-external-c14n-xmlatrs.xml", JAVAXLOC, null, "secret".getBytes("ASCII"), true);
    }

}
