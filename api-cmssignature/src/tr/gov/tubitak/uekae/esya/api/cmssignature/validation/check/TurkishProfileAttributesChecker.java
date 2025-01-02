package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.util.Calendar;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.UNSUCCESS;

//Do changes for pades also (PadesTurkishProfileAttributesChecker)
public class TurkishProfileAttributesChecker extends BaseChecker {

	private static final int WAIT_TIME = 300;   //for flexible signing time
    private boolean signOrValidate = false;

    public TurkishProfileAttributesChecker(){
    }

    public TurkishProfileAttributesChecker(boolean aSign){
        signOrValidate =  aSign;
    }

	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult) {

		Logger logger = LoggerFactory.getLogger(TurkishProfileAttributesChecker.class);

		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TURKISH_PROFILE_ATTRIBUTES_CHECKER),TurkishProfileAttributesChecker.class);

		if (aSigner.isTurkishProfile()){

			DigestAlg digestAlg = getPolicyDigestAlg(aSigner);
			if (digestAlg == null || digestAlg.getOID() != _algorithmsValues.id_sha256) {
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.PROFILE_POLICY_HASH_NOT_SHA256)));
				aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
			}

			List<EAttribute> stAttr = aSigner.getSignerInfo().getSignedAttribute(AttributeOIDs.id_signingTime);
			if (stAttr == null || stAttr.size() == 0) {
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_ATTRIBUTE_MISSING)));
				aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
			}
			List<EAttribute> certIdAttr = aSigner.getSignerInfo().getSignedAttribute(AttributeOIDs.id_aa_signingCertificateV2);
			if (certIdAttr == null || certIdAttr.size() == 0) {
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING)));
				aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
			}
		}

		if (aSigner.isTurkishProfile() || getParameters().containsKey(AllEParameters.P_VALIDATION_PROFILE)) {

			try {
				TurkishESigProfile profile = (TurkishESigProfile) getParameters().get(AllEParameters.P_VALIDATION_PROFILE);
				if (profile == null)
					profile = aSigner.getSignerInfo().getProfile();

				if (profile != TurkishESigProfile.P1_1) {
					if (!(signOrValidate && (aSigner.getType() == ESignatureType.TYPE_BES || aSigner.getType() == ESignatureType.TYPE_EPES))) {
						boolean result = false;
						Calendar signingTime = aSigner.getSignerInfo().getSigningTime();
						signingTime.add(Calendar.SECOND, (-1) * WAIT_TIME);
						Calendar timestampTime = getTime(aSigner);
						if (signingTime.after(timestampTime))
							result = true;

						signingTime.add(Calendar.SECOND, 7200 + WAIT_TIME);
						if (timestampTime.after(signingTime))
							result = true;

						if (result) {
							aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_NOT_AFTER_2H)));
							aCheckerResult.setResultStatus(UNSUCCESS);
							return false;
						}
					}
				}
			} 
			catch (Exception e) {
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TIME_ERROR)));
				aCheckerResult.setResultStatus(UNSUCCESS);
				logger.warn("Warning in TurkishProfileAttributesChecker", e);
				return false;
			}

			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
			return true;
		}
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NOT_A_TURKISH_PROFILE)));
		aCheckerResult.setResultStatus(SUCCESS);
		return true;
	}

	/**
	 * Returns time of signature time stamp
	 * 
	 * @return Calendar
	 * @throws ESYAException
	 * @throws Asn1Exception
	 */
	private Calendar getTime(Signer aSigner) throws ESYAException {
		EAttribute attr = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken).get(0);
		EContentInfo contentInfo = new EContentInfo(attr.getValue(0));
		ESignedData sd = new ESignedData(contentInfo.getContent());
		ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
		return tstInfo.getTime();
	}

	private DigestAlg getPolicyDigestAlg(Signer aSigner) {
		try {
			ESignaturePolicy sp = aSigner.getSignerInfo().getPolicyAttr();
			ESignaturePolicyId spi = sp.getSignaturePolicyId();
			return DigestAlg.fromOID(spi.getHashInfo().getHashAlg().getAlgorithm().value);
		} catch (Exception e) {
			logger.warn("Warning in TurkishProfileAttributesChecker", e);
			return null;
		}
	}
	/*
	private boolean isTurkishProfile(Signer aSigner) {
		try {
			TurkishESigProfile tp = aSigner.getSignerInfo().getProfile();
			if (tp == null)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	 */
}
