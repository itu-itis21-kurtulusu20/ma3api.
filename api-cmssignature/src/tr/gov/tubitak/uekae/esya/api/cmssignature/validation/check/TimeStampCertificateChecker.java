package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.*;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks the certificate of timestamp server
 * @author aslihan.kubilay
 *
 */
public class TimeStampCertificateChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(TimeStampCertificateChecker.class);
	private ESignedData mSignedData = null;
    private boolean closeFinders = false;

	public TimeStampCertificateChecker(ESignedData aSignedData)
	{
		mSignedData = aSignedData;
	}

	@Override
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult) 
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_CERTIFICATE_CHECKER), TimeStampCertificateChecker.class);
		Calendar tsTime = null;
		Calendar nextTSTime = null;
		try
		{
			//get time of the timestamp
			ETSTInfo tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
			tsTime = tstInfo.getTime();
			
			//Get time of successor time stamp.
			List<Calendar> timeStampTimes = getTimeStampTimes(aSigner.getSignerInfo());
			int index = timeStampTimes.indexOf(tsTime);
			if(timeStampTimes.size() > index+1)
			{
				///TODO aslında burada ats-hash indexe göre sertifika eklenmeli ve çıkarılmalı. bknz ->ATSHashIndexCollector
				nextTSTime = timeStampTimes.get(index+1);   
				//P_FORCE_STRICT_REFERENCE_USE parametresi set edilmediyse(null ise) bulucular kapatılacak.
				if(isSignatureTypeAboveEST(aSigner.getType())
						&& (Boolean.TRUE.equals(getParameters().get(AllEParameters.P_FORCE_STRICT_REFERENCE_USE)) || Boolean.TRUE.equals(getParameters().get(AllEParameters.P_VALIDATION_WITHOUT_FINDERS))))
					closeFinders = true;
				else
					closeFinders = false;
			}
			else
			{
				//signer is EST, one of its parent ESA
				if (getParameters().containsKey(AllEParameters.P_PARENT_ESA_TIME)) {
					Calendar parentESATime = ((Calendar) getParameters().get(AllEParameters.P_PARENT_ESA_TIME));
					if (parentESATime != null && parentESATime.after(tsTime)) //parentESATime her zaman sonra olmuyor mu?check kalksın?
						nextTSTime = parentESATime;
				} 
				else
					nextTSTime = Calendar.getInstance();
			}
		}
		catch(Exception aEx)
		{
			logger.warn("Warning in TimeStampCertificateChecker", aEx);
			aCheckerResult.addMessage(new ValidationMessage("Error while getting time from timestamp"));
			return false;
		}
		
		if( Boolean.TRUE.equals(getParameters().get(AllEParameters.P_PADES_SIGNATURE)) 
				&& Boolean.TRUE.equals(getParameters().get(AllEParameters.P_VALIDATION_WITHOUT_FINDERS)))
			closeFinders = true;

		ECertificate cer = mSignedData.getSignerInfo(0).getSignerCertificate(mSignedData.getCertificates());
		if(cer == null)
		{
			ESignerIdentifier signerIdentifier = mSignedData.getSignerInfo(0).getSignerIdentifier();
			cer = findCertFromFinders(signerIdentifier, aCheckerResult);
		}
			
		if(cer==null)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_CERTIFICATE_NOT_FOUND)));
			return false;
		}
		else if(!cer.isTimeStampingCertificate())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_CERTIFICATE_NOT_QUALIFIED)));
			return false;
		}
			
		CertificateChecker certificateChecker = new CertificateChecker();
		certificateChecker.setParameters(getParameters());
		
		boolean valid = certificateChecker.checkCertificateAtTime(cer, aCheckerResult, nextTSTime, true, closeFinders);		
		if (!valid){
			aCheckerResult.removeMessages();
			valid = certificateChecker.checkCertificateAtTime(cer, aCheckerResult, tsTime, true, closeFinders);
		}
		return valid;		
	}


	private List<Calendar> getTimeStampTimes(ESignerInfo aSignerInfo) throws ESYAException
	{
		List<Calendar> tsTimes = new ArrayList<Calendar>();
		List<EAttribute> tsAttributes;
		
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(ContentTimeStampAttr.OID);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(SignatureTimeStampAttr.OID);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(CAdES_C_TimeStampAttr.OID);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(TimeStampedCertsCrlsAttr.OID);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);
		addTimesOfTSs(tsAttributes, tsTimes);
		
		tsAttributes = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
		addTimesOfTSs(tsAttributes, tsTimes);	
		
		java.util.Collections.sort(tsTimes);
		
		return tsTimes;
		
	}

	private void addTimesOfTSs(List<EAttribute> tsAttributes,List<Calendar> tsTimes) throws ESYAException 
	{
		for (EAttribute attr : tsAttributes) 
		{
			tsTimes.add(SignatureTimeStampAttr.toTime(attr));
		}
	
	}

	private ECertificate findCertFromFinders(ESignerIdentifier aSignerIdentifier, CheckerResult aCheckerResult) 
	{
		ECertificate cer = null;
		try
		{
			CertificateSearchCriteria searchCriteria = null;
			if(aSignerIdentifier.getIssuerAndSerialNumber() != null)
			{
				searchCriteria = new CertificateSearchCriteria(aSignerIdentifier.getIssuerAndSerialNumber().getIssuer().stringValue(),
						aSignerIdentifier.getIssuerAndSerialNumber().getSerialNumber());
			}
			else if(aSignerIdentifier.getSubjectKeyIdentifier() != null)
			{
				searchCriteria = new CertificateSearchCriteria(aSignerIdentifier.getSubjectKeyIdentifier());
			}

			if(searchCriteria == null)
				cer = null;
			else {
				ValidationInfoResolver vir = new ValidationInfoResolver();

				List<ECertificate> certs = (List<ECertificate>)getParameters().get(AllEParameters.P_ALL_CERTIFICATES);
				vir.addCertificates(certs);

				cer = vir.resolve(searchCriteria).get(0);
			}	
		}
		catch(ClassCastException aEx)
		{
			logger.warn("Warning in TimeStampCertificateChecker", aEx);
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_,new String[]{"P_POLICY_FILE","ValidationPolicy"})));
		}

		return cer;
	}

    public void setCloseFinders(boolean closeFinders) {
        this.closeFinders = closeFinders;
    }
}
