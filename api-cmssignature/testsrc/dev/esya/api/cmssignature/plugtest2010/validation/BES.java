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
public class BES extends CMSSignatureTest
{
	public static String COMPANY;
	
	private String SigTYPE_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-BES.SCOK\\";
	private String OUTPUT_PATH;
	
	public String INPUT_PATH;
	public String mCompany;
	
	public BES(String company) 
	{	
		mCompany = company;
		OUTPUT_PATH = SigTYPE_PATH + "TU\\";
		INPUT_PATH = SigTYPE_PATH + mCompany + "\\";
	}
	
	@Parameters
	public static Collection<Object[]> data()
	{
		//Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
		
		
		//Object [][]data = new Object [][]{{"UPC"}};
		
		//									0		1		2		3		4		5		6		7		   8		9		10		11		12		13		
		//Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
		
		Object [][] data = new Object[][]{{"TU"}};
		return Arrays.asList(data);
	}
	
	private SignedDataValidationResult testVerifingBES(String file) throws Exception
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
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		String xmlFile = file.replaceAll("\\.p7s", ".xml");
		TestConstants.writeXml(sdvr,  OUTPUT_PATH + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingBES1()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingBES2()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-2.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	@Test
	public void testVerifingBES3()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES3-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-3.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingBES4()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-4.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	/*@Test
	public void testVerifingBES5()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES2-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-5.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}*/
	
	@Test
	public void testVerifingBES6()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES3-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-6.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingBES7()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-7.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	@Test
	public void testVerifingBES8()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-8.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	@Test
	public void testVerifingBES10()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-10.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	
	@Test
	public void testVerifingBES11()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES11-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-11.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	@Test
	public void testVerifingBES15()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-15.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	@Test
	public void testVerifingBES16()
	throws Exception
	{
		System.out.println("----------------------testVerifingBES4-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingBES("Signature-C-BES-16.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
}
