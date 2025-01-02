package bundle.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import java.io.FileInputStream;

public class TestConstants {

    private static final String DIRECTORY = "T:\\api-cmsenvelope\\sample-policy\\";
    private static final String POLICY_FILE = "certval-encryption-policy-test.xml";
    private static final String SC_PIN = "123456";
    private static final String LICENSE_PATH = "";
    private static final String CERT_BASE64 = "";

    public static String getPolicyFile() {
        return DIRECTORY + POLICY_FILE;
    }

    public static String getPIN() {
        return SC_PIN;
    }

    public static void setLicense() throws Exception {

        LicenseUtil.setLicenseXml(new FileInputStream(LICENSE_PATH));
    }

    public static ECertificate getReceiverCert() throws Exception {

        ECertificate cert = new ECertificate(CERT_BASE64.getBytes("ASCII"));

        // or use
        // ECertificate cert = ECertificate.readFromFile("Cert path");

        return cert;
    }
}
