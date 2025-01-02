package dev.esya.api.cmssignature.plugtest2010.negative;

import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponseBytes;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ESXLong_N extends CMSSignatureTest
{
	private String INPUT_PATH = getDirectory() + "CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\";
	
	public static List<ECRL> crltest1 = new ArrayList<ECRL>();
	public static List<ECRL> crltest2 = new ArrayList<ECRL>();
	public static List<ECRL> crltest3 = new ArrayList<ECRL>();
	
	public static List<EOCSPResponse> ocsptest4 = new ArrayList<EOCSPResponse>();
	public static List<EOCSPResponse> ocsptest5 = new ArrayList<EOCSPResponse>();
	public static List<EOCSPResponse> ocsptest6 = new ArrayList<EOCSPResponse>();
	
	static 
	{
		try
		{
			ECRL crlA1 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-LevelACAOK.crl"));
			ECRL crlA2 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-LevelACAOK.crl"));
			ECRL crlA3 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-LevelACAOK.crl"));
			
		
			ECRL crlB1 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-LevelBCAOK.crl"));
			ECRL crlB2 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-LevelBCAOK.crl"));
			ECRL crlB3 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-LevelBCAOK.crl"));
			
			ECRL crlRoot1 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-1.CERT-SIG-RootCAOK.crl"));
			ECRL crlRoot2 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-2.CERT-SIG-RootCAOK.crl"));
			ECRL crlRoot3 =  new ECRL(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-3.CERT-SIG-RootCAOK.crl"));
			
			
			crltest1.add(crlA1);
			crltest1.add(crlB1);
			crltest1.add(crlRoot1);
			
			crltest2.add(crlA2);
			crltest2.add(crlB2);
			crltest2.add(crlRoot2);
			
			crltest3.add(crlA3);
			crltest3.add(crlB3);
			crltest3.add(crlRoot3);
			
			EBasicOCSPResponse ocsp4 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-EE.bor.der"));
			EBasicOCSPResponse ocsp5 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-EE.bor.der"));
			EBasicOCSPResponse ocsp6 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-EE.bor.der"));
			
			EBasicOCSPResponse ocspA4 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-LevelACAOK.bor.der"));
			EBasicOCSPResponse ocspA5 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-LevelACAOK.bor.der"));
			EBasicOCSPResponse ocspA6 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-LevelACAOK.bor.der"));
			
			EBasicOCSPResponse ocspB4 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-4.CERT-SIG-LevelBCAOK.bor.der"));
			EBasicOCSPResponse ocspB5 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-5.CERT-SIG-LevelBCAOK.bor.der"));
			EBasicOCSPResponse ocspB6 = new EBasicOCSPResponse(AsnIO.dosyadanOKU("D:\\imza\\CAdES_InitialPackage\\CAdES-XLN.SCOK\\ETSI\\Signature-C-XLN-6.CERT-SIG-LevelBCAOK.bor.der"));
			
			
			EOCSPResponse eocsp4 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocsp4.getEncoded())));
			EOCSPResponse eocsp5 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocsp5.getEncoded())));
			EOCSPResponse eocsp6 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocsp6.getEncoded())));
			
			EOCSPResponse eocspA4 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspA4.getEncoded())));
			EOCSPResponse eocspA5 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspA5.getEncoded())));
			EOCSPResponse eocspA6 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspA6.getEncoded())));
			
			EOCSPResponse eocspB4 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspB4.getEncoded())));
			EOCSPResponse eocspB5 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspB5.getEncoded())));
			EOCSPResponse eocspB6 = new EOCSPResponse(new OCSPResponse(OCSPResponseStatus.successful(), new ResponseBytes(new int[]{1,2},ocspB6.getEncoded())));
			
			
			ocsptest4.add(eocsp4);
			ocsptest4.add(eocspA4);
			ocsptest4.add(eocspB4);
			
			ocsptest5.add(eocsp5);
			ocsptest5.add(eocspA5);
			ocsptest5.add(eocspB5);
			
			ocsptest5.add(eocsp6);
			ocsptest5.add(eocspA6);
			ocsptest5.add(eocspB6);
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private SignedDataValidationResult testVerifingESXLong(String file, Hashtable<String, Object> params) throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_PATH+file);

		
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(input, params);
		
		System.out.println(sdvr.toString());
		
		return sdvr;
	}
	

	@Test
	public void testVerifingESXLong1()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX1-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_CRLS, crltest1);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-1.p7s", params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESXLong2()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX2-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_CRLS, crltest2);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-2.p7s",params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESXLong3()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX3-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_CRLS, crltest3);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-3.p7s", params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESXLong4()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX4-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_OCSP_RESPONSES, ocsptest4);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-4.p7s", params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESXLong5()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX5-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_OCSP_RESPONSES, ocsptest5);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-5.p7s",params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
	
	@Test
	public void testVerifingESXLong6()
	throws Exception
	{
		System.out.println("----------------------testNegativeESX6-----------------------");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		params.put(EParameters.P_INITIAL_OCSP_RESPONSES, ocsptest6);
		
		SignedDataValidationResult sdvr = testVerifingESXLong("Signature-C-XLN-6.p7s", params);
		
		assertEquals(SignedData_Status.NOT_ALL_VALID, sdvr.getSDStatus());
	}
}
