package dev.esya.api.cmssignature.plugtest.convertion;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.HashMap;

public class ESA extends CMSSignatureTest
{
	private final String INPUT_DIR = getDirectory() +"creation\\plugtests\\";
	private final String OUTPUT_DIR = getDirectory() +"convertion\\plugtests\\esa\\";
	
	

	
	/**
	 * Add ArchiveTimeStampV2 attribute to ESXLong1(with crl references) signature
	 */
	public void testConvertToESAFromESXLong1_1()
	throws Exception
	{
		byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR+"esxlong1\\Signature-C-XL-1.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESXLong);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the esctimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-1.p7s");
	}
	
	
	/**
	 * Add ArchiveTimeStampV2 attribute to ESXLong2(with crl references) signature
	 */
	public void testConvertToESAFromESXLong2_2()
	throws Exception
	{
		byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR+"esxlong2\\Signature-C-XL-2.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESXLong);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the referencetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-2.p7s");
	}
	
	
	/**
	 * Add ArchiveTimeStampV2 attribute to ESXLong1(with ocsp references) signature
	 */
	public void testConvertToESAFromESXLong1_3()
	throws Exception
	{
		byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR+"esxlong1\\Signature-C-XL-3.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESXLong);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the esctimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-3.p7s");
	}
	
	/**
	 * Add ArchiveTimeStampV2 attribute to ESXLong2(with ocsp references) signature
	 */
	public void testConvertToESAFromESXLong2_4()
	throws Exception
	{
		byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR+"esxlong2//Signature-C-XL-4.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESXLong);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the referencetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-4.p7s");
	}
	
	/**
	 * Add 2 ArchiveTimeStampV2 attributes to ESA signature
	 */
	public void testConvertToESAFromESA_5()
	throws Exception
	{
		byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR+"esa//Signature-C-A-3.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-5.p7s");
	}
	
	/**
	 * 
	 * Add 2 ArchiveTimeStampV2 attributes to ESA signature
	 */
	public void testConvertToESAFromESA_6()
	throws Exception
	{
		byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR+"esa//Signature-C-A-4.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-6.p7s");
	}
	
	/**
	 * Add ArchiveTimeStampV2 attribute to EST signature
	 */
	public void testConvertToESAFromEST_7()
	throws Exception
	{
		byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR+"est\\Signature-C-T-1.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		//necessary for finding certificate and revocation values of the signer certificate
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-7.p7s");
	}
	
	
	/**
	 * Add 2 ArchiveTimeStampV2 attributes to ESA signature
	 */
	public void testConvertToESAFromESA_8()
	throws Exception
	{
		byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR+"esa\\Signature-C-A-7.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the sarchivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-8.p7s");
	}
	
	/**
	 * Add 2 ArchiveTimeStampV2 attributes to EST signature
	 */
	public void testConvertToESAFromEST_9()
	throws Exception
	{
		byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR+"est\\Signature-C-T-1.p7s");
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		//necessary for finding certificate and revocation values of the signer certificate
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-9.p7s");
	}
	
	
	
	
	
	
	
}
