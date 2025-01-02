package dev.esya.api.cmssignature.plugtest.validation.ENT;

import dev.esya.api.cmssignature.plugtest.validation.CMSValidationTest;
import junit.framework.TestCase;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

public class BES extends TestCase
{
	private static String INPUT_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH + "entrust//bes//";
	static  ValidationPolicy POLICY_FILE;
	
	static
	{
		try 
		{
			 POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_PATH + "policyBES.xml"));
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ESYAException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void testVerifingBES1()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES1-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-BES-1.p7s");

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	
	public void testVerifingBES2()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES2-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-BES-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingBES3()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES3-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-BES-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	public void testVerifingBES4()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-BES-4.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
		//POLICY_FILE.close();
	}
	
	public void testVerifingBES11()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES11-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-BES-11.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
		//POLICY_FILE.close();
		
	}
	
	public void testVerifingBES15()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES15-----------------------");
		CMSSignatureTest testConstants= new CMSSignatureTest();
		String ma3Bes = testConstants.getDirectory() +"creation\\plugtests\\bes\\";
		
		ValidationPolicy ma3POLICY_FILE = testConstants.getPolicy();
		
		byte[] input = AsnIO.dosyadanOKU(ma3Bes+"Signature-C-BES-15.p7s");

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, ma3POLICY_FILE);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
