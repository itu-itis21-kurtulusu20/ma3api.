package dev.esya.api.cmssignature.plugtest.creation;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EClaimedAttributes;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EPES extends  CMSSignatureTest
{
	private final String OUTPUT_DIR = getDirectory() +"creation\\plugtests\\epes\\";
	private final String SIGNATURE_POLICY_PATH = getDirectory() +"creation\\plugtests\\TARGET-SIGPOL-ETSI3.der";
	private final int[] SIGNATURE_POLICY_OID = new int[] {1,2,3,4,5,1}; 
	
	
	/**
	 * Create EPES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, 
	 * SignaturePolicyIdentifier attributes
	 * SignaturePolicyIdentifier Oid=1.2.3.4.5.1
	 * File=TARGET-SIGPOL-ETSI3.der
	 */
	public void testCreateEPES1()
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
		
		//necessary parameters for signaturepolicyidentifier attribute which is mandatory for EPES
		byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
		params.put(EParameters.P_POLICY_VALUE, policyValue);
		params.put(EParameters.P_POLICY_ID, SIGNATURE_POLICY_OID);
		params.put(EParameters.P_POLICY_DIGEST_ALGORITHM, DigestAlg.SHA1);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_EPES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-EPES-1.p7s");
		
	}
	
	
	/**
	 * Create EPES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, 
	 * SignerLocation, SignerAttributes (ClaimedAttributes),  CommitmentTypeIndication, 
	 * SignaturePolicyIdentifier attributes
	 * SignaturePolicyIdentifier Oid=1.2.3.4.5.1
	 * File=TARGET-SIGPOL-ETSI3.der
	 */
	public void testCreateEPES2()
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
		
		//Since SigningTime, SignerLocation, SignerAttributes (ClaimedAttributes),  
		//CommitmentTypeIndication attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
		optionalAttributes.add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[]{"TUBITAK UEKAE","GEBZE"}));
		optionalAttributes.add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] {attr1})));
		optionalAttributes.add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
		
		
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		//necessary for certificate validation.By default,certificate validation is done.But if the user 
		//does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
		params.put(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
		
		//necessary parameters for signaturepolicyidentifier attribute which is mandatory for EPES
		byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
		params.put(EParameters.P_POLICY_VALUE, policyValue);
		params.put(EParameters.P_POLICY_ID, SIGNATURE_POLICY_OID);
		params.put(EParameters.P_POLICY_DIGEST_ALGORITHM, DigestAlg.SHA1);
		
		//add signer
		bs.addSigner(ESignatureType.TYPE_EPES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-EPES-2.p7s");
		
	}
	
	

}
