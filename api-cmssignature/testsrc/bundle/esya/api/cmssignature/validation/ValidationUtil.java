package bundle.esya.api.cmssignature.validation;

import junit.framework.TestCase;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Hashtable;

public class ValidationUtil extends TestCase
{
	protected static String BASE_DIR = "T:\\api-cmssignature\\testdata\\cmssignature\\";
	protected static String CONFIG_FILE = "T:\\api-parent\\resources\\ug\\config\\esya-signature-config.xml";

	public static void checkByCommonAPI(byte [] signatureBytes) throws Exception {

		Context context = new Context(new File(BASE_DIR).toURI());
		context.setConfig(new Config(CONFIG_FILE));

		ByteArrayInputStream inputStream = new ByteArrayInputStream(signatureBytes);
		SignatureContainer signatureContainer = SignatureFactory.readContainer(SignatureFormat.CAdES, inputStream, context);

		ContainerValidationResult validationResult = signatureContainer.verifyAll();

		if (validationResult.getResultType() != ContainerValidationResultType.ALL_VALID) {
			System.out.println(validationResult.toString());
			assert validationResult.getResultType() == ContainerValidationResultType.ALL_VALID;
		}
	}

	public static void checkSignatureIsValid(byte [] signature, ISignable externalContent)throws Exception {
		CMSSignatureTest cmsSignatureTest = new CMSSignatureTest();
		SignedDataValidationResult sdvr = validate(signature, externalContent, cmsSignatureTest.getPolicy());

		if(sdvr.getSDStatus() != SignedData_Status.ALL_VALID)
			throw new Exception(sdvr.toString());
	}




	public static SignedDataValidationResult validate(byte [] signature, ISignable externalContent) throws Exception
	{
		CMSSignatureTest cmsSignatureTest = new CMSSignatureTest();
		return validate(signature, externalContent, cmsSignatureTest.getPolicy());
	}
	
	
	public static SignedDataValidationResult validate(byte [] signature, ISignable externalContent, ValidationPolicy policy) throws Exception
	{
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(EParameters.P_CERT_VALIDATION_POLICY, policy);
		if(externalContent != null)
			params.put(EParameters.P_EXTERNAL_CONTENT, externalContent);
		
		SignedDataValidation sdv = new SignedDataValidation();
		SignedDataValidationResult sdvr = sdv.verify(signature, params);
		
		return sdvr;
	}
	
	public static CertificateStatusInfo validate(ECertificate aCert) throws Exception
	{
		CMSSignatureTest cmsSignatureTest = new CMSSignatureTest();
		return validate(aCert, cmsSignatureTest.getPolicy());
	}
	
	public static CertificateStatusInfo validate(ECertificate aCert, ValidationPolicy policy) throws ESYAException 
	{
		ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
		vs.setBaseValidationTime(Calendar.getInstance());
		return CertificateValidation.validateCertificate(vs, aCert);
	}
}
