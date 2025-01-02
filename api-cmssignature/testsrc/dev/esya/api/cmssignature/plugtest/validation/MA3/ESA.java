package dev.esya.api.cmssignature.plugtest.validation.MA3;

import dev.esya.api.cmssignature.plugtest.validation.CMSValidationTest;
import junit.framework.TestCase;
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

public class ESA extends TestCase{

	private static final String INPUT_DIR = "T://MA3//api-cmssignature//testdata//cmssignature//imza//convertion//plugtests//esa//";
	private static ValidationPolicy POLICY_FILE = null;
	
	public ESA() throws FileNotFoundException, ESYAException 
	{
		POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(CMSValidationTest.INPUT_DIRECTORY_PATH+"ma3//esa//policyArchive.xml"));
	}
	
		
	public void testVerifingESA1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA3()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA4()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-4.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA5()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-5.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA6()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-6.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA7()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-7.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA8()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-8.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
	public static void testVerifingESA9()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-9.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
			System.out.println("SignedData is verified");
		else
		{
			System.out.println("SignedData is not verified");
			sdvr.printDetails();
		}
		
	}
	
}
