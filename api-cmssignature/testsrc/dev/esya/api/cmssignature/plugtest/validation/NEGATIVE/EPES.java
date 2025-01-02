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

public class EPES extends TestCase
{
	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//epes//";
	private static ValidationPolicy POLICY_FILE = null;
	private static final String SIGNATURE_POLICY_FILE = INPUT_DIR+ "TARGET-SIGPOL-ETSI3.der";
	
	
static{
		try {
			POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyEPES.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
}	

	/**
	 * This is a negative test case for checking SignaturePolicyIdentifier. 
	 * This test data has an SignaturePolicyIdentifier attribute with explicit 
	 * SignaturePolicyId however its valid of SignaturePolicyId.sigPolicyHash field does 
	 * *NOT* match to the hash value of signer policy file:TARGET-SIGPOL-ETSI3.der
	 * 
	 * 
	 * 0707070707070707070707070707070707070707 in SignaturePolicyIdentifier attribute (*WRONG HASH*)
	 * d5e01820d303705081085410c6ef25ee93dce930 calculated by SignaturePolicy.signPolicyInfo properly
	 */
	public void testVerifingEPES1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-EPESN-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		params.put(EParameters.P_POLICY_VALUE, signaturePolicyFile);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	
	/**
	 * Since we dont have signature policy file,we cannot run this one//TODO
	 * 
	 */
	public void testVerifingEPES2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-EPESN-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		params.put(EParameters.P_POLICY_VALUE, signaturePolicyFile);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	
	}
	

}
