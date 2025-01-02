package test.esya.api.cmssignature.testconstants;


import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;

public class UGTestConstants implements TestConstants
{
	//Dummy Comment to Commit


	//Revocation control from OCSP
    private final String PFX1_PATH = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_2.p12";
	//Revocation control from CRL
    private final String PFX2_PATH = "T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_1.p12";
	//EC certificate for sign
	private final String PFX3_PATH = "T:\\api-parent\\resources\\ug\\pfx\\esya-test-system\\test@test.gov.tr_724328.pfx";

	private final String PFX_PASSWORD = "123456";
	private final String EC_PFX_PASSWORD = "724328";


    private final String ZD_ADD = "http://zdsA1.test3.kamusm.gov.tr";
    private final int ZD_USER_ID = 1;
	private final String ZD_PASSWORD = "12345678";




	private final String DIRECTORY = "T:\\api-cmssignature\\testdata\\cmssignature\\imza\\";

	private final String POLICY_DIRECTORY = "T:\\api-parent\\resources\\ug\\config\\";
	private final String POLICY_FILE =  POLICY_DIRECTORY + "certval-policy-test.xml";
	private final String POLICY_FILE_CRL = POLICY_DIRECTORY + "certval-crl-policy-test.xml";
	private final String POLICY_FILE_OCSP = POLICY_DIRECTORY + "certval-ocsp-policy-test.xml";
	private final String POLICY_FILE_NoOCSP_NoCRL = POLICY_DIRECTORY + "certval-no-ocsp-no-crl-finder-policy-test.xml";
	
	private final String HUGE_FILE = "D:\\share\\film\\Life\\Life S01E01 Challenges of Life.mkv";

	private static ECertificate cert;
	private static ECertificate secondCert;
	private static ECertificate ecCert;

	private static PrivateKey privKey;
	private static PrivateKey secondPrivKey;
	private static PrivateKey ecPrivKey;

	
	public UGTestConstants() 
	{
		try
		{
			FileInputStream fis = new FileInputStream(PFX1_PATH);
			PfxParser pfxParser = new PfxParser(fis, PFX_PASSWORD);
			fis.close();
			cert = pfxParser.getFirstCertificate();
			privKey = pfxParser.getFirstPrivateKey();

			fis = new FileInputStream(PFX2_PATH);
			pfxParser = new PfxParser(fis, PFX_PASSWORD);
			fis.close();
			secondCert = pfxParser.getFirstCertificate();
			secondPrivKey = pfxParser.getFirstPrivateKey();

			fis = new FileInputStream(PFX3_PATH);
			pfxParser = new PfxParser(fis, EC_PFX_PASSWORD);
			fis.close();
			ecCert = pfxParser.getFirstCertificate();
			ecPrivKey = pfxParser.getFirstPrivateKey();
		}
		catch(Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	
	public String getDirectory()
	{
		return DIRECTORY;
	}
	
	public BaseSigner getSignerInterface(SignatureAlg aAlg)
	throws Exception
	{
		Signer signer = Crypto.getSigner(aAlg);
		signer.init(privKey);
		return signer;
	}

	public BaseSigner getSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams)
			throws Exception
	{
		Signer signer = Crypto.getSigner(aAlg);
		signer.init(privKey, algorithmParams);
		return signer;
	}

	public BaseSigner getSecondSignerInterface(SignatureAlg aAlg)
	throws Exception
	{
		Signer signer = Crypto.getSigner(aAlg);
		signer.init(secondPrivKey);
		return signer;
	}

	public BaseSigner getSecondSignerInterface(SignatureAlg aAlg, AlgorithmParams algorithmParams)
			throws Exception
	{
		Signer signer = Crypto.getSigner(aAlg);
		signer.init(secondPrivKey, algorithmParams);
		return signer;
	}


	public BaseSigner getECSignerInterface(SignatureAlg aAlg)
			throws Exception
	{
		Signer signer = Crypto.getSigner(aAlg);
		signer.init(ecPrivKey);
		return signer;
	}

	//Revocation control from OCSP
	public ECertificate getSignerCertificate()
	throws Exception
	{
		return cert;
	}

	public PrivateKey getSignerPrivateKey()
	throws Exception
	{
		return privKey;
	}

	//Revocation control from CRL
	public ECertificate getSecondSignerCertificate()
	throws Exception
	{
		return secondCert;
	}

	public ECertificate getECSignerCertificate()
			throws Exception
	{
		return ecCert;
	}
	
	public ValidationPolicy getPolicy() throws ESYAException, FileNotFoundException
	{
		return PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
	}
	
	public TSSettings getTSSettings()
	{
		return new TSSettings(ZD_ADD, ZD_USER_ID, ZD_PASSWORD, DigestAlg.SHA256);
	}

	public ISignable getSimpleContent()
	{
		try 
		{
			return new SignableByteArray("test".getBytes("ASCII"));
		} 
		catch (UnsupportedEncodingException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	public ISignable getHugeContent()
	{
		return new SignableFile(new File(HUGE_FILE));
	}

	public ValidationPolicy getPolicyCRL() throws ESYAException,
			FileNotFoundException {
		return PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE_CRL));
	}

	public ValidationPolicy getPolicyOCSP() throws ESYAException,
			FileNotFoundException {
		return PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE_OCSP));
	}
	
	public ValidationPolicy getPolicyNoOCSPNoCRL() throws ESYAException,
			FileNotFoundException {
		return PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE_NoOCSP_NoCRL));
	}
}
