package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.PathValidationRecord;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.Signature_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;

import java.util.*;


public class SignatureValidator
{
	private static Logger logger = LoggerFactory.getLogger(SignatureValidator.class);

	private List<Checker> mUserDefinedCheckers = new ArrayList<>();
	
	private List<ECertificate> mCerts = new ArrayList<ECertificate>();
	private List<ECRL> mCRLs = new ArrayList<ECRL>();
	private List<EBasicOCSPResponse> mOCSPs = new ArrayList<EBasicOCSPResponse>();
	

	
	BaseSignedData mSignedData = null;
	private byte [] mContentInfoBytes = null;
	/**
	 * Create SignatureValidator from byte array
	 * @param aContentInfo
	 */	
	public SignatureValidator(byte [] aContentInfo) throws CMSSignatureException
	{
    	mContentInfoBytes = aContentInfo;
		mSignedData = new BaseSignedData(aContentInfo);
	}
	
	private ArrayList<Checker> _getCheckersForSignType(ESignatureType aType) throws CMSSignatureException
	{
		ArrayList<Checker> besCheckerList = new ArrayList<Checker>();
		besCheckerList.add(new CryptoChecker());
		besCheckerList.add(new MessageDigestAttrChecker());
        besCheckerList.add(new SigningCertificateAttrChecker());
        //besCheckerList.add(new ContentTypeAttrChecker());//countersigner olmasina gore _getCheckers methodunda ekleniyor
        /*
        boolean strictReferences = false;
        // todo make it EParameters.P_USE_STRICT_REFERENCES
        if (aParams.containsKey(EParameters.P_FORCE_STRICT_REFERENCE_USE)){
            logger.debug("Use strict references is set");
            Object strict = aParams.get(EParameters.P_FORCE_STRICT_REFERENCE_USE);
            if (strict==null || !(strict instanceof Boolean)){
                logger.warn("todo_param_name_here parameter has wrong USE_STRICT_REFERENCES if set to false. ");
            }
            else
                strictReferences = (Boolean)strict;

        }
		besCheckerList.add(new CertificateChecker(strictReferences));
		*/
		besCheckerList.add(new CertificateChecker());
		if(aType == ESignatureType.TYPE_BES)
			return besCheckerList;
		
		ArrayList<Checker> epesCheckerList = new ArrayList<Checker>();
		epesCheckerList.addAll(besCheckerList);
		//epesCheckerList.add(new SignaturePolicyChecker());
		
		if(aType == ESignatureType.TYPE_EPES)
			return epesCheckerList;
		
		ArrayList<Checker> estCheckerList = new ArrayList<Checker>();
		estCheckerList.addAll(besCheckerList);
		estCheckerList.add(new SignatureTimeStampAttrChecker());
		
		if(aType == ESignatureType.TYPE_EST)
			return estCheckerList;
		
		
		ArrayList<Checker> escCheckerList = new ArrayList<Checker>();
		escCheckerList.addAll(estCheckerList);
		
		if(aType == ESignatureType.TYPE_ESC)
			return escCheckerList;
		
		ArrayList<Checker> esxt1CheckerList = new ArrayList<Checker>();
		esxt1CheckerList.addAll(escCheckerList);
		esxt1CheckerList.add(new CAdES_C_TimeStampAttrChecker());
		
		if(aType == ESignatureType.TYPE_ESX_Type1)
			return esxt1CheckerList;
		
		
		ArrayList<Checker> esxt2CheckerList = new ArrayList<Checker>();
		esxt2CheckerList.addAll(escCheckerList);
		esxt2CheckerList.add(new TimeStampedCertsCrlsRefsAttrChecker());
		
		if(aType == ESignatureType.TYPE_ESX_Type2)
			return esxt2CheckerList;
		
		ArrayList<Checker> esxlongCheckerList = new ArrayList<Checker>();
		esxlongCheckerList.addAll(escCheckerList);
		esxlongCheckerList.add(new RevocationRefsValuesMatchChecker());
		esxlongCheckerList.add(new CertificateRefsValuesMatchChecker());
		
		if(aType == ESignatureType.TYPE_ESXLong)
			return esxlongCheckerList;
		
		ArrayList<Checker> esxlongt1CheckerList = new ArrayList<Checker>();
		esxlongt1CheckerList.addAll(esxlongCheckerList);
		esxlongt1CheckerList.add(new CAdES_C_TimeStampAttrChecker());
		
		if(aType == ESignatureType.TYPE_ESXLong_Type1)
			return esxlongt1CheckerList;
		
		
		ArrayList<Checker> esxlongt2CheckerList = new ArrayList<Checker>();
		esxlongt2CheckerList.addAll(esxlongCheckerList);
		esxlongt2CheckerList.add(new TimeStampedCertsCrlsRefsAttrChecker());
		
		if(aType == ESignatureType.TYPE_ESXLong_Type2)
			return esxlongt2CheckerList;
		
		///v3 bes'e göre değişti
		ArrayList<Checker> esaCheckerList = new ArrayList<Checker>();
		esaCheckerList.addAll(besCheckerList);
		
		ArrayList<Checker> optionalCheckers = new ArrayList<Checker>();
		optionalCheckers.add(new CAdES_C_TimeStampAttrChecker());
		optionalCheckers.add(new TimeStampedCertsCrlsRefsAttrChecker());
		esaCheckerList.add(new CheckAllChecker(optionalCheckers,false));
		
		ArrayList<Checker> optionalCheckers2 = new ArrayList<Checker>();
		optionalCheckers2.add(new ArchiveTimeStampAttrChecker());
		optionalCheckers2.add(new ArchiveTimeStampV2AttrChecker());
		optionalCheckers2.add(new ArchiveTimeStampV3AttrChecker());
		esaCheckerList.add(new CheckAllChecker(optionalCheckers2,true));

		if(aType == ESignatureType.TYPE_ESA || aType == ESignatureType.TYPE_ESAv2)
			return esaCheckerList;

		throw new CMSSignatureException("UnKnown Signature Type");

	}
	
	/**
	 * Set certificates
	 * @param aCerts
	 */
	public void setCertificates(List<ECertificate> aCerts)
	{
		mCerts = aCerts;
	}
	/**
	 * Set CRLs
	 * @param aCRLs
	 */
	public void setCRLs(List<ECRL> aCRLs)
	{
		mCRLs = aCRLs;
	}
	/**
	 * Set OCSPs
	 * @param aOCSPs
	 */
	public void setOCSPs(List<EBasicOCSPResponse> aOCSPs)
	{
		mOCSPs = aOCSPs;
	}
	
	List<Checker> _getCheckers(Signer aSigner,boolean aIsCounterSigner,ESignatureType aType, Map<String, Object> aParams) throws CMSSignatureException
	{
		List<Checker> checkers = _getCheckersForSignType(aType);
		
		if(!aIsCounterSigner)
			checkers.add(new ContentTypeAttrChecker());
		
		if (aSigner.getType() == ESignatureType.TYPE_ESA || aSigner.getType() == ESignatureType.TYPE_ESAv2) { //check sırası karışık oldu biraz
			if (aSigner._checkIfSignerIsESAV2()) { 
				checkers.add(new SignatureTimeStampAttrChecker());
				checkers.add(new RevocationRefsValuesMatchChecker());
				checkers.add(new CertificateRefsValuesMatchChecker());
			} else { 
				if(aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).size()>0)
					checkers.add(new SignatureTimeStampAttrChecker());
				
				if ((aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues).size() > 0 
						&& aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues).size() > 0)) {
					checkers.add(new RevocationRefsValuesMatchChecker());
					checkers.add(new CertificateRefsValuesMatchChecker());
				} else if((aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs).size() == 0 
						&& aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs).size() == 0)){
					aParams.put(AllEParameters.P_ESAV3_ABOVE_EST, true);
				}
			}
		}
		
		if(aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_sigPolicyId).size()>0){
		    checkers.add(new SignaturePolicyChecker());

		}
		if(aSigner.isTurkishProfile()){
			if(!Boolean.TRUE.equals(aParams.get(AllEParameters.P_PADES_SIGNATURE)))
				checkers.add(new TurkishProfileAttributesChecker());
		}
		if(aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_contentTimestamp).size()>0)
		    checkers.add(new ContentTimeStampAttrChecker());
		
		return checkers;
	}
	/**
	 * Verify signature
	 * @param aSVR
	 * @param aSigner
	 * @param aIsCounterSigner signature is counter signature or not
	 * @param aParams verification parameter
	 */
	public void verify(SignatureValidationResult aSVR,Signer aSigner,boolean aIsCounterSigner,Map<String,Object> aParams)
	throws CMSSignatureException
	{
		ESignatureType type = aSigner.getType();

		List<Checker> checkersForSignatureType = _getCheckers(aSigner, aIsCounterSigner, type, aParams);


		ECertificate signerCert = _findCertificate(mCerts, aSigner.getSignerInfo().getSignerIdentifier(), aParams);
		
		if(signerCert==null)
			throw new CMSSignatureException("Signer Certificate cant be found.");
		
		checkLicense(signerCert);		
		
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put(AllEParameters.P_SIGNING_CERTIFICATE, signerCert);
		parameters.put(AllEParameters.P_SIGNED_DATA, mSignedData.getSignedData());
		parameters.put(AllEParameters.P_ALL_CERTIFICATES, mCerts);
		parameters.put(AllEParameters.P_ALL_CRLS, mCRLs);
		parameters.put(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES, mOCSPs);
		parameters.put(AllEParameters.P_CONTENT_INFO_BYTES, mContentInfoBytes);
		
		if(aParams!=null)
		{
			parameters.putAll(aParams);
		}
		
		_putParameters(parameters);
		
		
		aSVR.setSignerCertificate((ECertificate) parameters.get(AllEParameters.P_SIGNING_CERTIFICATE));
		
		if (type == ESignatureType.TYPE_ESA) { //TODO hızlandır
			
			SignedDataValidation sdv = new SignedDataValidation();
			sdv._fillCertRevocationLists(mSignedData.getSignedData(), aSigner,aParams);
			parameters.put(AllEParameters.P_ALL_CERTIFICATES, sdv.mCerts);
			parameters.put(AllEParameters.P_ALL_CRLS, sdv.mCRLs);
			parameters.put(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES,sdv.mOCSPs);
			
			if (aSigner._checkIfSignerIsESAV3()) { //arşiv v2 varsa ,önceden, bu if'e girmeyebilir mi?
				ATSHashIndexCollector atsCollector = new ATSHashIndexCollector();
				atsCollector.checkATSHashIndex(aSigner);  //TODO son ATSv3e göre dönüyor certs,crls ve ocsps. tek tek bakılmalı aslında
				 List<ECertificate> certs = sdv.mCerts;
				 List<ECRL> crls = sdv.mCRLs;
				 List<EBasicOCSPResponse> ocsps = sdv.mOCSPs;
				 certs.removeAll(atsCollector.mAllCerts); 
				 crls.removeAll(atsCollector.mAllCrls);
				 ocsps.removeAll(atsCollector.mAllOcsps);
				 
				parameters.put(AllEParameters.P_ALL_CERTIFICATES,certs);
				parameters.put(AllEParameters.P_ALL_CRLS, crls);
				parameters.put(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES,ocsps);
			}
		}

		boolean verified = true;

		List<Checker> allCheckers = new ArrayList<>();
		allCheckers.addAll(mUserDefinedCheckers);
		allCheckers.addAll(checkersForSignatureType);

		for (Checker checker: allCheckers)
		{
			checker.setParameters(parameters);		
			CheckerResult cresult = new CheckerResult();
			boolean result = checker.check(aSigner,cresult);
			aSVR.addCheckResult(cresult);			
			if (verified) verified = result;
		}
		
		Signature_Status parentSStatus = aSVR.getSignatureStatus();
		SignatureValidationResult svr = aSVR;
		
		//parent signature is invalid
		if(parentSStatus == Signature_Status.INVALID)
		{
		    svr.setSignatureStatus(Signature_Status.INVALID);
		    svr.setDescription("Imza Dogrulanamadi");
		    svr.getCheckerResults().add(_addParentSignatureCheckerResult(CheckerResult_Status.UNSUCCESS));
		}
		//parent signature is incomplete
		else if(parentSStatus == Signature_Status.INCOMPLETE)
		{
		    svr.getCheckerResults().add(_addParentSignatureCheckerResult(CheckerResult_Status.UNSUCCESS));
		    if(!verified)
		    {
			svr.setSignatureStatus(Signature_Status.INVALID);
			_interpretSignatureVerification(svr);
		    }
		    else
		    {
			svr.setSignatureStatus(Signature_Status.INCOMPLETE);
			svr.setDescription("Imza Dogrulanamadi");
		    }
		}
		//parent signature is verified or no parent signature
		else
		{
		    if(!verified)
		    {
			svr.setSignatureStatus(Signature_Status.INVALID);
			_interpretSignatureVerification(svr);
		    }
		    else
		    {
			setMaturityState(svr);
			svr.setSignatureStatus(Signature_Status.VALID);
		    }
		}
		
		List<Signer> css = aSigner.getCounterSigners();
		
		
		for(Signer cs : css)
		{
			_checkCounterSigner(aSigner,cs,aSVR,parameters);
			
		}
		
	}

	private CheckerResult _addParentSignatureCheckerResult(CheckerResult_Status status)
	{
	    CheckerResult cr = new CheckerResult();
	    cr.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNATURE_CHECKER), ParentSignatureResult.class);
	    cr.setResultStatus(status);
	    
	    if(status == CheckerResult_Status.SUCCESS)
	    	cr.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNATURE_VALID)));
	    else
	    	cr.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.PARENT_SIGNATURE_INVALID)));
	    
	    return cr;
	}
	
	
	private void _checkCounterSigner(Signer aParent,Signer aSigner,SignatureValidationResult aSVR,Map<String,Object> aParams)
	throws CMSSignatureException
	{
		aParams.put(AllEParameters.P_PARENT_SIGNER_INFO, aParent.getSignerInfo());
		
		ECertificate signerCert = _findCertificate(mCerts, aSigner.getSignerInfo().getSignerIdentifier(), aParams);
		
		if(signerCert==null)
			throw new CMSSignatureException("Signer Certificate cant be found.");
		
		aParams.put(AllEParameters.P_SIGNING_CERTIFICATE, signerCert); 
		/* yukarıdan geliyor zaten certs,crls ve ocsps. sadece arşiv tarafından korunulanlar geliyor hem. Tekrar koymak yanlış gibi.
		aParams.put(AllEParameters.P_ALL_CERTIFICATES, mCerts);
		aParams.put(AllEParameters.P_ALL_CRLS, mCRLs);
		aParams.put(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES, mOCSPs);
		*/
		try {
			if (!aParams.containsKey(AllEParameters.P_PARENT_ESA_TIME)) {
				Calendar archiveTime = aSigner.getESAv2Time();
				if (archiveTime == null) {
					archiveTime = aSigner.getESAv3Time();
				}
				if (archiveTime != null) {
					aParams.put(AllEParameters.P_PARENT_ESA_TIME, archiveTime);
				}
			}
		} catch (ESYAException e) {
			logger.warn("Warning in SignatureValidator", e);
			throw new CMSSignatureException("Error occurred while checking this signer protected by any ATS v3 or not");
		}
		
		SignatureValidationResult svr = new SignatureValidationResult();
		if(aSVR.getSignatureStatus() != Signature_Status.VALID)
		{
		    svr.setSignatureStatus(aSVR.getSignatureStatus());
		}
		
		verify(svr,aSigner,true,aParams);
	
		svr.setSignerCertificate(signerCert);
		
		aSVR.addCounterSigValidationResult(svr);
		
		aParams.remove(AllEParameters.P_PARENT_ESA_TIME);
	}
	
	private void checkLicense(ECertificate aCer) 
	{
		try
    	{
    		boolean isTest = LV.getInstance().isTL(Urunler.CMSIMZA);
    		if(isTest)
    			if(!aCer.getSubject().getCommonNameAttribute().toLowerCase().contains("test"))
    			{
    				throw new ESYARuntimeException("You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
    			}
    	}
    	catch(LE ex)
    	{
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
		
	}

	private void _putParameters(Map<String,Object> aParameters) 
	{
		if(!aParameters.containsKey(AllEParameters.P_TRUST_SIGNINGTIMEATTR))
		{
			aParameters.put(AllEParameters.P_TRUST_SIGNINGTIMEATTR, DefaultValidationParameters.DEFAULT_TRUST_SIGNINGTIMEATTR);
			if(logger.isDebugEnabled())
				logger.debug("P_TRUST_SIGNINGTIMEATTR parameter is not set by user.P_TRUST_SIGNINGTIMEATTR is set to false.");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_TRUST_SIGNINGTIMEATTR) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_TRUST_SIGNINGTIMEATTR, DefaultValidationParameters.DEFAULT_TRUST_SIGNINGTIMEATTR);
				logger.debug("P_TRUST_SIGNINGTIMEATTR parameter has wrong type.P_TRUST_SIGNINGTIMEATTR is set to true.");
			}
		}
		
		if(!aParameters.containsKey(AllEParameters.P_GRACE_PERIOD))
		{
			aParameters.put(AllEParameters.P_GRACE_PERIOD, DefaultValidationParameters.DEFAULT_GRACE_PERIOD);
			if(logger.isDebugEnabled())
				logger.debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_GRACE_PERIOD) instanceof Long))
			{
				aParameters.put(AllEParameters.P_GRACE_PERIOD, DefaultValidationParameters.DEFAULT_GRACE_PERIOD);
				logger.debug("DEFAULT_GRACE_PERIOD parameter has wrong DEFAULT_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS))
		{
			aParameters.put(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS, DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE);
			if(logger.isDebugEnabled())
				logger.debug("P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is not set by user.P_TOLERATE_SIGNING_TIME_BY_SECONDS is set to " + DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE +".");
		}
		else
		{
			if (!(aParameters.get(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS) instanceof Long))
			{
				aParameters.put(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS, DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE);
				logger.debug("The type of the P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is improper. The parameter will be set to " + DefaultValidationParameters.DEFAULT_SIGNING_TIME_TOLERANCE +".");
			}
		}

		if(!aParameters.containsKey(AllEParameters.P_IGNORE_GRACE))
		{
			aParameters.put(AllEParameters.P_IGNORE_GRACE, DefaultValidationParameters.DEFAULT_IGNORE_GRACE);
			if(logger.isDebugEnabled())
				logger.debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " + DefaultValidationParameters.DEFAULT_GRACE_PERIOD +".");
		}
		else
		{
			if(!(aParameters.get(AllEParameters.P_IGNORE_GRACE) instanceof Boolean))
			{
				aParameters.put(AllEParameters.P_IGNORE_GRACE, DefaultValidationParameters.DEFAULT_IGNORE_GRACE);
				logger.debug("DEFAULT_IGNORE_GRACE parameter has wrong DEFAULT_IGNORE_GRACE is set to " + DefaultValidationParameters.DEFAULT_IGNORE_GRACE +".");
			}
		}
		
	}

	private ECertificate _findCertificate(List<ECertificate> aCerts, ESignerIdentifier aSID, Map<String,Object> aParams)
	{
		for(ECertificate cert:aCerts)
		{
			if(aSID.isEqual(cert))
				return cert;
		}
		
		return findCertFromFinders(aSID, aParams);
	}
	
	private ECertificate findCertFromFinders(ESignerIdentifier aSignerIdentifier, Map<String,Object> aParams) 
	{
		ECertificate cer = null;
		/*Object policyO = aParams.get(AllEParameters.P_CERT_VALIDATION_POLICY);
		if(policyO != null)
		{
			try
			{*/
				//ValidationPolicy policy = (ValidationPolicy) policyO;
				//ValidationSystem vs = CertificateValidation.createValidationSystem(policy);
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
        else
        {
            ValidationInfoResolver vir = new ValidationInfoResolver();
            vir.addCertificates(mCerts);
            List<ECertificate> certs = vir.resolve(searchCriteria);
            if(certs == null || certs.size() == 0)
                cer = null;
            else
                cer = certs.get(0);
        }
			/*}
			catch(ClassCastException aEx)
			{
				cer = null;
			} 
			/*catch (ESYAException e)
			{
				cer = null;
			}
		}   */
		
		return cer;
	}
	
	
	private void _interpretSignatureVerification(SignatureValidationResult svr) 
	{
		List<CheckerResult> checkerResults =  svr.getCheckerResults();
		for (CheckerResult checkerResult : checkerResults) 
		{
			if(checkerResult.getResultStatus() != CheckerResult_Status.SUCCESS)
				if(!checkerResult.getCheckerClass().equals(CertificateChecker.class))
				{
					svr.setSignatureStatus(Signature_Status.INVALID);
					svr.setDescription("Imza Dogrulanamadi");
					return;
				}
		}
		
		CertificateStatusInfo csi = svr.getCertStatusInfo();
		if(csi==null || csi.getValidationHistory() == null || csi.getValidationHistory().size() == 0) 
		{
			svr.setSignatureStatus(Signature_Status.INVALID);
			svr.setDescription("Imza Dogrulanamadi");
		}
		else
		{
			CertificateStatus cs = csi.getCertificateStatus();
			if(cs == CertificateStatus.PATH_VALIDATION_FAILURE)
			{
				List<PathValidationRecord> pathRecords = csi.getValidationHistory();
				boolean inComplete = true;
				for (PathValidationRecord pathValidationRecord : pathRecords) 
				{
					if(pathValidationRecord.getResultCode() == PathValidationResult.REVOCATION_CONTROL_FAILURE)
						inComplete = true;
					else 
						inComplete = false;
				}
				if(inComplete)
				{
					svr.setSignatureStatus(Signature_Status.INCOMPLETE);
					svr.setDescription("Imza Dogrulanamadi");
				}
				else
				{
					svr.setSignatureStatus(Signature_Status.INVALID);
					svr.setDescription("Imza Dogrulanamadi");
				}
			}
			else
			{
				svr.setSignatureStatus(Signature_Status.INVALID);
				svr.setDescription("Imza Dogrulanamadi");
			}
		}
		
	}

	private void setMaturityState(SignatureValidationResult aSvr) 
	{
		CertificateStatusInfo certStatusInfo = aSvr.getCertStatusInfo();
		Calendar signingTime = aSvr.getSigningTime();
		
		List<OCSPResponseStatusInfo> ocspStatusInfos = certStatusInfo.getOCSPResponseInfoList();
		List<CRLStatusInfo> crlStatusInfos = certStatusInfo.getCRLInfoList();
		
		//Find latest OCSP and CRL to set maturity level.
		
		if (ocspStatusInfos.size() != 0)
        {
			 OCSPResponseStatusInfo latestOcspStatusInfo = ocspStatusInfos.get(0);
             for (int i = 1; i < ocspStatusInfos.size(); i++)
             {
                 if (ocspStatusInfos.get(i).getOCSPResponse().getBasicOCSPResponse().getProducedAt().after(latestOcspStatusInfo.getOCSPResponse().getBasicOCSPResponse().getProducedAt()))
                     latestOcspStatusInfo = ocspStatusInfos.get(i);
             }
             
             if(latestOcspStatusInfo.getOCSPResponse().getBasicOCSPResponse().getProducedAt().after(signingTime))
 				aSvr.setValidationState(ValidationState.MATURE);
 			else
 				aSvr.setValidationState(ValidationState.PREMATURE);
        }
		else if(crlStatusInfos.size() != 0)
		{
			CRLStatusInfo latestCrlStatusInfo = crlStatusInfos.get(0);
            for (int i = 1; i < crlStatusInfos.size(); i++)
            {
                if (crlStatusInfos.get(i).getCRL().getThisUpdate().after(latestCrlStatusInfo.getCRL().getThisUpdate()))
                    latestCrlStatusInfo = crlStatusInfos.get(i);
            }
            
			if(latestCrlStatusInfo.getCRL().getThisUpdate().after(signingTime))
				aSvr.setValidationState(ValidationState.MATURE);
			else
				aSvr.setValidationState(ValidationState.PREMATURE);
		}
	}

	protected void setUserDefinedCheckers(List<Checker> aUserDefinedCheckers) {
		mUserDefinedCheckers = aUserDefinedCheckers;
	}

	static class ParentSignatureResult
	{
		CheckerResult cr = new CheckerResult();
	    
		ParentSignatureResult(CheckerResult aCR)
		{
			cr = aCR;
		}
	    
	    CheckerResult getParentSignatureCheckerResult()
	    {
	    	return cr;
	    }
	}
}
