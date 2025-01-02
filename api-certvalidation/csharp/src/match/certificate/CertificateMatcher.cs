using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate
{
    /**
     * Base class for certificate matchers.
     *
     * <p>Matches Certificate - Issuer certificate or
     * CRL - CRL issuers certificate.
     *</p>
     *
     * @author IH
     */
    public abstract class CertificateMatcher :Matcher
    {
        protected ECertificate mFoundCertificate;

        protected abstract bool _matchCertificate(ECertificate aSertifika);
        protected abstract bool _matchCertificate(ECRL aSil);

        /**
        * İki sertifikayı SMSertifikası-Sertifika ilişkisi şeklinde eşleştirir.
        */
        public bool matchCertificate(ECertificate aCertificate, ECertificate aIssuerCertificate)
        {
            mFoundCertificate = aIssuerCertificate;
            return (_matchCertificate(aCertificate));
        }

        public bool matchCertificate(ECRL aCRL, ECertificate aIssuerCertificate)
        {
            mFoundCertificate = aIssuerCertificate;
            return (_matchCertificate(aCRL));

        }       
    }
}
