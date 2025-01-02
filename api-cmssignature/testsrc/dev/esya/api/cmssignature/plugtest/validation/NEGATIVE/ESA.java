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

/**
 * negative test cases in plugtests
 * @author aslihan.kubilay
 *
 */
public class ESA extends TestCase
{
	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//esa//";
	private static ValidationPolicy POLICY_FILE = null;
	
	public ESA() throws FileNotFoundException, ESYAException 
	{
		POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream( INPUT_DIR+"policyArchive.xml"));
	}		
	
	
	/**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than 
	 * the time in ArchiveTimeStamp.
	 * 
	 * 	26-Oct-2008 02:00Z - SignatureTimeStamp (*)
	 *	26-Oct 2008 00:00Z - TimestampedCertsCRLs
	 *	26-Oct 2008 01:00Z - ArchiveTimeStamp
	 * 
	 */
	public void testVerifyingESA1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-AN-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
						
	}
	
	
	/**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the TimestampedCertsCRLs is ulterior than the time in ArchiveTimeStamp.
	 * 
	 * 26-Oct-2008 00:00Z - SignatureTimeStamp
	 * 26-Oct 2008 02:00Z - TimestampedCertsCRLs (*)
	 * 26-Oct 2008 01:00Z - ArchiveTimeStamp
	 */
	public void testVerifyingESA2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-AN-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
			
	}
	
	/**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the ESCTimeStamp is ulterior than the time in ArchiveTimeStamp.
	 * 
	 * 26-Oct-2008 00:00Z - SignatureTimeStamp
	 * 26-Oct 2008 02:00Z - ESCTimeStamp (*)
	 * 26-Oct 2008 01:00Z - ArchiveTimeStamp
	 * 
	 */
	public void testVerifyingESA3()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-AN-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	

}
