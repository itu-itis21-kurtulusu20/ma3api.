package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CRLCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.OCSPResponseCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CRLReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.OCSPReference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.CertRevInfoExtractor;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.UniqueCertRevInfo;

import java.util.List;


/**
 * @author ahmety
 * date: Dec 31, 2009
 */
public class CompleteRevocationRefsValidator implements Validator
{
    /*
If CompleteRevocationRefs is present the verifier should perform the following
steps:

1) If RevocationValues is present, the verifier should check that they actually
provide adequate revocation information for all the certificates required for
verifying the electronic signature. If so, the verifier should check the
references in CompleteRevocationRefs against the values in RevocationValues
proceeding as indicated in step 3).

2) If RevocationValues is not present, the verifier should get the revocation
information data referenced in the property and check if they actually provide
adequate revocation information for all the certificates required for verifying
the electronic signature. If so, the verifier should check the references
against this data proceeding as indicated in step 3).
3) If the verifier has retrieved CRLs, the verifier should check that each CRL
correctly matches its reference within RevocationRefs. For doing this, for each
CRL:
    - If there is no CRLRefs element, the verifier should treat this signature
    as invalid. If there is a non-empty list, the verifier should take the first
    CRLRef element in the list and:
        a) Check that the string format of the issuer's DN of the CRL generated
        as stated in XMLDSIG, is the same as the value present in the Issuer
        element.

        b) Check that the time indicated by the thisUpdate field in the CRL is
        the same as the time indicated by the IssueTime element.

        c) If the CRL contains the cRLNumber extension, check that its value is
        the same as the value indicated by the Number element.

        d) If the aforementioned checks are successful, compute the digest of
        the CRL according to the algorithm indicated in the ds:DigestMethod
        element, base-64 encode the result and check if this is the same as the
        contents of the ds:DigestValue element.
    - If any of these checks fails, repeat the process for the next CRLRef
    elements until finding one satisfying them or finishing the list. If none of
    the references matches the CRL, the verifier should treat the signature as
    invalid.

4) If the verifier has retrieved OCSP responses, check that each OCSP response
correctly matches its reference within RevocationRefs. For doing this, for each
OCSP response:
    - If there is no OCSPRefs element, the verifier should treat this signature
    as invalid. If there is a list and is not empty, take the first OCSPRef
    element in the list and:
        a) Check that the content of ResponderID element matches the content of
        the responderID field within the OCSP response. If the content of this
        field is the byName choice, check if its string format is the same. If
        the content of this field is the byKey choice, the ResponderID should
        contain the base-64 encoded key digest. The verifier should check if
        this value matches the byKey choice.

        b) Check that the time indicated by the thisUpdate field in the OCSP
        response is the same as the time indicated by the ProducedAt element.

        c) If the aforementioned checks are successful, compute the digest of
        the OCSP response according to the algorithm indicated in the
        ds:DigestMethod element, base-64 encode the result and check if this is
        the same as the contents of the ds:DigestValue element.

        - If any of the checks fails, repeat the process for the next OCSPRef
        elements until finding one satisfying them or finishing the list. If
        none of the references matches the OCSP response, the verifier should
        treat the signature as invalid.
5) Check that there are no CRLRef elements referencing other CRLs than those
that have been retrieved in steps 1) or 2).

6) Check that there are no OCSPRef elements referencing other OCSP responses
than those that have been retrieved in steps 1) or 2).

     */
    private static Logger logger = LoggerFactory.getLogger(CompleteRevocationRefsValidator.class);
    private CertRevInfoExtractor extractor = new CertRevInfoExtractor();


    /**
     * @param aSignature   to be validated
     * @param aCertificate used for signature
     * @return null if this validator is not related to signature
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          if unexpected errors occur on IO, or
     *          crypto operations etc.
     */
    public ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate) throws XMLSignatureException
    {
        QualifyingProperties qp = aSignature.getQualifyingProperties();
        UnsignedSignatureProperties usp = qp.getUnsignedSignatureProperties();
        if (usp==null)
            return null;

        CompleteRevocationRefs revocationRefs = usp.getCompleteRevocationRefs();
        if (revocationRefs==null)
            return null;

        Context context = aSignature.getContext();
        boolean strictReferences = context.getConfig().getValidationConfig().isForceStrictReferences();
        ValidationResultType errorType = strictReferences ? ValidationResultType.INVALID : ValidationResultType.WARNING;

        // sertifika doğrulama zincirindeki iptal bilgileri, referanslarda var mi?
        CertificateStatusInfo current = context.getValidationResult(aSignature);

        UniqueCertRevInfo info = extractor.trace(current);

        for(ECRL crl : info.getCrls())
        {
             if (!contains(revocationRefs, crl)){

                 logger.info(I18n.translate("validation.references.revRefs.missing"));
                 logger.info(crl.toString());

                 return new ValidationResult(errorType,
                                            I18n.translate("validation.check.revocationRefs"),
                                            I18n.translate("validation.references.revRefs.missing", "CRL"),
                                            crl.toString(), getClass());
             }
        }

        for(EOCSPResponse ocsp : info.getOcspResponses())
        {
            System.out.println("OCSP : " + ocsp);
            if (!contains(revocationRefs, ocsp)){

                logger.info(I18n.translate("validation.references.revRefs.missing"));
                logger.info(ocsp.toString());

                return new ValidationResult(errorType,
                                            I18n.translate("validation.check.revocationRefs"),
                                            I18n.translate("validation.references.revRefs.missing", "OCSP"),
                                            ocsp.toString(), getClass());
            }
        }

        // eldeki referanslar dogrulamada kullanilmis mi?

        for (int i=0; i<revocationRefs.getOCSPReferenceCount(); i++){
            OCSPReference ocspRef = revocationRefs.getOCSPReference(i);
            if (!contains(info.getOcspResponses(), ocspRef) && !containsOCSPReference(context.getTimestampValidationResults(aSignature), ocspRef)){

                logger.info(I18n.translate("validation.references.revRefs.extra"));
                logger.info(ocspRef.toString());

                return new ValidationResult(errorType,
                                            I18n.translate("validation.check.revocationRefs"),
                                            I18n.translate("validation.references.revRefs.extra"),
                                            ocspRef.toString(), getClass());
            }
        }
        for (int i=0; i<revocationRefs.getCRLReferenceCount(); i++){
            CRLReference crlRef = revocationRefs.getCRLReference(i);
            if (!contains(info.getCrls(), crlRef) && !containsCrlReference(context.getTimestampValidationResults(aSignature), crlRef)){

                logger.info(I18n.translate("validation.references.revRefs.extra"));
                logger.info(crlRef.toString());

                return new ValidationResult(errorType,
                                            I18n.translate("validation.check.revocationRefs"),
                                            I18n.translate("validation.references.revRefs.extra"),
                                            crlRef.toString(), getClass());
            }
        }

        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.revocationRefs"),
                                    I18n.translate("validation.references.revRefs.valid"),
                                    null, getClass());
    }

    private OCSPResponseCriteriaMatcher mOcspMatcher = new OCSPResponseCriteriaMatcher();
    private CRLCriteriaMatcher mCrlMatcher = new CRLCriteriaMatcher();

    private boolean contains(CompleteRevocationRefs aRefs, ECRL aCrl){
        for (int i=0;i<aRefs.getCRLReferenceCount();i++){
            CRLReference ref = aRefs.getCRLReference(i);
            CRLSearchCriteria criteria = ref.toSearchCriteria();
            if (mCrlMatcher.match(criteria, aCrl))
                return true;
        }
        return false;
    }

    private boolean contains(CompleteRevocationRefs aRefs, EOCSPResponse aOcsp){
        for (int i=0;i<aRefs.getOCSPReferenceCount();i++){
            OCSPReference ref = aRefs.getOCSPReference(i);
            OCSPSearchCriteria criteria = ref.toSearchCriteria();
            if (mOcspMatcher.match(criteria, aOcsp))
                return true;
        }
        return false;
    }

    private boolean contains(List<EOCSPResponse> aResponses, OCSPReference aOcspRef){
        OCSPSearchCriteria criteria = aOcspRef.toSearchCriteria();
        for (EOCSPResponse response : aResponses) {
            if (mOcspMatcher.match(criteria, response))
                return true;
        }
        return false;
    }

    private boolean contains(List<ECRL> aCrls, CRLReference aCrlRef){
        CRLSearchCriteria criteria = aCrlRef.toSearchCriteria();
        for (ECRL crl : aCrls) {
            if (mCrlMatcher.match(criteria, crl))
                return true;
        }
        return false;
    }

    private boolean containsOCSPReference(List<SignedDataValidationResult> aSDVRs, OCSPReference aReference)
    {
        for (SignedDataValidationResult sdvr : aSDVRs){
            UniqueCertRevInfo info = extractor.trace(sdvr);
            if (contains(info.getOcspResponses(), aReference)){
                return true;
            }
        }
        return false;
    }

    private boolean containsCrlReference(List<SignedDataValidationResult> aSDVRs, CRLReference aReference)
    {
        for (SignedDataValidationResult sdvr : aSDVRs){

            UniqueCertRevInfo info = extractor.trace(sdvr);
            if (contains(info.getCrls(), aReference))
                return true;
        }
        return false;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
}
