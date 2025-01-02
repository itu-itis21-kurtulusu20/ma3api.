package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerLocation;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ImzayaEklenebilecekAlanlar extends TestCase 
{
	String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() + "testdata\\support\\yasemin_sign.cer";
	String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
	
	public void testImzayaEkle() throws Exception
	{
		BaseSignedData bs = new BaseSignedData(); 
		bs.addContent(new SignableByteArray("test".getBytes())); 
						 
		HashMap<String, Object> params = new HashMap<String, Object>(); 
		ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE)); 
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);	
		params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
				 
		ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

		SmartCard sc = new SmartCard(CardType.AKIS); 
		long slot = SmartOp.findSlotNumber(CardType.AKIS);
		long session = sc.openSession(slot); 
		sc.login(session, "12345"); 
				 
		BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slot, cert.getSerialNumber().toByteArray(), 
				SignatureAlg.RSA_SHA1.getName());
		
List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));
optionalAttributes.add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[]{"TUBITAK UEKAE","GEBZE"}));
optionalAttributes.add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
optionalAttributes.add(new ContentIdentifierAttr("PL123456789".getBytes("ASCII")));
		 
bs.addSigner(ESignatureType.TYPE_BES, cert , signer, optionalAttributes, params);
 				 
sc.logout(session); 
sc.closeSession(session);

//reading Attributes
BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
List<EAttribute> attrs ;
Signer aSigner = bs2.getSignerList().get(0);

attrs = aSigner.getAttribute(SigningTimeAttr.OID);
Calendar st = SigningTimeAttr.toTime(attrs.get(0));
System.out.println("Signing time: " + st.getTime());

attrs = aSigner.getAttribute(SignerLocationAttr.OID);
ESignerLocation sl = SignerLocationAttr.toSignerLocation(attrs.get(0));
StringBuilder sb = new StringBuilder();
for (String address : sl.getPostalAddress()) 
	sb.append(" " + address);
System.out.println("\nCountry: " + sl.getCountry() +
		           "\nCity: " + sl.getLocalityName() +
		           "\nAdress: "	+ sb);

attrs = aSigner.getAttribute(ContentIdentifierAttr.OID);
byte [] ci = ContentIdentifierAttr.toIdentifier(attrs.get(0));
System.out.println("\n" + Arrays.toString(ci));

attrs = aSigner.getAttribute(CommitmentTypeIndicationAttr.OID);
CommitmentType ct = CommitmentTypeIndicationAttr.toCommitmentType(attrs.get(0));
System.out.println("\n" + ct);
	}
}
