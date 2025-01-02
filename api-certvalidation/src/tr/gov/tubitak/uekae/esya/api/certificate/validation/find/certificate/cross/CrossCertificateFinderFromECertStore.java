package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Find cross certificate in ESYA Certificate Store
 */
public class CrossCertificateFinderFromECertStore extends CrossCertificateFinder
{
    private static final Logger logger = LoggerFactory.getLogger(CrossCertificateFinderFromECertStore.class);

    /**
     * Depoda çapraz sertifika bulur
     */
    protected List<ECertificate> _findCrossCertificate()
    {
        List<ECertificate> certs = new ArrayList<ECertificate>();

        try {
            CertStore certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
            CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);

            String trustAnchorHash = mParameters.getParameterAsString(PARAM_TA);
            List<DepoKokSertifika> certStoreRootCerts = certStoreRootCertOps.listStoreRootCertificates(new CertificateSearchTemplate(), null, _guvenlikSeviyeleriniAl(), trustAnchorHash);
            for(DepoKokSertifika sertifika : certStoreRootCerts)
            {
                try
                {
                    ECertificate certificate = new ECertificate(sertifika.getValue());
                    certs.add(certificate);
                }
                catch (Exception aEx)
                {
                    logger.error("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                }
            }
        }
        catch (Exception aEx) {
            logger.error("Error in CrossCertificateFinderFromECertStore" + aEx.getMessage(), aEx);
        }

        return certs;

    }

    protected GuvenlikSeviyesi[] _guvenlikSeviyeleriniAl()
    {
        //tüm güvenlik seviyesindekileri getir
        return null;
    }

}
