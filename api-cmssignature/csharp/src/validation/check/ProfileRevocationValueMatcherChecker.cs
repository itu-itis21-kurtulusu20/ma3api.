using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    class ProfileRevocationValueMatcherChecker : BaseChecker
    {
        readonly CertificateStatusInfo mCsi;
        readonly bool signOrValidate;

        public ProfileRevocationValueMatcherChecker(CertificateStatusInfo aCsi)
	    {
		    mCsi = aCsi;
	        signOrValidate = false;
	    }

        public ProfileRevocationValueMatcherChecker(CertificateStatusInfo aCsi, bool aSign)
        {
            mCsi = aCsi;
            signOrValidate = aSign;
        }
	
	protected override bool _check(Signer aSigner, CheckerResult aCheckerResult) 
	{
		aCheckerResult.setCheckerName(Msg.getMsg(Msg.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER), typeof(ProfileRevocationValueMatcherChecker));

        try
        {
            TurkishESigProfile profile = null;
            Object mProfile = null;
            getParameters().TryGetValue(AllEParameters.P_VALIDATION_PROFILE, out mProfile);
            profile = (TurkishESigProfile)mProfile;

            if (profile == null)
             profile = aSigner.getSignerInfo().getProfile();

            ESignatureType signatureType = aSigner.getType();
                      
            bool isXlongOrAbove = true;
            if (signatureType == ESignatureType.TYPE_BES
                    || signatureType == ESignatureType.TYPE_EPES
                    || signatureType == ESignatureType.TYPE_EST
                    || signatureType == ESignatureType.TYPE_ESC)
            {
                isXlongOrAbove = false;
            }

            if (!isXlongOrAbove && (profile == TurkishESigProfile.P3_1 || profile == TurkishESigProfile.P4_1))
            {
                if (signOrValidate)
                {
                    return setSuccess(aCheckerResult);
                }
                else
                {
                    String message= Msg.getMsg(Msg.PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL, new [] {profile.getProfileName(), signatureType.name()});
                    return setFail(aCheckerResult, message);
                }
            }

			if(profile == TurkishESigProfile.P3_1)
			{
                if (isXlongOrAbove)
                {
                    List<CRLStatusInfo> crlInfoList = mCsi.getCRLInfoList();
                    foreach (CRLStatusInfo crlStatusInfo in crlInfoList)
                    {
                        if (crlStatusInfo.getCRLStatus() == CRLStatus.VALID)
                        {
                            return setSuccess(aCheckerResult);
                        }
                    }
                }
                String message = Msg.getMsg(Msg.PROFILE_P3_DOESNOT_USE_CRL);
                return setFail(aCheckerResult, message);
			}
			
			if(profile == TurkishESigProfile.P4_1)
			{
                if (isXlongOrAbove)
                {
                    List<OCSPResponseStatusInfo> ocspResponseInfoList = mCsi.getOCSPResponseInfoList();
                    foreach (OCSPResponseStatusInfo ocspResponseStatusInfo in ocspResponseInfoList)
                    {
                        if (ocspResponseStatusInfo.getOCSPResponseStatus() ==
                            OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                        {
                            return setSuccess(aCheckerResult);
                        }
                    }
                }
                String message = Msg.getMsg(Msg.PROFILE_P4_DOESNOT_USE_OCSP);
                return setFail(aCheckerResult, message);
			}
		} 
		catch (ESYAException e) 
		{
            logger.Warn("Warning in ProfileRevocationValueMatcherChecker", e);
            String message = Msg.getMsg(Msg.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
            return setFail(aCheckerResult, message);
		}
        return setSuccess(aCheckerResult);
	}

    private bool setSuccess(CheckerResult aCheckerResult)
    {
        aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL)));
        aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
        return true;
    }

    private bool setFail(CheckerResult aCheckerResult, String message)
    {
        aCheckerResult.addMessage(new ValidationMessage(message));
        aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
        return false;
    }

    }


}
