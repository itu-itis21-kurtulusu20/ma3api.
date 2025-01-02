package dev.esya.api.dist.manualExamples;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.util.HashMap;

public class ImzaTipleriArasindaDonusum 
{
	String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
	String BES_SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
	String CONVERTED_TO_EST_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\10.p7s";
	
	public void testImzaTipleriArasindaDonusum() throws Exception
	{
		byte[] inputBES = AsnIO.dosyadanOKU(BES_SIGNATURE_FILE);
		
		BaseSignedData sd = new BaseSignedData(inputBES);
				
				
		HashMap<String, Object> params = new HashMap< String, Object>();
				
		//necessary for getting signaturetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());

		ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
			
				
		//necessary for validating signer certificate according to time of //signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
				
		sd.getSignerList().get(0).convert(ESignatureType.TYPE_EST, params);
				
		AsnIO.dosyayaz(sd.getEncoded(),CONVERTED_TO_EST_FILE);
	}

	private Object getTSSettings() 
	{
		return TestConstants.getTSSettings();
	}
}
