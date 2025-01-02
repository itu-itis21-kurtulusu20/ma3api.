using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;
using tr.gov.tubitak.uekae.esya.api.common.tools;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
     * Checks signer's certificate
     * @author aslihan.kubilay
     *
     */
    public class CertificateChecker : BaseChecker
    {
        private bool strictReferences = false;
        private Signer mSigner;

        public CertificateChecker()
        {
        }

        public CertificateChecker(bool aStrictReferences)
        {
            strictReferences = aStrictReferences;
        }
        //@SuppressWarnings("unchecked")
        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            //aCheckerResult.setCheckerName("Certificate Validation Checker");
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.CERTIFICATE_VALIDATION_CHECKER), typeof(CertificateChecker));
            mSigner = aSigner;
            try
            {
                ECertificate cert = aSigner.getSignerCertificate();
                DateTime? signingTime = _findDate(aSigner.getSignerInfo());
                
                Object ignoreGraceObj = false;
                getParameters().TryGetValue(AllEParameters.P_IGNORE_GRACE, out ignoreGraceObj);
                bool ignoreGrace = (bool)ignoreGraceObj;

                Object offline = false;
                getParameters().TryGetValue(AllEParameters.P_VALIDATION_WITHOUT_FINDERS, out offline);
                bool closeFinders = true.Equals(offline);
                if (!closeFinders)
                {
                    if (isSignatureTypeAboveEST(aSigner.getType()))
                    {
                        Object searchRevData = false;
                        getParameters().TryGetValue(AllEParameters.P_FORCE_STRICT_REFERENCE_USE, out searchRevData);
                        closeFinders = true.Equals(searchRevData);

                        Object strictRef = false;
                        getParameters().TryGetValue(AllEParameters.P_ESAV3_ABOVE_EST, out strictRef);
                        if (!true.Equals(strictRef))
                        {
                            strictReferences = closeFinders;
                        }
                    }
                }

                bool allResult = checkCertificateAtTime(cert, aCheckerResult, signingTime, ignoreGrace, closeFinders);
			    CertificateCheckerResultObject resultObj = (CertificateCheckerResultObject) aCheckerResult.getResultObject();
			
			    List<Checker> checkers = new List<Checker>();


                if ((aSigner.isTurkishProfile() || getParameters().ContainsKey(AllEParameters.P_VALIDATION_PROFILE)) && resultObj != null)
                {
                    if (resultObj.getCertStatusInfo().getCertificateStatus() == CertificateStatus.VALID)
                    {                       
                            ProfileRevocationValueMatcherChecker profileRevValueChecker = new ProfileRevocationValueMatcherChecker(resultObj.getCertStatusInfo());
                            checkers.Add(profileRevValueChecker);
                        
                    }
                }
			
			    foreach(Checker checker in checkers)
			    {
				    CheckerResult cresult = new CheckerResult();
				    checker.setParameters(getParameters());
				    bool result = checker.check(aSigner, cresult);
				    allResult = allResult && result;
				    aCheckerResult.addCheckerResult(cresult);
			    }
                if(!allResult)
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);

			    return allResult;

            }
            catch (CMSSignatureException aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_VALUE_NOT_FOUND), aEx));
                return false;
            }
        }

        public bool checkCertificateAtTime(ECertificate aCert, CheckerResult aCheckerResult, DateTime? aSigningTime, bool aIgnoreGrace, bool aCloseFinders)
        {
            return checkCertificateAtTime(aCert, aCheckerResult, aSigningTime, false, aIgnoreGrace, aCloseFinders);
        }

        public bool checkCertificateAtTime(ECertificate cert, CheckerResult aCheckerResult, DateTime? aSigningTime,
            bool returnTrustedCerts, bool ignoreGrace, bool aCloseFinders)
        {

            Object policyFileO = null;
            Object policiesO = null;
            getParameters().TryGetValue(EParameters.P_CERT_VALIDATION_POLICY, out policyFileO);
            getParameters().TryGetValue(EParameters.P_CERT_VALIDATION_POLICIES, out policiesO);
            if (policyFileO == null && policiesO == null)
            {
                aCheckerResult.addMessage(
                    new ValidationMessage(Msg.getMsg(Msg._0_MISSING_PARAMETER, new[] { "P_CERT_VALIDATION_POLICIES" }),
                                          new NullParameterException()));
                return false;
            }
            //ValidationPolicy policy = null;
            CertValidationPolicies policies = null;
            if (policiesO != null)
            {
                try
                {
                    policies = (CertValidationPolicies)policiesO;
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_, new[] {"P_POLICY_FILE", "ValidationPolicy"}), aEx));
                    return false;
                }
                logger.Info("Found 'policies' parameter, ignoring policy file param");
            }
            else
            {
                // if no policies param found, seek policy and register it as  default policy
                try
                {
                    ValidationPolicy policy = (ValidationPolicy)policyFileO;
                    policies = new CertValidationPolicies();
                    policies.register(null, policy);
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_, new[] { "P_POLICY_FILE", "ValidationPolicy" }), aEx));
                    return false;
                }
            }
            Object initialCertsO = null;
            getParameters().TryGetValue(AllEParameters.P_ALL_CERTIFICATES, out initialCertsO);
            List<ECertificate> initialCerts = new List<ECertificate>();
            if (initialCertsO != null)
            {
                try
                {
                    initialCerts = (List<ECertificate>)initialCertsO;
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_,
                                       new[] { "P_ALL_CERTIFICATES", " List<ECertificate>" }), aEx));
                    return false;
                }
            }


            Object initialCrlsO = null;
            getParameters().TryGetValue(AllEParameters.P_ALL_CRLS, out initialCrlsO);
            List<ECRL> initialCrls = new List<ECRL>();
            if (initialCrlsO != null)
            {
                try
                {
                    initialCrls = (List<ECRL>)initialCrlsO;
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_, new[] { "P_ALL_CRLS", " List<ECRL>" }), aEx));
                    return false;
                }
            }


            Object initialOCSPsO = null;
            getParameters().TryGetValue(AllEParameters.P_ALL_BASIC_OCSP_RESPONSES, out initialOCSPsO);
            List<EBasicOCSPResponse> initialBasicOCSPs = new List<EBasicOCSPResponse>();
            List<EOCSPResponse> initialOCSPs = new List<EOCSPResponse>();
            if (initialOCSPsO != null)
            {
                try
                {
                    initialBasicOCSPs = (List<EBasicOCSPResponse>)initialOCSPsO;
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_,
                                       new[] { "P_ALL_BASIC_OCSP_RESPONSES", " List<EBasicOCSPResponse>" }), aEx));
                    return false;
                }
            }

            foreach (EBasicOCSPResponse resp in initialBasicOCSPs)
            {
                initialOCSPs.Add(CMSSignatureUtil.convertBasicOCSPRespToOCSPResp(resp));
            }

            Object trustedCertsO = null;
            getParameters().TryGetValue(AllEParameters.P_TRUSTED_CERTIFICATES, out trustedCertsO);
            List<ECertificate> trustedCerts = new List<ECertificate>();
            if (trustedCertsO != null)
            {
                try
                {
                    trustedCerts = (List<ECertificate>)trustedCertsO;
                }
                catch (InvalidCastException aEx)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(
                            Msg.getMsg(Msg._0_WRONG_PARAMETER_TYPE_1_,
                                       new[] { "P_TRUSTED_CERTIFICATES", " List<ECertificate>" }), aEx));
                    return false;
                }
            }

            DateTime? currentTime = _getCurrentTime();

            //setting certificate validation time interval
            DateTime? baseValidationTime;
            DateTime? lastRevocationTime;

            Object gracePeriod, revocInfoPeriod;
            //long gracePeriod = (long)getParameters().TryGetValue(AllEParameters.P_GRACE_PERIOD, );
            getParameters().TryGetValue(EParameters.P_GRACE_PERIOD, out gracePeriod);
            //long revocInfoPeriod = (long)getParameters()[AllEParameters.P_REVOCINFO_PERIOD];
            getParameters().TryGetValue(EParameters.P_REVOCINFO_PERIOD, out revocInfoPeriod);

            bool doNotUsePastRevocationInfo;

            DateTime? endOfGrace = aSigningTime.Value.AddSeconds((long)gracePeriod);
            //If grace period has passed, validate certificate in the past.         
            if (currentTime > endOfGrace)
            {
                doNotUsePastRevocationInfo = true;
            }
            else
            {
                //Validate now, CRL has not published yet. 
                doNotUsePastRevocationInfo = false;
            }
            if (ignoreGrace)
                doNotUsePastRevocationInfo = false;

            baseValidationTime = aSigningTime;
            if (revocInfoPeriod != null)
            {
                lastRevocationTime = baseValidationTime.Value.AddSeconds((long)revocInfoPeriod);
                //Last revocation time must not be after the certificate expiration date.
                //If the signature time is close to the certificate expiration date, last revocation time
                //can pass the certificate expiration date because of grace period. 
                if (lastRevocationTime > cert.getNotAfter())
                    lastRevocationTime = getLaterTime(cert.getNotAfter(), endOfGrace);
            }
            else
            {
                //If there is no revocation info period is defined. Select end of grace period or the 
                //certificate expiration date.
                lastRevocationTime = getLaterTime(cert.getNotAfter(), endOfGrace);
            }

            Object virObj = null;
            getParameters().TryGetValue(AllEParameters.P_VALIDATION_INFO_RESOLVER, out virObj);
            if (virObj != null && !strictReferences)
            {
                ValidationInfoResolver vir = (ValidationInfoResolver)virObj;
                initialCerts.AddRange(vir.getInitialCertificates());
                initialCrls.AddRange(vir.getInitialCRLs());
                initialOCSPs.AddRange(vir.getInitialOCSPResponses());
            }

            CertificateStatusInfo csi = null;
            ValidationSystem vs = null;
            try
            {
                ValidationPolicy policy = policies.getPolicyFor(cert);
                vs = CertificateValidation.createValidationSystem(policy);
                //remove all finders

                if(aCloseFinders == true)
			    {
				    vs.getFindSystem().setCertificateFinders(new List<CertificateFinder>());
			        vs.getFindSystem().setCRLFinders(new List<CRLFinder>());
			        vs.getFindSystem().setOCSPResponseFinders(new List<OCSPResponseFinder>());
				    List<RevocationChecker> revocationCheckers = vs.getCheckSystem().getRevocationCheckers();
				    foreach (RevocationChecker revocationChecker in revocationCheckers) 
				    {
					    revocationChecker.setFinders(new List<Finder>());
				    }
			    }
                if (strictReferences)
                {
                    vs.getFindSystem().findTrustedCertificates();
                    try
                    {
                        RevocationInfoMap map = fillRevocationInfoMap(cert,
                                                                        vs.getFindSystem().getTrustedCertificates(),
                                                                        initialCerts,
                                                                        initialCrls,
                                                                        initialOCSPs);
                        List<RevocationChecker> list = new List<RevocationChecker>();
                        RevocationChecker checker = new RevocationCheckerFromMap(map);
                        checker.setParentSystem(vs);
                        list.Add(checker);
                        vs.getCheckSystem().setRevocationCheckers(list);
                    }
                    catch (Exception aEx)
                    {
                        logger.Debug("Error occurred in RevocationInfoMap generation", aEx);
                        aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERTIFICATE_REVOCATION_MAP_INCOMPLETE)));

                        aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                        return false;
                    }
                }

            }
            catch (ESYAException aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(
                        "Can not create validationsystem from policy",
                        aEx));
                return false;
            }
            try
            {
                vs.setBaseValidationTime(baseValidationTime);
                vs.setLastRevocationTime(lastRevocationTime);
                vs.setUserInitialCertificateSet(initialCerts);
                if (!strictReferences)
                {
                    vs.setUserInitialCRLSet(initialCrls);
                    vs.setUserInitialOCSPResponseSet(initialOCSPs);
                }
                vs.setValidCertificateSet(trustedCerts);


            Object digestAlgForOCSP = null;
            getParameters().TryGetValue(AllEParameters.P_OCSP_DIGEST_ALG, out digestAlgForOCSP);
			if (digestAlgForOCSP != null) {
				List<RevocationChecker> rvCheckers = vs.getCheckSystem().getRevocationCheckers();
				foreach (RevocationChecker rvChecker in rvCheckers) {
					if (rvChecker is RevocationFromOCSPChecker) {
                        foreach (Finder ocspChecker in rvChecker.getFinders<Finder>())
                        {
							if (ocspChecker is OCSPResponseFinderFromAIA) {
								((OCSPResponseFinderFromAIA) ocspChecker).setDigestAlgForOcspFinder((DigestAlg) digestAlgForOCSP);
							}
						}
					}
				}
			}

                csi = CertificateValidation.validateCertificate(vs, cert, doNotUsePastRevocationInfo);

                if (returnTrustedCerts)
                    csi.setTrustedCertificates(vs.getFindSystem().getTrustedCertificates());

                CertificateCheckerResultObject resultObject = new CertificateCheckerResultObject(csi, aSigningTime);
                aCheckerResult.setResultObject(resultObject);
            }
            catch (Exception aEx)
            {
                logger.Debug("Error occurred in cert validation", aEx);
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERTIFICATE_VALIDATION_UNSUCCESSFUL), aEx));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
            }
            if (csi.getCertificateStatus() == CertificateStatus.VALID)
            {
                //aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CERTIFICATE_VALIDATION_SUCCESSFUL)));
                logger.Debug("Validated certificate.");
                aCheckerResult.addMessage(new ValidationMessage(csi.getDetailedMessage()));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                return true;
            }
            else
            {
                logger.Debug("Cannot validate certificate: " + csi.getDetailedMessage());
                aCheckerResult.addMessage(new ValidationMessage(csi.getDetailedMessage()));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
            }
        }

        public bool checkCertificateAtTime(ECertificate aCert, CheckerResult aCheckerResult, DateTime? aSigningTime, bool aIgnoreGrace)
        {
            return checkCertificateAtTime(aCert, aCheckerResult, aSigningTime, false, aIgnoreGrace);
        }

        private DateTime? getLaterTime(DateTime? time1, DateTime? time2)
        {
            return time1 > time2 ? time1 : time2;
        }

        /*private DateTime? addSeconds(DateTime? aTime, long sSeconds)
        {
            DateTime? time = new DateTime(aTime.Value.Ticks);
            return new DateTime(time.Value.AddSeconds(sSeconds).Ticks);
        }*/

        private DateTime? _getCurrentTime()
        {
            if (getParameters().ContainsKey(EParameters.P_CURRENT_TIME))
                return ((DateTime?)getParameters()[EParameters.P_CURRENT_TIME]);
            return DateTime.UtcNow;
        }


        /*
         * First try to get time information from signaturetimestamp,second from the signingtime attribute and if not found,
         * return the current time information
         */

        private DateTime? _findDate(ESignerInfo aSignerInfo)
        {
            try
            {
                List<EAttribute> tsAttrs = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
                if (tsAttrs.Count != 0)
                {
                    //get time information from the first signature timestamp
                    EAttribute tsAttr = tsAttrs[0];
                    return SignatureTimeStampAttr.toTime(tsAttr);
                    /*
                    EContentInfo ci = new EContentInfo(tsAttr.getValue(0));
                    ESignedData sd = new ESignedData(ci.getContent());
                    ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                    return tstInfo.getTime();
                     * */
                }

                Object esav3 = false;
                getParameters().TryGetValue(AllEParameters.P_ESAV3_ABOVE_EST, out esav3);
                if (true.Equals(esav3))
                {
                    tsAttrs = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
                    // if BES+v3, get time information from the first atsv3
                    EAttribute tsAttr = tsAttrs[0];
                    return SignatureTimeStampAttr.toTime(tsAttr);

                }

                Boolean trustSigningTimeAttr = false;
                if (getParameters().ContainsKey(EParameters.P_TRUST_SIGNINGTIMEATTR))
                    trustSigningTimeAttr = (Boolean)getParameters()[EParameters.P_TRUST_SIGNINGTIMEATTR];

                if (trustSigningTimeAttr)
                {
                    List<EAttribute> stAttrs = aSignerInfo.getSignedAttribute(AttributeOIDs.id_signingTime);
                    if (stAttrs.Count != 0)
                    {
                        EAttribute stAttr = stAttrs[0];
                        Time time = new Time();
                        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
                        time.Decode(decBuf);
                        DateTime? signingTime = UtilTime.timeToDate(time);
                        if (signingTime == null)
                            throw new CMSSignatureException("Error while getting signing time information from signerinfo");
                        return signingTime;
                    }
                }


                if (getParameters().ContainsKey(EParameters.P_SIGNING_TIME))
                {
                    Object signingTime = null;
                    //object sTime = null;
                    getParameters().TryGetValue(EParameters.P_SIGNING_TIME, out signingTime);
                    if (signingTime != null)
                        return (DateTime)signingTime;
                }
                //signer is BES, one of its parent ESA
                if (getParameters().ContainsKey(AllEParameters.P_PARENT_ESA_TIME))
                {
                    Object parentESATime = null;
                    getParameters().TryGetValue(AllEParameters.P_PARENT_ESA_TIME, out parentESATime);
                    if (parentESATime != null)
                        return (DateTime) parentESATime;
                }
                //todo Now or UtcNow ??
                return _getCurrentTime();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error while getting signing time information from signerinfo", aEx);
            }
        }

        private RevocationInfoMap fillRevocationInfoMap(ECertificate cert,
                                                    List<ECertificate> trustedCerts,
                                                    List<ECertificate> initialCerts,
                                                    List<ECRL> initialCrls,
								  					List<EOCSPResponse> initialOCSPs)
	{
        RevocationInfoMap map = new RevocationInfoMap();

        ValidationInfoResolver resolver = new ValidationInfoResolver();
        Object virObj = null;
        getParameters().TryGetValue(AllEParameters.P_VALIDATION_INFO_RESOLVER, out virObj);
        if (virObj != null)
        {
            resolver = (ValidationInfoResolver)virObj;
        }
        resolver.addCertificates(initialCerts);
        resolver.addCertificates(trustedCerts);
        resolver.addCRLs(initialCrls);
        resolver.addOCSPResponses(initialOCSPs);


        ECompleteCertificateReferences certificateReferences = mSigner.getSignerInfo().getCompleteCertificateReferences();
        ECompleteRevocationReferences revocationReferences = mSigner.getSignerInfo().getCompleteRevocationReferences();

        if (certificateReferences.getCount()!=revocationReferences.getCount()-1){
            logger.Error("Cert and Revocation Reference counts does not match! ");
        }

        EOtherCertID[] certIDs = certificateReferences.getCertIDs();
        ECrlOcspRef[] revIDs = revocationReferences.getCrlOcspRefs();

        for (int i=0; i<revIDs.Length; i++){

            ECrlOcspRef crlOcspRef = revIDs[i];

            ECrlValidatedID[] crlIds = crlOcspRef.getCRLIds();
            EOcspResponsesID[] ocspIds = crlOcspRef.getOcspResponseIds();

            bool foundRevInfo = false;
            bool foundCert = false;


            List<ECRL> foundCRLs = new List<ECRL>();
            List<EOCSPResponse> foundResponses = new List<EOCSPResponse>();

            if (crlIds!=null){
                foreach (ECrlValidatedID crlId in crlIds) {
                    CRLSearchCriteria crlSearchCriteria = new CRLSearchCriteria();

                    crlSearchCriteria.setDigestValue(crlId.getDigestValue());
                    crlSearchCriteria.setDigestAlg(DigestAlg.fromOID(crlId.getDigestAlg()));
                    if (crlId.getObject().crlIdentifier != null)
                    {
                        crlSearchCriteria.setIssuer(crlId.getCrlissuer().stringValue());
                        crlSearchCriteria.setIssueTime(crlId.getCrlIssuedTime().Value.ToLocalTime());
                        crlSearchCriteria.setNumber(crlId.getCrlNumber());
                    }
                    List<ECRL> crls = resolver.resolve(crlSearchCriteria);
                    if (crls!=null && crls.Count > 0) {
                        foundRevInfo = true;
                        foundCRLs.Add(crls[0]);
                    }
                }
            }

            if (ocspIds!=null){
                foreach (EOcspResponsesID ocspId in ocspIds) {
                    OCSPSearchCriteria ocspSearchCriteria = new OCSPSearchCriteria();
                    if (ocspId.getObject().ocspRepHash != null)
                    {
                        ocspSearchCriteria.setDigestValue(ocspId.getDigestValue());
                        ocspSearchCriteria.setDigestAlg(DigestAlg.fromOID(ocspId.getDigestAlg()));
                    }
                    EResponderID responder = ocspId.getOcspResponderID();
                    if (responder.getType()==EResponderID._BYKEY)
                        ocspSearchCriteria.setOCSPResponderIDByKey(responder.getResponderIdByKey());
                    else
                        ocspSearchCriteria.setOCSPResponderIDByName(responder.getResponderIdByName().stringValue());
                    ocspSearchCriteria.setProducedAt(ocspId.getProducedAt().Value.ToLocalTime());

                    List<EOCSPResponse> ocsps = resolver.resolve(ocspSearchCriteria);
                    if (ocsps!=null && ocsps.Count > 0) {
                        foundRevInfo = true;
                        foundResponses.Add(ocsps[0]);
                    }
                }
            }

            ECertificate certificate = cert;
            CertificateSearchCriteria certificateSearchCriteria=null;
            if (i>0){
                EOtherCertID certId = certIDs[i-1];

                certificateSearchCriteria = new CertificateSearchCriteria();
                //// todo csc.setIssuer();
                certificateSearchCriteria.setDigestValue(certId.getDigestValue());
                certificateSearchCriteria.setDigestAlg(DigestAlg.fromOID(certId.getDigestAlg()));

                certificateSearchCriteria.setSerial(certId.getIssuerSerial());
                List<ECertificate> certs = resolver.resolve(certificateSearchCriteria);
                if (certs.Count==0){
                    logger.Error("Cant resolve CERT reference "+certificateSearchCriteria);
                }
                // buraya bir else koymak lazim gibi geldi, hata vermisti size 0 olunca
                else {
                    certificate = certs[0];
                    foundCert = true;
                }
            }

            if (foundRevInfo == false)
            {
                if (certificate.isSelfIssued())
                {
                    logger.Trace("Self Signed Certificate revocation info does not exist as expected. " + certificate.getSubject().getCommonNameAttribute());
                }
                else if (certificate.isOCSPSigningCertificate() && certificate.hasOCSPNoCheckExtention())
                {
                    logger.Trace("OCSP Certificate revocation info does not exist as expected. " + certificate.getSubject().getCommonNameAttribute());
                }
                else
                {
                    logger.Error("Cant resolve CERT revocation info for " + certificate);
                }
            }

            if (i==0 || foundCert){
                foreach (ECRL crl in foundCRLs){
                    map.appendCRL(certificate, crl);
                }
                foreach (EOCSPResponse ocspResponse in foundResponses){
                    map.appendOCSPResponse(certificate, ocspResponse);
                }
            }
        }
		return map;
	}

    }
}