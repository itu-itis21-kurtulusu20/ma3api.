package dev.esya.api.cmssignature.plugtest2010.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponseBytes;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith (value = Parameterized.class)
public class ESC extends CMSSignatureTest 
{
	private final String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-C.SCOK\\TU\\";
	
	//private static ValidationPolicy POLICY_FILE_NoCRL_RevocOCSP;
	//private static ValidationPolicy POLICY_FILE_NoCRL_NoOCSP;
	
	public static String COMPANY;
	
	private String SigTYPE_INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-C.SCOK\\";

	public String INPUT_PATH;
	private String mCompany;
	
	public ESC(String company) 
	{	
		System.out.println(company);
		mCompany = company;
		INPUT_PATH = SigTYPE_INPUT_PATH + company + "\\";
	}
	
	@Parameters
	public static Collection<Object[]> data()
	{
		//Object [][]data = new Object [][]{{"LUX"}};
		//Object [][]data = new Object [][]{{"TU"},{"ELD"},{"LUX"},{"ASC"},{"CRY"},{"DIC"}};
		//									0		1		2		3		4		5		6		7		   8		9		10		11		12		13
		//Object [][]data = new Object [][]{{"ASC"}, {"CER"},{"CRY"},{"DIC"},{"ELD"},{"ENI"},{"FED"},{"IAIK"},{"LUX"},{"MSEC"},{"PSPW"},{"SSC"},{"TU"},{"UPC"}};
		Object [][] data = new Object[][]{{"TU"}};
		return Arrays.asList(data);
	}
	
	
	private SignedDataValidationResult testVerifingESC(String file, Hashtable<String, Object> params) throws Exception
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

		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		String xmlFile = file.replaceAll("\\.p7s", ".xml");
		TestConstants.writeXml(sdvr, OUTPUT_DIR + "Verification_of_"+ mCompany + "_" + xmlFile);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	
	@Test
	public void testVerifingESC1()
	throws Exception
	{
		System.out.println("----------------------testVerifingESA1-----------------------");
		
		Hashtable<String, Object> params = getParams("",".crl",getPolicyNoOCSPNoCRL());
		
		SignedDataValidationResult sdvr = testVerifingESC("Signature-C-C-1.p7s",params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
	}
	

	@Test
	public void testVerifingESC2()
	throws Exception
	{
		System.out.println("----------------------testVerifingEPES2-----------------------");
		
		Hashtable<String, Object> params = getParams("C-C-2",".der", getPolicyOCSP());
		
		SignedDataValidationResult sdvr = testVerifingESC("Signature-C-C-2.p7s", params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	private Hashtable<String, Object> getParams(String searchStr,String fileType, ValidationPolicy policy) throws Exception 
	{
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
	
		ArrayList<String> reqFiles = new ArrayList<String>();
		File f = new File(INPUT_PATH);
		String [] fileNames = f.list();
		for (String file : fileNames) 
		{
			if(file.contains(searchStr) && file.contains(fileType))
				reqFiles.add(file);
		}
		
		if(fileType.contains("crl"))
		{
			List<ECRL> crlList = new ArrayList<ECRL>();
			for (String files : reqFiles) 
			{
				byte [] crlBytes = AsnIO.dosyadanOKU(INPUT_PATH + files);
				ECRL ecrl = new ECRL(crlBytes);
				crlList.add(ecrl);
			}
			params.put(EParameters.P_INITIAL_CRLS, crlList);
			
		}
		else if(fileType.contains("der"))
		{
			List<EOCSPResponse> ocsplist = new ArrayList<EOCSPResponse>();
			for (String files : reqFiles) 
			{
				byte [] ocspBytes = AsnIO.dosyadanOKU(INPUT_PATH + files);
				EBasicOCSPResponse eBasicOcsp = new EBasicOCSPResponse(ocspBytes);
				EOCSPResponse eocsp = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},eBasicOcsp.getEncoded())));
				ocsplist.add(eocsp);
			}
			params.put(EParameters.P_INITIAL_OCSP_RESPONSES, ocsplist);
		}
		return params;
	}
}
