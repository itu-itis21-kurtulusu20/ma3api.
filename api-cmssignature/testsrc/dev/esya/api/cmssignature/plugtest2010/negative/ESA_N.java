package dev.esya.api.cmssignature.plugtest2010.negative;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

public class ESA_N extends CMSSignatureTest
{
	private String INPUT_PATH =  getDirectory() + "CAdES_InitialPackage//CAdES-AN.SCOK//ETSI//";
	
	private SignedDataValidationResult testVerifingESA(String file) throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+file);

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	public void testVerifingESA1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	public void testVerifingESA2()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	public void testVerifingESA3()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA3-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-AN-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
