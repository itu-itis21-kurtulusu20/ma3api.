package dev.esya.api.cmssignature.orcun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;

public class KSMTestConstants 
{

	private final String DIRECTORY = "T:\\api-cmssignature\\";
	
	private final String SIGNING_CERTIFICATE_PATH = DIRECTORY + "testdata\\support\\19193592100NES0.cer";
	private final String SECOND_SIGNING_CERTIFICATE_PATH = DIRECTORY + "testdata\\support\\19193592100NES0.cer";
	private final String POLICY_FILE = DIRECTORY + "testdata\\support\\policy.xml";
	
	private BaseSigner signer;
	
	public BaseSigner getSignerInterface(SignatureAlg aAlg) throws Exception {
		if(signer == null)
		{
			SmartCard sc = new SmartCard(CardType.AKIS);
			long slot = sc.getSlotList()[2];
			long session = sc.openSession(slot);
			try
			{
				sc.login(session, "*****");
			}
			catch (PKCS11Exception ex) 
			{
				if( ex.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN)
					throw ex;
			}
			signer = new SCSignerWithKeyLabel(sc, session, slot, "19193592100NES0", SignatureAlg.RSA_SHA1.getName());
		}
		
		return signer;
	}

	
	public BaseSigner getSecondSignerInterface(SignatureAlg aAlg)
			throws Exception {
		return getSignerInterface(aAlg);
	}

	
	public ECertificate getSignerCertificate() throws Exception {
		return ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
	}

	
	public ECertificate getSecondSignerCertificate() throws Exception {
		return getSignerCertificate();
	}

	
	public ValidationPolicy getPolicy() throws ESYAException,
			FileNotFoundException {
		return PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
	}

	
	public TSSettings getTSSettings() 
	{
		return new TSSettings("http://zd.kamusm.gov.tr", 2, "@Humbus34tef".toCharArray());
	}

	
	public String getDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getPIN() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean getCheckQCStatement() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
