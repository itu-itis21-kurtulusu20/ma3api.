using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using Checker = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.Checker;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    //@ApiClass
    public class SignatureValidator
    {
        private readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private List<Checker> mCheckers = new List<Checker>();

        private List<ECertificate> mCerts = new List<ECertificate>();
        private List<ECRL> mCRLs = new List<ECRL>();
        private List<EBasicOCSPResponse> mOCSPs = new List<EBasicOCSPResponse>();

        private static readonly bool DEFAULT_TRUST_SIGNINGTIMEATTR = false;
        private static readonly long DEFAULT_GRACE_PERIOD = 86400L;
        private static readonly int  DEFAULT_SIGNING_TIME_TOLERANCE = 300;
        private static readonly Boolean DEFAULT_IGNORE_GRACE = false;

        private readonly BaseSignedData mSignedData;
        private readonly byte[] mContentInfoBytes;

        public SignatureValidator(byte[] aContentInfo)
        {
            mContentInfoBytes = aContentInfo;
            mSignedData = new BaseSignedData(aContentInfo);
        }


        private List<Checker> _getCheckersForSignType(ESignatureType aType)
        {
            List<Checker> besCheckerList = new List<Checker>();
            besCheckerList.Add(new CryptoChecker());
            besCheckerList.Add(new MessageDigestAttrChecker());
            besCheckerList.Add(new SigningCertificateAttrChecker());
            //besCheckerList.add(new ContentTypeAttrChecker());//countersigner olmasina gore _getCheckers methodunda ekleniyor
            besCheckerList.Add(new CertificateChecker());

            if (aType == ESignatureType.TYPE_BES)
                return besCheckerList;

            List<Checker> epesCheckerList = new List<Checker>();
            epesCheckerList.AddRange(besCheckerList);
            epesCheckerList.Add(new SignaturePolicyChecker());

            if (aType == ESignatureType.TYPE_EPES)
                return epesCheckerList;

            List<Checker> estCheckerList = new List<Checker>();
            estCheckerList.AddRange(besCheckerList);
            estCheckerList.Add(new SignatureTimeStampAttrChecker());

            if (aType == ESignatureType.TYPE_EST)
                return estCheckerList;


            List<Checker> escCheckerList = new List<Checker>();
            escCheckerList.AddRange(estCheckerList);

            if (aType == ESignatureType.TYPE_ESC)
                return escCheckerList;

            List<Checker> esxt1CheckerList = new List<Checker>();
            esxt1CheckerList.AddRange(escCheckerList);
            esxt1CheckerList.Add(new CAdES_C_TimeStampAttrChecker());

            if (aType == ESignatureType.TYPE_ESX_Type1)
                return esxt1CheckerList;


            List<Checker> esxt2CheckerList = new List<Checker>();
            esxt2CheckerList.AddRange(escCheckerList);
            esxt2CheckerList.Add(new TimeStampedCertsCrlsRefsAttrChecker());

            if (aType == ESignatureType.TYPE_ESX_Type2)
                return esxt2CheckerList;

            List<Checker> esxlongCheckerList = new List<Checker>();
            esxlongCheckerList.AddRange(escCheckerList);
            esxlongCheckerList.Add(new RevocationRefsValuesMatchChecker());
            esxlongCheckerList.Add(new CertificateRefsValuesMatchChecker());

            if (aType == ESignatureType.TYPE_ESXLong)
                return esxlongCheckerList;

            List<Checker> esxlongt1CheckerList = new List<Checker>();
            esxlongt1CheckerList.AddRange(esxlongCheckerList);
            esxlongt1CheckerList.Add(new CAdES_C_TimeStampAttrChecker());

            if (aType == ESignatureType.TYPE_ESXLong_Type1)
                return esxlongt1CheckerList;

            List<Checker> esxlongt2CheckerList = new List<Checker>();
            esxlongt2CheckerList.AddRange(esxlongCheckerList);
            esxlongt2CheckerList.Add(new TimeStampedCertsCrlsRefsAttrChecker());

            if (aType == ESignatureType.TYPE_ESXLong_Type2)
                return esxlongt2CheckerList;

            ///v3 bes'e göre değişti
            List<Checker> esaCheckerList = new List<Checker>();
            esaCheckerList.AddRange(besCheckerList);

            List<Checker> optionalCheckers = new List<Checker>();
            optionalCheckers.Add(new CAdES_C_TimeStampAttrChecker());
            optionalCheckers.Add(new TimeStampedCertsCrlsRefsAttrChecker());
            esaCheckerList.Add(new CheckAllChecker(optionalCheckers, false));

            List<Checker> optionalCheckers2 = new List<Checker>();
            optionalCheckers2.Add(new ArchiveTimeStampAttrChecker());
            optionalCheckers2.Add(new ArchiveTimeStampV2AttrChecker());
            optionalCheckers2.Add(new ArchiveTimeStampV3AttrChecker());
            esaCheckerList.Add(new CheckAllChecker(optionalCheckers2, true));

            if (aType == ESignatureType.TYPE_ESA || aType == ESignatureType.TYPE_ESAv2)
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

        private List<Checker> _getCheckers(Signer aSigner, bool aIsCounterSigner, ESignatureType aType, Dictionary<String, Object> aParams)
        {
            List<Checker> checkers = _getCheckersForSignType(aType);
            if (!aIsCounterSigner)
                checkers.Add(new ContentTypeAttrChecker());

            if (aSigner.getType() == ESignatureType.TYPE_ESA || aSigner.getType() == ESignatureType.TYPE_ESAv2)
            {
                if (aSigner._checkIfSignerIsESAV2())
                {
                    //check sırası karışık oldu biraz
                    checkers.Add(new SignatureTimeStampAttrChecker());
                    checkers.Add(new RevocationRefsValuesMatchChecker());
                    checkers.Add(new CertificateRefsValuesMatchChecker());
                }
                else
                {
                    if (aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).Count > 0)
                        checkers.Add(new SignatureTimeStampAttrChecker());
                    if ((aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certValues).Count > 0
                         && aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationValues).Count > 0))
                    {
                        checkers.Add(new RevocationRefsValuesMatchChecker());
                        checkers.Add(new CertificateRefsValuesMatchChecker());
                    }
                    else if ((aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certificateRefs).Count == 0
                        && aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_revocationRefs).Count == 0))
                    {
                        aParams[AllEParameters.P_ESAV3_ABOVE_EST] = true;
                    }
                }
            }

            if (aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_sigPolicyId).Count > 0)
                {
                    checkers.Add(new SignaturePolicyChecker());
                }

            if (aSigner.isTurkishProfile())
            {
                checkers.Add(new TurkishProfileAttributesChecker());
            }

            if (aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_contentTimestamp).Count > 0)
                checkers.Add(new ContentTimeStampAttrChecker());

            return checkers;
            }


        //public void verify(Signer aSigner, bool aIsCounterSigner, Dictionary<String, Object> aParams)
        /**
 * Verify signature
 * @param aSVR
 * @param aSigner
 * @param aIsCounterSigner signature is counter signature or not
 * @param aParams verification parameter
 */
        public void verify(SignatureValidationResult aSVR, Signer aSigner, bool aIsCounterSigner,
                           Dictionary<String, Object> aParams)
        {
            ESignatureType type = aSigner.getType();

            mCheckers = _getCheckers(aSigner, aIsCounterSigner, type, aParams);

            ECertificate signerCert = _findCertificate(mCerts, aSigner.getSignerInfo().getSignerIdentifier(), aParams);

            if (signerCert == null)
                throw new CMSSignatureException("Signer Certificate cant be found.");

            checkLicense(signerCert);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[AllEParameters.P_SIGNING_CERTIFICATE] = signerCert;
            parameters[AllEParameters.P_SIGNED_DATA] = mSignedData.getSignedData();
            parameters[AllEParameters.P_ALL_CERTIFICATES] = mCerts;
            parameters[AllEParameters.P_ALL_CRLS] = mCRLs;
            parameters[AllEParameters.P_ALL_BASIC_OCSP_RESPONSES] = mOCSPs;
            parameters[AllEParameters.P_CONTENT_INFO_BYTES] = mContentInfoBytes;

            if (aParams != null)
            {
                Dictionary<String, Object>.KeyCollection kc = aParams.Keys;
                foreach (String p in kc)
                {
                    parameters[p] = aParams[p];
                }
            }
            //parameters.putAll(aParams);
            _putParameters(parameters);
            aSVR.setSignerCertificate((ECertificate)parameters[AllEParameters.P_SIGNING_CERTIFICATE]);

            if (type == ESignatureType.TYPE_ESA)//TODO hızlandır
            {
                SignedDataValidation sdv = new SignedDataValidation();
                sdv._fillCertRevocationLists(mSignedData.getSignedData(), aSigner, aParams);
                parameters[AllEParameters.P_ALL_CERTIFICATES] = sdv.mCerts;
                parameters[AllEParameters.P_ALL_CRLS] = sdv.mCRLs;
                parameters[AllEParameters.P_ALL_BASIC_OCSP_RESPONSES] = sdv.mOCSPs;

                if (aSigner._checkIfSignerIsESAV3()) //arşiv v2 varsa ,önceden, bu if'e girmeyebilir mi?
                {
                    ATSHashIndexCollector atsCollector = new ATSHashIndexCollector();
                    atsCollector.checkATSHashIndex(aSigner); //TODO son ATSv3e göre dönüyor certs,crls ve ocsps. tek tek bakılmalı aslında
                    List<ECertificate> certs = sdv.mCerts;
                    List<ECRL> crls = sdv.mCRLs;
                    List<EBasicOCSPResponse> ocsps = sdv.mOCSPs;


                    foreach (ECertificate cert in atsCollector.mAllCerts)
                    {
                        certs.Remove(cert);
                    }
                    foreach (ECRL crl in atsCollector.mAllCrls)
                    {
                        crls.Remove(crl);
                    }
                    foreach (EBasicOCSPResponse response in atsCollector.mAllOcsps)
                    {
                        ocsps.Remove(response);
                    }

                    parameters[AllEParameters.P_ALL_CERTIFICATES] = certs;
                    parameters[AllEParameters.P_ALL_CRLS] = crls;
                    parameters[AllEParameters.P_ALL_BASIC_OCSP_RESPONSES] = ocsps;
                }
            }

            bool verified = true;

            foreach (Checker checker in mCheckers)
            {
                checker.setParameters(parameters);
                CheckerResult cresult = new CheckerResult();
                bool result = checker.check(aSigner, cresult);
                aSVR.addCheckResult(cresult);
                if (verified) verified = result;
            }

            Types.Signature_Status parentSStatus = aSVR.getSignatureStatus();
            SignatureValidationResult svr = aSVR;

            //parent signature is invalid
            if (parentSStatus == Types.Signature_Status.INVALID)
            {
                svr.setSignatureStatus(Types.Signature_Status.INVALID);
                svr.setDescription("Imza Dogrulanamadi");
                svr.getCheckerResults().Add(_addParentSignatureCheckerResult());
            }
            //parent signature is incomplete
            else if (parentSStatus == Types.Signature_Status.INCOMPLETE)
            {
                svr.getCheckerResults().Add(_addParentSignatureCheckerResult());
                if (!verified)
                {
                    svr.setSignatureStatus(Types.Signature_Status.INVALID);
                    _interpretSignatureVerification(svr);
                }
                else
                {
                    svr.setSignatureStatus(Types.Signature_Status.INCOMPLETE);
                    svr.setDescription("Imza Dogrulanamadi");
                }
            }
            //parent signature is verified or no parent signature
            else
            {
                if (!verified)
                {
                    svr.setSignatureStatus(Types.Signature_Status.INVALID);
                    _interpretSignatureVerification(svr);
                }
                else
                {
                    setMaturityState(svr);
                    svr.setSignatureStatus(Types.Signature_Status.VALID);
                }
            }

            List<Signer> css = aSigner.getCounterSigners();


            foreach (Signer cs in css)
            {
                _checkCounterSigner(aSigner, cs, aSVR, parameters);
            }
        }
        
        private CheckerResult _addParentSignatureCheckerResult()
        {
            CheckerResult cr = new CheckerResult();
            cr.setCheckerName("Parent Signature Checker", typeof(ParentSignatureResult));
            cr.addMessage(new ValidationMessage("Ust imza dogrulanamadi"));
            cr.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);

            return cr;
        }

        private void _checkCounterSigner(Signer aParent, Signer aSigner, SignatureValidationResult aSVR,
                                         Dictionary<String, Object> aParams)
        {
            aParams[AllEParameters.P_PARENT_SIGNER_INFO] = aParent.getSignerInfo();

            ECertificate signerCert = _findCertificate(mCerts, aSigner.getSignerInfo().getSignerIdentifier(), aParams);

            if (signerCert == null)
                throw new CMSSignatureException("Signer Certificate cant be found.");

            aParams[AllEParameters.P_SIGNING_CERTIFICATE] = signerCert;
            /* yukarıdan geliyor zaten certs,crls ve ocsps. sadece arşiv tarafından korunulanlar geliyor hem. Tekrar koymak yanlış gibi.
            aParams[AllEParameters.P_ALL_CERTIFICATES] = mCerts;
            aParams[AllEParameters.P_ALL_CRLS] = mCRLs;
            aParams[AllEParameters.P_ALL_BASIC_OCSP_RESPONSES] = mOCSPs;
            */
            try
            {
                if (!aParams.ContainsKey(AllEParameters.P_PARENT_ESA_TIME))
                {
                    DateTime? archiveTime = aSigner.getESAv2Time();
                    if (archiveTime == null)
                    {
                        archiveTime = aSigner.getESAv3Time();
                    }
                    if (archiveTime != null)
                    {
                        aParams[AllEParameters.P_PARENT_ESA_TIME] = archiveTime;
                    }
                }
            }
            catch (ESYAException e)
            {
                throw new CMSSignatureException(
                        "Error occurred while checking this signer protected by any ATS v3 or not");
            }

            SignatureValidationResult svr = new SignatureValidationResult();
            if (aSVR.getSignatureStatus() != Types.Signature_Status.VALID)
            {
                svr.setSignatureStatus(aSVR.getSignatureStatus());
            }

            verify(svr, aSigner, true, aParams);

            svr.setSignerCertificate(signerCert);

            aSVR.addCounterSigValidationResult(svr);

            aParams.Remove(AllEParameters.P_PARENT_ESA_TIME);
        }


        private void checkLicense(ECertificate aCer)
        {
            try
            {
                bool isTest = LV.getInstance().isTestLicense(LV.Products.CMSIMZA);
                if (isTest)
                    if (!aCer.getSubject().getCommonNameAttribute().ToLower().Contains("test"))
                    {
                        throw new SystemException(
                            "You have test license, you can only use certificates that contains \"test\" string in common name of certificate");
                    }
            }
            catch (LE ex)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new SystemException("Lisans kontrolu basarisiz.", ex);
            }
        }

        private void _putParameters(Dictionary<String, Object> aParameters)
        {
            if (!aParameters.ContainsKey(EParameters.P_TRUST_SIGNINGTIMEATTR))
            {
                aParameters[EParameters.P_TRUST_SIGNINGTIMEATTR] = DEFAULT_TRUST_SIGNINGTIMEATTR;
                if (logger.IsDebugEnabled)
                    logger.Debug(
                        "P_TRUST_SIGNINGTIMEATTR parameter is not set by user.P_TRUST_SIGNINGTIMEATTR is set to false.");
            }
            else
            {
                if (!(aParameters[EParameters.P_TRUST_SIGNINGTIMEATTR] is Boolean))
                {
                    aParameters[EParameters.P_TRUST_SIGNINGTIMEATTR] = DEFAULT_TRUST_SIGNINGTIMEATTR;
                    logger.Debug(
                        "P_TRUST_SIGNINGTIMEATTR parameter has wrong type.P_TRUST_SIGNINGTIMEATTR is set to true.");
                }
            }

            if (!aParameters.ContainsKey(EParameters.P_GRACE_PERIOD))
            {
                aParameters[EParameters.P_GRACE_PERIOD] = DEFAULT_GRACE_PERIOD;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_GRACE_PERIOD parameter is not set by user.P_GRACE_PERIOD is set to " +
                                   DEFAULT_GRACE_PERIOD + ".");
            }
            else
            {
                if (!(aParameters[EParameters.P_GRACE_PERIOD] is long))
                {
                    aParameters[EParameters.P_GRACE_PERIOD] = DEFAULT_GRACE_PERIOD;
                    logger.Debug("DEFAULT_GRACE_PERIOD parameter has wrong DEFAULT_GRACE_PERIOD is set to " +
                                   DEFAULT_GRACE_PERIOD + ".");
                }
            }
            if (!aParameters.ContainsKey(EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS))
            {
                aParameters[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = DEFAULT_SIGNING_TIME_TOLERANCE;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is not set by user.P_TOLERATE_SIGNING_TIME_BY_SECONDS is set to " + DEFAULT_SIGNING_TIME_TOLERANCE + ".");
            }
            else
            {
                if (!(aParameters[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] is int))
                {
                    aParameters[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS] = DEFAULT_SIGNING_TIME_TOLERANCE;
                    logger.Debug("The type of the P_TOLERATE_SIGNING_TIME_BY_SECONDS parameter is improper. The parameter will be set to " + DEFAULT_SIGNING_TIME_TOLERANCE + ".");
                }
            }

            if (!aParameters.ContainsKey(EParameters.P_IGNORE_GRACE))
            {
                aParameters[EParameters.P_IGNORE_GRACE] = DEFAULT_IGNORE_GRACE;
                if (logger.IsDebugEnabled)
                    logger.Debug("P_GRACE_PERIOD parameter is not set by user.P_IGNORE_GRACE is set to " + DEFAULT_IGNORE_GRACE + ".");
            }
            else
            {
                if (!(aParameters[EParameters.P_IGNORE_GRACE] is Boolean))
                {
                    aParameters[EParameters.P_GRACE_PERIOD] = DEFAULT_IGNORE_GRACE;
                    logger.Debug("P_IGNORE_GRACE parameter has wrong value. P_IGNORE_GRACE is set to " + DEFAULT_IGNORE_GRACE + ".");
                }
            }
        }

        private ECertificate _findCertificate(List<ECertificate> aCerts, ESignerIdentifier aSID,
                                              Dictionary<String, Object> aParams)
        {
            foreach (ECertificate cert in aCerts)
            {
                if (aSID.isEqual(cert))
                    return cert;
            }
            return findCertFromFinders(aSID, aParams);
        }

        private ECertificate findCertFromFinders(ESignerIdentifier aSignerIdentifier, Dictionary<String, Object> aParams)
        {
            ECertificate cer = null;
                    CertificateSearchCriteria searchCriteria = null;
                    if (aSignerIdentifier.getIssuerAndSerialNumber() != null)
                    {
                        searchCriteria =
                            new CertificateSearchCriteria(
                                aSignerIdentifier.getIssuerAndSerialNumber().getIssuer().stringValue(),
                                aSignerIdentifier.getIssuerAndSerialNumber().getSerialNumber());
                    }
                    else if (aSignerIdentifier.getSubjectKeyIdentifier() != null)
                    {
                        searchCriteria = new CertificateSearchCriteria(aSignerIdentifier.getSubjectKeyIdentifier());
                    }

                    if (searchCriteria == null)
                        cer = null;
                    else
                    {
                         ValidationInfoResolver vir = new ValidationInfoResolver();
                          vir.addCertificates(mCerts);
                         List<ECertificate> certs = vir.resolve(searchCriteria);
           
                        if (certs == null || certs.Count == 0)
                            cer = null;
                        else
                            cer = certs[0];
                    }

            return cer;
        }


        private void _interpretSignatureVerification(SignatureValidationResult svr)
        {
            List<CheckerResult> checkerResults = svr.getCheckerResults();
            foreach (CheckerResult checkerResult in checkerResults)
            {
                if (checkerResult.getResultStatus() != Types.CheckerResult_Status.SUCCESS)
                    if (!checkerResult.getCheckerClass().Equals(typeof(CertificateChecker)))
                    {
                        svr.setSignatureStatus(Types.Signature_Status.INVALID);
                        svr.setDescription("Imza Dogrulanamadi");
                        return;
                    }
            }

            CertificateStatusInfo csi = svr.getCertStatusInfo();
            if (csi == null || csi.getValidationHistory() == null || csi.getValidationHistory().Count == 0)
            {
                svr.setSignatureStatus(Types.Signature_Status.INVALID);
                svr.setDescription("Imza Dogrulanamadi");
            }
            else
            {
                CertificateStatus cs = csi.getCertificateStatus();
                if (cs == CertificateStatus.PATH_VALIDATION_FAILURE)
                {
                    List<PathValidationRecord> pathRecords = csi.getValidationHistory();
                    bool inComplete = true;
                    foreach (PathValidationRecord pathValidationRecord in pathRecords)
                    {
                        if (pathValidationRecord.getResultCode() == PathValidationResult.REVOCATION_CONTROL_FAILURE)
                            inComplete = true;
                        else
                            inComplete = false;
                    }
                    if (inComplete)
                    {
                        svr.setSignatureStatus(Types.Signature_Status.INCOMPLETE);
                        svr.setDescription("Imza Dogrulanamadi");
                    }
                    else
                    {
                        svr.setSignatureStatus(Types.Signature_Status.INVALID);
                        svr.setDescription("Imza Dogrulanamadi");
                    }
                }
                else
                {
                    svr.setSignatureStatus(Types.Signature_Status.INVALID);
                    svr.setDescription("Imza Dogrulanamadi");
                }
            }
        }

        private void setMaturityState(SignatureValidationResult aSvr)
        {
            CertificateStatusInfo certStatusInfo = aSvr.getCertStatusInfo();
            DateTime? signingTime = aSvr.getSigningTime();

            List<OCSPResponseStatusInfo> ocspStatusInfos = certStatusInfo.getOCSPResponseInfoList();
            List<CRLStatusInfo> crlStatusInfos = certStatusInfo.getCRLInfoList();
            //Find latest OCSP and CRL to set maturity level.
            if (ocspStatusInfos.Count != 0)
            {
                OCSPResponseStatusInfo latestOcspStatusInfo = ocspStatusInfos[0];
                for (int i = 1; i < ocspStatusInfos.Count; i++)
                {
                    if (ocspStatusInfos[i].getOCSPResponse().getBasicOCSPResponse().getProducedAt() > latestOcspStatusInfo.getOCSPResponse().getBasicOCSPResponse().getProducedAt())
                        latestOcspStatusInfo = ocspStatusInfos[i];
                }

                if (latestOcspStatusInfo.getOCSPResponse().getBasicOCSPResponse().getProducedAt() > signingTime)
                    aSvr.setValidationState(ValidationState.MATURE);
                else
                    aSvr.setValidationState(ValidationState.PREMATURE);
            }
            else if (crlStatusInfos.Count != 0)
            {
                CRLStatusInfo latestCrlStatusInfo = crlStatusInfos[0];
                for (int i = 1; i < crlStatusInfos.Count; i++)
                {
                    if (crlStatusInfos[i].getCRL().getThisUpdate() > latestCrlStatusInfo.getCRL().getThisUpdate())
                        latestCrlStatusInfo = crlStatusInfos[i];
                }

                if (latestCrlStatusInfo.getCRL().getThisUpdate() > signingTime)
                    aSvr.setValidationState(ValidationState.MATURE);
                else
                    aSvr.setValidationState(ValidationState.PREMATURE);
            }
        }
    }

    internal static class ParentSignatureResult
    {
        private static readonly CheckerResult cr = new CheckerResult();

        static ParentSignatureResult()
        {
            cr.setCheckerName("Parent Signature Checker", typeof(ParentSignatureResult));
            cr.addMessage(new ValidationMessage("Ust imza dogrulanamadi"));
            cr.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
        }

        private static CheckerResult getParentSignatureCheckerResult()
        {
            return cr;
        }
    }
}