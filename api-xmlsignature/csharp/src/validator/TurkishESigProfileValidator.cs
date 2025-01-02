using System;
using System.Collections.Generic;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{
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
    public class TurkishESigProfileValidator : Validator
    {
        private static readonly ILog Logger = LogManager.GetLogger(typeof(TurkishESigProfileValidator));

        public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
        {
            QualifyingProperties qualifyingProperties = aSignature.QualifyingProperties;         
            UnsignedSignatureProperties unsignedSignatureProperties = qualifyingProperties.UnsignedSignatureProperties;


            try
            {
                TurkishESigProfile profile = aSignature.Context.Config.ValidationConfig.getValidationProfile();
                if (profile == null)
                {
                    SignaturePolicyIdentifier signaturePolicyIdentifier = qualifyingProperties.SignedSignatureProperties.SignaturePolicyIdentifier;
                    if (signaturePolicyIdentifier != null)
                        profile = signaturePolicyIdentifier.getTurkishESigProfile();
                }

                if (profile == null)
                {
                    return new ValidationResult(ValidationResultType.VALID,
                        I18n.translate("validation.check.signaturePolicyAttributes"),
                        I18n.translate("validation.policy.notTurkishProfile"),
                        null, GetType());
                }


                RevocationValues revocationValues = null;
                SignatureType signatureType = aSignature.SignatureType;

                if (profile == TurkishESigProfile.P4_1 || profile == TurkishESigProfile.P3_1)
                {
                    try
                    {
                        revocationValues = unsignedSignatureProperties.RevocationValues;
                    }
                    catch (Exception e)
                    {
                        // todo - belki daha iyi bir hata mesaji verebiliriz
                        // return invalid
                        Logger.Warn("Warning in TurkishESigProfileValidator", e);
                        return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, GetType());
                    }
                }

                if (profile.equals(TurkishESigProfile.P4_1))
                {// but not XL or above
                    if (signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A)
                    {
                        return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, GetType());
                    }

                    // does not have OCSP for signer certificate
                    bool OCSPfound = false;

                    EName signerCertIssuer = aSignature.KeyInfo.resolveCertificate().getIssuer();

                    //for all ocsp responses
                    IList<EOCSPResponse> OCSPs = revocationValues.AllOCSPResponses;
                    foreach (EOCSPResponse OCSP in OCSPs)
                    {
                        // check all certificates in each ocsp response
                        for (int i = 0; i < OCSP.getBasicOCSPResponse().getCertificateCount(); i++)
                        {
                            if (OCSP.getBasicOCSPResponse().getCertificate(i).getIssuer().Equals(signerCertIssuer))
                            {
                                OCSPfound = true;
                                break;
                            }
                        }

                        if (OCSPfound)
                            break;
                    }

                    if (!OCSPfound)
                        return new ValidationResult(ValidationResultType.INVALID, I18n.translate("validation.check.signaturePolicyAttributes"), I18n.translate("validation.policy.noOCSP"), null, GetType());
                }
                else if (profile.equals(TurkishESigProfile.P3_1))
                {
                    EName signerCertIssuer;// but not XL or above
                    if (signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A)
                    {
                        return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, GetType());
                    }

                    bool CRLfound = false;
                    signerCertIssuer = aSignature.KeyInfo.resolveCertificate().getIssuer();

                    // for all crls
                    IList<ECRL> CRLs = revocationValues.AllCRLs;
                    foreach (ECRL crl in CRLs)
                    {
                        if (crl.getIssuer().Equals(signerCertIssuer))
                        {
                            CRLfound = true;
                            break;
                        }
                    }
                    if (!CRLfound)
                        return new ValidationResult(ValidationResultType.INVALID, I18n.translate("validation.check.signaturePolicyAttributes"), I18n.translate("validation.policy.noCRL"), null, GetType());
                }
                else if (profile.equals(TurkishESigProfile.P2_1))
                {// but not T or above
                    if (signatureType != SignatureType.XAdES_T && signatureType != SignatureType.XAdES_C && signatureType != SignatureType.XAdES_X
                        && signatureType != SignatureType.XAdES_X_L && signatureType != SignatureType.XAdES_A)
                    {

                        return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.invalidSignatureType"),
                            null, GetType());
                    }
                }
            }
            catch (Exception e)
            {
                Logger.Warn("Warning in TurkishESigProfileValidator", e);
                return new ValidationResult(ValidationResultType.INVALID,
                    I18n.translate("validation.check.signaturePolicyAttributes"),
                    I18n.translate("validation.policy.cantcheck"),
                    null, GetType());
            }

            return new ValidationResult(ValidationResultType.VALID,
                I18n.translate("validation.check.signaturePolicyAttributes"),
                I18n.translate("validation.policy.successful"),
                null, GetType());
        }

        public string Name
        {
            get { return GetType().Name; }
        }
    }
}
