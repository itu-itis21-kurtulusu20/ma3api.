package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;

/**
 * Matches The base CRL and delta by checking whether they have the same issuer. 
 */
public class DeltaCRLIssuerMatcher extends DeltaCRLMatcher
{
    /**
     * Verilen Base SİL ile delta-SİL i issuer alanlarına bakarak eşleştirir
     */
    protected boolean _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL)
    {
        return aCRL.getIssuer().equals(aDeltaCRL.getIssuer());
    }
}
