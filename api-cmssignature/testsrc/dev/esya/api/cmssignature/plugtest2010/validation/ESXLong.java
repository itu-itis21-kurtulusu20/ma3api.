package dev.esya.api.cmssignature.plugtest2010.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

@RunWith (value = Parameterized.class)
public class ESXLong extends CMSSignatureTest
{
	private final String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage//CAdES-XL.SCOK//TU//";

	private String SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage//CAdES-XL.SCOK//";

	public String INPUT_PATH;

	private String mCompany;
	

	
	public ESXLong(String company) 
	{	
		System.out.println(company);
		mCompany = company;
		INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
	}
	
	@Parameters
	public static Collection<Object[]> data()
	{
								//		        0		1		2		3		4		   5		6		7		   8		9		10		11		12		13		
		//Object [][]data = new Object [][]{/*{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"}};*/ {"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
		//Object [][]data = new Object [][]{{"ELD"},{"TU"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
		Object [][] data = new Object[][]{{"TU"}};
		return Arrays.asList(data);
	}
	
	private SignedDataValidationResult testVerifingESXLong(String file, ValidationPolicy policy) throws Exception
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
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		String xmlFile = file.replaceAll("\\.p7s", ".xml");
		TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingESX1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESX1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-1.p7s",getPolicyNoOCSPNoCRL());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESX2()
	throws Exception
	{
		System.out.println("----------------------testVerifingESX2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-2.p7s",getPolicyNoOCSPNoCRL());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESX3()
	throws Exception
	{
		System.out.println("----------------------testVerifingESX3-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-3.p7s",getPolicyNoOCSPNoCRL());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESX4()
	throws Exception
	{
		System.out.println("----------------------testVerifingESX4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XL-4.p7s",getPolicyNoOCSPNoCRL());
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
