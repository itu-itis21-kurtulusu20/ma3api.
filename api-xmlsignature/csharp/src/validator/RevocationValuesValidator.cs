using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	/// <summary>
	/// @author ayetgin
	/// </summary>
	public class RevocationValuesValidator : Validator
	{

        /**
         * Validates as INVALID if crl or ocsp reference and value counts do not match or,
         * there is no value for at least one crl or ocsp reference; otherwise VALID.
         * Matching is done according to the digest algorithm of each crl and ocsp reference.
         * @param aSignature to be validated
         * @param aCertificate used for signature
         * @return ValidationResult VALID or INVALID
         * @throws XMLSignatureException
         */
		public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
		{
			Context context = aSignature.Context;
            bool strict = context.Config.ValidationConfig.ForceStrictReferences;
            SignatureType type = aSignature.SignatureType;

            // check only if strict reference is forced and for signature types XAdES_X_L and XAdES_A
            if (strict && (type==SignatureType.XAdES_X_L || type==SignatureType.XAdES_A)){

                RevocationValues values = aSignature.QualifyingProperties.UnsignedSignatureProperties.RevocationValues;
                CompleteRevocationRefs refs = aSignature.QualifyingProperties.UnsignedSignatureProperties.CompleteRevocationRefs;

                // check if CRL value and reference counts match
                if (values.CRLValueCount != refs.CRLReferenceCount){
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.revocationValues"),
                                                I18n.translate("validation.values.revVals.countMismatch", "CRL"),
                                                null, GetType());
                }

                // check if OCSP value and reference counts match
                if (values.OCSPValueCount != refs.OCSPReferenceCount){
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.revocationValues"),
                                                I18n.translate("validation.values.revVals.countMismatch", "OCSP"),
                                                null, GetType());
                }

                // check CRL consistency
                IList<ECRL> allCRLs = values.AllCRLs;
                // for each CRL reference...
                for (int i=0; i<refs.CRLReferenceCount; i++){
                    CRLReference crlReference = refs.getCRLReference(i);
                    bool found = false;
                    // ...check if there is a corresponding value
                    foreach (ECRL crl in allCRLs){
                        try {
                            byte[] crlDigest  = DigestUtil.digest(crlReference.DigestMethod.Algorithm, crl.getEncoded());
                            if (crlReference.DigestValue.SequenceEqual(crlDigest)){
                                found = true;
                                break;
                            }
                        } catch (CryptoException x){
                            throw new XMLSignatureException("Cant digest crl", x);
                        }
                    }
                    if (!found)
                    {
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.revocationValues"),
                                                    I18n.translate("validation.values.revVals.missingValue", "CRL"),
                                                    crlReference.ToString(), GetType());
                    }
                }

                // check OCSP consistency
                IList<EOCSPResponse> allOCSPResponses = values.AllOCSPResponses;
                // for each OCSP reference...
                for (int i=0; i<refs.OCSPReferenceCount; i++){
                    OCSPReference ocspReference = refs.getOCSPReference(i);
                    bool found = false;
                    // ...check is there is a corresponding value
                    foreach (EOCSPResponse ocspResponse in allOCSPResponses){
                        try {
                            byte[] ocspDigest  = DigestUtil.digest(ocspReference.DigestAlgAndValue.DigestMethod.Algorithm, ocspResponse.getEncoded());
                            if (ocspReference.DigestAlgAndValue.DigestValue.SequenceEqual(ocspDigest)){
                                found = true;
                                break;
                            }
                        } catch (CryptoException x){
                            throw new XMLSignatureException("Cant digest ocsp", x);
                        }
                    }
                    if (!found)
                    {
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.revocationValues"),
                                                    I18n.translate("validation.values.revVals.missingValue", "OCSP"),
                                                    ocspReference.ToString(), GetType());
                    }
                }
            }

            return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.revocationValues"),
                                        I18n.translate("validation.values.revVals.valid"),
                                        null, GetType());
		}

		public virtual string Name
		{
			get
			{
				return this.GetType().Name;
			}
		}
	}

}