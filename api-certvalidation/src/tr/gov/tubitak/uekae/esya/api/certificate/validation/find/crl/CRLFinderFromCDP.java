package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate according to the CRL Distribution Points
 * (CDP) extension information in the certificate.
 */
public abstract class CRLFinderFromCDP extends CRLFinder {

    private static Logger logger = LoggerFactory.getLogger(CRLFinderFromCDP.class);
    private static FixedSizedCache<String, ECRL> CRL_CACHE = new FixedSizedCache<>(16, Duration.ofMinutes(5));

    /**
     * Sertifikadaki CDP adresindeki sili bulur.
     */
    protected List<ECRL> _findCRL(ECertificate aCertificate)
    {
        List<ECRL> crlList = new ArrayList<ECRL>();
        List<String> addresses = _getAddresses(aCertificate);
        if (addresses.isEmpty()) 
        {
            logger.debug("Sertifika CDP uzantısından adres alınamadı");
            return crlList;
        }
        logger.debug("Sertifika CDP uzantısından adresler alındı.");
        boolean useCache = mParameters.getParameterBoolean(CACHE, true);
        for (String address : addresses) 
        {
            try 
            {
                Chronometer c = new Chronometer("Find CRL");
                c.start();

                ECRL crl = null;
                if(useCache) {
                    crl = CRL_CACHE.getItem(address, () -> _getCRL(address));
                } else {
                    crl = _getCRL(address);
                }

                logger.info(c.stopSingleRun());

                if (crl == null) 
                {
                    logger.warn("CRL can not be read from " + address);
                }
                else
                    crlList.add(crl);

                if (mParentSystem!=null)
                    mParentSystem.getSaveSystem().registerCRL(aCertificate, crl);
                    //Find.saveCRL(mParentSystem.getSaveSystem(),crl);
            }
            catch (Exception ex)
            {
                logger.error("Can not get CRL from " + address, ex);
            }
        }

        return crlList;
    }

    abstract List<String> _getAddresses(ECertificate aCertificate);

    abstract ECRL  _getCRL(String aAddress);

    abstract EName _getCRLIssuer(ECertificate aCertificate);

}
