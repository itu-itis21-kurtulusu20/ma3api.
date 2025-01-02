package dev.esya.api.cmssignature.plugtest.creation;

import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ESA extends CMSSignatureTest 
{
	private final String OUTPUT_DIR = getDirectory() + "creation\\plugtests\\esa\\";;
	
	
	public BaseSignedData getEsxLongType1withCRL() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyCRL());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong_Type1, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	public BaseSignedData getEsxLongType2withCRL() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyCRL());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	public BaseSignedData getEsxLongType1withOCSP() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyOCSP());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong_Type1, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	public BaseSignedData getEsxLongType2withOCSP() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyOCSP());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	public BaseSignedData getEsxLongwithCRL() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyCRL());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	public BaseSignedData getEsxLongwithOCSP() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicyOCSP());
		
		//parameters for ContentTimeStamp attribute
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		return bs;
	}
	
	/**
	 * Add ArchiveTimeStampV2 attribute to ESXLong1(with crl references) signature
	 */
	public void testConvertToESAFromESXLong1_1()
	throws Exception
	{
		byte[] inputESXLong = getEsxLongType1withCRL().getEncoded();
		
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
		byte[] inputESXLong = getEsxLongType2withCRL().getEncoded();
		
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
		byte[] inputESXLong =  getEsxLongType1withOCSP().getEncoded();
		
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
		byte[] inputESXLong = getEsxLongType2withOCSP().getEncoded();
		
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
		byte[] inputESA =  getEsxLongType1withOCSP().getEncoded();
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		//Thread.currentThread().sleep(2000);

		//create necessary parameters for convertion
		HashMap<String, Object> params2 = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params2.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params2.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params2.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		bs2.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR+"Signature-C-A-5.p7s");
	}
	
	/**
	 * 
	 * Add 2 ArchiveTimeStampV2 attributes to ESA signature
	 */
	public void testConvertToESAFromESA_6()
	throws Exception
	{
		byte[] inputESA =  getEsxLongType2withOCSP().getEncoded();
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		//Thread.currentThread().sleep(2000);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params2 = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params2.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params2.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the archivetimestamp
		params2.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		bs2.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params2);
		
		AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR+"Signature-C-A-6.p7s");
	}
	
	/**
	 * Add ArchiveTimeStampV2 attribute to EST signature
	 */
	public void testConvertToESAFromEST_7()
	throws Exception
	{
		byte[] inputEST =  getEsxLongwithCRL().getEncoded();
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO,  getTSSettings());
		
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
		byte[] inputESA = getEsxLongwithCRL().getEncoded();
		
		BaseSignedData bs = new BaseSignedData(inputESA);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the sarchivetimestamp
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		//Thread.currentThread().sleep(2000);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params2 = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params2.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params2.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the sarchivetimestamp
		params2.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		bs2.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params2);
		
		AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR+"Signature-C-A-8.p7s");
	}
	
	/**
	 * Add ArchiveTimeStampV2 attributes to ESX signature
	 */
	public void testConvertToESAFromEST_9()
	throws Exception
	{
		byte[] inputEST = getEsxLongwithOCSP().getEncoded();
		
		BaseSignedData bs = new BaseSignedData(inputEST);
		
		//create necessary parameters for convertion
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for getting archivetimestamp
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO,  getTSSettings());
		
		//necessary for finding certificate and revocation values of the signaturetimestamp
		//necessary for finding certificate and revocation values of the signer certificate
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		bs.getSignerList().get(0).convert(ESignatureType.TYPE_ESA, params);
		
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-A-9.p7s");
	}
	
}
