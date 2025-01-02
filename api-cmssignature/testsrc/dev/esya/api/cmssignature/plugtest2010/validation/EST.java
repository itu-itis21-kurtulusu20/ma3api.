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
public class EST extends CMSSignatureTest
{
public static String COMPANY;
	
	private String SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-T.SCOK\\";
	private String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-T.SCOK\\TU\\";
	

	public String INPUT_PATH;

	private String mCompany;
	
	public EST(String company) 
	{	
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
	
	private SignedDataValidationResult testVerifingEST(String file) throws Exception
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
		TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingEST1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		SignedDataValidationResult sdvr = testVerifingEST("Signature-C-T-1.p7s");
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
}
