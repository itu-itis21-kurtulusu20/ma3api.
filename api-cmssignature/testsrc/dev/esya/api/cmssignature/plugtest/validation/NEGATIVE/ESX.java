package dev.esya.api.cmssignature.plugtest.validation.NEGATIVE;

import dev.esya.api.cmssignature.plugtest.validation.CMSValidationTest;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;

/**
 * Certificates and revocation references must be in certificate store in order to verify
 * the signature for ESX types
 * @author aslihan.kubilay
 *
 */
public class ESX extends TestCase
{

	private static final String INPUT_DIR = CMSValidationTest.INPUT_DIRECTORY_PATH+"//negative//esx//";
	private static ValidationPolicy POLICY_FILE = null;
	private static final String CERTS_CRLS_PATH = CMSValidationTest.INPUT_DIRECTORY_PATH+"//certscrls//";
	
	public ESX() throws FileNotFoundException, ESYAException 
	{
		POLICY_FILE = PolicyReader.readValidationPolicy(new FileInputStream(INPUT_DIR+"policyESX.xml"));
	}
	//Add reference values before running test cases
	static 
	{
		try
		{
			CertStore cs = new CertStore();
			
			CertStoreCertificateOps cerOps = new CertStoreCertificateOps(cs);
			cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-LevelA.cer"), 1);
			cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-LevelB.cer"), 1);
			cerOps.writeCertificate(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-Root.cer"), 1);
			
			
			CertStoreCRLOps crlOps = new CertStoreCRLOps(cs);
			crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-LevelA-20090206.crl"), 1L);
			crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-LevelB-20090206.crl"), 1L);
			crlOps.writeCRL(AsnIO.dosyadanOKU(CERTS_CRLS_PATH+"Signature-C-XN-1.CERT-SIG-Root-20090206.crl"), 1L);
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than the time 
	 * in TimestampedCertsCRLs
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *	26-Oct-2008 01:00Z - SignatureTimeStamp
	 *	26-Oct 2008 00:00Z - TimestampedCertsCRLs
	 *
	 *
	 *	In order to validate the signer certificate,reference values must be added to certstore prior
	 * 
	 */
	public void testVerifyingESX1()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XN-1.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	}
	
	
	/**
	 * This is a negative test case for verifying time ordering between time stamps.
	 * In this test case, The time in the SignatureTimeStamp is ulterior than the time in ESCTimeStamp.
	 * 
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *  26-Oct-2008 01:00Z - SignatureTimeStamp
	 *  26-Oct 2008 00:00Z - ESCTimeStamp
	 *  
	 *  
	 * In order to validate the signer certificate,reference values must be added to certstore prior
	 */
	public void testVerifyingESX2()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XN-2.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	} 
	
	
	/**
	 * This is a negative test case for TimestampedCertsCRLs.
	 * The hash value of messageImprint in TimestampedCertsCRLs does *NOT* match to the hash value of
	 * corresponding CompleteCertificateRefs and CompleteRevocationRefs.
	 * 
	 * ETSI Invalid-Sig Valid-Cert
	 *	26-Oct-2008 00:00Z - SignatureTimeStamp
	 *	26-Oct 2008 01:00Z - TimestampedCertsCRLs
	 * 
	 * In order to validate the signer certificate,reference values must be added to certstore prior
	 * 
	 */
	public void testVerifyingESX3()
	throws Exception
	{
		byte[] input = AsnIO.dosyadanOKU(INPUT_DIR+"Signature-C-XN-3.p7s");
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		
		params.put(EParameters.P_CERT_VALIDATION_POLICY, POLICY_FILE);
		
		SignedDataValidation sd = new SignedDataValidation();
		SignedDataValidationResult sdvr = sd.verify(input, params);
		
		assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
		
	} 
}
