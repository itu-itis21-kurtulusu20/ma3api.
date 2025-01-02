package test.esya.api.cmssignature.structural;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Assert;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;

public class ContentTest extends CMSSignatureTest {

    @Test
    public void testAddTwoInternal() throws Exception {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        try{
            bs.addContent(getSimpleContent());
            throw new RuntimeException("AddContent must throw Exception in the second time!");
        }
        catch (CMSSignatureException Ex){
            Assert.assertTrue("Second AddContent should throw CMSSignatureException", true);
        }
    }

    @Test
    public void testAddTwoExternal() throws CMSSignatureException {
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), false);

        try{
            bs.addContent(getSimpleContent(), false);
            throw new RuntimeException("AddContent must throw Exception in the second time!");
        }
        catch (CMSSignatureException Ex){
            Assert.assertTrue("Second AddContent should throw CMSSignatureException", true);
        }
    }

    @Test
    public void testAddOneInternalOneExternal() throws CMSSignatureException{
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        try{
            bs.addContent(getSimpleContent(), false);
            throw new RuntimeException("AddContent must throw Exception in the second time!");
        }
        catch (CMSSignatureException Ex){
            Assert.assertTrue("Second AddContent should throw CMSSignatureException", true);
        }
    }

    @Test
    public void testAddOneExternalOneInternal() throws CMSSignatureException{
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent(), false);

        try{
            bs.addContent(getSimpleContent());
            throw new RuntimeException("AddContent must throw Exception in the second time!");
        }
        catch (CMSSignatureException Ex){
            Assert.assertTrue("Second AddContent should throw CMSSignatureException", true);
        }
    }

    @Test
    public void testValidateOneExternalOneInternal() throws Exception{
        BaseSignedData bs = new BaseSignedData();
        bs.addContent(getSimpleContent());

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256, null), null, params);


        ISignable content = new SignableByteArray("test".getBytes());

        try {
            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), content);
            throw new RuntimeException("Validating internal content & external content together should throw CMSSignatureException");
        }catch (CMSSignatureException ex){
            Assert.assertTrue("Validating internal content & external content together should throw CMSSignatureException", true);
        }
    }
}
