package dev.esya.api.cmssignature.mobilimza;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MobileSigner;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Operator;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.PhoneNumberAndOperator;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class VodafoneMobilImzaTest {

    @Before
    public void setUp() {
        BasicConfigurator.configure();
    }

    @Test
    public void testCreateSignature() throws Exception {

        BasicConfigurator.configure();

        String policyFile = "C:\\GitRepo\\ma3api\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";

        ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(policyFile));
        HashMap<String, Object> params = new HashMap<>();

        // In real system, validate certificate by giving parameter "true" instead of "false"
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

        List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
        optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(new SignableFile(new File(policyFile)), true);
        bs.addSigner(ESignatureType.TYPE_BES, null, getVodafoneSigner(), optionalAttributes, params);

        AsnIO.dosyayaz(bs.getEncoded(), "C:\\a\\vod.p7s");
    }

    @Test
    public void addCounterSignature() throws Exception {
        byte[] content = AsnIO.dosyadanOKU("C:\\a\\vod.p7s");
        BaseSignedData baseSignedData = new BaseSignedData(content);
        BaseSigner vodafoneSigner = getVodafoneSigner();

        HashMap<String, Object> params = new HashMap<>();

        // In real system, validate certificate by giving parameter "true" instead of "false"
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        //add first level two signatures.
        baseSignedData.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, null, vodafoneSigner, null, params);

        byte[] sign = baseSignedData.getEncoded();

        FileUtil.writeBytes("C:\\a\\counter.p7s", sign);
    }

    private BaseSigner getVodafoneSigner() {
        MSSParams mobilParams = new MSSParams("tbtkbilgem", "tbtkb1lg3m", "mobilimza.vodafone.com.tr");
        mobilParams.setMsspSignatureQueryUrl("https://mobilimza.vodafone.com.tr:443/Dianta2/MSS_SignatureService");
        mobilParams.setMsspRequestTimeout(5000);
        mobilParams.setConnectionTimeoutMs(30000);

        PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator("5411896685", Operator.VODAFONE);
        EMobileSignerConnector emsspClientConnector = new EMobileSignerConnector(mobilParams);
        // get signer certificate necessary field for signing from operator
        emsspClientConnector.setCertificateInitials(phoneNumberAndOperator);

        MobileSigner signer = new MobileSigner(emsspClientConnector, phoneNumberAndOperator, null, "rapor", SignatureAlg.RSA_SHA256.getName(), null);

        signer.getFingerPrintInfo().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object fingerPrintObj) {
                String fingerPrint = (String) fingerPrintObj;
                System.out.println("MA3 API imza fingerprint: " + fingerPrint);
            }
        });


        return signer;
    }
}
