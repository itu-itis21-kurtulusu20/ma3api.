package dev.esya.api.cmssignature.mobilimza;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Operator;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.PhoneNumberAndOperator;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class TurkTelekomMobilImzaTest {

    @Test
    public void createSignature() throws Exception {

        BaseSigner signer = getTurkTelekomSigner();

        String policyFile = "C:\\ma3api\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(new SignableFile(new File(policyFile)), true);

        ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(policyFile));
        HashMap<String, Object> params = new HashMap<>();

        // In real system, validate certificate by giving parameter "true" instead of "false"
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

        bs.addSigner(ESignatureType.TYPE_BES, null, signer, null, params);

        AsnIO.dosyayaz(bs.getEncoded(), "C:\\a\\turktelekom.p7s");
    }

    private BaseSigner getTurkTelekomSigner() {
        MSSParams mobilParams = new MSSParams("Tubitak_KamuSM", "Tubitak2021", "");
        mobilParams.setMsspProfileQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_ProfileQueryService");
        mobilParams.setMsspSignatureQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_SignatureService");
        mobilParams.setMsspRequestTimeout(5000);
        mobilParams.setConnectionTimeoutMs(30000);

        PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("05078553311", Operator.TURKTELEKOM);
        EMobileSignerConnector emsspClientConnector = new EMobileSignerConnector(mobilParams);
        // get signer certificate necessary field for signing from operator
        emsspClientConnector.setCertificateInitials(phoneNumberAndOperator);

        MobileSigner mobileSigner =  new MobileSigner(emsspClientConnector, phoneNumberAndOperator, null, "rapor", SignatureAlg.RSA_SHA256.getName(), null);


        mobileSigner.getFingerPrintInfo().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object fingerPrintObj) {
                String fingerPrint = (String) fingerPrintObj;
                System.out.println("Tekil imza fingerprint: " + fingerPrint);
            }
        });
        
        return mobileSigner;
    }
}
