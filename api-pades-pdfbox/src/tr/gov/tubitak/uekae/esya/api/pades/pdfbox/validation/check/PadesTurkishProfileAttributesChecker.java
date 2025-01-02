package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureImpl;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.PadesChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ValidationResultDetailImpl;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.util.Calendar;
import java.util.List;

public class PadesTurkishProfileAttributesChecker implements PadesChecker {

	private static final int WAIT_TIME = 300;   //for flexible signing time

	public ValidationResultDetail check(PAdESSignature signature) {

		String checkerName = CMSSignatureI18n.getMsg(E_KEYS.TURKISH_PROFILE_ATTRIBUTES_CHECKER);
		String message = CMSSignatureI18n.getMsg(E_KEYS.TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL);

		SignaturePolicyIdentifier spi = signature.getSignaturePolicy();
		if(spi==null)
			return null;

		TurkishESigProfile profile = signature.getContainer().getContext().getConfig().getCertificateValidationConfig().getValidationProfile();
		if(profile == null)
			profile = spi.getTurkishESigProfile();

		if (profile != null) {

			DigestAlg digestAlg = spi.getDigestAlg();
			if (digestAlg == null|| digestAlg.getOID() != _algorithmsValues.id_sha256) {
				message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_POLICY_HASH_NOT_SHA256);
				return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.INVALID);
			}

			if (signature.getSigningTimeAttrFromM() == null) {
				message = CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_ATTRIBUTE_MISSING);
				return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.INVALID);
			}			
			List<EAttribute> certIdAttr = ((CMSSignatureImpl)signature.getUnderlyingObject()).getSigner().getSignerInfo().getSignedAttribute(AttributeOIDs.id_aa_signingCertificateV2);
			if (certIdAttr == null || certIdAttr.size() == 0) {
				message = CMSSignatureI18n.getMsg(E_KEYS.SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING);
				return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.INVALID);
			}

			if(profile != TurkishESigProfile.P1_1){
				boolean result = false;
				Calendar signingTime = signature.getSigningTimeAttrFromM();
				Calendar timestampTime = signature.getDocTimeStampTime();

				if(timestampTime != null){
					signingTime.add(Calendar.SECOND,(-1)*WAIT_TIME);				
					if (signingTime.after(timestampTime))
						result = true;

					signingTime.add(Calendar.SECOND, 7200 + WAIT_TIME);
					if (timestampTime.after(signingTime))
						result = true;

					if (result) {
						message = CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_NOT_AFTER_2H);
						return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.INVALID);
					}
				}
				else{
					message = CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_NOT_FOUND); //TODO isim değişikliği
					return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.INVALID);
				}
			}
			return new ValidationResultDetailImpl(getClass(), checkerName, message,  ValidationResultType.VALID);
		}		
		return null;
	}
}