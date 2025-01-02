package dev.esya.api.cmssignature.plugtest2010.creation;

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

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EST extends CMSSignatureTest 
{
	private final String OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-T.SCOK\\TU\\";
	
	static
	{
		Authenticator.setDefault(new EtsiTestAuthenticator()); 
	}
	
	public void testTForm() throws Exception
	{
		BaseSignedData bs = new BaseSignedData();
		
		//add content which will be signed
		bs.addContent(getSimpleContent());
		
		//Since SigningTime and ContentTimeStamp attributes are optional,add them to optional attributes list
		List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
		optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

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
		bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, params);
		
		//write the contentinfo to file
		AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR+"Signature-C-T-1.p7s");
	}
	
}
