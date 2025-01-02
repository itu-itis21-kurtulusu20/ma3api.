package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.deltacrl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.Matcher;

/**
 * Base class for delta-CRL matchers.
 *
 * Delta-CRL matchers decides if a base CRL and a delta-CRL are matching by
 * checking different features specified in RFC 5280. 
 *
 * @author IH
 */
public abstract class DeltaCRLMatcher extends Matcher {

    public DeltaCRLMatcher()
    {
        super();
    }

    protected abstract boolean _macthDeltaCRL(ECRL aCRL, ECRL aDeltaCRL);

    /**
     * Verilen Base SİL ile delta-SİL i eşleştirir
     */
    public boolean deltaSilEslestir(ECRL aCRL, ECRL aDeltaCRL)
    {
        if (!_macthDeltaCRL(aCRL, aDeltaCRL)) {
            return false;
        }
        else if (mNextMatcher != null) {
            return ((DeltaCRLMatcher) mNextMatcher).deltaSilEslestir(aCRL, aDeltaCRL);
        }
        else {
            return true;
        }
    }

}
