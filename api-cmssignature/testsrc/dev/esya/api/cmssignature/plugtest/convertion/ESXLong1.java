package dev.esya.api.cmssignature.plugtest.convertion;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.HashMap;

public class ESXLong1 extends CMSSignatureTest {
	
	private final String INPUT_DIR = getDirectory() +"convertion\\plugtests\\esc\\";
	private final String OUTPUT_DIR = getDirectory() +"convertion\\plugtests\\esxlong1\\";
	
	
	/**
	 * Create ESXLong1 signature with just CRL revocation references
	 * Possible input: Signature-C-C-1.p7s
	 * The values of the references must be in the certificate store
	 */
	public void testConvertESXLong1FromESC_1()
	throws Exception
	{
		byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-C-1.p7s");
		BaseSignedData bs = new BaseSignedData(inputESC);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting esctimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY,getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESXLong_Type1, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-XL-1.p7s");
		
	}
	
	
	/**
	 * Create ESXLong1 signature with just OCSP revocation references
	 * Possible input: Signature-C-C-2.p7s
	 * The values of the references must be in the certificate store
	 */
	public void testConvertESXLong1FromESC_3()
	throws Exception
	{
		byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-C-2.p7s");
		BaseSignedData bs = new BaseSignedData(inputESC);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting esctimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESXLong_Type1, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-XL-3.p7s");
		
	}
	
	
}
