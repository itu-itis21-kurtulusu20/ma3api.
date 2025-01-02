package dev.esya.api.dist.manualExamples;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager.SmartCardManager;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;

public class SertifikaDogrulamaKutuphanesininKullanimi extends TestCase 
{
	private static final String POLICY_FILE = "T:\\api-parent\\sample-policy\\policy.xml";

	public void testSertifikaDogrulama() throws ESYAException
	{
		ValidationPolicy policy = TestConstants.getPolicy();
		ECertificate cert = SmartCardManager.getInstance().getSignatureCertificate(TestConstants.getCheckQCStatement());
		
ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
vs.setBaseValidationTime(Calendar.getInstance());
CertificateStatusInfo csi = CertificateValidation.validateCertificate(vs, cert);
	}
	
	
	public void addPolitikayaBulucuEkle() throws Exception
	{
ValidationPolicy POLICY = PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));

//For UEKAE Test Environment, we add our test roots.
HashMap<String, Object> parameters = new HashMap<String, Object>();
parameters.put("dizin", "T:\\api-cmssignature\\testdata\\support\\UGRootCerts\\");
POLICY.bulmaPolitikasiAl().addTrustedCertificateFinder("tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted.TrustedCertificateFinderFromFileSystem",
		parameters);
	}
}
