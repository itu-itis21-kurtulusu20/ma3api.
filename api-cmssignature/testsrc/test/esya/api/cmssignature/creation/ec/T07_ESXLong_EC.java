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
 * Created by sura.emanet on 27.12.2019.
 */

@RunWith(Parameterized.class)
public class T07_ESXLong_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T07_ESXLong_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Test
    //create signeddata with one esxlong signature
    public void testCreateESXLong() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();

        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();

        //necassary for getting signaturetimestamp
        params.put(EParameters.P_TSS_INFO, getTSSettings());

        //necessary for validation of signer certificate according to time in signaturetimestamp attribute
        //while validation,references and values are also gathered
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        //add signer
        bs.addSigner(ESignatureType.TYPE_ESXLong, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void testCreateSerial() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();

        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();

        //necassary for getting signaturetimestamp
        params.put(EParameters.P_TSS_INFO, getTSSettings());

        //necessary for validation of signer certificate according to time in signaturetimestamp attribute
        //while validation,references and values are also gathered
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        //add signer
        bs.addSigner(ESignatureType.TYPE_ESXLong, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());

        params.put(EParameters.P_EXTERNAL_CONTENT, getSimpleContent());

        bs2.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_ESXLong, getSecondSignerCertificate(), getSecondSignerInterface(SignatureAlg.RSA_SHA256), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }
}
