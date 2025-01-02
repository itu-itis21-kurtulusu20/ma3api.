package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CertificateCheckerResultObject;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;

import java.text.MessageFormat;
import java.util.List;

@SuppressWarnings("serial")

public class CertificateValidationException extends CMSSignatureException 
{
	private CheckerResult checkerResult;
	private ECertificate certificate;

	public CertificateValidationException(ECertificate certificate, CheckerResult aCheckResult)
	{
		checkerResult = aCheckResult;
		this.certificate = certificate;
	}
	
	public CertificateValidationException(CheckerResult aCheckResult)
	{
		checkerResult = aCheckResult;
	}
	
	public CheckerResult getCheckerResult()
	{
		return checkerResult;
	}
	
	public CertificateStatusInfo getCertStatusInfo()
	{
		return ((CertificateCheckerResultObject) checkerResult.getResultObject()).getCertStatusInfo();
	}
	

	public String toString() 
	{
		StringBuilder sb = new StringBuilder();

		sb.append(MessageFormat.format("Certificate: {0}. ", certificate.getSubject().getCommonNameAttribute()));
		
		sb.append(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_VALIDATION_EXCEPTION));
		
		if(checkerResult.getResultObject() != null)			
		{
			if(checkerResult.getResultStatus()!=CheckerResult_Status.SUCCESS){
				sb.append(getCertStatusInfo().getDetailedMessage());
				return sb.toString();		
			}
			
			for(CheckerResult result : checkerResult.getCheckerResults()){
				if(result.getResultStatus()!=CheckerResult_Status.SUCCESS){
					sb.append(result.getCheckResult());
					return sb.toString();
				}
			}
			
		}
		List<IValidationResult> results = checkerResult.getMessages();
		for (IValidationResult iValidationResult : results) 
		{
			sb.append(iValidationResult.toString());
		}

		return sb.toString();

	}
	
	@Override
	public String getMessage()
	{
		return toString();
	}
	
}
