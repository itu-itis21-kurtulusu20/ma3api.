package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CertificateValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.util.Arrays;
import java.util.List;

/**
 * @author ayetgin
 * Checks the consistency of CertificateValues and CompleteCertificateReferences
 */
public class CertificateValuesValidator implements Validator
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
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        Context context = aSignature.getContext();
        boolean strict = context.getConfig().getValidationConfig().isForceStrictReferences();
        SignatureType type = aSignature.getSignatureType();

        // check only if strict reference is forced and for signature types XAdES_X_L and XAdES_A
        if (strict && (type==SignatureType.XAdES_X_L || type==SignatureType.XAdES_A)){

            CertificateValues values = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCertificateValues();
            CompleteCertificateRefs refs = aSignature.getQualifyingProperties().getUnsignedSignatureProperties().getCompleteCertificateRefs();

            // check if certificate value and reference counts match
            if (values.getCertificateCount() != refs.getCertificateReferenceCount()){
                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.certValues"),
                                            I18n.translate("validation.values.certVals.countMismatch"),
                                            null, getClass());
            }

            List<ECertificate> allCerts = values.getAllCertificates();
            // for each reference...
            for (int i=0; i<refs.getCertificateReferenceCount(); i++){
                CertID id = refs.getCertificateReference(i);
                boolean found = false;
                // ...check if there is a corresponding value
                for (ECertificate certificate : allCerts){
                    try {
                        byte[] certDigest  = DigestUtil.digest(id.getDigestMethod().getAlgorithm(), certificate.getEncoded());
                        if (Arrays.equals(id.getDigestValue(), certDigest)){
                            found = true;
                            break;
                        }
                    } catch (CryptoException x){
                        throw new XMLSignatureException("Cant digest cert", x);
                    }
                }
                if (!found){
                    return new ValidationResult(ValidationResultType.INVALID,
                                                I18n.translate("validation.check.certValues"),
                                                I18n.translate("validation.values.certVals.missingValue"),
                                                id.toString(), XMLSignature.class);
                }
            }
        }

        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.certValues"),
                                    I18n.translate("validation.values.certVals.valid"),
                                    null, getClass());
    }

    public String getName()
    {
        return getClass().getName();   
    }
}
