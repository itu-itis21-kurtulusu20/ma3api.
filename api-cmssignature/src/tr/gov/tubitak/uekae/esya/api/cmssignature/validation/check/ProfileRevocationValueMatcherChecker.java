package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.UNSUCCESS;

//Do changes for pades also (PadesProfileRevocationValueMatcherChecker)
public class ProfileRevocationValueMatcherChecker extends BaseChecker {

    protected static Logger logger = LoggerFactory.getLogger(ProfileRevocationValueMatcherChecker.class);

    CertificateStatusInfo mCsi;
    boolean signOrValidate;

    public ProfileRevocationValueMatcherChecker(CertificateStatusInfo aCsi) {
        mCsi = aCsi;
        signOrValidate = false;
    }

    public ProfileRevocationValueMatcherChecker(CertificateStatusInfo aCsi, boolean aSign) {
        mCsi = aCsi;
        signOrValidate = aSign;
    }

    @Override
    protected boolean _check(Signer aSigner, CheckerResult aCheckerResult) {
        aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER), ProfileRevocationValueMatcherChecker.class);

        try {
            TurkishESigProfile profile = (TurkishESigProfile) getParameters().get(AllEParameters.P_VALIDATION_PROFILE);
            if(profile == null)
              profile = aSigner.getSignerInfo().getProfile();

            ESignatureType signatureType = aSigner.getType();

            boolean isXlongOrAbove = true;
            if (signatureType == ESignatureType.TYPE_BES
                    || signatureType == ESignatureType.TYPE_EPES
                    || signatureType == ESignatureType.TYPE_EST
                    || signatureType == ESignatureType.TYPE_ESC) {
                isXlongOrAbove = false;
            }

            if (!isXlongOrAbove && (profile == TurkishESigProfile.P3_1 || profile == TurkishESigProfile.P4_1)) {
                if (signOrValidate) {
                    return setSuccess(aCheckerResult);
                } else {
                    String message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL, new String[]{profile.getProfileName(), signatureType.toString()});
                    return setFail(aCheckerResult, message);
                }
            }

            if (profile == TurkishESigProfile.P3_1) {

                List<CRLStatusInfo> crlInfoList = mCsi.getCRLInfoList();
                for (CRLStatusInfo crlStatusInfo : crlInfoList) {
                    if (crlStatusInfo.getCRLStatus() == CRLStatus.VALID) {
                        return setSuccess(aCheckerResult);
                    }
                }
                String message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_P3_DOESNOT_USE_CRL);
                return setFail(aCheckerResult, message);
            }

            else if (profile == TurkishESigProfile.P4_1) {

                List<OCSPResponseStatusInfo> ocspResponseInfoList = mCsi.getOCSPResponseInfoList();
                for (OCSPResponseStatusInfo ocspResponseStatusInfo : ocspResponseInfoList) {
                    if (ocspResponseStatusInfo.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID) {
                        return setSuccess(aCheckerResult);
                    }
                }
                String message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_P4_DOESNOT_USE_OCSP);
                return setFail(aCheckerResult, message);
            }
        } catch (ESYAException e) {
            logger.warn("Warning in ProfileRevocationValueMatcherChecker", e);
            String message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
            return setFail(aCheckerResult, message);
        }

        return setSuccess(aCheckerResult);
    }

    private boolean setSuccess(CheckerResult aCheckerResult){
        aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL)));
        aCheckerResult.setResultStatus(SUCCESS);
        return true;
    }

    private boolean setFail(CheckerResult aCheckerResult, String message){
        aCheckerResult.addMessage(new ValidationMessage(message));
        aCheckerResult.setResultStatus(UNSUCCESS);
        return false;
    }
}
