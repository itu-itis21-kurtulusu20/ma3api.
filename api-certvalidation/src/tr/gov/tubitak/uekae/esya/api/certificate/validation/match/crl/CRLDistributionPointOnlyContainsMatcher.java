package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EBasicConstraints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EIssuingDistributionPoint;

/**
 * Matches a Certificate and a CRL according to the
 * CRLDistributionPointOnlyContains extension information.
 *
 * @author IH
 */
public class CRLDistributionPointOnlyContainsMatcher extends CRLMatcher {

    private static final Logger logger = LoggerFactory.getLogger(CRLDistributionPointOnlyContainsMatcher.class);

    /**
     * Sil Issuing Distribution Point eklentisindeki onlyContains özellikleri ile
     * Sertifika  BasicConstraint eklentisini eşleştirir
     */
    protected boolean _matchCRL(ECertificate aCertificate, ECRL aCRL)
    {
        EIssuingDistributionPoint idp = aCRL.getCRLExtensions().getIssuingDistributionPoint();
        if (idp == null) {
            logger.debug("Silde Issuing Distribution Point uzantısı yok");
            return true;
        }

        EBasicConstraints bc = aCertificate.getExtensions().getBasicConstraints();
        if (bc != null && bc.isCA() && idp.isOnlyContainsUserCerts()) {
            logger.debug("Sertifika SM sertifikası, Sil onlyContainsUserCerts");
            return false;
        }
        if (!(bc != null && bc.isCA()) && idp.isOnlyContainsCACerts()) {
            logger.debug("Sertifika SM sertifikası değil, Sil onlyContainsCACerts");
            return false;
        }
        return true;
    }

}
