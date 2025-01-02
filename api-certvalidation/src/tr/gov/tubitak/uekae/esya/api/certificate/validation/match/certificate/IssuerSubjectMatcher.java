package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Matches the certificate and the CA certificate according to the subject
 * field of the CA certificate and the issuer field of the certificate
 */
public class IssuerSubjectMatcher extends CertificateMatcher
{
    private static Logger logger = LoggerFactory.getLogger(IssuerSubjectMatcher.class);

    /**
     * Bulunan sertifikayı verilen sertifikayla issuer-sbuject ilişkisiyle eşleştirir
     */
    protected boolean _matchCertificate(ECertificate aCertificate)
    {
        return aCertificate.getIssuer().equals(mFoundCertificate.getSubject());
    }

    protected boolean _matchCertificate(ECRL aCRL)
    {
        boolean match = aCRL.getIssuer().equals(mFoundCertificate.getSubject());
        if (logger.isDebugEnabled())
        {
            logger.debug("match ? "+aCRL.getIssuer().stringValue());
            if (match)
                logger.debug("Found matching certificate!");
            else
                logger.trace(mFoundCertificate.getSubject().stringValue()+" does not match!");
        }
        return match;
    }

}

