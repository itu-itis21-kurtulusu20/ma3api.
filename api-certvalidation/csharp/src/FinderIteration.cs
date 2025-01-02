using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Iteration for certificate finding 
     */
    public abstract class FinderIteration
    {
        public abstract bool nextIteration(ValidationSystem aValidationSystem, ECertificate iCertificate);

        protected abstract bool _nextSource(ValidationSystem aValidationSystem, ECertificate iCertificate);

    }
}
