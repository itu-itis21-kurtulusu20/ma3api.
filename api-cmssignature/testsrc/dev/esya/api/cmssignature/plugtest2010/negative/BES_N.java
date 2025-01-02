package dev.esya.api.cmssignature.plugtest2010.negative;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

public class BES_N extends CMSSignatureTest 
{
	private final String INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-BESN.SCOK//ETSI//";

	private SignedDataValidationResult testVerifing(String file, ValidationPolicy policy) throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+file);

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	public void testBESN_4() throws Exception
	{
		System.out.println("----------------------testNegativeBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifing("Signature-C-BESN-4.p7s", getPolicy());
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	public void testBESN_5() throws Exception
	{
		System.out.println("----------------------testNegativeBES5-----------------------");
		
		SignedDataValidationResult sdvr = testVerifing("Signature-C-BESN-5.p7s", getPolicy());
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
}
