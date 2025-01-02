package dev.esya.api.cmssignature.plugtest2010.negative;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

public class EPES_N extends CMSSignatureTest
{
	private final String INPUT_PATH =  getDirectory() + "CAdES_InitialPackag\\CAdES-EPESN.SCOK\\ETSI\\";
	private String SIGNATURE_POLICY_PATH = getDirectory() + "CAdES_InitialPackage\\data\\TARGET-SIGPOL-ETSI3.der";
	private final int[] SIGNATURE_POLICY_OID = new int[] {1,2,3,4,5,1}; 
	
	
	private SignedDataValidationResult testVerifingEPES(String file) throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+file);

		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
		params.put(EParameters.P_POLICY_VALUE, policyValue);
		params.put(EParameters.P_POLICY_ID, SIGNATURE_POLICY_OID);
		params.put(EParameters.P_POLICY_DIGEST_ALGORITHM, DigestAlg.SHA1);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	public void testEPESN_1() throws Exception
	{
		System.out.println("----------------------testNegativeEPES1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPESN-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	public void testEPESN_2() throws Exception
	{
		System.out.println("----------------------testNegativeEPES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPESN-2.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}

}
