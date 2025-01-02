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

import java.util.Hashtable;

public class EPES extends TestCase 
{
	private static String INPUT_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH+"//entrust//epes//";
	private ValidationPolicy POLICY_FILE ;
	private static final String SIGNATURE_POLICY_FILE = INPUT_PATH+ "TARGET-SIGPOL-ETSI3.der";
	
	public EPES() throws ESYAException
	{
		POLICY_FILE = PolicyReader.readValidationPolicy(INPUT_PATH+"policyEPES.xml");
	}
	
	public void testVerifingEPES1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-EPES-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY,POLICY_FILE);
		params.put(EParameters.P_POLICY_VALUE, signaturePolicyFile);
		
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	
	public void testVerifingEPES2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+"Signature-C-EPES-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		byte[] signaturePolicyFile = AsnIO.dosyadanOKU(SIGNATURE_POLICY_FILE);
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY,POLICY_FILE);
		params.put(EParameters.P_POLICY_VALUE, signaturePolicyFile);
		
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
