using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross
{
    /**
     * Base class for Cross Certificate Matcher classes.
     *
     * @author IH
     */
    public abstract class CrossCertificateMatcher : Matcher
    {
        protected abstract bool _matchCrossCertificate(ECertificate aCertificate,
                                                      ECertificate aCrossCertificate);

        /**
         * Sertifika ile çapraz sertifika arasındaki eşleştirmeyi yapar
         */
        public bool matchCrossCertificate(ECertificate aCertificate,
                                             ECertificate aCrossCertificate)
        {
            return (_matchCrossCertificate(aCertificate, aCrossCertificate));
        }
    }
}
