package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.List;


/**
 * Checks the signature format consistency with Turkish Electronic Signature standards
 *
 * If P4,
 *
 * - It has to be XL or A
 * - It has to have OCSP as revocation data
 *
 * If P3,
 *
 * - It has to be XL or A
 * - It has to have CRL as revocation data
 *
 * If P2,
 *
 * - It has to be T or above
 *
 * Note:
 * If you are planning to to upgrade later, let's say you sign T with P3 and will upgrade 1 hour later,
 * you should not validate that temporary signature or you can use a modified config file without this validator
 *
 * @author suleyman.uslu
 */
public class TurkishESigProfileValidator implements Validator {

    protected static Logger logger = LoggerFactory.getLogger(TurkishESigProfileValidator.class);

    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException {
        QualifyingProperties qualifyingProperties = aSignature.createOrGetQualifyingProperties();
        UnsignedSignatureProperties unsignedSignatureProperties = qualifyingProperties.getUnsignedSignatureProperties();

        try {
            TurkishESigProfile profile = aSignature.getContext().getConfig().getValidationConfig().getValidationProfile();
            if(profile == null) {
                SignaturePolicyIdentifier signaturePolicyIdentifier = qualifyingProperties.getSignedSignatureProperties().getSignaturePolicyIdentifier();
                if(signaturePolicyIdentifier != null && signaturePolicyIdentifier.getSignaturePolicyId() != null)
                    profile = signaturePolicyIdentifier.getTurkishESigProfile();
            }

            if(profile == null) {
                return new ValidationResult(ValidationResultType.VALID,
                        I18n.translate("validation.check.signaturePolicyAttributes"),
                        I18n.translate("validation.policy.notTurkishProfile"),
                        null, getClass());
            }

            RevocationValues revocationValues = null;
            SignatureType signatureType = aSignature.getSignatureType();
            if (profile == TurkishESigProfile.P4_1|| profile == TurkishESigProfile.P3_1){
                try {
                    revocationValues = unsignedSignatureProperties.getRevocationValues();
                } catch (Exception e) {
                    // todo - belki daha iyi bir hata mesaji verebiliriz
                    // return invalid
                    logger.warn("Warning in TurkishESigProfileValidator", e);
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, getClass());
                }
            }


            if (profile.equals(TurkishESigProfile.P4_1)) {// but not XL or above
                if (signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A) {
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, getClass());
                }

                // does not have OCSP for signer certificate
                boolean OCSPfound = false;

                EName signerCertIssuer = aSignature.getKeyInfo().resolveCertificate().getIssuer();

                //for all ocsp responses
                List<EOCSPResponse> OCSPs = revocationValues.getAllOCSPResponses();
                for (EOCSPResponse OCSP : OCSPs) {
                    // check all certificates in each ocsp response
                    for (int i = 0; i < OCSP.getBasicOCSPResponse().getCertificateCount(); i++) {
                        if (OCSP.getBasicOCSPResponse().getCertificate(i).getIssuer().equals(signerCertIssuer)) {
                            OCSPfound = true;
                            break;
                        }
                    }

                    if (OCSPfound)
                        break;
                }

                if (!OCSPfound)
                    return new ValidationResult(ValidationResultType.INVALID, I18n.translate("validation.check.signaturePolicyAttributes"), I18n.translate("validation.policy.noOCSP"), null, getClass());


            } else if (profile.equals(TurkishESigProfile.P3_1)) {
                EName signerCertIssuer;// but not XL or above
                if (signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A) {
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, getClass());
                }

                boolean CRLfound = false;
                signerCertIssuer = aSignature.getKeyInfo().resolveCertificate().getIssuer();

                // for all crls
                List<ECRL> CRLs = revocationValues.getAllCRLs();
                for (ECRL crl : CRLs) {
                    if (crl.getIssuer().equals(signerCertIssuer)) {
                        CRLfound = true;
                        break;
                    }
                }
                if (!CRLfound)
                    return new ValidationResult(ValidationResultType.INVALID, I18n.translate("validation.check.signaturePolicyAttributes"), I18n.translate("validation.policy.noCRL"), null, getClass());


            } else if (profile.equals(TurkishESigProfile.P2_1)) {// but not T or above
                if (signatureType != SignatureType.XAdES_T && signatureType != SignatureType.XAdES_C && signatureType != SignatureType.XAdES_X
                        && signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A) {

                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, getClass());
                }

            }
        } catch (Exception e) {
            logger.warn("Warning in TurkishESigProfileValidator", e);
            return new ValidationResult(ValidationResultType.INVALID,
                    I18n.translate("validation.check.signaturePolicyAttributes"),
                    I18n.translate("validation.policy.cantcheck"),
                    null, getClass());
        }

        return new ValidationResult(ValidationResultType.VALID,
                I18n.translate("validation.check.signaturePolicyAttributes"),
                I18n.translate("validation.policy.successful"),
                null, getClass());
    }

    public String getName() {
        return getClass().getName();
    }
}
