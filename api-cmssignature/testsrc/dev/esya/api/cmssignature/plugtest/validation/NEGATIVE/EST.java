package dev.esya.api.cmssignature.plugtest.validation.NEGATIVE;

import dev.esya.api.cmssignature.plugtest.validation.CMSValidationTest;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.util.Hashtable;

/**
 * negative test cases in plugtests
 * 
 * @author aslihan.kubilay
 *
 */
public class EST extends TestCase 
{
	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//est//";
	private static ValidationPolicy POLICY_FILE = null;
	
	
	static{
			try {
				POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyEST.xml"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	/**
	 * This is a negative test case for verifying signer certificate at the time in SignatureTimeStamp.
	 * At the time in SignatureTimeStamp, the signer certificate has been already expired.
	 * 
	 * ETSI Invalid-Cert Expired
	 *  26-Oct-2008 10:29Z - signer certificate expired
	 *  26-Oct 2008 11:00Z - SignatureTimeStamp
	 *  
	 *  
	 * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 * 
	 */
	public void testVerifyingEST1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-TN-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	
	/**
	 * This is a negative test case for verifying signer certificate at the time in SignatureTimeStamp.
	 * At the time in SignatureTimeStamp, the signer certificate has been already revoked.
	 *
	 *
	 *  ETSI Invalid-Cert Revoked SN:51ABEDC95B57
	 *  25-Oct-2008 10:15Z - signer certificate revoked SN:51ABEDC95B57
     *  26-Oct 2008 00:00Z - SignatureTimeStamp
     *  
     *  
     * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 */
	public void testVerifyEST2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-TN-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	
	/**
	 * This is a negative test case for SignatureTimeStamp.
	 * The hash value of messageImprint in SignatureTimeStamp does *NOT* match to the hash value of corresponding signature value of signerInfo.
	 * 
	 * 
	 * 
	 * Since we dont have a valid crl for the time in signaturetimestamp, the certificate validation
	 * will fail also //TODO
	 */
	public void verifyEST3()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-TN-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
