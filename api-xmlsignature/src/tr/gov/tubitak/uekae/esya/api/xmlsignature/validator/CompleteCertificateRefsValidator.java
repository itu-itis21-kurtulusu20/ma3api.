package tr.gov.tubitak.uekae.esya.api.xmlsignature.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.certval.match.CertificateCriteriaMatcher;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteCertificateRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.CertRevInfoExtractor;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.UniqueCertRevInfo;

import java.util.List;


/**
 * If CompleteCertificateRefs is present the verifier should:
 * <ul><li>
 * 1) Gain access to all the CA certificates that are part of the certification
 * path
 * <li>
 * 2) Check that for each certificate in the aforementioned set, the property
 * contains its corresponding reference. For doing this the values of the
 * IssuerSerial, ds:DigestMethod and ds:DigestValue should be checked
 * <li>
 * 3) Check that there are no references to certificates out of those that are
 * part of the certification path.
 * </ul>
 *
 * @author ahmety
 * date: Nov 13, 2009
 */
public class CompleteCertificateRefsValidator implements Validator
{

    private static Logger logger = LoggerFactory.getLogger(CompleteCertificateRefsValidator.class);

    private CertificateCriteriaMatcher mMatcher = new CertificateCriteriaMatcher();
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

        CompleteCertificateRefs certRefs = usp.getCompleteCertificateRefs();
        if (certRefs==null)
            return null;

        Context context = aSignature.getContext();

        CertificateStatusInfo csi = context.getValidationResult(aSignature);

        UniqueCertRevInfo info = extractor.trace(csi);
        List<ECertificate> certs = info.getCertificates();

        // referanslar cert validation path'de var mi?
        for (int i=0; i<certRefs.getCertificateReferenceCount();i++){
            CertID certId = certRefs.getCertificateReference(i);
            if (!contains(certs, certId)){
                // bir de timestamp validasyonunda kullanilmis mi ona bakalim...
                if (!validationresultsContainsCertId(context.getTimestampValidationResults(aSignature), certId)) {

                    logger.info(I18n.translate("validation.references.certRefs.extraCert"));
                    logger.info(certId+"");

                    return new ValidationResult(ValidationResultType.INVALID,
                            I18n.translate("validation.check.certRefs"),
                            I18n.translate("validation.references.certRefs.extraCert"),
                            certId.toString(), XMLSignature.class);
                }
            }
        }

        // validation path'deki sertifikalara referans var mi
        // imza sertifikasi completecertrefs de olmak zorunda degil..
        for (ECertificate cert : certs){
            if (!contains(certRefs, cert) && !aCertificate.equals(cert) && !(cert.isCACertificate() && cert.isSelfIssued())){
                logger.info(I18n.translate("validation.references.certRefs.missingCert"));
                logger.info(cert+"");

                return new ValidationResult(ValidationResultType.INVALID,
                                            I18n.translate("validation.check.certRefs"),
                                            I18n.translate("validation.references.certRefs.missingCert"),
                                            cert.toString(), getClass());
            }
        }

        return new ValidationResult(ValidationResultType.VALID,
                                    I18n.translate("validation.check.certRefs"),
                                    I18n.translate("validation.references.certRefs.valid"),
                                    null, getClass());
    }

    private boolean validationresultsContainsCertId(List<SignedDataValidationResult> aValidationResults, CertID aCertID){
        for (SignedDataValidationResult validationResult : aValidationResults) {
            UniqueCertRevInfo info = extractor.trace(validationResult);
            if (contains(info.getCertificates(), aCertID))
                return true;
        }
        return false;
    }

    private boolean contains(List<ECertificate> aCertificates, CertID aCertID){
        CertificateSearchCriteria criteria = aCertID.toSearchCriteria();

        for (ECertificate certificate : aCertificates) {
            if (mMatcher.match(criteria, certificate))
                return true;
        }
        return false;
    }

    private boolean contains(CompleteCertificateRefs aRefs, ECertificate aCertificate){
        for (int i=0; i< aRefs.getCertificateReferenceCount(); i++){
            CertID ref = aRefs.getCertificateReference(i);
            CertificateSearchCriteria criteria = ref.toSearchCriteria();

            if (mMatcher.match(criteria, aCertificate))
                return true;
        }
        return false;
    }


    public String getName()
    {
        return getClass().getSimpleName();
    }

}
