package dev.esya.api.cmssignature.plugtest2010.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

@RunWith (value = Parameterized.class)
public class EPES extends CMSSignatureTest
{
	public String COMPANY;
	
	private String SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-EPES.SCOK\\";
	private String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-EPES.SCOK\\TU\\";
	
	private final String SIGNATURE_POLICY_PATH = OUTPUT_DIR + "TARGET-SIGPOL-ETSI3.der";
	private final int[] SIGNATURE_POLICY_OID = new int[] {1,2,3,4,5,1}; 
	
	
	public String INPUT_PATH;

	private String mCompany;
	
		
	public EPES(String company) 
	{	
		System.out.println(company);
		mCompany = company;
		
		INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
	}
	
	@Parameters
	public static Collection<Object[]> data()
	{
		//Object [][]data = new Object [][]{{"ASC"}};
		//Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
		
		//									0		1		2		3		4		5		6		7		   8		9		10		11		12		13		
		//Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
		Object [][] data = new Object[][]{{"TU"}};
		return Arrays.asList(data);
	}
	
	private SignedDataValidationResult testVerifingEPES(String file) throws Exception
	{
		byte[] input = null;
		try
		{
			input = AsnIO.dosyadanOKU(INPUT_PATH+file);
		}
		catch(Exception ex)
		{
			input = null;
		}
			
		//org.junit.Assume.assumeNotNull(input);		


		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		
		
		byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
		params.put(EParameters.P_POLICY_VALUE, policyValue);
		params.put(EParameters.P_POLICY_ID, SIGNATURE_POLICY_OID);
		params.put(EParameters.P_POLICY_DIGEST_ALGORITHM, DigestAlg.SHA1);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		String xmlFile = file.replaceAll("\\.p7s", ".xml");
		TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingEPES1()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPES-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingEPES2()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingEPES("Signature-C-EPES-2.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
