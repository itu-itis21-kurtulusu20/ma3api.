package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;

/**
 * Matches the certificate and the CA certificate according to the
 * SubjectKeyIdentifier extension of the CA certificate and
 * AuthorityKeyIdentifier extension of the certificate 
 */
public class KeyIDMatcher extends CertificateMatcher
{
    private static Logger logger = LoggerFactory.getLogger(KeyIDMatcher.class);

    /**
     * Sertifika ve verilen sertifika eşleşiyor mu Yetkili Anahtar Tanımlayıcı
     * eklentilerine bakarak kontrol eder
     */
    protected boolean _matchCertificate(ECertificate aCertificate)
    {
        EAuthorityKeyIdentifier aki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
        if (aki == null)
            return true;

        return _esitMi(aki);
    }

    /**
     * Sertifika ile verilen SİL'in imzalayan sertifikaları eşleşiyor mu
     * Yetkili Anahtar Tanımlayıcı eklentilerine bakarak kontrol eder
     */
    protected boolean _matchCertificate(ECRL aCRL)
    {
        EAuthorityKeyIdentifier aki = aCRL.getCRLExtensions().getAuthorityKeyIdentifier();
        if (aki == null) {
            logger.debug("Authority key Id not found!");
            return true;
        }


        return _esitMi(aki);
    }

    private boolean _esitMi(EAuthorityKeyIdentifier aAKI)
    {
        ESubjectKeyIdentifier smSki = mFoundCertificate.getExtensions().getSubjectKeyIdentifier();
        if (smSki==null){
            logger.debug("Found cert Authority key Id not found!");
            return true;
        }
        boolean akiMatchesSKi = UtilEsitlikler.esitMi(aAKI.getKeyIdentifier(), smSki.getValue());
        if (!akiMatchesSKi){
            logger.trace("Autority Key Id does not match Subject Key Id!");
        }
        return akiMatchesSKi; 
    }

}
