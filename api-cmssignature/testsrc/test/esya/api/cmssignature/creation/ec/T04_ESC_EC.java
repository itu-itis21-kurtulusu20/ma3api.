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
 * Created by sura.emanet on 19.12.2019.
 */

@RunWith(Parameterized.class)
public class T04_ESC_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T04_ESC_EC(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Test
    public void testCreateESC() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        //by default,qc statement is checked. In order to use test certificates,set this parameter
        //add signer
        bs.addSigner(ESignatureType.TYPE_ESC, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }
}
