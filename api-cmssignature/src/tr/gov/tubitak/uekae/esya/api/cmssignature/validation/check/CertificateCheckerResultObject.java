package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import java.io.Serializable;
import java.util.Calendar;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;


public class CertificateCheckerResultObject implements Serializable
{
	CertificateStatusInfo certStatusInfo;
	Calendar signingTime;
	
	public CertificateCheckerResultObject(CertificateStatusInfo aCertStatusInfo, Calendar aSigningTime)
	{
		certStatusInfo = aCertStatusInfo;
		signingTime = aSigningTime;
	}
	
	public Calendar getSigningTime()
	{
		return signingTime;
	}
	
	public CertificateStatusInfo getCertStatusInfo()
	{
		return certStatusInfo;
	}
}
