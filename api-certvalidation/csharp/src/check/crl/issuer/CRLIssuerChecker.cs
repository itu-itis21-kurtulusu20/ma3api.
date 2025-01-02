using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer
{
    /**
     * Base class for crl issuer checkers.
     *
     * CRL Issuer Checkers perform controls about the features of the crl related
     * with its issuer.
     *
     * @author IH
     */
    public abstract class CRLIssuerChecker :Checker
    {
        protected abstract PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                                   ECRL aCRL,
                                                   ECertificate aIssuerCertificate,
                                                   CRLStatusInfo aCRLStatusInfo);
        
        /**
         * SİL'in imzalayan sertifikası ile ilgili kontrollerini yapar
         */
        public PathValidationResult check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECRL aCRL,
                                          ECertificate aIssuerCertificate,
                                          CRLStatusInfo aCRLStatusInfo)
        {
            return _check(aIssuerCheckParameters, aCRL, aIssuerCertificate, aCRLStatusInfo);
        }

    }
}
