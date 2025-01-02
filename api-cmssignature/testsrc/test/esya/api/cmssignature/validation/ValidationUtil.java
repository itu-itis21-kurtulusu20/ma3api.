package test.esya.api.cmssignature.validation;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.FileNotFoundException;
import java.util.Hashtable;

public class ValidationUtil
{
	public static void checkSignatureIsValid(byte [] signature, ISignable externalContent)throws Exception {

		SignedDataValidationResult sdvr = validate(signature, externalContent);

		if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
			throw new Exception(sdvr.toString());
	}

	public static SignedDataValidationResult validate(byte [] aSignature, ISignable aExternalContent) throws FileNotFoundException, ESYAException, CMSSignatureException
	{
		CMSSignatureTest testContants = new CMSSignatureTest();
		ValidationPolicy policy = testContants.getPolicy();

		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		if(aExternalContent != null)
			params.put(EParameters.P_EXTERNAL_CONTENT, aExternalContent);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(aSignature, params);
		
		if(sdvr.getSDStatus() !=  SignedData_Status.ALL_VALID)
			System.out.println("İmza doğrulamadı");
		
		return sdvr;
	}
	
	
}
