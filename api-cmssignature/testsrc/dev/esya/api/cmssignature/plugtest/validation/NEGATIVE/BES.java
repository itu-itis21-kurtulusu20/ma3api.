package dev.esya.api.cmssignature.plugtest.validation.NEGATIVE;

import dev.esya.api.cmssignature.plugtest.validation.CMSValidationTest;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.List;

/**
 * negative test cases in plugtests
 * @author aslihan.kubilay
 *
 */
public class BES extends TestCase 
{
	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//bes//";
	private static ValidationPolicy POLICY_FILE = null;

	static{
		try {
			POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyBES.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * This is a negative test case for checking ESSSigningCertificate.
	 * This test data has an ESSSigningCertificate attribute however its valid of certHash field 
	 * does *NOT* match to the hash value of signer certificate.
	 *
	 */
	public void testVerifingBES4()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");

		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-BESN-4.p7s");

		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);

		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);

		sdvr.printDetails();

		List<SignatureValidationResult> results = sdvr.getSDValidationResults();
		SignatureValidationResult result1 = results.get(0);

		List<CheckerResult> checkResults = result1.getCheckerResults();

		for (CheckerResult checkerResult : checkResults) 
		{
			if(checkerResult.getCheckerName().equals("Signing Certificate Attribute Checker"))
				assertEquals(CheckerResult_Status.UNSUCCESS, checkerResult.getResultStatus());
		}

	}


	/**
	 * This is a negative test case for checking ESSSigningCertificateV2.
	 * This test data has an ESSSigningCertificateV2 attribute with SHA-512 however its valid of 
	 * certHash field does *NOT* match to the hash value of signer certificate.
	 * 
	 */
	public void testVerifingBES5()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");

		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-BESN-5.p7s");

		Hashtable<String, Object> params = new Hashtable<String, Object>();

		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);

		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);

		sdvr.printDetails();

		List<SignatureValidationResult> results = sdvr.getSDValidationResults();
		SignatureValidationResult result1 = results.get(0);

		List<CheckerResult> checkResults = result1.getCheckerResults();

		for (CheckerResult checkerResult : checkResults) 
		{
			if(checkerResult.getCheckerName().equals("Signing Certificate Attribute Checker"))
				assertEquals(CheckerResult_Status.UNSUCCESS, checkerResult.getResultStatus());
		}
	}
}
