using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl
{
    /**
     * Base class for crl matchers.
     *
     * @author IH
     */
    public abstract class CRLMatcher: Matcher
    {
        protected abstract bool _matchCRL(ECertificate aCertificate, ECRL aCRL);

        /**
         * Sertifika ile SİL'i eşleştirir
         */
        public bool matchCRL(ECertificate aCertificate, ECRL aCRL)
        {
            return (_matchCRL(aCertificate, aCRL));
        }
    }
}
