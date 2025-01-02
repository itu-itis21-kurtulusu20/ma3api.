package test.esya.api.cmssignature.creation;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;


public class T04_ESC extends CMSSignatureTest
{
	//create signeddata with one esc signature
	@Test
	public void testCreateESC()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		bs.addContent(getSimpleContent());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necassary for getting signaturetimestamp
		params.put(EParameters.P_TSS_INFO, getTSSettings());

		
		//necessary for validation of signer certificate according to time in signaturetimestamp attribute
		//while validation,references are also gathered 
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		
		//by default,qc statement is checked. In order to use test certificates,set this parameter
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESC, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
		
	}

}
