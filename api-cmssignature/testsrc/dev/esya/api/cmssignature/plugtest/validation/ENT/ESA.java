package dev.esya.api.cmssignature.plugtest.validation.ENT;


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

/**
 * Since archive signatures are verified, all validation data necessary for signature
 * verification must be in signeddata. So in policy file,no certificate,crl or ocsp finder
 * is specified
 * 
 * @author aslihan.kubilay
 *
 */
public class ESA extends TestCase{

	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"entrust//esa//";
	public ValidationPolicy POLICY_FILE;
	
	public ESA() throws FileNotFoundException, ESYAException 
	{
		POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyArchive.xml"));
		//BasicConfigurator.configure();
	}
	
	
	
	public void testVerifingESA1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		//System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA2()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA2-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	public void testVerifingESA3()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA3-----------------------");
		
		//BasicConfigurator.configure();
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA4()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA4-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-4.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA5()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA5-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-5.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	

	
	public void testVerifingESA6()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA6-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-6.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA7()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA7-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-7.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA8()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA8-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-8.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	public void testVerifingESA9()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA9-----------------------");
		
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-A-9.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	
	
}
