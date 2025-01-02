package dev.esya.api.cmssignature.plugtest2010.negative;

import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

public class ESX_N extends CMSSignatureTest
{
	private String INPUT_PATH =  getDirectory() + "CAdES_InitialPackage\\CAdES-XN.SCOK\\ETSI\\";

	
	private SignedDataValidationResult testVerifingESX(String file) throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+file);

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingESX1()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-1.p7s");
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESX2()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-2.p7s");
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESX3()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESX("Signature-C-XN-3.p7s");
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
}
