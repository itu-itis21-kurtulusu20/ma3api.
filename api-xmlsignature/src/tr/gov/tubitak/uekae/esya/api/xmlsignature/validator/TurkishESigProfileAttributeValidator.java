package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SigningCertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
public class TurkishESigProfileAttributeValidator implements Validator {

    protected static Logger logger = LoggerFactory.getLogger(TurkishESigProfileAttributeValidator.class);

    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException {

        QualifyingProperties qualifyingProperties = aSignature.createOrGetQualifyingProperties();

        UnsignedSignatureProperties unsignedSignatureProperties = qualifyingProperties.getUnsignedSignatureProperties();

        SignedProperties signedProperties = qualifyingProperties.getSignedProperties();

        SignedSignatureProperties signedSignatureProperties = signedProperties.getSignedSignatureProperties();

        SignaturePolicyIdentifier signaturePolicyIdentifier = signedSignatureProperties.getSignaturePolicyIdentifier();

        // if signature policy identifier exists
        if(signaturePolicyIdentifier != null) {

            try {

                // if digest algorithm is SHA-256
                DigestAlg digestAlg = signaturePolicyIdentifier.getSignaturePolicyId().getDigestMethod().getAlgorithm();

                if(digestAlg == null || digestAlg != DigestAlg.SHA256)
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.policy.signaturePolicyAttributes"),
                            I18n.translate("validation.policy.notsha2"),
                            null, getClass());


                // if signing certificate exists
                SigningCertificate signingCertificate = signedSignatureProperties.getSigningCertificate();

                if(signingCertificate == null)
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.certificate.cantFound"),
                            null, getClass());


                // if digest algorithm of signing certificate is SHA-256
                List<CertID> certIDs = signingCertificate.getCertIDListCopy();

                for(CertID certID : certIDs) {

                    if( certID.getDigestMethod().getAlgorithm() != DigestAlg.SHA256)
                        return new ValidationResult(ValidationResultType.INVALID,
                                I18n.translate("validation.check.signaturePolicyAttributes"),
                                I18n.translate("validation.signingCertificate.notSha2"),
                                null, getClass());
                }


                // if declared signing time exists
                XMLGregorianCalendar signingTime = signedSignatureProperties.getSigningTime();

                if(signingTime == null)
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.signaturePolicyAttributes"),
                            I18n.translate("validation.signingTime.notFound"),
                            null, getClass());



                SignatureType signatureType = aSignature.getSignatureType();

                // if signature is T or above
                if(signatureType != SignatureType.XAdES_BES && signatureType != SignatureType.XAdES_EPES) {

                    try {

                        // TS time consistency, CRL and root check
                        List<SignatureTimeStamp> signatureTimeStamps = unsignedSignatureProperties.getSignatureTimeStamps();

                        for (SignatureTimeStamp signatureTimeStamp : signatureTimeStamps) {


                            // if signature time stamp is later than signing time
                            GregorianCalendar signingTimeGre = signingTime.toGregorianCalendar();
                            if(signingTimeGre.after(signatureTimeStamp.getEncapsulatedTimeStamp(0).getTime()))
                                return new ValidationResult(ValidationResultType.INVALID,
                                        I18n.translate("validation.check.signaturePolicyAttributes"),
                                        I18n.translate("validation.timestamp.notAfterSigningTime"),
                                        null, getClass());

                            // if signature time stamp is before 2 hours after signing time
                            signingTimeGre.add(Calendar.SECOND, 7200);
                            if(signatureTimeStamp.getEncapsulatedTimeStamp(0).getTime().after(signingTimeGre))
                                return new ValidationResult(ValidationResultType.INVALID,
                                        I18n.translate("validation.check.signaturePolicyAttributes"),
                                        I18n.translate("validation.timestamp.notWithin2HoursOfSigningTime"),
                                        null, getClass());


                            /*
                             * karar verildi buranin
                             * kontrol eidlmemesine
                             * ben de o kadar yazdim
                             * kiyamadim silmeye
                             *
                            // add validation data from CMS structure of timestamp
                            List<ECertificate> certificates = signatureTimeStamp.getEncapsulatedTimeStamp(0).getSignedData().getCertificates();
                            List<ECRL> CRLs = signatureTimeStamp.getEncapsulatedTimeStamp(0).getSignedData().getCRLs();

                            // add validation data from XML structure of signature (TimeStampValidationData)
                            TimeStampValidationData TSvalidationData = unsignedSignatureProperties.getValidationDataForTimestamp(signatureTimeStamp);
                            if( TSvalidationData != null ) {

                                RevocationValues TSrevocationValues = TSvalidationData.getRevocationValues();

                                if( TSrevocationValues != null )
                                    CRLs.addAll(TSrevocationValues.getAllCRLs());

                                CertificateValues TScertificateValues = TSvalidationData.getCertificateValues();

                                if( TScertificateValues != null )
                                    certificates.addAll(TScertificateValues.getAllCertificates());
                            }

                            // if signature timestamp has its root certificate
                            // todo - we only check if there is a self-signing certificate, have to check if it is TS's root
                            boolean rootFound = false;

                            for( ECertificate certificate : certificates ) {
                                if( certificate.isSelfIssued() ) {
                                    rootFound = true;
                                    break;
                                }
                            }

                            if( !rootFound )
                                return new ValidationResult(ValidationResultType.INVALID,
                                        I18n.translate("validation.check.signaturePolicyAttributes"),
                                        I18n.translate("validation.timestamp.certificateValue.noRootCert"),
                                        null, getClass());

                            // if timestamp validation data has CRL of signing certificate of signature timestamp
                            // todo - we only look if there is a CRL, but have to check whether it is the CRL of the corresponding certificate
                            if( CRLs.isEmpty() )
                                return new ValidationResult(ValidationResultType.INVALID,
                                        I18n.translate("validation.check.signaturePolicyAttributes"),
                                        I18n.translate("validation.timestamp.revocationValue.noCRL"),
                                        null, getClass());
                             */
                        }
                    }
                    catch (Exception e) {
                        logger.warn("Warning in TurkishESigProfileAttributeValidator", e);
                        return new ValidationResult(ValidationResultType.INVALID,
                                I18n.translate("validation.check.signaturePolicyAttributes"),
                                I18n.translate("validation.policy.timeStampCheckError"),
                                null, getClass());
                    }
                }
            }
            catch (Exception e) {
                logger.warn("Warning in TurkishESigProfileAttributeValidator", e);
                return new ValidationResult(ValidationResultType.WARNING,
                        I18n.translate("validation.check.signaturePolicyAttributes"),
                        I18n.translate("validation.policy.cantcheck"),
                        null, getClass());
            }

            return new ValidationResult(ValidationResultType.VALID,
                    I18n.translate("validation.check.signaturePolicyAttributes"),
                    I18n.translate("validation.policy.successful"),
                    null, getClass());
        }

        return new ValidationResult(ValidationResultType.VALID,
                I18n.translate("validation.check.signaturePolicyAttributes"),
                I18n.translate("validation.policy.notTurkishProfile"),
                null, getClass());
    }

    public String getName() {
        return getClass().getName();
    }
}
