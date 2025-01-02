package test.esya.api.cmssignature;

import test.esya.api.cmssignature.testconstants.TestConstants;
import test.esya.api.cmssignature.testconstants.UGTestConstants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class CMSSignatureTest
{
	public TestConstants msTestConstants = new UGTestConstants();
	
	public BaseSigner getSignerInterface(SignatureAlg aAlg) throws Exception
	{
		return msTestConstants.getSignerInterface(aAlg);
	}

	public BaseSigner getSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams) throws Exception
	{
		return msTestConstants.getSignerInterface(aAlg, algorithmParams);
	}

	public BaseSigner getECSignerInterface(SignatureAlg aAlg) throws Exception
	{
		return msTestConstants.getECSignerInterface(aAlg);
	}
	
	public BaseSigner getSecondSignerInterface(SignatureAlg aAlg) throws Exception
	{
		return msTestConstants.getSecondSignerInterface(aAlg);
	}

	public BaseSigner getSecondSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams) throws Exception
	{
		return msTestConstants.getSecondSignerInterface(aAlg, algorithmParams);
	}
	
	public ECertificate getSignerCertificate() throws Exception
	{
		return msTestConstants.getSignerCertificate();
	}
	
	public ECertificate getSecondSignerCertificate() throws Exception
	{
		return msTestConstants.getSecondSignerCertificate();
	}

	public ECertificate getECSignerCertificate() throws Exception
	{
		return msTestConstants.getECSignerCertificate();
	}
	
	public ValidationPolicy getPolicy() throws ESYAException, FileNotFoundException
	{
		return msTestConstants.getPolicy();
	}
	
	public ValidationPolicy getPolicyCRL() throws ESYAException, FileNotFoundException
	{
		return msTestConstants.getPolicyCRL();
	}
	
	public ValidationPolicy getPolicyOCSP() throws ESYAException, FileNotFoundException
	{
		return msTestConstants.getPolicyOCSP();
	}
	
	public ValidationPolicy getPolicyNoOCSPNoCRL() throws ESYAException, FileNotFoundException
	{
		return msTestConstants.getPolicyNoOCSPNoCRL();
	}
	
	public TSSettings getTSSettings()
	{
		return msTestConstants.getTSSettings();
	}
	
	public String getDirectory()
	{
		return msTestConstants.getDirectory();
	}
	
	public ISignable getSimpleContent()
	{
		return msTestConstants.getSimpleContent();
	}
	
	public ISignable getHugeContent()
	{
		return msTestConstants.getHugeContent();
	}

	public byte [] createBasicCMSSignature() throws Exception{
		BaseSignedData bs = new BaseSignedData();
		bs.addContent(getSimpleContent());

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(),
				getSignerInterface(SignatureAlg.RSA_SHA256), null, params);

		return bs.getEncoded();
	}

}