package dev.esya.api.cmssignature.plugtest.convertion;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.HashMap;

public class ESC extends  CMSSignatureTest
{
	private final String INPUT_DIR = getDirectory() +"convertion\\plugtests\\est\\";
	private final String OUTPUT_DIR = getDirectory() +"convertion\\plugtests\\esc\\";
	
	
	/**
	 * ESC With just crl references(not ocsp)
	 * Possible input data: Signature-C-T-1.p7s 
	 */
	public void testConvertToESCFromEST_1()
	throws Exception
	{
		byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-T-1.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for signer certificate validation to find references
		//because in specification,just crls wanted,policy file must be given with just crl finders
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyCRL());
		
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESC, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-C-1.p7s");
		
	}
	
	
	/**
	 * ESC With just basicocsp references(not crl)
	 * Possible input data: Signature-C-T-1.p7s 
	 */
	public void testConvertToESCFromEST_2()
	throws Exception
	{
		byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-T-1.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for signer certificate validation to find references
		//because in specification,just ocsps wanted,policy file must be given with just ocsp finders
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyOCSP());
		
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESC, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-C-2.p7s");
		
	}
	
	
	
	
	
	
}
