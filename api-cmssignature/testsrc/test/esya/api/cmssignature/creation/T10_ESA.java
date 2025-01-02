package test.esya.api.cmssignature.creation;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;

public class T10_ESA extends CMSSignatureTest
{
	//create archive signature
	@Test
	public void testCreateESA()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_TSS_INFO, getTSSettings());
        params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_ESA, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

	@Test
	public void testDetachedESA()
			throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent(), false);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_ESA, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
	}

}
