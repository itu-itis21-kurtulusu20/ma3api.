package xmlsig.samples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.OfflineResolver;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Represents a base for signature samples
 * @author suleyman.uslu
 */
public class SampleBase
{
    private static Logger LOGGER = LoggerFactory.getLogger(SampleBase.class);

    protected static String ROOT_DIR;
    protected static String BASE_DIR;
    protected static String POLICY_FILE;
    protected static String PFX_FILE;
    protected static String PFX_PASS;
    protected static String PIN;
    protected static String LICENSE;

    protected static ECertificate CERTIFICATE;
    protected static PrivateKey PRIVATE_KEY;

    protected static OfflineResolver POLICY_RESOLVER;

    public static final int[] OID_POLICY_P2 = new int[]{2,16,792,1,61,0,1,5070,3,1,1};
    public static final int[] OID_POLICY_P3 = new int[]{2,16,792,1,61,0,1,5070,3,2,1};
    public static final int[] OID_POLICY_P4 = new int[]{2,16,792,1,61,0,1,5070,3,3,1};

    private static final String ENVELOPE_XML =
                    "<envelope>\n" +
                    "  <data id=\"data1\">\n" +
                    "    <item>Item 1</item>\n"+
                    "    <item>Item 2</item>\n"+
                    "    <item>Item 3</item>\n"+
                    "  </data>\n" +
                    "</envelope>\n";

    /**
     * Initialize paths ans other variables
     */
    static {

        URL root = SampleBase.class.getResource("../../../");
        String classPath = root.getPath();
        File binDir = new File(classPath);

        //ROOT_DIR = binDir.getParent();
        ROOT_DIR = "C:\\CC\\suleyman.uslu_ESYA_MA3API_int\\MA3\\api-xmlsignature";
        BASE_DIR = ROOT_DIR + "/ornekler/XADES/signatures/";
        POLICY_FILE = ROOT_DIR + "/config/test/certval-policy-test.xml";
        //PFX_FILE = ROOT_DIR + "/sertifika deposu/072801_test2.pfx";
        PFX_FILE = ROOT_DIR + "/sertifika deposu/suleyman.uslu_283255@ug.net.pfx";
        //PFX_PASS = "072801";
        PFX_PASS = "283255";
        PIN = "12345";
        LICENSE = ROOT_DIR + "/lisans/lisans.xml";

        System.out.println("Base dir : " + BASE_DIR);

        loadLicense();

        POLICY_RESOLVER = new OfflineResolver();
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.1.1", ROOT_DIR + "/config/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.2.1", ROOT_DIR + "/config/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");
        POLICY_RESOLVER.register("urn:oid:2.16.792.1.61.0.1.5070.3.3.1", ROOT_DIR + "/config/profiller/Elektronik_Imza_Kullanim_Profilleri_Rehberi.pdf", "text/plain");

        try {

            KeyStore ks = KeyStore.getInstance("PKCS12");

            ks.load(new FileInputStream(PFX_FILE), PFX_PASS.toCharArray());

            String alias = ks.aliases().nextElement();
            PRIVATE_KEY = (PrivateKey)ks.getKey(alias, PFX_PASS.toCharArray());
            Certificate cert = ks.getCertificate(alias);
            CERTIFICATE = new ECertificate(cert.getEncoded());
        }
        catch (Exception x){
            x.printStackTrace();
        }
    }

    public static void loadLicense()
    {
        LOGGER.debug("License is being loaded from: " + LICENSE);
        try {
            LicenseUtil.setLicenseXml(new FileInputStream(LICENSE));
        } catch (ESYAException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates sample envelope xml that will contain signature inside
     * @return envelope in Document format
     * @throws ESYAException
     */
    public Document newEnvelope() throws ESYAException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            return db.parse(new ByteArrayInputStream(ENVELOPE_XML.getBytes()));
        }
        catch (Exception x){
            // we shouldn't be here if ENVELOPE_XML is valid
            x.printStackTrace();
        }
        throw new ESYAException("Cant construct envelope xml ");
    }
}
