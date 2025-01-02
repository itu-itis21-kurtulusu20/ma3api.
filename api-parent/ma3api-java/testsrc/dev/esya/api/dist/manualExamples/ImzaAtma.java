package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithCertSerialNo;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class ImzaAtma extends TestCase 
{
	String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() + "testdata\\support\\yasemin_sign.cer";
	String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
	
	
	public void testImzasizBirVerininİmzalanmasi() throws Exception
	{
		String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
		//--------------------------------------------------------------------------------//
BaseSignedData bs = new BaseSignedData(); 
bs.addContent(new SignableByteArray("test".getBytes())); 
				 
//create parameters necessary for signature creation 
HashMap<String, Object> params = new HashMap<String, Object>(); 
ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE)); 
/*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/ 
params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);			 
		 
ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

SmartCard sc = new SmartCard(CardType.AKIS); 
long slot = SmartOp.findSlotNumber(CardType.AKIS);
long session = sc.openSession(slot); 
sc.login(session, "12345"); 
		 
BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slot, cert.getSerialNumber().toByteArray(), 
		SignatureAlg.RSA_SHA1.getName());
		 
/*add signer. Since the specified attributes are mandatory for bes,null is given as parameter for optional attributes*/ 
bs.addSigner(ESignatureType.TYPE_BES, cert , signer, null, params);
 				 
//write the contentinfo to file 
AsnIO.dosyayaz(bs.getEncoded(),SIGNATURE_FILE);	 
sc.logout(session); 
sc.closeSession(session);
	}
	
	public void testParalelImzaEklenmesi()throws Exception
	{
		String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
		String NEW_SIGNATURE_ADDED_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\2.p7s"; 
		//--------------------------------------------------------------------------------------------//
byte [] signature = AsnIO.dosyadanOKU(SIGNATURE_FILE);
BaseSignedData bs = new BaseSignedData(signature); 
				 
//create parameters necessary for signature creation 
HashMap<String, Object> params = new HashMap<String, Object>(); 
ValidationPolicy policy =  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE)); 
/*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/ 
params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);			 
		 
ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
 
SmartCard sc = new SmartCard(CardType.AKIS); 
long slot = SmartOp.findSlotNumber(CardType.AKIS);
long session = sc.openSession(slot); 
sc.login(session, "12345"); 
		 
BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slot, cert.getSerialNumber().toByteArray(), 
		SignatureAlg.RSA_SHA1.getName()); 
	 	
//add signer. Since the specified attributes are mandatory for bes,null is given as parameter for 
//optional attributes 
bs.addSigner(ESignatureType.TYPE_BES, cert , signer, null, params); 
				
//write the contentinfo to file 
AsnIO.dosyayaz(bs.getEncoded(),NEW_SIGNATURE_ADDED_FILE);	 
sc.logout(session); 
sc.closeSession(session);
	}
	
	
	public void testSeriImzaEklenmesi() throws Exception
	{
		String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
		String NEW_SIGNATURE_ADDED_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\3.p7s"; 
		//------------------------------------------------------------------------------------------//
byte [] signature = AsnIO.dosyadanOKU(SIGNATURE_FILE);
BaseSignedData bs = new BaseSignedData(signature);
 				 
//create parameters necessary for signature creation 
HashMap<String, Object> params = new HashMap<String, Object>(); 
ValidationPolicy policy =  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE)); 
/*necessary for certificate validation.By default,certificate validation is done.But if the user does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false*/ 
params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);			 
		 
ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);

SmartCard sc = new SmartCard(CardType.AKIS); 
long slot = SmartOp.findSlotNumber(CardType.AKIS);
long session = sc.openSession(slot); 
sc.login(session, "12345"); 
		 
BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slot, cert.getSerialNumber().toByteArray(), 
		SignatureAlg.RSA_SHA1.getName()); 		 
Signer firstSigner = bs.getSignerList().get(0); 

firstSigner.addCounterSigner(ESignatureType.TYPE_BES, cert , signer, null, params); 
				 
//write the contentinfo to file 
AsnIO.dosyayaz(bs.getEncoded(),NEW_SIGNATURE_ADDED_FILE);	 
sc.logout(session); 
sc.closeSession(session);
	}
	
	
	public void testAyrikImzaAtilmasi() throws Exception
	{
		String MOVIE_FILE = "D:\\share\\film\\Life\\Life S01E01 Challenges of Life.mkv";
		String SIGNATURE_FILE = TestConstants.getDirectory() + "\\testdata\\support\\bes\\HugeExternalContent.p7s";
		
		//-------------------------------------------------------------------------//
		BaseSignedData bs = new BaseSignedData();
		
		File file = new File(MOVIE_FILE);
		ISignable signable = new SignableFile(file,2048);
		bs.addContent(signable,false);
				
		//create parameters necessary for signature creation
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		ValidationPolicy policy =  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE)); 
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		
		ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
						
		SmartCard sc = new SmartCard(CardType.AKIS); 
		long [] slots = sc.getSlotList(); 
		long session = sc.openSession(slots[0]); 
		sc.login(session, "12345"); 
				 
		BaseSigner signer = new SCSignerWithCertSerialNo (sc, session, slots[0], cert.getSerialNumber().toByteArray(), 
				SignatureAlg.RSA_SHA1.getName()); 		 
		
		
		bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);
				
		AsnIO.dosyayaz(bs.getEncoded(), SIGNATURE_FILE);
		
		sc.logout(session); 
		sc.closeSession(session);
	}
}
