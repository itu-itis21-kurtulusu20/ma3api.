package test.esya.api.cmssignature.creation.ec;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by sura.emanet on 13.12.2019.
 */

@RunWith(Parameterized.class)
public class T01_BES_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T01_BES_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Test
    public void createSign()throws Exception
    {
        //PropertyConfigurator.configure("C:\\GitRepo\\ma3api\\api-cmssignature\\log4j.properties");
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void createParallelSignature() throws Exception
    {
        //first signature
        BaseSignedData first_bs = new BaseSignedData();
        first_bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        first_bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);
        byte [] signatureBytes = first_bs.getEncoded();

        //second signature
        BaseSignedData second_bs = new BaseSignedData(signatureBytes);
        second_bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(second_bs.getEncoded(), null);
    }

    @Test
    public void createSerialSignature() throws Exception
    {
        //first signature
        BaseSignedData first_bs = new BaseSignedData();
        first_bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        first_bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);
        byte [] signatureBytes = first_bs.getEncoded();

        //second signature
        BaseSignedData second_bs = new BaseSignedData(signatureBytes);
        second_bs.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(second_bs.getEncoded(), null);
    }

    @Test
    public void createDetachedSignature() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), false);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);
        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
    }
}
