package test.esya.api.cmssignature.creation;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;

public class T06_ESX2 extends CMSSignatureTest
{
	//create signeddata with one esx2 signature
	@Test
	public void testCreateESX2()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		bs.addContent(getSimpleContent());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necassary for getting signaturetimestamp and reference timestamp
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		
		//necessary for validation of signer certificate according to time in signaturetimestamp attribute
		//while validation,references are also gathered
		params.put(EParameters.P_CERT_VALIDATION_POLICY,getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESX_Type2, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}
	
}
