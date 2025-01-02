package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.Signature_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.ValidationState;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class ImzaDogrulamaIslemleri extends TestCase
{
	String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
	String SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";
	String CONTENT_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\test.dat";
	String SIGNING_CERTIFICATE_PATH = TestConstants.getDirectory() + "testdata\\support\\zeldal.ozdemir#ug.netSIGN0.cer";
	String NES_SIGNATURE = TestConstants.getDirectory() + "testdata\\support\\nes\\ESXLong-1.p7s";
	
	public void testImzaDogrulamaIslemleri() throws Exception
	{
		byte[] signedData = AsnIO.dosyadanOKU(SIGNATURE_FILE);
		ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
				
		SignedDataValidation sdv = new SignedDataValidation();
		
		SignedDataValidationResult sdvr = sdv.verify(signedData, params);

		if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
			System.out.println("İmzaların hepsi doğrulamadı");		

		System.out.println(sdvr.toString());
	}
	
	public void testBirImzaDogrulamaIslemleri() throws Exception
	{
		ECertificate searchingCert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
		SignedDataValidationResult sdvr = getValidationResult();
		List<SignatureValidationResult>  results = sdvr.getSDValidationResults();
		for (SignatureValidationResult svr : results) 
		{
			ECertificate cert = svr.getSignerCertificate();
			if(searchingCert.equals(cert))
			{
				if(svr.getSignatureStatus() != Signature_Status.VALID)
				{
					System.out.println("İmza doğrulamadı");
					System.out.println(svr);
				}
			}
		}
	}
	
	public void testImzaSonucu() throws Exception
	{
		BaseSignedData bs = getBaseSignedData();
		SignedDataValidationResult sdvr = getValidationResult();
		Signer  firstCounterSigner = bs.getSignerList().get(0).getCounterSigners().get(0);
		SignatureValidationResult firstCounterSignerVR = sdvr.getSDValidationResults().get(0).getCounterSigValidationResults().get(0);

		
	
	}
	
	public void testOnDogrulama()
	{
		SignedDataValidationResult sdvr = getValidationResult();
		
		if(sdvr.getSDValidationResults().get(0).getValidationState() == 
															ValidationState.PREMATURE)
		System.out.println("Ön doğrulama yapıldı.");
	}
	
	public void testAyrikİmzaninDogrulanmasi() throws Exception
	{
		byte[] signedData = AsnIO.dosyadanOKU(SIGNATURE_FILE);
		ISignable content = new SignableFile(new File(CONTENT_FILE));
		
		ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
		Hashtable<String, Object> params = new Hashtable< String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		params.put(EParameters.P_EXTERNAL_CONTENT, content );
				
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(signedData, params);

		if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
			System.out.println("İmzaların hepsi doğrulamadı");		

		System.out.println(sdvr.toString());
	}
	
	public void testSertifikaDogrulama() throws Exception
	{
		ValidationPolicy policy=  PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
		ECertificate cert = ECertificate.readFromFile(SIGNING_CERTIFICATE_PATH);
		
ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
vs.setBaseValidationTime(Calendar.getInstance());
CertificateStatusInfo csi = CertificateValidation.validateCertificate(vs, cert);
if(csi.getCertificateStatus() != CertificateStatus.VALID)
	System.out.println("Sertifika dogrulanamadi");
	
	}
	
	public void testImzaAtan() throws Exception
	{
		byte [] signedData = AsnIO.dosyadanOKU(NES_SIGNATURE);
		
BaseSignedData bsd = new  BaseSignedData(signedData);
ECertificate cert = bsd.getSignerList().get(0).getSignerCertificate();

if(cert == null){
	System.out.println("İmzaci bilgisi yok");
} else {
	System.out.println("İsim & Soyisim: " + cert.getSubject().getCommonNameAttribute());
	System.out.println("TC Kimlik No: " + cert.getSubject().getSerialNumberAttribute());
}
	}
	
	

	private BaseSignedData getBaseSignedData() {
		// TODO Auto-generated method stub
		return null;
	}

	private SignedDataValidationResult getValidationResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
