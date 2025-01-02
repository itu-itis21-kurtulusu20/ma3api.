package dev.esya.api.cmssignature.plugtest.creation;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EClaimedAttributes;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentHints;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * BES signatures created according to the specifications in
 * 2009 ETSI CADES PLUGTEST
 * @author aslihan.kubilay
 *
 */
public class BES extends CMSSignatureTest
{

	private final String OUTPUT_DIR = getDirectory() +"creation\\plugtests\\bes\\";
	private final String ATTRIBUTE_CERTIFICATE_PATH = getDirectory() +"creation\\plugtests\\attributecertificate.cer";
		
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType 
	 * attributes
	 */
	public void testCreateBES1()
	throws Exception
	{
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		//Since the specified attributes are mandatory for bes,null is given as parameter for optional attributes
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), null, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-1.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime 
	 * attributes
	 */
	public void testCreateBES2()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime attribute is optional,add it to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-2.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * SignerLocation attributes
	 */
	public void testCreateBES3()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and SigningLocation attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[]{"TUBITAK UEKAE","GEBZE"}));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-3.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * SignerAttributes (ClaimedAttributes) attributes
	 */
	public void testCreateBES4()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//create the claimed role attribute for signerattributes attribute
		EAttribute attr1 = new EAttribute(new Attribute());
		attr1.setType(new Asn1ObjectIdentifier(new int[]{1,3,6,7,8,10}));
		Asn1UTF8String role = new Asn1UTF8String("supervisor");
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		role.encode(encBuf);
		attr1.addValue(encBuf.getMsgCopy());
		
		
		//Since SigningTime and SignerAttributes attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] {attr1})));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
				
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-4.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * SignerAttributes (CertifiedAttributes) attributes
	 */
	public void testCreateBES5()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Put attributecertificate to parameters necessary for SignerAttributes attribute
		EAttributeCertificate ac = new EAttributeCertificate();
		ac = new EAttributeCertificate((AttributeCertificate) AsnIO.dosyadanOKU(ac.getObject(), ATTRIBUTE_CERTIFICATE_PATH));
		
		
		//Since SigningTime and SignerAttributes attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new SignerAttributesAttr(ac));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
				
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-5.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * ContentHints attributes
	 */
	public void testCreateBES6()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and ContentHints attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new ContentHintsAttr(new EContentHints("text/plain", new Asn1ObjectIdentifier(new int[]{1, 2, 840, 113549, 1, 7 ,1 }))));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-6.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * ContentIdentifier attributes
	 */
	public void testCreateBES7()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and ContentIdentifier attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new ContentIdentifierAttr("PL123456789".getBytes("ASCII")));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-7.p7s");
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * CommitmentTypeIndication attributes
	 */
	public void testCreateBES8()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and CommitmentTypeIndication attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-8.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * ContentTimeStamp attributes
	 */
	public void testCreateBES10()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and ContentTimeStamp attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new ContentTimeStampAttr());
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//parameters for ContentTimeStamp attribute
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-10.p7s");
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * CounterSignature attributes
	 */
	public void testCreateBES11()
	throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime attribute is optional,add it to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		List<IAttribute> optionalAttributes2 = new ArrayList<IAttribute>();
		optionalAttributes2.add(new SigningTimeAttr(Calendar.getInstance()));
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes2, params);
		
		
		//add countersignature with the same certificate and same paremeters.
		//different certificate and parameters may be given
		bs.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-11.p7s");
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, SignerLocation, 
	 * SignerAttributes (ClaimedAttributes), ContentHints, ContentIdentifier, 
	 * CommitmentTypeIndication, ContentTimeStamp       
	*/
	public void testCreateBES15()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		
		//create the claimed role attribute for signerattributes attribute
		EAttribute attr1 = new EAttribute(new Attribute());
		attr1.setType(new Asn1ObjectIdentifier(new int[]{1,3,6,7,8,10}));
		Asn1UTF8String role = new Asn1UTF8String("supervisor");
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
		role.encode(encBuf);
		attr1.addValue(encBuf.getMsgCopy());
		
		
		//Specified attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[]{"TUBITAK UEKAE","GEBZE"}));
		optionalAttributes.add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] {attr1})));
		optionalAttributes.add(new ContentHintsAttr(new EContentHints("text/plain", new Asn1ObjectIdentifier(new int[]{1, 2, 840, 113549, 1, 7 ,1 }))));
		optionalAttributes.add(new ContentIdentifierAttr("PL123456789".getBytes("ASCII")));
		optionalAttributes.add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
		optionalAttributes.add(new ContentTimeStampAttr());
		
		
		
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//parameters for ContentTimeStamp attribute
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, getTSSettings());
		
		
		/*List<IAttribute> optionalAttributes2 = new ArrayList<IAttribute>();
		optionalAttributes2.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes2.add(new SignerLocationAttr());
		optionalAttributes2.add(new SignerAttributesAttr());
		optionalAttributes2.add(new ContentHintsAttr());
		optionalAttributes2.add(new ContentIdentifierAttr());
		optionalAttributes2.add(new CommitmentTypeIndicationAttr());
		optionalAttributes2.add(new ContentTimeStampAttr());*/
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		
		//add countersignature with the same certificate and same paremeters.
		//different certificate and parameters may be given
		bs.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-15.p7s");
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV2, ContentType, SigningTime 
	 * attributes
	 */
	public void testCreateBES16()
	throws Exception
	{
		//getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//SigningTime attribute is optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//Since reference digest alg is  different from SHA-1,the mandatory attribute SigningCertificateV2 will be added to signature
		//instead of SigningCertificateV1
		params.put(EParameters.P_REFERENCE_DIGEST_ALG, DigestAlg.SHA256);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-BES-16.p7s");
		
	}
	
}
