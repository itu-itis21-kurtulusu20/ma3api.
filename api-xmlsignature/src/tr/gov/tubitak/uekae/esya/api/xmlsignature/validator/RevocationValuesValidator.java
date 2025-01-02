package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CRLReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.OCSPReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.Arrays;
import java.util.List;

/**
 * @author ayetgin
 * Checks the consistency of RevocationValues and CompleteRevocationReferences
 */
public class RevocationValuesValidator implements Validator
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
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        Context context = aSignature.getContext();
        boolean strict = context.getConfig().getValidationConfig().isForceStrictReferences();
        SignatureType type = aSignature.getSignatureType();

        // check only if strict reference is forced and for signature types XAdES_X_L and XAdES_A
        if (strict && (type==SignatureType.XAdES_X_L || type==SignatureType.XAdES_A)){

            RevocationValues values = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getRevocationValues();
            CompleteRevocationRefs refs = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCompleteRevocationRefs();

            // check if CRL value and reference counts match
            if (values.getCRLValueCount() != refs.getCRLReferenceCount()){
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.revocationValues"),
                                            I18n.translate("validation.values.revVals.countMismatch", "CRL"),
                                            null, getClass());
            }

            // check if OCSP value and reference counts match
            if (values.getOCSPValueCount() != refs.getOCSPReferenceCount()){
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.revocationValues"),
                                            I18n.translate("validation.values.revVals.countMismatch", "OCSP"),
                                            null, getClass());
            }

            // check CRL consistency
            List<ECRL> allCRLs = values.getAllCRLs();
            // for each CRL reference...
            for (int i=0; i<refs.getCRLReferenceCount(); i++){
                CRLReference crlReference = refs.getCRLReference(i);
                boolean found = false;
                // ...check if there is a corresponding value
                for (ECRL crl : allCRLs){
                    try {
                        byte[] crlDigest  = DigestUtil.digest(crlReference.getDigestMethod().getAlgorithm(), crl.getEncoded());
                        if (Arrays.equals(crlReference.getDigestValue(), crlDigest)){
                            found = true;
                            break;
                        }
                    } catch (CryptoException x){
                        throw new XMLSignatureException("Cant digest crl", x);
                    }
                }
                if (!found){
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.revocationValues"),
                                                I18n.translate("validation.values.revVals.missingValue", "CRL"),
                                                crlReference.toString(), getClass());
                }
            }

            // check OCSP consistency
            List<EOCSPResponse> allOCSPResponses = values.getAllOCSPResponses();
            // for each OCSP reference...
            for (int i=0; i<refs.getOCSPReferenceCount(); i++){
                OCSPReference ocspReference = refs.getOCSPReference(i);
                boolean found = false;
                // ...check is there is a corresponding value
                for (EOCSPResponse ocspResponse : allOCSPResponses){
                    try {
                        byte[] ocspDigest  = DigestUtil.digest(ocspReference.getDigestAlgAndValue().getDigestMethod().getAlgorithm(), ocspResponse.getEncoded());
                        if (Arrays.equals(ocspReference.getDigestAlgAndValue().getDigestValue(), ocspDigest)){
                            found = true;
                            break;
                        }
                    } catch (CryptoException x){
                        throw new XMLSignatureException("Cant digest ocsp", x);
                    }
                }
                if (!found){
                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.revocationValues"),
                            I18n.translate("validation.values.revVals.missingValue", "OCSP"),
                            ocspReference.toString(), getClass());
                }
            }
        }

        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.revocationValues"),
                                    I18n.translate("validation.values.revVals.valid"),
                                    null, getClass());
    }

    public String getName()
    {
        return getClass().getName();  
    }
}
