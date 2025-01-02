package dev.esya.api.cmssignature.plugtest2010.validation;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.Signature_Status;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

public class TestConstants 
{
//	public static ValidationPolicy POLICY_FILE;
//
//	static
//	{
//		try 
//		{
//			Authenticator.setDefault(new EtsiTestAuthenticator()); 
//			
//			POLICY_FILE = PolicyReader.readValidationPolicy(CMSSignatureTest.INPUT_DIRECTORY_PATH+"validation//plugtests2010//policy.xml");
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//	}
//	
	public static void writeXml(SignedDataValidationResult sdvr, String file) throws Exception
	{
		
		if(sdvr.getSDStatus() == SignedData_Status.ALL_VALID)
		{
			String valid = "<gecerli>ACIKLAMA</gecerli>";
			valid.replaceAll("ACIKLAMA", sdvr.toString());
			AsnIO.dosyayaz(valid.getBytes(), file);
		}
		else
		{
			List<SignatureValidationResult> sdValidationResults = sdvr.getSDValidationResults();
			boolean allIncomplete = true;
			for (SignatureValidationResult signatureValidationResult : sdValidationResults) 
			{
				if(signatureValidationResult.getSignatureStatus() != Signature_Status.INCOMPLETE)
					allIncomplete = false;
			}
			if(allIncomplete == true)
			{
				String failed = "<eksik>ACIKLAMA</eksik>";
				failed.replaceAll("ACIKLAMA", sdvr.toString());
				AsnIO.dosyayaz(failed.getBytes(), file);
			}
			else
			{
				String failed = "<gecersiz>ACIKLAMA</gecersiz>";
				failed.replaceAll("ACIKLAMA", sdvr.toString());
				AsnIO.dosyayaz(failed.getBytes(), file);
			}
		}
	}
}
