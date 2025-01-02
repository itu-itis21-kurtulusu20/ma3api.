package dev.esya.api.dist.manualExamples;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Examples gets the test constants from that class. Default UGTestConstants is set.
 * @author orcun.ertugrul
 *
 */
public class TestConstants 
{
	private static final String DIRECTORY = "";
	
	private static final String PIN = "12345";

	private static ValidationPolicy POLICY;
	
	private static final String POLICY_FILE = "T:\\api-parent\\sample-policy\\policy.xml";
	
	public static String getDirectory()
	{
		return DIRECTORY;
	}
	
	public synchronized static ValidationPolicy getPolicy() throws ESYAException
	{
		if(POLICY == null)
		{
			try 
			{
				POLICY = PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));
				
				//For UEKAE Test Environment, we add our test roots.
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dizin", "T:\\api-cmssignature\\testdata\\support\\UGRootCerts\\");
				POLICY.bulmaPolitikasiAl().addTrustedCertificateFinder("tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted.TrustedCertificateFinderFromFileSystem",
						parameters);
			} 
			catch (FileNotFoundException e) 
			{
				throw new RuntimeException("Policy file could not be found", e);
			}
		}
		return POLICY;
	}
	
	public static TSSettings getTSSettings()
	{
		return new TSSettings("http://tzd.kamusm.gov.tr", 1901, "12345678");
	}
	
	public static DigestAlg getTSDigestAlg()
	{
		return DigestAlg.SHA1;
	}
	
	//To-Do Get PIN from user
	public static String getPIN()
	{
		return PIN;
	}
	public static boolean getCheckQCStatement()
	{
		return false;
	}

	public static boolean validateCertificate()
	{
		return false;
	}
}

