package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;


import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;

import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponderID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationCheckerFromMap;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationFromOCSPChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationInfoMap;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp.OCSPResponseFinderFromAIA;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SignatureTimeStampAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.certval.*;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Checks signer's certificate
 * @author aslihan.kubilay
 *
 */
public class CertificateChecker extends BaseChecker
{
	private boolean strictReferences = false;
	private Signer mSigner;

	public CertificateChecker()
	{
	}

	public CertificateChecker(boolean aStrictReferences)
	{
		strictReferences = aStrictReferences;
	}

	@Override
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_VALIDATION_CHECKER), CertificateChecker.class);
		mSigner = aSigner;
		try 
		{
			ECertificate cert = aSigner.getSignerCertificate();
			Calendar signingTime = _findDate(aSigner.getSignerInfo());

			Boolean ignoreGrace = (Boolean)getParameters().get(AllEParameters.P_IGNORE_GRACE);

			boolean closeFinders = Boolean.TRUE.equals(getParameters().get(AllEParameters.P_VALIDATION_WITHOUT_FINDERS));

			if (!closeFinders) {
				if(isSignatureTypeAboveEST(aSigner.getType()))
				{
					closeFinders = Boolean.TRUE.equals(getParameters().get(AllEParameters.P_FORCE_STRICT_REFERENCE_USE));
					if (!Boolean.TRUE.equals(getParameters().get(AllEParameters.P_ESAV3_ABOVE_EST))) {
						strictReferences = closeFinders;
					}
				}
			}

			boolean allResult = checkCertificateAtTime(cert, aCheckerResult, signingTime, ignoreGrace, closeFinders);
			CertificateCheckerResultObject resultObj = (CertificateCheckerResultObject) aCheckerResult.getResultObject();

			List<Checker> checkers = new ArrayList<Checker>();

			if((aSigner.isTurkishProfile() || getParameters().containsKey(AllEParameters.P_VALIDATION_PROFILE)) && resultObj != null)
			{
				if(resultObj.getCertStatusInfo().getCertificateStatus() == CertificateStatus.VALID){
					if(!Boolean.TRUE.equals(getParameters().get(AllEParameters.P_PADES_SIGNATURE))){
						ProfileRevocationValueMatcherChecker profileRevValueChecker = new ProfileRevocationValueMatcherChecker(resultObj.getCertStatusInfo());
						checkers.add(profileRevValueChecker);
					}
				}
			}

			for(Checker checker:checkers)
			{
				CheckerResult cresult = new CheckerResult();
				checker.setParameters(getParameters());
				boolean result = checker.check(aSigner, cresult);
				allResult = allResult && result;
				aCheckerResult.addCheckerResult(cresult);
			}
			if(!allResult)
				aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
			return allResult;
		}
		catch (Exception aEx) 
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_VALUE_NOT_FOUND),aEx));
			return false;
		}

	}

	public boolean checkCertificateAtTime(ECertificate cert,CheckerResult aCheckerResult, Calendar aSigningTime, 
			boolean returnTrustedCerts, boolean ignoreGrace, boolean aCloseFinders)
	{

		Object policyO = getParameters().get(AllEParameters.P_CERT_VALIDATION_POLICY);
		Object policiesO = getParameters().get(AllEParameters.P_CERT_VALIDATION_POLICIES);
		if(policyO==null && policiesO==null)
		{
			aCheckerResult.addMessage(new ValidationMessage(
					CMSSignatureI18n.getMsg(E_KEYS._0_MISSING_PARAMETER,"P_CERT_VALIDATION_POLICIES"),
					new NullParameterException()));
			return false;
		}
		//ValidationPolicy policy = null;
		CertValidationPolicies policies = null;

		if (policiesO!=null){
			try {
				policies = (CertValidationPolicies)policiesO;
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_POLICY_FILE","ValidationPolicy"),
						aEx));
				return false;
			}
			logger.info("Found 'policies' parameter, ignoring policy file param");
		}
		else {
			// if no policies param found, seek policy and register it as  default policy
			try
			{
				ValidationPolicy policy = (ValidationPolicy) policyO;
				policies = new CertValidationPolicies();
				policies.register(null, policy);
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_POLICY_FILE","ValidationPolicy"),
						aEx));
				return false;
			}
		}


		Object initialCertsO = getParameters().get(AllEParameters.P_ALL_CERTIFICATES);
		List<ECertificate> initialCerts = new ArrayList<ECertificate>();
		if(initialCertsO != null)
		{
			try
			{
				initialCerts = (List<ECertificate>) initialCertsO;
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_ALL_CERTIFICATES"," List<ECertificate>"),
						aEx));
				return false;
			}
		}

		Object initialCrlsO = getParameters().get(AllEParameters.P_ALL_CRLS);
		List<ECRL> initialCrls = new ArrayList<ECRL>();
		if(initialCrlsO != null)
		{
			try
			{
				initialCrls = (List<ECRL>) initialCrlsO;
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_ALL_CRLS"," List<ECRL>"),
						aEx));
				return false;
			}
		}

		Object initialOCSPsO = getParameters().get(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES);
		List<EBasicOCSPResponse> initialBasicOCSPs = new ArrayList<EBasicOCSPResponse>();
		List<EOCSPResponse> initialOCSPs = new ArrayList<EOCSPResponse>();
		if(initialOCSPsO != null)
		{
			try
			{
				initialBasicOCSPs = (List<EBasicOCSPResponse>) initialOCSPsO;
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_ALL_BASIC_OCSP_RESPONSES"," List<EBasicOCSPResponse>"),
						aEx));				
				return false;
			}
		}

		for(EBasicOCSPResponse resp:initialBasicOCSPs)
		{
			initialOCSPs.add(CMSSignatureUtil.convertBasicOCSPRespToOCSPResp(resp));
		}

		Object trustedCertsO = getParameters().get(AllEParameters.P_TRUSTED_CERTIFICATES);
		List<ECertificate> trustedCerts = new ArrayList<ECertificate>();
		if(trustedCertsO != null)
		{
			try
			{
				trustedCerts = (List<ECertificate>) trustedCertsO;
			}
			catch(ClassCastException aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(
						CMSSignatureI18n.getMsg(E_KEYS._0_WRONG_PARAMETER_TYPE_1_, "P_TRUSTED_CERTIFICATES"," List<ECertificate>"),
						aEx));
				return false;
			}
		}


		Calendar currentTime = _getCurrentTime();

		//setting certificate validation time interval
		Calendar baseValidationTime;
		Calendar lastRevocationTime;

		Long gracePeriod = (Long)getParameters().get(AllEParameters.P_GRACE_PERIOD);
		Long revocInfoPeriod = (Long)getParameters().get(AllEParameters.P_REVOCINFO_PERIOD);

		boolean doNotUsePastRevocationInfo;

		Calendar endOfGrace = (Calendar)aSigningTime.clone();
		endOfGrace.add(Calendar.SECOND, gracePeriod.intValue());

		//If grace period has passed, validate certificate in the past.
		if(currentTime.after(endOfGrace))
		{
			doNotUsePastRevocationInfo = true;
		}
		else
		{
			//Validate now, CRL has not published yet. 
			doNotUsePastRevocationInfo = false;
		}

		if(ignoreGrace == true)
			doNotUsePastRevocationInfo = false;

		baseValidationTime = aSigningTime;
		if(revocInfoPeriod != null)
		{
			lastRevocationTime = (Calendar) baseValidationTime.clone(); 
			lastRevocationTime.add(Calendar.SECOND, revocInfoPeriod.intValue());
			//Last revocation time must not be after the certificate expiration date.
			//If the signature time is close to the certificate expiration date, last revocation time
			//can pass the certificate expiration date because of grace period. 
			if(lastRevocationTime.after(cert.getNotAfter()))
				lastRevocationTime = getLaterTime(cert.getNotAfter(), endOfGrace); 
		}
		else
			//If there is no revocation info period is defined. Select end of grace period or the 
			//certificate expiration date.
			lastRevocationTime = getLaterTime( cert.getNotAfter(), endOfGrace);

		ValidationInfoResolver vir = (ValidationInfoResolver)getParameters().get(AllEParameters.P_VALIDATION_INFO_RESOLVER);
		if(vir != null && !strictReferences){
			initialCerts.addAll(vir.getInitialCertificates());
			initialCrls.addAll(vir.getInitialCRLs());
			initialOCSPs.addAll(vir.getInitialOCSPResponses());
		}

		CertificateStatusInfo csi = null;
		ValidationSystem vs = null;
		try
		{
			ValidationPolicy policy = policies.getPolicyFor(cert);
			vs = CertificateValidation.createValidationSystem(policy);
			if(aCloseFinders)
			{
				vs.getFindSystem().getCertificateFinders().clear();
				vs.getFindSystem().getCRLFinders().clear();
				vs.getFindSystem().getOCSPResponseFinders().clear();
				List<RevocationChecker> revocationCheckers = vs.getCheckSystem().getRevocationCheckers();
				for (RevocationChecker revocationChecker : revocationCheckers) 
				{
					revocationChecker.getFinders().removeAll(revocationChecker.getFinders());
				}
			}
			if (strictReferences){
				vs.getFindSystem().findTrustedCertificates();
				try
				{
					RevocationInfoMap map =  fillRevocationInfoMap(cert,
							vs.getFindSystem().getTrustedCertificates(),
							initialCerts, 
							initialCrls, 
							initialOCSPs);
					List<RevocationChecker> list = new ArrayList<RevocationChecker>();
					RevocationChecker checker = new RevocationCheckerFromMap(map);
					checker.setParentSystem(vs);
					list.add(checker);
					vs.getCheckSystem().setRevocationCheckers(list);
				}
				catch(Exception aEx){
					logger.debug("Error occurred in RevocationInfoMap generation", aEx);
					aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_REVOCATION_MAP_INCOMPLETE)));       			
					aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
					return false;
				}
			}

		}
		catch(ESYAException aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(
					"Can not create validationsystem from policy",aEx));
			return false;
		}

		try
		{
			vs.setBaseValidationTime(baseValidationTime);
			vs.setLastRevocationTime(lastRevocationTime);
			vs.setUserInitialCertificateSet(initialCerts);
			if (!strictReferences){
				vs.setUserInitialCRLSet(initialCrls);
				vs.setUserInitialOCSPResponseSet(initialOCSPs);
			}
			vs.setValidCertificateSet(trustedCerts);

			Object digestAlgO = getParameters().get(AllEParameters.P_OCSP_DIGEST_ALG);
			if (digestAlgO != null) {
				List<RevocationChecker> rvCheckers = vs.getCheckSystem().getRevocationCheckers();
				for (RevocationChecker rvChecker : rvCheckers) {
					if (rvChecker instanceof RevocationFromOCSPChecker) {
						for (Finder ocspChecker : rvChecker.getFinders()) {
							if (ocspChecker instanceof OCSPResponseFinderFromAIA) {
								((OCSPResponseFinderFromAIA) ocspChecker).setDigestAlgForOcspFinder((DigestAlg) digestAlgO);
							}
						}
					}
				}
			}

			csi = CertificateValidation.validateCertificate(vs, cert, doNotUsePastRevocationInfo);
			if(returnTrustedCerts == true)
				csi.setTrustedCertificates(vs.getFindSystem().getTrustedCertificates());

			CertificateCheckerResultObject resultObject = new CertificateCheckerResultObject(csi, aSigningTime);
			aCheckerResult.setResultObject(resultObject);

		}
		catch(Exception aEx)
		{
			logger.debug("Error occurred in cert validation", aEx);
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CERTIFICATE_VALIDATION_UNSUCCESSFUL),aEx));
			aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
			return false;
		}

		if(csi.getCertificateStatus()==CertificateStatus.VALID)
		{
			logger.debug("Validated certificate.");
			aCheckerResult.addMessage(new ValidationMessage(csi.getDetailedMessage()));
			aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
			return true;
		}
		else
		{
			logger.debug("Cannot validate certificate: "+csi.getDetailedMessage());
			aCheckerResult.addMessage(new ValidationMessage(csi.getDetailedMessage()));
			aCheckerResult.setResultStatus(CheckerResult_Status.UNSUCCESS);
			return false;
		}
	}

	public boolean checkCertificateAtTime(ECertificate aCert,CheckerResult aCheckerResult, Calendar aSigningTime, boolean aIgnoreGrace, boolean aCloseFinders)
	{
		return checkCertificateAtTime(aCert, aCheckerResult, aSigningTime, false, aIgnoreGrace, aCloseFinders);
	}


	private Calendar getLaterTime(Calendar time1, Calendar time2) 
	{
		return time1.after(time2) ? time1 : time2;
	}

	//	private Date addSeconds(Date aTime, Long sSeconds) 
	//	{
	//		return new Date(aTime.getTime() + sSeconds * 1000);
	//	}

	private Calendar _getCurrentTime() 
	{
		if(getParameters().containsKey(AllEParameters.P_CURRENT_TIME))
			return ((Calendar)getParameters().get(AllEParameters.P_CURRENT_TIME));

		return Calendar.getInstance();
	}

	/*
	 * First try to get time information from signature timestamp,second from the signingtime attribute and if not found,
	 * return the current time information
	 */
	private Calendar _findDate(ESignerInfo aSignerInfo)
			throws CMSSignatureException
			{
		try
		{
			List<EAttribute> tsAttrs = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
			if(!tsAttrs.isEmpty())
			{
				//get time information from the first signature timestamp
				EAttribute tsAttr = tsAttrs.get(0);
				return SignatureTimeStampAttr.toTime(tsAttr);
				/*
				EContentInfo ci = new EContentInfo(tsAttr.getValue(0));
				ESignedData sd = new ESignedData(ci.getContent());
				ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
				return tstInfo.getTime();
				 */				
			}

			if (Boolean.TRUE.equals(getParameters().get( AllEParameters.P_ESAV3_ABOVE_EST))) {
				tsAttrs = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
				// if BES+v3, get time information from the first atsv3
				EAttribute tsAttr = tsAttrs.get(0);
				return SignatureTimeStampAttr.toTime(tsAttr);

			}

			Boolean trustSigningTimeAttr = false;
			if(getParameters().containsKey(AllEParameters.P_TRUST_SIGNINGTIMEATTR))
				trustSigningTimeAttr = (Boolean) getParameters().get(AllEParameters.P_TRUST_SIGNINGTIMEATTR);

			if(trustSigningTimeAttr == true)
			{
				List<EAttribute> stAttrs = aSignerInfo.getSignedAttribute(AttributeOIDs.id_signingTime);
				if(!stAttrs.isEmpty())
				{
					EAttribute stAttr = stAttrs.get(0);
					Time time = new Time();
					Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
					time.decode(decBuf);
					Calendar signingTime = UtilTime.timeToCalendar(time);
					if(signingTime == null)
						throw new CMSSignatureException("Error while getting signing time information from signerinfo");
					return signingTime;
				}

				if (getParameters().containsKey(AllEParameters.P_SIGNING_TIME_ATTR)) {
					Calendar signingTime = ((Calendar) getParameters().get(AllEParameters.P_SIGNING_TIME_ATTR));
					if (signingTime != null)
						return signingTime;
				}
			}

			if (getParameters().containsKey(AllEParameters.P_SIGNING_TIME)){
				Calendar signingTime = ((Calendar)getParameters().get(AllEParameters.P_SIGNING_TIME));
				if (signingTime!=null)
					return signingTime;
			}
			//signer is BES, one of its parent ESA
			if (getParameters().containsKey(AllEParameters.P_PARENT_ESA_TIME)){
				Calendar parentESATime = ((Calendar)getParameters().get(AllEParameters.P_PARENT_ESA_TIME));
				if (parentESATime!=null)
					return parentESATime;
			}

			return _getCurrentTime();
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error while getting signing time information from signerinfo",aEx);
		}
			}

	private RevocationInfoMap fillRevocationInfoMap(ECertificate cert,
			List<ECertificate> trustedCerts,
			List<ECertificate> initialCerts,
			List<ECRL> initialCrls,
			List<EOCSPResponse> initialOCSPs)
					throws ESYAException
					{
		RevocationInfoMap map = new RevocationInfoMap();

		ValidationInfoResolver resolver = new ValidationInfoResolver();
		ValidationInfoResolver vir = (ValidationInfoResolver)getParameters().get(AllEParameters.P_VALIDATION_INFO_RESOLVER);
		if(vir != null){
			resolver = vir;        	
		}
		resolver.addCertificates(initialCerts);
		resolver.addCertificates(trustedCerts);
		resolver.addCRLs(initialCrls);
		resolver.addOCSPResponses(initialOCSPs);


		ECompleteCertificateReferences certificateReferences = mSigner.getSignerInfo().getCompleteCertificateReferences();
		ECompleteRevocationReferences revocationReferences = mSigner.getSignerInfo().getCompleteRevocationReferences();

		if (certificateReferences.getCount()!=revocationReferences.getCount()-1){
			logger.error("Cert and Revocation Reference counts does not match! ");
		}

		EOtherCertID[] certIDs = certificateReferences.getCertIDs();
		ECrlOcspRef[] revIDs = revocationReferences.getCrlOcspRefs();

		for (int i=0; i<revIDs.length; i++){

			ECrlOcspRef crlOcspRef = revIDs[i];

			ECrlValidatedID[] crlIds = crlOcspRef.getCRLIds();
			EOcspResponsesID[] ocspIds = crlOcspRef.getOcspResponseIds();

			boolean foundRevInfo = false;
			boolean foundCert = false;


			List<ECRL> foundCRLs = new ArrayList<ECRL>();
			List<EOCSPResponse> foundResponses = new ArrayList<EOCSPResponse>();

			if (crlIds!=null){
				for (ECrlValidatedID crlId : crlIds) {
					CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

					crlSearchCriteria.setDigestValue(crlId.getDigestValue());
					crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(crlId.getDigestAlg()));

					if (crlId.getObject().crlIdentifier != null) {
						crlSearchCriteria.setIssuer(crlId.getCrlissuer().stringValue());
						crlSearchCriteria.setIssueTime(crlId.getCrlIssuedTime().getTime());
						crlSearchCriteria.setNumber(crlId.getCrlNumber());
					}
					List<ECRL> crls = resolver.resolve(crlSearchCriteria);
					if (crls!=null && crls.size() > 0) {
						foundRevInfo = true;
						foundCRLs.add(crls.get(0));
					}
				}
			}

			if (ocspIds!=null){
				for (EOcspResponsesID ocspId : ocspIds) {
					OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();

					if(ocspId.getObject().ocspRepHash != null){
						ocspSearchCriteria.setDigestValue(ocspId.getDigestValue());
						ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(ocspId.getDigestAlg()));
					}
					EResponderID responder = ocspId.getOcspResponderID();
					if (responder.getType()==EResponderID._BYKEY)
						ocspSearchCriteria.setOCSPResponderIDByKey(responder.getResponderIdByKey());
					else
						ocspSearchCriteria.setOCSPResponderIDByName(responder.getResponderIdByName().stringValue());
					ocspSearchCriteria.setProducedAt(ocspId.getProducedAt().getTime());

					List<EOCSPResponse> ocsps = resolver.resolve(ocspSearchCriteria);
					if (ocsps!=null && ocsps.size() > 0) {
						foundRevInfo = true;
						foundResponses.add(ocsps.get(0));
					}
				}
			}

			ECertificate certificate = cert;
			CertificateSearchCriteria certificateSearchCriteria=null;
			if (i>0){
				EOtherCertID certId = certIDs[i-1];

				certificateSearchCriteria = new CertificateSearchCriteria();
				// todo csc.setIssuer();
				certificateSearchCriteria.setDigestValue(certId.getDigestValue());
				certificateSearchCriteria.setDigestAlg(DigestAlg.fromOID(certId.getDigestAlg()));

				certificateSearchCriteria.setSerial(certId.getIssuerSerial());
				List<ECertificate> certs = resolver.resolve(certificateSearchCriteria);
				if (certs.size()==0){
					logger.error("Cant resolve CERT reference "+certificateSearchCriteria);
				}
				// buraya bir else koymak lazim gibi geldi, hata vermisti size 0 olunca
				else {
					certificate = certs.get(0);
					foundCert = true;
				}
			}

			if(foundRevInfo == false) {
				if(certificate.isSelfIssued()) {
					logger.trace("Self Signed Certificate revocation info does not exist as expected. " + certificate.getSubject().getCommonNameAttribute());
				} else if(certificate.isOCSPSigningCertificate() && certificate.hasOCSPNoCheckExtention()) {
					logger.trace("OCSP Certificate revocation info does not exist as expected. " + certificate.getSubject().getCommonNameAttribute());
				} else {
					logger.error("Cant resolve CERT revocation info for "+ certificate);
				}
			}

			if (i==0 || foundCert){
				for (ECRL crl : foundCRLs){
					map.appendCRL(certificate, crl);
				}
				for (EOCSPResponse ocspResponse : foundResponses){
					map.appendOCSPResponse(certificate, ocspResponse);
				}
			}
		}
		return map;
					}
}