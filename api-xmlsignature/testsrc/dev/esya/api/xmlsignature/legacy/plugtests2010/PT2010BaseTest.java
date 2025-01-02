package dev.esya.api.xmlsignature.legacy.plugtests2010;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Authenticator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class PT2010BaseTest extends TestCase
{
    protected final static String BASELOC =
            "T:\\api-xmlsignature\\testdata\\pt2010\\";

    protected final static String CERT_VAL_POLICY_OFFLINE =
        "T:\\api-xmlsignature\\testresources\\certval-pt2010-scok-offline-config.xml";

    protected final static String CERT_VAL_POLICY_CRL = CERT_VAL_POLICY_OFFLINE;
        //"T:\\api-xmlsignature\\config\\certval-pt2010-scok-config.xml";


    protected final static String CERT_VAL_POLICY_OCSP = CERT_VAL_POLICY_OFFLINE;
        //"T:\\api-xmlsignature\\config\\certval-pt2010-scok-ocsp-config.xml";

    protected static CertValidationPolicies OCSP_POLICIES = new CertValidationPolicies();
    protected static CertValidationPolicies CRL_POLICIES = new CertValidationPolicies();


    protected final static String MA3_DIR = "TU";

    protected final static String DATA_DIR =
            "T:\\api-xmlsignature\\testdata\\pt2010\\Data\\";

    protected final static String MATERIAL =
            "T:\\api-xmlsignature\\testdata\\pt2010\\CryptographicMaterial\\";

    protected final static String MIMETYPE_XML = "text/xml";
    
    protected boolean validateManifests = true;

    protected static ECertificate CERTIFICATE;
    protected static PrivateKey PRIVATEKEY;

    protected static final OfflineResolver OFFLINE_RESOLVER = new OfflineResolver();

    static{
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/TestCases/Data/PolicyData.xml",
                BASELOC + "/Data/PolicyData.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/XAdES/TestCases/Data/testPolicyBase64.txt",
                BASELOC + "/Data/testPolicyBase64.txt",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://xades-portal.etsi.org/protected/XAdES/TestCases/Data/PolicyData.xml",
                BASELOC + "/Data/PolicyData.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "1.2.3.4.5.6.7.8.9",
                BASELOC + "/Data/PolicyData.xml",
                "text/xml");

        // egroup
        OFFLINE_RESOLVER.register(
                "http://www.egroup.hu/pki/policies/egts/ETSI_plugtest_20101025_CRL.xml",
                BASELOC + "/Data/egroup/ETSI_plugtest_20101025_CRL.xml",
                "text/xml");
        OFFLINE_RESOLVER.register(
                "http://www.egroup.hu/pki/policies/egts/ETSI_plugtest_20101025_OCSP.xml",
                BASELOC + "/Data/egroup/ETSI_plugtest_20101025_OCSP.xml",
                "text/xml");



    }

    protected static AttributeCertificate ROLE_CERTIFICATE;

    static {
        // force init

        try {
            Authenticator a = new EtsiTestAuthenticator();

            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] password = "123456".toCharArray();
            ks.load(new FileInputStream(MATERIAL+"SCOK\\AhmetKeyCert.p12"), password);
            String alias = ks.aliases().nextElement();
            PRIVATEKEY = (PrivateKey)ks.getKey(alias, password);
            Certificate cert = ks.getCertificate(alias);
            CERTIFICATE = new ECertificate(cert.getEncoded());

            ROLE_CERTIFICATE = new AttributeCertificate();
            // todo 
            ROLE_CERTIFICATE = (AttributeCertificate) AsnIO.dosyadanOKU(ROLE_CERTIFICATE, MATERIAL+"SCOK\\AhmetRoleCert.crt");

            OCSP_POLICIES.register(null, PolicyReader.readValidationPolicy(CERT_VAL_POLICY_OCSP));
            CRL_POLICIES.register(null, PolicyReader.readValidationPolicy(CERT_VAL_POLICY_CRL));

        }catch (Exception x){
            x.printStackTrace();
        }
    }

    public void validate(String fileName, String baseURI, IResolver resolver, boolean positiveTypeTest)
    throws Exception
	{
    	validate(fileName, baseURI, resolver, null, null, null, positiveTypeTest);
	}


    public void validate(String fileName, String baseURI, IResolver resolver, String certPolicy, boolean positiveTypeTest)
    throws Exception
    {
    	validate(fileName, baseURI, resolver, null, null, certPolicy, positiveTypeTest);
    }


    public void validate(String fileName, String baseURI, IResolver resolver,
                         byte[] secretKey, ECertificate sertifika, String certPolicy, boolean positiveTypeTest)
            throws Exception
    {
    	System.out.println("<<---------------------------------->>");
    	System.out.println("<<  "+baseURI+fileName+"  >>");
    	System.out.println("<<---------------------------------->>");
        FileDocument d = new FileDocument(new File(baseURI + fileName), MIMETYPE_XML, null);

        Context c = new Context(baseURI);

        c.setValidateManifests(validateManifests);
        
        if (certPolicy==null)
        	certPolicy = CERT_VAL_POLICY_OCSP;

        CertValidationPolicies policies = new CertValidationPolicies();

        ValidationPolicy policy = PolicyReader.readValidationPolicy(certPolicy);
        policies.register(null, policy);

        c.getConfig().getValidationConfig().setCertValidationPolicies(policies);
        //todo
        c.setValidateCertificates(true);

        XMLSignature signature = XMLSignature.parse(d, c);

        if (resolver != null) {
            c.getResolvers().add(resolver);
        }

        ValidationResult verified;
        try {
	        if (secretKey!=null) {
	            verified = signature.verify(secretKey);
	        }
	        else if (sertifika!=null){
	            verified = signature.verify(sertifika);
	        }
	        else {
	            verified = signature.verify();
	        }
        } catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
        System.out.println("--validation result---------");
        System.out.println(verified.getType());
        System.out.println("----------------------------");

        String[] splitted = baseURI.split("\\\\");
        String parentDir = splitted[splitted.length-1];
        String testDir = splitted[splitted.length-2];
        FileOutputStream fos = new FileOutputStream(BASELOC+testDir+"\\TU\\Verification_of_"+parentDir+"_"+fileName);
        fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes("UTF-8"));
        fos.write(verified.toXml().getBytes("UTF-8"));
        fos.close();

        System.out.println(""+verified.toXml());

        boolean valid = verified.getType()== ValidationResultType.VALID;
        if (positiveTypeTest) {
            assertTrue(valid);
        }
        else {
            assertFalse(valid);
        }
    }


    public static void main(String[] args) throws Exception
    {
        System.out.println(""+ Arrays.asList(BASELOC.split("\\\\")));
    }

}
