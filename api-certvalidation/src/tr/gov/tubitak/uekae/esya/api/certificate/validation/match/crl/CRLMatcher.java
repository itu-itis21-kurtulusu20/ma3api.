package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.Matcher;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Base class for crl matchers.
 *
 * @author IH
 */
public abstract class CRLMatcher extends Matcher {

    protected abstract boolean _matchCRL(ECertificate aCertificate, ECRL aCRL);

    /**
     * Sertifika ile SİL'i eşleştirir
     */
    public boolean matchCRL(ECertificate aCertificate, ECRL aCRL) {
        return (_matchCRL(aCertificate, aCRL));
    }

}
