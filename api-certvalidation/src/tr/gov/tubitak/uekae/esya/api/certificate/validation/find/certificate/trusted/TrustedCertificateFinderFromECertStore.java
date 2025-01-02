package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds trusted certificates from local Certificate Store.
 *
 * @author isilh
 */
public class TrustedCertificateFinderFromECertStore extends TrustedCertificateFinder {

    private static final Logger logger = LoggerFactory.getLogger(TrustedCertificateFinderFromECertStore.class);

    protected List<ECertificate> _findTrustedCertificate()
    {
        List<ECertificate> certificates = new ArrayList<ECertificate>();

        List<DepoKokSertifika> ds;
        // herhangibir kısıtlama yok, tüm kök sertifikaları getir
        try 
        {
        	String defaultStorePath = null;
        	if(mParentSystem != null)
        		defaultStorePath = mParentSystem.getDefaultStorePath();
        	CertStore certStore = StoreFinder.createCertStore(mParameters, defaultStorePath);
            CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);
        	List<GuvenlikSeviyesi> guvenlikSeviyeleri = getGuvenlikSeviyesi();

            String trustAnchorHash = mParameters.getParameterAsString(PARAM_TA);
            ds = certStoreRootCertOps.listStoreRootCertificates(null, null,
                                                                guvenlikSeviyeleri.toArray(new GuvenlikSeviyesi[guvenlikSeviyeleri.size()]), trustAnchorHash);
            try
        	{
				certStore.closeConnection();
			} 
        	catch (CertStoreException e) 
        	{
        		logger.error("Connection couldn't closed", e);
			}
        }
        catch (CertStoreException aEx) 
        {
            logger.error("Sertifikalar listelenirken hata oluştu", aEx);
            return certificates;
        }
        for (DepoKokSertifika sertifika : ds) 
        {
            try 
            {
                ECertificate certificate = new ECertificate(sertifika.getValue());
                certificates.add(certificate);
            }
            catch (Exception aEx) 
            {
                logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
            }
        }

        return certificates;
    }
}
