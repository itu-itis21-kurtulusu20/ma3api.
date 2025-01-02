package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.Matcher;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Base class for certificate matchers.
 *
 * <p>Matches Certificate - Issuer certificate or
 * CRL - CRL issuers certificate.
 *</p>
 *
 * @author IH
 */
public abstract class CertificateMatcher extends Matcher
{
    protected ECertificate mFoundCertificate;

    protected abstract boolean _matchCertificate(ECertificate aSertifika);
	protected abstract boolean _matchCertificate(ECRL aSil);

    /**
    * İki sertifikayı SMSertifikası-Sertifika ilişkisi şeklinde eşleştirir.
    */
    public boolean matchCertificate(ECertificate aCertificate, ECertificate aIssuerCertificate)
    {
        mFoundCertificate = aIssuerCertificate;
        return (_matchCertificate(aCertificate));
    }

    public boolean matchCertificate(ECRL aCRL, ECertificate aIssuerCertificate)
    {
        mFoundCertificate = aIssuerCertificate;
        return (_matchCertificate(aCRL));

    }

}
