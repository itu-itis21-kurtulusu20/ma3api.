using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.validator
{

    using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
    using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
    using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
    using CertificateStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
    using CRLStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
    using CRLStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
    using OCSPResponseStatusInfo = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo;
    using OCSPResponseStatus = tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPResponseStatusInfo.OCSPResponseStatus;
    using CRLCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.CRLCriteriaMatcher;
    using CRLSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
    using OCSPResponseCriteriaMatcher = tr.gov.tubitak.uekae.esya.api.signature.certval.match.OCSPResponseCriteriaMatcher;
    using OCSPSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
    using SignatureValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignatureValidationResult;
    using SignedDataValidationResult = tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
    using tr.gov.tubitak.uekae.esya.api.xmlsignature;
    using QualifyingProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.QualifyingProperties;
    using UnsignedSignatureProperties = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
    using CRLReference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CRLReference;
    using CompleteRevocationRefs = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
    using OCSPReference = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.OCSPReference;
    using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;



    /// <summary>
    /// @author ahmety
    /// date: Dec 31, 2009
    /// </summary>
    public class CompleteRevocationRefsValidator : Validator
    {
        private readonly CertRevInfoExtractor extractor = new CertRevInfoExtractor();
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



        /// <param name="aSignature">   to be validated </param>
        /// <param name="aCertificate"> used for signature </param>
        /// <returns> null if this validator is not related to signature </returns>
        /// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
        ///          if unexpected errors occur on IO, or
        ///          crypto operations etc. </exception>
        public virtual ValidationResult validate(XMLSignature aSignature, ECertificate aCertificate)
        {
            QualifyingProperties qp = aSignature.QualifyingProperties;
            UnsignedSignatureProperties usp = qp.UnsignedSignatureProperties;
            if (usp == null)
            {
                return null;
            }

            CompleteRevocationRefs revocationRefs = usp.CompleteRevocationRefs;
            if (revocationRefs == null)
            {
                return null;
            }

            Context context = aSignature.Context;
            bool strictReferences = context.Config.ValidationConfig.ForceStrictReferences;
            ValidationResultType errorType = strictReferences ? ValidationResultType.INVALID : ValidationResultType.WARNING;


            // sertifika dogrulama zincirindeki iptal bilgileri, referanslarda var mi?
            CertificateStatusInfo current = context.getValidationResult(aSignature);

            UniqueCertRevInfo info = extractor.trace(current);

            foreach (ECRL crl in info.getCrls())
            {
                if (!contains(revocationRefs, crl))
                {
                    return new ValidationResult(errorType,
                                                I18n.translate("validation.check.revocationRefs"),
                                                I18n.translate("validation.references.revRefs.missing", "CRL"),
                                                crl.ToString(), GetType());
                }
            }

            foreach (EOCSPResponse ocsp in info.getOcspResponses())
            {
                if (!contains(revocationRefs, ocsp))
                {
                    return new ValidationResult(errorType,
                                                I18n.translate("validation.check.revocationRefs"),
                                                I18n.translate("validation.references.revRefs.missing", "OCSP"),
                                                ocsp.ToString(), GetType());
                }
            }

            // eldeki referanslar dogrulamada kullanilmis mi?

            for (int i = 0; i < revocationRefs.OCSPReferenceCount; i++)
            {
                OCSPReference ocspRef = revocationRefs.getOCSPReference(i);
                if (!contains(info.getOcspResponses(), ocspRef) && !containsOCSPReference(context.getTimestampValidationResults(aSignature), ocspRef))
                {
                    return new ValidationResult(errorType,
                                                I18n.translate("validation.check.revocationRefs"),
                                                I18n.translate("validation.references.revRefs.extra"),
                                                ocspRef.ToString(), GetType());
                }
            }
            for (int i = 0; i < revocationRefs.CRLReferenceCount; i++)
            {
                CRLReference crlRef = revocationRefs.getCRLReference(i);
                if (!contains(info.getCrls(), crlRef) && !containsCrlReference(context.getTimestampValidationResults(aSignature), crlRef))
                {
                    return new ValidationResult(errorType,
                                                I18n.translate("validation.check.revocationRefs"),
                                                I18n.translate("validation.references.revRefs.extra"),
                                                crlRef.ToString(), GetType());
                }
            }
            return new ValidationResult(ValidationResultType.VALID,
                                        I18n.translate("validation.check.revocationRefs"),
                                        I18n.translate("validation.references.revRefs.valid"),
                                        null, GetType());
        }

        private readonly OCSPResponseCriteriaMatcher mOcspMatcher = new OCSPResponseCriteriaMatcher();
        private readonly CRLCriteriaMatcher mCrlMatcher = new CRLCriteriaMatcher();

        private bool contains(CompleteRevocationRefs aRefs, ECRL aCrl)
        {
            for (int i = 0; i < aRefs.CRLReferenceCount; i++)
            {
                CRLReference @ref = aRefs.getCRLReference(i);
                CRLSearchCriteria criteria = @ref.toSearchCriteria();
                if (mCrlMatcher.match(criteria, aCrl))
                {
                    return true;
                }
            }
            return false;
        }

        private bool contains(CompleteRevocationRefs aRefs, EOCSPResponse aOcsp)
        {
            for (int i = 0; i < aRefs.OCSPReferenceCount; i++)
            {
                OCSPReference @ref = aRefs.getOCSPReference(i);
                OCSPSearchCriteria criteria = @ref.toSearchCriteria();
                if (mOcspMatcher.match(criteria, aOcsp))
                {
                    return true;
                }
            }
            return false;
        }

        private bool contains(IList<EOCSPResponse> aResponses, OCSPReference aOcspRef)
        {
            OCSPSearchCriteria criteria = aOcspRef.toSearchCriteria();
            foreach (EOCSPResponse response in aResponses)
            {
                if (mOcspMatcher.match(criteria, response))
                {
                    return true;
                }
            }
            return false;
        }

        private bool contains(IList<ECRL> aCrls, CRLReference aCrlRef)
        {
            CRLSearchCriteria criteria = aCrlRef.toSearchCriteria();
            foreach (ECRL crl in aCrls)
            {
                if (mCrlMatcher.match(criteria, crl))
                {
                    return true;
                }
            }
            return false;
        }

        private bool containsOCSPReference(IList<SignedDataValidationResult> aSDVRs, OCSPReference aReference)
        {
            foreach (SignedDataValidationResult sdvr in aSDVRs)
            {
                UniqueCertRevInfo info = extractor.trace(sdvr);
                if (contains(info.getOcspResponses(), aReference))
                {
                    return true;
                }
            }
            return false;
        }

        private bool containsCrlReference(IList<SignedDataValidationResult> aSDVRs, CRLReference aReference)
        {
            foreach (SignedDataValidationResult sdvr in aSDVRs)
            {

                UniqueCertRevInfo info = extractor.trace(sdvr);
                if (contains(info.getCrls(), aReference))
                    return true;
            }
            return false;
        }

        private IList<CRLStatusInfo> extractAllCrlInfos(CertificateStatusInfo csi)
        {
            List<CRLStatusInfo> infos = clear(csi.getCRLInfoList());
            CertificateStatusInfo current = csi;
            while ((current = current.getSigningCertficateInfo()) != null)
            {
                infos.AddRange(clear(current.getCRLInfoList()));
            }
            return infos;
        }

        private List<CRLStatusInfo> clear(IList<CRLStatusInfo> rawList)
        {
            List<CRLStatusInfo> result = new List<CRLStatusInfo>();
            foreach (CRLStatusInfo csi in rawList)
            {
                if (csi.getCRLStatus() == CRLStatus.VALID)
                {
                    result.Add(csi);
                }
            }
            return result;
        }

        private List<CRLStatusInfo> extractAllDeltaCrlInfos(CertificateStatusInfo csi)
        {
            List<CRLStatusInfo> infos = clear(csi.getDeltaCRLInfoList());
            CertificateStatusInfo current = csi;
            while ((current = current.getSigningCertficateInfo()) != null)
            {
                infos.AddRange(clear(current.getDeltaCRLInfoList()));
            }
            return infos;
        }

        private List<OCSPResponseStatusInfo> extractAllOCSPInfos(CertificateStatusInfo csi)
        {
            List<OCSPResponseStatusInfo> infos = clearOCSPInfos(csi.getOCSPResponseInfoList());
            CertificateStatusInfo current = csi;
            while ((current = current.getSigningCertficateInfo()) != null)
            {
                infos.AddRange(clearOCSPInfos(current.getOCSPResponseInfoList()));
            }
            return infos;
        }

        private List<OCSPResponseStatusInfo> clearOCSPInfos(IList<OCSPResponseStatusInfo> rawList)
        {
            List<OCSPResponseStatusInfo> result = new List<OCSPResponseStatusInfo>();
            foreach (OCSPResponseStatusInfo orsi in rawList)
            {
                if (orsi.getOCSPResponseStatus() == OCSPResponseStatusInfo.OCSPResponseStatus.VALID)
                {
                    result.Add(orsi);
                }
            }
            return result;
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