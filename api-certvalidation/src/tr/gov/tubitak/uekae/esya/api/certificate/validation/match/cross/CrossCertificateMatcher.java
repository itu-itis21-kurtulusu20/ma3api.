package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.Matcher;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Base class for Cross Certificate Matcher classes.
 *
 * @author IH
 */
public abstract class CrossCertificateMatcher extends Matcher
{
    protected abstract boolean _matchCrossCertificate(ECertificate aCertificate,
                                                      ECertificate aCrossCertificate);

    /**
     * Sertifika ile çapraz sertifika arasındaki eşleştirmeyi yapar
     */
    public boolean matchCrossCertificate(ECertificate aCertificate,
                                         ECertificate aCrossCertificate)
    {
        return (_matchCrossCertificate(aCertificate, aCrossCertificate));
    }

}
