package dev.esya.api.xmlsignature.legacy;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import gnu.crypto.util.Base64;
import gnu.crypto.util.PRNG;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author ahmety
 * date: Jul 1, 2009
 */
public class XMLBaseTest extends TestCase
{

    protected boolean validateManifests = true;
    
    protected final static String BASELOC = "T:\\api-xmlsignature\\testdata\\";

    protected final static String BASE_INCELEME = BASELOC + "\\inceleme\\";
    

    protected final static String BASE_MA3 = BASELOC + "ma3\\";

    protected final static String BASE_MA3_INCLUDE = BASE_MA3 + "include\\";

    protected final static String BASE_XAdES =BASELOC + "XAdES\\";


    protected final static String IAIKLOC = BASELOC + "at/iaik/ixsil/";

    protected final static String MIMETYPE_XML = "text/xml";

    protected final static String JAVAXLOC = BASELOC + "javax\\xml\\crypto\\dsig\\";

    protected final static String PHAOSLOC = BASELOC + "com\\phaos\\phaos-xmldsig-three\\";

    protected final static String BALTIMORELOC = BASELOC + "ie\\baltimore\\merlin-examples\\";


    protected static final OfflineResolver OFFLINE_RESOLVER = new OfflineResolver();

    static{
        OFFLINE_RESOLVER.register(
                "http://www.w3.org/TR/xml-stylesheet",
                BASELOC + "org/w3c/www/TR/xml-stylesheet.html",
                "text/html");
        OFFLINE_RESOLVER.register(
                "http://www.w3.org/TR/2000/REC-xml-20001006",
                BASELOC + "org/w3c/www/TR/2000/REC-xml-20001006",
                "text/xml");
        OFFLINE_RESOLVER.register("http://www.nue.et-inf.uni-siegen.de/index.html",
                BASELOC + "org/apache/xml/security/temp/nuehomepage",
                "text/html");
        OFFLINE_RESOLVER.register(
                "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/id2.xml",
                BASELOC + "org/apache/xml/security/temp/id2.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://xmldsig.pothole.com/xml-stylesheet.txt",
                BASELOC + "com/pothole/xmldsig/xml-stylesheet.txt",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://www.w3.org/Signature/2002/04/xml-stylesheet.b64",
                BASELOC + "ie/baltimore/merlin-examples/merlin-xmldsig-twenty-three/xml-stylesheet.b64",
                "text/plain");
        OFFLINE_RESOLVER.register(
                "http://www.ietf.org/rfc/rfc3161.txt",
                BASELOC + "org/ietf/rfc3161.txt",
                "text/plain");
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/TestCases/Data/PolicyData.xml",
                BASELOC + "XAdES/Data/PolicyData.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/XAdES/TestCases/Data/PolicyData.xml",
                BASELOC + "XAdES/Data/PolicyData.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "urn:oid:1.2.3.4.5.3",
                BASELOC + "XAdES/Data/PolicyData.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/TestCases/Data/testPolicyBase64.txt",
                BASELOC + "XAdES/Data/testPolicyBase64.txt",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "urn:oid:1.2.752.76.1.777.814.999.9",
                BASELOC + "XAdES/T/TW/sigp.html",
                "text/html");
        OFFLINE_RESOLVER.register(
                "http://www.trustweaver.com/download/sigp.html",
                BASELOC + "XAdES/T/TW/sigp.html",
                "text/html");

        // crypto material
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/certs/LevelBCAOK.crt",
                BASELOC + "XAdES/CryptographicMaterial/SCOK/certs/LevelBCAOK.cer",
                "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/certs/LevelACAOK.crt",
            BASELOC + "XAdES/CryptographicMaterial/SCOK/certs/LevelACAOK.cer",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/certs/RootCAOK.crt",
            BASELOC + "XAdES/CryptographicMaterial/SCOK/TrustAnchors/RootCAOK.cer",
            "bin");

        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/crls/SCOK/LevelBCAOK.crl",
            BASELOC + "XAdES/CryptographicMaterial/SCOK/LevelBCAOK.crl",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/crls/SCOK/LevelACAOK.crl",
            BASELOC + "XAdES/CryptographicMaterial/SCOK/LevelACAOK.crl",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/crls/SCOK/RootCAOK.crl",
            BASELOC + "XAdES/CryptographicMaterial/SCOK/RootCAOK.crl",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/OCSP?ca=LevelBCAOK",
            BASELOC + "XAdES/C/ENT/Signature-X-C-2.CERT-SIG-EE.ocs.der",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/OCSP?ca=LevelACAOK",
            BASELOC + "XAdES/C/ENT/Signature-X-C-2.CERT-SIG-LevelB.ocs.der",
            "bin");
        OFFLINE_RESOLVER.register(
            "http://xades-portal.etsi.org/protected/capso/OCSP?ca=RootCAOK",
            BASELOC + "XAdES/C/ENT/Signature-X-C-2.CERT-SIG-LevelA.ocs.der",
            "bin");

        // hack for getting stupid xception printstacktrace first!
        PRNG.nextBytes(new byte[1]);
    }


    public void validate(String fileName, String baseURI, IResolver resolver, boolean positiveTypeTest)
            throws Exception
    {
        validate(fileName, baseURI, resolver, null, null, positiveTypeTest);
    }

    public void validate(String fileName, String baseURI, IResolver resolver,
                         String relativeCerthPath, boolean positiveTypeTest)
            throws Exception
    {
        FileDocument fd = new FileDocument(new File(baseURI+relativeCerthPath));
        ECertificate sertifika = new ECertificate(fd.getBytes());
        validate(fileName, baseURI, resolver, null, sertifika, positiveTypeTest);
    }

    public void validate(String fileName, String baseURI, IResolver resolver,
                         byte[] secretKey, boolean positiveTypeTest)
            throws Exception
    {
        validate(fileName, baseURI, resolver, secretKey, null, positiveTypeTest);
    }

    public void validate(String fileName, String baseURI, IResolver resolver,
                         byte[] secretKey, ECertificate sertifika, boolean positiveTypeTest)
            throws Exception
    {
        FileDocument d = new FileDocument(new File(baseURI + fileName), MIMETYPE_XML, null);

        Context c = new Context(baseURI);
        System.out.println(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
        //Calendar signatureDate = new GregorianCalendar();
        //signatureDate.setTime(DateFormat.getDateInstance(DateFormat.SHORT).parse("18.02.2009"));
        //c.setUserValidationTime(signatureDate);
        c.setValidateManifests(validateManifests);
        XMLSignature signature = XMLSignature.parse(d, c);

        if (resolver != null) {
            c.getResolvers().add(resolver);
        }

        ValidationResult verified;
        if (secretKey!=null) {
            verified = signature.verify(secretKey);
        }
        else if (sertifika!=null){
            verified = signature.verify(sertifika);
        }
        else {
            verified = signature.verify();
        }

        System.out.println("--validation result---------");
        System.out.println(verified.toXml());
        System.out.println("----------------------------");

        boolean valid = verified.getType()== ValidationResultType.VALID;
        if (positiveTypeTest) {
            assertTrue(valid);
        }
        else {
            assertFalse(valid);
        }
    }

    protected TSSettings getZDAyar(){
        //return new ZDAyar("http://20.1.5.39/", 80, 21, "12345678".toCharArray());
        return new TSSettings("http://30.1.1.10",61,"12345678".toCharArray());
        //return new TSSettings("http://30.1.1.10",80,"20.1.5.54",8080,"ug.uekae",61,"12345678".toCharArray(),"ahmety","123456".toCharArray());
    }

    public static void main(String[] args) throws Exception
    {
        byte[] timestamp = new byte[]{48,-126,2,-32,48,21,2,1,0,48,16,12,14,79,112,101,114,97,116,105,111,110,32,79,107,97,121,48,-126,2,-59,6,9,42,-122,72,-122,-9,13,1,7,2,-96,-126,2,-74,48,-126,2,-78,2,1,3,49,11,48,9,6,5,43,14,3,2,26,5,0,48,88,6,11,42,-122,72,-122,-9,13,1,9,16,1,4,-96,73,4,71,48,69,2,1,1,6,2,42,3,48,33,48,9,6,5,43,14,3,2,26,5,0,4,20,87,52,12,-127,-77,-79,-4,14,93,0,91,1,-118,-10,79,79,-58,107,7,-6,2,8,104,-72,-57,27,-93,-59,109,-67,24,15,50,48,49,48,48,55,48,50,48,56,52,49,51,50,90,49,-126,2,68,48,-126,2,64,2,1,1,48,-127,-115,48,-127,-128,49,56,48,54,6,3,85,4,3,12,47,69,66,71,32,69,108,101,107,116,114,111,110,105,107,32,83,101,114,116,105,102,105,107,97,32,72,105,122,109,101,116,32,83,97,-60,-97,108,97,121,-60,-79,99,-60,-79,115,-60,-79,49,55,48,53,6,3,85,4,10,12,46,69,66,71,32,66,105,108,105,-59,-97,105,109,32,84,101,107,110,111,108,111,106,105,108,101,114,105,32,118,101,32,72,105,122,109,101,116,108,101,114,105,32,65,46,-59,-98,46,49,11,48,9,6,3,85,4,6,19,2,84,82,2,8,36,-48,76,38,-94,65,27,-70,48,9,6,5,43,14,3,2,26,5,0,-96,-127,-116,48,26,6,9,42,-122,72,-122,-9,13,1,9,3,49,13,6,11,42,-122,72,-122,-9,13,1,9,16,1,4,48,28,6,9,42,-122,72,-122,-9,13,1,9,5,49,15,23,13,49,48,48,55,48,50,48,56,52,49,51,50,90,48,35,6,9,42,-122,72,-122,-9,13,1,9,4,49,22,4,20,-121,-10,-95,79,-43,-117,-107,60,-73,-102,-46,-8,-92,61,-23,-33,-90,-60,59,-126,48,43,6,11,42,-122,72,-122,-9,13,1,9,16,2,12,49,28,48,26,48,24,48,22,4,20,-32,-23,6,6,-25,98,-47,-112,-106,114,-16,107,97,-126,18,90,81,37,101,92,48,13,6,9,42,-122,72,-122,-9,13,1,1,1,5,0,4,-126,1,0,31,-86,118,65,-120,-105,-50,10,90,-11,16,-95,-75,-38,-40,56,98,68,21,-75,-37,23,12,-31,-91,62,-49,40,56,87,-57,34,-60,-71,38,124,19,0,-110,-39,110,52,-75,31,18,-122,78,-47,-34,-53,117,-61,76,-21,-1,-98,-40,-48,115,100,62,-120,-46,43,118,-30,-110,39,-39,-85,-91,83,58,-16,83,-59,53,-73,105,-118,22,123,-109,-21,96,55,13,1,-89,2,26,-126,114,123,-50,-16,-81,-33,-72,-69,108,2,-8,-40,-24,64,-2,50,59,112,89,-34,91,-77,96,109,79,-112,-26,-20,74,106,31,-3,-98,16,-39,39,112,100,-123,-39,95,67,-95,-118,-79,104,68,-67,26,82,98,-78,10,-3,-90,69,6,-115,74,75,55,66,87,-44,43,98,-93,6,-36,-12,-45,58,-110,-119,-27,3,95,-96,117,-100,-95,21,97,-40,-80,37,-62,-114,-126,89,-101,-94,-116,-122,92,-58,-110,3,-127,-110,113,-69,57,67,121,-2,-125,-87,19,-31,-22,45,-75,94,-83,120,33,77,58,-103,31,-43,-32,93,-32,90,114,-119,13,89,12,-84,15,121,99,97,65,72,-51,3,29,53,10,56,-22,-106,-69,-6,-57,-56,84,-14,71,-16,95,-70,81,-36,67,-116,63,-83,84,-102};
        System.out.println(""+ Base64.encode(timestamp));

        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(timestamp);
        new TimeStampResp().decode(decBuf);


        //new EContentInfo(timestamp);
    }

}
