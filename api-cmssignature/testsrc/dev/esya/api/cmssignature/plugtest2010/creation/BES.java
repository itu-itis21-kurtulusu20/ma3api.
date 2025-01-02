package dev.esya.api.cmssignature.plugtest2010.creation;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EClaimedAttributes;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentHints;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerLocation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.net.Authenticator;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class BES extends CMSSignatureTest 
{
	private final String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-BES.SCOK\\TU\\";
	private final String ATTRIBUTE_CERTIFICATE_PATH = getDirectory() + "creation\\plugtests2010\\attributecertificate.cer";
	

	static
	{
		Authenticator.setDefault(new EtsiTestAuthenticator()); 
	}
	
	
	/**
	 * generates and verifies the most simple CAdES-BES form 
	 * *WITHOUT* SigningTime attribute. Implementation shall be add a ESSSigningCertificate 
	 * or ESSSigningCertificateV2 attribute to generating signature to respect CAdES 
	 * (See 'RFC 5126 5.7.3'). A ContentType and a MessageDigest attributes also shall be 
	 * added to the signature to respect CMS (See 'RFC 3852'). 
	 * @throws CryptoException 
	 */
	public void testCreateBES1() throws Exception
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
	 * generates and verifies the most simple CAdES-BES form with SigningTime attribute which is the most 
	 * common optional attribute in CMS SignedData and describes untrusted time when the data was signed. 
	 * @throws Exception
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
		//POLICY_FILE = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
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
		//POLICY_FILE = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
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
		
		//parameters for ContentHints attribute
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-6.p7s");
	}
	
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
	 * ContentIdentifier attributes
	 */
	public void testCreateBES7()
	throws Exception
	{
		//POLICY_FILE = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
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
		//POLICY_FILE = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");
		
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
		params.put(EParameters.P_CERT_VALIDATION_POLICY,getPolicy());
		
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
		TSSettings settings = getTSSettings(); 
			
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(),getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
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
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		bs2.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR+"Signature-C-BES-11.p7s");
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, SignerLocation, 
	 * SignerAttributes (ClaimedAttributes), ContentHints, ContentIdentifier, 
	 * CommitmentTypeIndication, ContentTimeStamp       
	*/
	public void testCreateBES15()
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
		TSSettings settings = getTSSettings();
		params.put(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
		params.put(EParameters.P_TSS_INFO, settings);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
		
		List<Signer> signer =  bs2.getSignerList();
		signer.get(0).getSignerInfo().getSignerIdentifier();
		
		//add countersignature with the same certificate and same paremeters.
		//different certificate and parameters may be given
		bs2.getSignerList().get(0).addCounterSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR+"Signature-C-BES-15.p7s");
		
		
		//Checking Attributes
		BaseSignedData bs3 = new BaseSignedData(bs2.getEncoded());
		
		List<EAttribute> attrs = bs3.getSignerList().get(0).getAttribute(SignerLocationAttr.OID);
		ESignerLocation sl = SignerLocationAttr.toSignerLocation(attrs.get(0));
		
		assertEquals("TURKEY", sl.getCountry());
		assertEquals("KOCAELİ", sl.getLocalityName());
		assertEquals(true, Arrays.equals(new String[]{"TUBITAK UEKAE","GEBZE"}, sl.getPostalAddress()));
		
		
		attrs = bs3.getSignerList().get(0).getAttribute(ContentIdentifierAttr.OID);
		byte [] identifier = ContentIdentifierAttr.toIdentifier(attrs.get(0));
		
		assertEquals(true, Arrays.equals("PL123456789".getBytes("ASCII"), identifier));
		
		
	}
	
	/**
	 * Create BES with MessageDigest, ESSSigningCertificateV2, ContentType, SigningTime 
	 * attributes
	 */
	
	public void testCreateBES16()
	throws Exception
	{
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
