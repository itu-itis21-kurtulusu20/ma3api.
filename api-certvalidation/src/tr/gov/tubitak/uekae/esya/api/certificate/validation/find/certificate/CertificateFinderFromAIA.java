package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds issuer certificate according to Authority Info Access (AIA) extension
 * information. 
 */
public abstract class CertificateFinderFromAIA extends CertificateFinder
{
    private static Logger logger = LoggerFactory.getLogger(CertificateFinderFromAIA.class);
    private static FixedSizedCache<String, ECertificate> CERT_CACHE = new FixedSizedCache<>(32, Duration.ofHours(2));

    /**
     * Finds issuer certificate of input certificate from AuthorityInfoAccess extension.
     */
    protected List<ECertificate> _findCertificate(ECertificate eCertificate)
    {
        if (eCertificate == null) {
            return null;
        }

        logger.debug("AIA için gelen sertifika "+eCertificate.getSubject().stringValue());

        List<String> addresses = _getAddresses(eCertificate);
        if (addresses == null || addresses.isEmpty()) {
            logger.debug("Sertifika AIA uzantısından adres alınamadı");
            return null;
        }

        boolean useCache = mParameters.getParameterBoolean(CACHE, true);

        List<ECertificate> certificates = new ArrayList<ECertificate>();
        for (String address : addresses) 
        {
            ECertificate certificate = null;

            if(useCache) {
                try {
                    certificate = CERT_CACHE.getItem(address, () -> _getCertificate(address));
                } catch (Exception e) {
                    logger.error("Can not get certificate from " + address, e);
                }
            } else {
                certificate = _getCertificate(address);
            }

            if (certificate!=null)
                certificates.add(certificate);
            else
                logger.warn("Certificate can not be read from " + address);

        }
        return certificates;
    }

    protected abstract List<String> _getAddresses(ECertificate aCertificate);

    protected abstract ECertificate _getCertificate(String aAddress);


}
