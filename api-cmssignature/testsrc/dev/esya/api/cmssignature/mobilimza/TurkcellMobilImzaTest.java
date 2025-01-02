package dev.esya.api.cmssignature.mobilimza;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.IMobileSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Operator;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.PhoneNumberAndOperator;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class TurkcellMobilImzaTest {

    static String file1Path = "C:/Users/ma3/Desktop/hello1.txt"; // document which will be signed

    static MobileSigner mobileSigner = null;

    private static final Map<Integer, Operator> intToTypeMap = new HashMap<>();

    static {
        for (Operator type : Operator.values()) {
            intToTypeMap.put(type.ordinal(), type);
        }
    }

    @Test
    public void createSignature() throws Exception {

        BasicConfigurator.configure();

        BaseSigner signer = getTurkcellSigner();

        String policyFile = "C:\\ma3api\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(new SignableFile(new File(policyFile)), true);

        ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(policyFile));
        HashMap<String, Object> params = new HashMap<>();

        // In real system, validate certificate by giving parameter "true" instead of "false"
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

        bs.addSigner(ESignatureType.TYPE_BES, null, signer, null, params);

        AsnIO.dosyayaz(bs.getEncoded(), "C:\\a\\turkcell.p7s");
    }

    private BaseSigner getTurkcellSigner() {
        MSSParams mobilParams = new MSSParams("http://MImzaTubitak", "tubitak_025", "www.turkcelltech.com");
        mobilParams.setMsspProfileQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_ProfileQueryPort");
        mobilParams.setMsspSignatureQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_Signature");
        PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05352506610", Operator.TURKCELL);
        mobilParams.setMsspRequestTimeout(5000);
        mobilParams.setConnectionTimeoutMs(30000);

        EMobileSignerConnector emsspClientConnector = new EMobileSignerConnector(mobilParams);

        // get signer certificate necessary field for signing from operator
        emsspClientConnector.setCertificateInitials(phoneNumberAndOperator);

        return new MobileSigner(emsspClientConnector, phoneNumberAndOperator, null, "rapor", SignatureAlg.RSA_SHA256.getName(), null);
    }

    public static void main(String[] args) {
        try {
            // creating single signature
            createSingleSignature(mobileSigner);

            /*
            // creating multiple signature
            // multi-signature is only supported by Turkcell operator
            createMultipleSigning(mobileSigner);
            */

        } catch (Exception ex) {
            System.out.println("Error in TestMobileSignClient: " + ex.getMessage());
        }
    }

    public static void createSingleSignature(MobileSigner mobileSigner) throws ESYAException {
        try {
            mobileSigner.getFingerPrintInfo().addObserver(new Observer() {
                @Override
                public void update(Observable o, Object fingerPrintObj) {
                    String fingerPrint = (String) fingerPrintObj;
                    System.out.println("Tekil imza fingerprint: " + fingerPrint);
                }
            });

            SignatureResult result = signData(file1Path, mobileSigner);

            if (result.isExceptionOccured()) {
                System.out.println("Exception occured for: " + result.getInformativeText());
                result.getException().printStackTrace();
            } else
                System.out.println("Signature created for: " + result.getInformativeText());

        } catch (Exception ex) {
            String errorString = "Error in creating single signature: ";
            System.out.println(errorString + ex.getMessage());
            throw new ESYAException(errorString, ex);
        }
    }

    public static SignatureResult signData(String filePath, IMobileSigner signer) {
        try {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableFile(new File(filePath)), true);

            ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream("C:\\Users\\ma3\\Desktop\\ValidationFiles\\certval-policy-test.xml"));
            HashMap<String, Object> params = new HashMap<>();

            // In real system, validate certificate by giving parameter "true" instead of "false"
            params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
            params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

            // Since SigningTime attribute is optional, add it to optional attributes list
            List<IAttribute> optionalAttributes = new ArrayList<>();
            optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

            bs.addSigner(ESignatureType.TYPE_BES, null, signer, optionalAttributes, params);

            AsnIO.dosyayaz(bs.getEncoded(), filePath + ".p7s");

        } catch (Exception ex) {
            return new SignatureResult(ex, true, signer.getInformativeText());
        }

        return new SignatureResult(null, false, signer.getInformativeText());
    }
}
