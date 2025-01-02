package test.esya.api.cmssignature.creation;

import bundle.esya.api.cmssignature.validation.ValidationUtil;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import java.util.HashMap;

public class T07_ESXLong extends CMSSignatureTest
{
	//create signeddata with one esxlong signature
	@Test
	public void testCreateESXLong()
	throws Exception
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
		bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);

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
		bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);
		
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		
		params.put(EParameters.P_EXTERNAL_CONTENT, getSimpleContent());
		
		bs2.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_ESXLong, getSecondSignerCertificate(), getSecondSignerInterface(SignatureAlg.RSA_SHA1), null, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
	}

	@Test
	public void createSignatureInTwoSteps() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent(), false);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_TSS_INFO, getTSSettings());

		byte [] dtbs = bs.initAddingSigner(ESignatureType.TYPE_BES, getSignerCertificate(), SignatureAlg.RSA_SHA256, null, null, params);
		byte[] bsdBytes = bs.getEncoded();

		BaseSigner signer = getSignerInterface(SignatureAlg.RSA_SHA256, null);
		byte[] signature = signer.sign(dtbs);

		finishSigning(bsdBytes, signature, params);
	}

	private void finishSigning(byte[] bsdBytes, byte [] signature, HashMap<String, Object> params) throws Exception
	{
		BaseSignedData bs = new BaseSignedData(bsdBytes);
		bs.finishAddingSigner(signature);
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESXLong, params);

		ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
	}
}
