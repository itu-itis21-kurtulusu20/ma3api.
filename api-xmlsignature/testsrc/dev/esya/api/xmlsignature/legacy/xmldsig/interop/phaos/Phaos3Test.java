package dev.esya.api.xmlsignature.legacy.xmldsig.interop.phaos;

import dev.esya.api.xmlsignature.legacy.XMLBaseTest;
import gnu.crypto.util.Base64;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @author ahmety
 * date: Aug 11, 2009
 */
public class Phaos3Test extends XMLBaseTest
{

    @Override
    protected void setUp() throws Exception
    {
        validateManifests = false;
    }

    @Override
    protected void tearDown() throws Exception
    {
        validateManifests = true;
    }

    public void testDsaDetached() throws Exception
    {
        validate("signature-dsa-detached.xml", PHAOSLOC, OFFLINE_RESOLVER,true);
    }

    public void testDsaEnveloped() throws Exception
    {
        validate("signature-dsa-enveloped.xml", PHAOSLOC, null, true);
    }

    public void testDsaEnveloping() throws Exception
    {
        validate("signature-dsa-enveloping.xml", PHAOSLOC, null, true);
    }

    public void testDsaManifest() throws Exception
    {
        validate("signature-dsa-manifest.xml", PHAOSLOC, null, true);
    }

    public void testHmacShortDetached() throws Exception
    {
        validate("signature-hmac-sha1-40-c14n-comments-detached.xml", PHAOSLOC,
                 OFFLINE_RESOLVER, "test".getBytes("ASCII"), true);
    }

    public void testHmacShortExclusiveDetached() throws Exception
    {
        validate("signature-hmac-sha1-40-exclusive-c14n-comments-detached.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "test".getBytes("ASCII"), true);
    }

    public void testHmacExclusiveDetached() throws Exception
    {
        validate("signature-hmac-sha1-exclusive-c14n-comments-detached.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "test".getBytes("ASCII"), true);
    }


    public void testHmacExclusiveEnveloped() throws Exception
    {
        validate("signature-hmac-sha1-exclusive-c14n-enveloped.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "test".getBytes("ASCII"), true);
    }

    public void testRsaDetachedB64() throws Exception
    {
        validate("signature-rsa-detached-b64-transform.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaDetachedXPath() throws Exception
    {
        validate("signature-rsa-detached-xpath-transform.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaDetachedXsltBadRetrieval() throws Exception
    {
        validate("signature-rsa-detached-xslt-transform-bad-retrieval-method.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, false);
    }

    public void testRsaDetachedXsltRetrieval() throws Exception
    {
        validate("signature-rsa-detached-xslt-transform-retrieval-method.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaDetachedXslt() throws Exception
    {
        validate("signature-rsa-detached-xslt-transform.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaDetached() throws Exception
    {
        validate("signature-rsa-detached.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaEnvelopedBadDigest() throws Exception
    {
        validate("signature-rsa-enveloped-bad-digest-val.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, false);
    }

    public void testRsaEnveloped() throws Exception
    {
        validate("signature-rsa-enveloped.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaEnveloping() throws Exception
    {
        validate("signature-rsa-enveloping.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    // todo document.xml
    public void testRsaManifestX509CertChain() throws Exception
    {
        validate("signature-rsa-manifest-x509-data-cert-chain.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    // todo document.xml
    public void testRsaManifestX509DataCert() throws Exception
    {
        validate("signature-rsa-manifest-x509-data-cert.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    // todo bilinmeyen imza formati
    public void testRsaManifestX509DataIssuerSerial() throws Exception
    {
        validate("signature-rsa-manifest-x509-data-issuer-serial.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "certs/rsa-cert.der", true);
    }

    // todo bilinmeyen imza formati
    public void testRsaManifestX509DataSKI() throws Exception
    {
        validate("signature-rsa-manifest-x509-data-ski.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "certs/rsa-cert.der", true);
    }

    // todo bilinmeyen imza formati
    public void testRsaManifestX509DataSubjectName() throws Exception
    {
        validate("signature-rsa-manifest-x509-data-subject-name.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, "certs/rsa-cert.der", true);
    }

    // todo
    public void testRsaManifestX509Data() throws Exception
    {
        validate("signature-rsa-manifest-x509-data.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    // todo
    public void testRsaManifest() throws Exception
    {
        validate("signature-rsa-manifest.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    public void testRsaXpathEnveloped() throws Exception
    {
        validate("signature-rsa-xpath-transform-enveloped.xml",
                 PHAOSLOC, OFFLINE_RESOLVER, true);
    }

    /*
    todo fat
    */

    public static void main(String[] args) throws Exception
    {
        byte[] stringBytes =
                 ("<player id=\"10012\" bats=\"left\" throws=\"right\">\n" +
                 "\t<!-- Here's a comment -->\n" +
                 "\t<name>Alfonso Soriano</name>\n" +
                 "\t<position>2B</position>\n" +
                 "\t<team>New York Yankees</team>\n" +
                 "</player>\n").getBytes();

        FileDocument fd = new FileDocument(new File(
                "c:\\ahmet\\prj\\Sunucu\\YENIAPI\\ESYA_YENIAPI_int\\MA3\\ESYA_NEWAPI\\testdata\\xmlsignatures\\com\\phaos\\phaos-xmldsig-three\\document.xml"
        ));

        byte[] fileBytes = fd.getBytes();


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document node = db.parse(new ByteArrayInputStream(stringBytes));
        byte[] xmlBytes = XmlUtil.outputDOM(node, null);

        byte[] digest1 = DigestUtil.digest(DigestAlg.SHA1, stringBytes);
        byte[] digest2 = DigestUtil.digest(DigestAlg.SHA1, fileBytes);
        byte[] digest3 = DigestUtil.digest(DigestAlg.SHA1, xmlBytes);


        System.out.println("string digest : "+ Base64.encode(digest1));
        System.out.println("file digest   : "+ Base64.encode(digest2));
        System.out.println("xml digest    : "+ Base64.encode(digest3));
    }



}
