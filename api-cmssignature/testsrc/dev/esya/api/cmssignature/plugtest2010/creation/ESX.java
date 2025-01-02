package dev.esya.api.cmssignature.plugtest2010.creation;

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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

public class ESX extends CMSSignatureTest 
{
	private final String OUTPUT_DIR = getDirectory()+ "CAdES_InitialPackage\\CAdES-X.SCOK\\TU\\";;
	
	static
	{
		Authenticator.setDefault(new EtsiTestAuthenticator()); 
	}
	
	public void testCreateESX1() throws Exception
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
		bs.addSigner(ESignatureType.TYPE_ESX_Type1, getSignerCertificate(),getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-1.p7s");
	}
	
	public void testCreateESX2() throws Exception
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
		bs.addSigner(ESignatureType.TYPE_ESX_Type2, getSignerCertificate(),getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-2.p7s");
	}
	
	public void testCreateESX3() throws Exception
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
		bs.addSigner(ESignatureType.TYPE_ESX_Type1, getSignerCertificate(),getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-3.p7s");
	}
	
	public void testCreateESX4() throws Exception
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
		bs.addSigner(ESignatureType.TYPE_ESX_Type2, getSignerCertificate(),getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-X-4.p7s");
	}
}
