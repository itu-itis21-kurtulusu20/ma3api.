package tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.check;

import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.PadesChecker;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.validation.ValidationResultDetailImpl;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier;

import java.util.List;

public class PadesProfileRevocationValueMatcherChecker implements PadesChecker {
	CertificateStatusInfo mCsi;

	public PadesProfileRevocationValueMatcherChecker(CertificateStatusInfo aCsi) {
		mCsi = aCsi;
	}

	public ValidationResultDetail check(PAdESSignature signature) {

		String checkerName = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER);
		String message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL);
		SignaturePolicyIdentifier spi = signature.getSignaturePolicy();

		try {
			TurkishESigProfile profile = signature.getContainer().getContext().getConfig().getCertificateValidationConfig().getValidationProfile();

			//if validation profile parameter is not set and signature has policy identifier
			 if (profile == null && spi != null) {
				profile = spi.getTurkishESigProfile();
			}

			if(profile == null)
				return null;

			SignatureType signatureType = signature.getSignatureType();
			boolean isXlongOrAbove = true;
			if (signatureType == SignatureType.ES_BES
					|| signatureType == SignatureType.ES_EPES
					|| signatureType == SignatureType.ES_T
					|| signatureType == SignatureType.ES_C) {
				isXlongOrAbove = false;
			}

			if (!isXlongOrAbove && (profile == TurkishESigProfile.P3_1 || profile == TurkishESigProfile.P4_1)) {
				message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
				return new ValidationResultDetailImpl(getClass(), checkerName, message, ValidationResultType.INVALID);
			}

			if (profile == TurkishESigProfile.P3_1) {
				List<CRLStatusInfo> crlInfoList = mCsi.getCRLInfoList();
				for (CRLStatusInfo crlStatusInfo : crlInfoList) {
					if (crlStatusInfo.getCRLStatus() == CRLStatus.VALID) {
						return new ValidationResultDetailImpl(getClass(),checkerName, message,ValidationResultType.VALID);
					}
				}
				message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
				return new ValidationResultDetailImpl(getClass(), checkerName, message, ValidationResultType.INVALID);
			}

			if (profile == TurkishESigProfile.P4_1) {
				List<OCSPResponseStatusInfo> ocspResponseInfoList = mCsi.getOCSPResponseInfoList();
				for (OCSPResponseStatusInfo ocspResponseStatusInfo : ocspResponseInfoList) {
					if (ocspResponseStatusInfo.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID) {
						return new ValidationResultDetailImpl(getClass(),checkerName, message,ValidationResultType.VALID);
					}
				}
				message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
				return new ValidationResultDetailImpl(getClass(), checkerName, message, ValidationResultType.INVALID);
			}
		}
		catch (Exception e) {
			message = CMSSignatureI18n.getMsg(E_KEYS.PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL);
			return new ValidationResultDetailImpl(getClass(), checkerName,message, ValidationResultType.INVALID);
		}

		return new ValidationResultDetailImpl(getClass(), checkerName, message, ValidationResultType.VALID);
	}
}
