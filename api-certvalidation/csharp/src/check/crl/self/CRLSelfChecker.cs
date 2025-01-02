using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self
{
    /**
     * <p>Base class for CRL self checkers.
     *
     * <p>CRL Self checkers perform the controls about the structure and the format
     * of the crl itself.
     *
     * @author IH
     */
    public abstract class CRLSelfChecker : Checker
    {
        public CRLSelfChecker() : base() { }

        protected abstract PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo);

        public PathValidationResult check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
        {
            return _check(aCRL, aCRLStatusInfo);
        }
    }
}
