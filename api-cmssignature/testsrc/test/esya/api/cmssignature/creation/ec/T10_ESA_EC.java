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
public class T10_ESA_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T10_ESA_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Test
    //create archive signature
    public void testCreateESA() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_ESA, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }

    @Test
    public void testDetachedESA() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), false);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_ESA, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
    }
}
