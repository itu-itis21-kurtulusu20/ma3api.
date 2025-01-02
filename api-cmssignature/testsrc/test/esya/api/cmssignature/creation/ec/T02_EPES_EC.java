package test.esya.api.cmssignature.creation.ec;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.*;

/**
 * Created by sura.emanet on 18.12.2019.
 */

@RunWith(Parameterized.class)
public class T02_EPES_EC extends CMSSignatureTest {

    private SignatureAlg signatureAlg;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{SignatureAlg.ECDSA_SHA384}});
    }

    public T02_EPES_EC(SignatureAlg signatureAlg) { this.signatureAlg = signatureAlg; }

    @Test
    public void testCreateP1() throws Exception
    {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
        optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

        bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate() , getECSignerInterface(signatureAlg), optionalAttributes, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
    }
}
