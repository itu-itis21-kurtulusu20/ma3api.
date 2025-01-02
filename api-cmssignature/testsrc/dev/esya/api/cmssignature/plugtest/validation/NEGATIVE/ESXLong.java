package dev.esya.api.cmssignature.plugtest.validation.NEGATIVE;

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

public class ESXLong extends TestCase
{
	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//esxlong//";
	private static ValidationPolicy POLICY_FILE = null;
	
	
	public ESXLong() throws FileNotFoundException, ESYAException {
		POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyESXLong.xml"));
	}
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
	 * In this test case, The crlIdentifier.issuedTime field does *NOT* match to the thisUpdate of 
	 * corresponding CRL in the RevocationValues.
	 * 
	 */
	public void testVerifyingESXLong1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues.
	 * In this test case, The crlIdentifier.crlNumber field does *NOT* match to the crlNumber extension of 
	 * corresponding CRL in the RevocationValues.
	 * 
	 */
	public void testVerifyingESXLong2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
	 * In this test case, The crlHash field does *NOT* match to the hash value of corresponding CRL in the RevocationValues.
	 * @throws Exception
	 */
	public void testVerifyingESXLong3()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
	 * In this test case, The ocspIdentifier.responderID field does *NOT* match to the responderID field of 
	 * corresponding BasicOCSPResponse in the RevocationValues.
	 * 
	 */
	public void testVerifyingESXLong4()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-4.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
	 * In this test case, The ocspIdentifier.producedAt field does *NOT* match to the producedAt field of corresponding 
	 * BasicOCSPResponse in the RevocationValues.
	 * 
	 */
	public void testVerifyingESXLong5()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-5.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	/**
	 * This is a negative test case for matching between CompleteRevocationRefs and RevocationValues. 
	 * In this test case, The ocspRepHash field does *NOT* match to the hash value of corresponding BasicOCSPResponse in the RevocationValues.
	 * 
	 */
	public void testVerifyingESXLong6()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XLN-6.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}

}
