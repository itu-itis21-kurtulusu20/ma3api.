using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl
{
    /**
     * Matches The base CRL and delta by checking whether they have the same issuer. 
     */
    public class DeltaCRLIssuerMatcher: DeltaCRLMatcher
    {
        /**
         * Verilen Base SİL ile delta-SİL i issuer alanlarına bakarak eşleştirir
         */
        protected override bool _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
        {
            return aCRL.getIssuer().Equals(aDeltaCRL.getIssuer());
        }
    }
}
