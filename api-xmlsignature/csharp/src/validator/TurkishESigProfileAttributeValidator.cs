using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{
    /**
     * Checks if the signature satisfies Turkish Electronic Signature standards
     *
     * For all types (P1, P2, P3 and P4),
     *
     * 1- Digest algorithm stated in policy identifier must be SHA-256
     * 2- Digest algorithm of signing certificate must be SHA-256
     * 3- Signing time has to be existed
     *
     * If T or above (P2, P3 and P4)
     *
     * 4- Signature timestamp has to be later than signing time
     * 5- Signature timestamp has to be taken with in 2 hours of signing (more specifically, the signing time)
     *
     * If XL or above (P3 and P4)
     *
     * 6- Revocation data in TimeStampValidationData must be CRL
     * 7- TimeStampValidationData must have root of timestamp signing certificate
     *
     * @author suleyman.uslu
     *
     */
    public class TurkishESigProfileAttributeValidator : Validator
    {
        public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
        {
            QualifyingProperties qualifyingProperties = aSignature.createOrGetQualifyingProperties();

            UnsignedSignatureProperties unsignedSignatureProperties = qualifyingProperties.UnsignedSignatureProperties;

            SignedProperties signedProperties = qualifyingProperties.SignedProperties;

            SignedSignatureProperties signedSignatureProperties = signedProperties.SignedSignatureProperties;

            SignaturePolicyIdentifier signaturePolicyIdentifier = signedSignatureProperties.SignaturePolicyIdentifier;

            // if signature policy identifier exists
            if (signaturePolicyIdentifier != null)
            {
                try
                {

                    DigestAlg digestAlg = signaturePolicyIdentifier.SignaturePolicyId.DigestMethod.Algorithm;

                    // if digest algorithm is SHA-256
                    if (digestAlg == null || digestAlg != DigestAlg.SHA256)
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.policy.signaturePolicyAttributes"),
                                                    I18n.translate("validation.policy.notsha2"),
                                                    null, GetType());

                    SigningCertificate signingCertificate = signedSignatureProperties.SigningCertificate;

                    // if signing certificate exists
                    if (signingCertificate == null)
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signaturePolicyAttributes"),
                                                    I18n.translate("validation.certificate.cantFound"),
                                                    null, GetType());

                    // if digest algorithm of signing certificate is SHA-256
                    IList<CertID> certIDs = signingCertificate.CertIDListCopy;

                    foreach(CertID certID in certIDs) {

                        if( certID.DigestMethod.Algorithm != DigestAlg.SHA256)
                            return new ValidationResult(ValidationResultType.INVALID,
                                                        I18n.translate("validation.check.signaturePolicyAttributes"),
                                                        I18n.translate("validation.signingCertificate.notSha2"),
                                                        null, GetType());
                    }

                    DateTime? signingTime = signedSignatureProperties.SigningTime;

                    // if declared signing time exists
                    if (signingTime == null)
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.signaturePolicyAttributes"),
                                                    I18n.translate("validation.signingTime.notFound"),
                                                    null, GetType());
                    

                    SignatureType signatureType = aSignature.SignatureType;

                    // if signature is T or above
                    if (signatureType != SignatureType.XAdES_BES && signatureType != SignatureType.XAdES_EPES)
                    {

                        try {

                            // TS time consistency, CRL and root check
                            IList<SignatureTimeStamp> signatureTimeStamps = unsignedSignatureProperties.getSignatureTimeStamps();

                            foreach (SignatureTimeStamp signatureTimeStamp in signatureTimeStamps) {

                                // imza zamanı önce olmalı, tolerasyon için 2dk eklendi.                            
                                if (signingTime.Value.CompareTo(signatureTimeStamp.getEncapsulatedTimeStamp(0).Time.AddMinutes(2)) > 0)
                                    return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.timestamp.notAfterSigningTime"),
                                            null, GetType());

                                // imza atıldıktan sonra iki saat geçmeden zaman damgası alınmalı
                                signingTime = signingTime.Value.AddHours(2);
                                if (signatureTimeStamp.getEncapsulatedTimeStamp(0).Time.CompareTo(signingTime) > 0)
                                    return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.timestamp.notWithin2HoursOfSigningTime"),
                                            null, GetType());

                                
                                /* karar verildi buranin
                                 * kontrol edilmemesine
                                 * ben de o kadar yazdim
                                 * kiyamadim silmeye
                                 * 
                                // add validation data from CMS structure of timestamp
                                List<ECertificate> certificates = signatureTimeStamp.getEncapsulatedTimeStamp(0).SignedData.getCertificates();
                                List<ECRL> CRLs = signatureTimeStamp.getEncapsulatedTimeStamp(0).SignedData.getCRLs();

                                // add validation data from XML structure of signature (TimeStampValidationData)
                                TimeStampValidationData TSvalidationData = unsignedSignatureProperties.getValidationDataForTimestamp(signatureTimeStamp);
                                if( TSvalidationData != null ) {

                                    RevocationValues TSrevocationValues = TSvalidationData.RevocationValues;

                                    if( TSrevocationValues != null )
                                        CRLs.AddRange(TSrevocationValues.AllCRLs);

                                    CertificateValues TScertificateValues = TSvalidationData.CertificateValues;

                                    if( TScertificateValues != null )
                                        certificates.AddRange(TScertificateValues.AllCertificates);
                                }

                                // if signature timestamp has its root certificate
                                // todo - we only check if there is a self-signing certificate, have to check if it is TS's root
                                bool rootFound = false;

                                foreach( ECertificate certificate in certificates ) {
                                    if( certificate.isSelfIssued() ) {
                                        rootFound = true;
                                        break;
                                    }
                                }

                                if( !rootFound )
                                    return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.timestamp.certificateValue.noRootCert"),
                                            null, GetType());

                                // if timestamp validation data has CRL of signing certificate of signature timestamp
                                // todo - we only look if there is a CRL, but have to check whether it is the CRL of the corresponding certificate
                                if( CRLs.Count == 0 )
                                    return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.timestamp.revocationValue.noCRL"),
                                            null, GetType());
                                 */
                            }
                        }
                        catch (Exception e) {

                            return new ValidationResult(ValidationResultType.INVALID,
                                    I18n.translate("validation.check.signaturePolicyAttributes"),
                                    I18n.translate("validation.policy.timeStampCheckError"),
                                    null, GetType());
                        }
                    }
                }
                catch(Exception ignored)
                {
                    return new ValidationResult(ValidationResultType.WARNING,
                        I18n.translate("validation.check.signaturePolicyAttributes"),
                        I18n.translate("validation.policy.cantcheck"),
                        null, GetType());
                }

                return new ValidationResult(ValidationResultType.VALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.policy.successful"),
                                            null, GetType());
            }

            return new ValidationResult(ValidationResultType.VALID,
                                            I18n.translate("validation.check.signaturePolicyAttributes"),
                                            I18n.translate("validation.policy.notTurkishProfile"),
                                            null, GetType());
        }

        public string Name
        {
            get { return GetType().Name; }
        }
    }
}
