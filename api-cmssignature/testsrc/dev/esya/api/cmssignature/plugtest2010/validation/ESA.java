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
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;

@RunWith (value = Parameterized.class)
public class ESA extends CMSSignatureTest
{
	private String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-A.SCOK\\TU\\";

	public String mCompany;
	
	private String SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-A.SCOK\\";

	public String INPUT_PATH;
	
	
	
	
	
	public ESA(String company) 
	{	
		System.out.println(company);
		mCompany = company;
		INPUT_PATH = SigTYPE_INPUT_PATH + company + "//";
	}
	
	@Parameters
	public static Collection<Object[]> data()
	{
		//Object [] [] data = new Object [][]{{"MSEC"}};
		//Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"}};
		//Object [][]data =  {{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
		//Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
		
		Object [][] data = new Object[][]{{"TU"}};
		return Arrays.asList(data);
	}
	
	private SignedDataValidationResult testVerifingESA(String file) throws Exception
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
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyNoOCSPNoCRL());
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		String xmlFile = file.replaceAll("\\.p7s", ".xml");
		TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingESA1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESA2()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-2.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	@Test
	public void testVerifingESA3()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-3.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESA4()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-4.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	@Test
	public void testVerifingESA5()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-5.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESA6()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-6.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	@Test
	public void testVerifingESA7()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-7.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESA8()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA8-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-8.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	@Test
	public void testVerifingESA9()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA9-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingESA("Signature-C-A-9.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
