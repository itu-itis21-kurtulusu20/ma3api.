package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Find issuer certificate of the input certificate from ESYA Certificate Store.
 *
 * @author IH
 */
public class CertificateFinderFromECertStore extends CertificateFinder {

    private static final Logger logger = LoggerFactory.getLogger(CertificateFinderFromECertStore.class);

    public CertificateFinderFromECertStore() {
        super();
    }

    protected List<ECertificate> _findCertificate() {
        return _findCertificate(null);
    }

    protected List<ECertificate> _findCertificate(ECertificate aCertificate) {
        List<ECertificate> certificates = new ArrayList<ECertificate>();
        
        // depodan gelecek sonuçları kısıtlayabilmek için issuer-subject eşitliğini zorunlu tuttuk
        CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();

        if (aCertificate != null) 
        {
            certSearchTemplate.setSubject(aCertificate.getIssuer());
        }

        ItemSource<DepoSertifika> sertifikaItemSource = null;
        DepoSertifika depoSertifika;
        CertStore certStore = null;
        try {
        	certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
            CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
            sertifikaItemSource = certStoreCertOps.listStoreCertificate(certSearchTemplate);
            depoSertifika = sertifikaItemSource.nextItem();
            while (depoSertifika != null) {
                ECertificate esertifika = new ECertificate(depoSertifika.getValue());
                certificates.add(esertifika);
                depoSertifika = sertifikaItemSource.nextItem();
            }
        } catch (CertStoreException aEx) {
            logger.error("Sertifikalar listelenirken hata oluştu", aEx);
            return null;
        } catch (ESYAException aEx) {
            logger.error("İlk depo sertifika nesnesi alınırken hata oluştu", aEx);
            return null;
        } finally {
            try {
                if (sertifikaItemSource != null) 
                	sertifikaItemSource.close();
                if(certStore != null)
                	certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.error("Connection couldn't closed", e);
            }
        }

        return certificates;
    }

    /*
    @Override
    public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
            throws ESYAException {
        if (aCriteria == null)
            throw new ArgErrorException("Expected search criteria");


        // search criteria
        CertificateSearchTemplate certSearchTemplate = createSearchTemplate(aCriteria);

        return locate(certSearchTemplate);
    }

    protected List<ECertificate> locate(CertificateSearchTemplate aSearchTemplate) {
        CertStore certStore;
        try {
            certStore = new CertStore();
        } catch (
                CertStoreException aEx) {
            logger.error("Sertifika deposuna ulaşılamadı", aEx);
            return null;
        }
        CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);

        ItemSource<DepoSertifika> sertifikaItemSource = null;
        DepoSertifika depoSertifika;

        List<ECertificate> certificates = new ArrayList<ECertificate>();
        try {
            sertifikaItemSource = certStoreCertOps.listStoreCertificate(aSearchTemplate);
            depoSertifika = sertifikaItemSource.nextItem();

            while (depoSertifika != null) {
                try {
                    ECertificate esertifika = new ECertificate(depoSertifika.getValue());
                    certificates.add(esertifika);
                } catch (ESYAException aEx) {
                    logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                }
                depoSertifika = sertifikaItemSource.nextItem();
            }

        } catch (CertStoreException aEx) {
            logger.error("Sertifikalar listelenirken hata oluştu", aEx);
            return null;
        } catch (ESYAException aEx) {
            logger.error("İlk depo sertifika nesnesi alınırken hata oluştu", aEx);
            return null;
        } finally {
            if (sertifikaItemSource != null) sertifikaItemSource.close();
            try {
                certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.error("Connection couldn't closed", e);
            }
        }
        return certificates;
    }    */

}
