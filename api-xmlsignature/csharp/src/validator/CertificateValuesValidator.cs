using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using ValidationResult = tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

    /**
     * @author ayetgin
     * Checks the consistency of CertificateValues and CompleteCertificateReferences
     */
	public class CertificateValuesValidator : Validator
	{

        /**
         * Validates as INVALID if certificate reference and value counts do not match or,
         * there is no value for at least one reference; otherwise VALID.
         * Matching is done according to the digest algorithm of each reference.
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

                CertificateValues values = aSignature.QualifyingProperties.UnsignedSignatureProperties.CertificateValues;
                CompleteCertificateRefs refs = aSignature.QualifyingProperties.UnsignedSignatureProperties.CompleteCertificateRefs;

                // check if certificate value and reference counts match
                if (values.CertificateCount != refs.CertificateReferenceCount){
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.certValues"),
                                                I18n.translate("validation.values.certVals.countMismatch"),
                                                null, GetType());
                }

                IList<ECertificate> allCerts = values.AllCertificates;
                // for each reference...
                for (int i=0; i<refs.CertificateReferenceCount; i++){
                    CertID id = refs.getCertificateReference(i);
                    bool found = false;
                    // ...check if there is a corresponding value
                    foreach (ECertificate certificate in allCerts){
                        try {
                            byte[] certDigest  = DigestUtil.digest(id.DigestMethod.Algorithm, certificate.getEncoded());
                            if (id.DigestValue.SequenceEqual(certDigest)){
                                found = true;
                                break;
                            }
                        } catch (CryptoException x){
                            throw new XMLSignatureException("Cant digest cert", x);
                        }
                    }
                    if (!found)
                    {
                        return new ValidationResult(ValidationResultType.INVALID,
                                                    I18n.translate("validation.check.certValues"),
                                                    I18n.translate("validation.values.certVals.missingValue"),
                                                    id.ToString(), typeof(XMLSignature));
                    }
                }
            }

            return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.certValues"),
                                        I18n.translate("validation.values.certVals.valid"),
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