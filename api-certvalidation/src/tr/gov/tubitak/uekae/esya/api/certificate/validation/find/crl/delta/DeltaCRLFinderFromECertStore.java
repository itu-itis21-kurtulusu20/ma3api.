package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SILTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSIL;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CRLSearchTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds delta-CRL from the local Certificate Store
 *
 * @author isilh
 */
public class DeltaCRLFinderFromECertStore extends DeltaCRLFinder {

    private static final Logger logger = LoggerFactory.getLogger(DeltaCRLFinderFromECertStore.class);

    protected List<ECRL> _findDeltaCRL(ECRL aBaseCRL) {
        return _findDeltaCRL();
    }

    protected List<ECRL> _findDeltaCRL(ECertificate aCertificate) {
        return _findDeltaCRL();
    }

    private List<ECRL> _findDeltaCRL() {

        List<ECRL> crls = new ArrayList<ECRL>();

        ItemSource<DepoSIL> silItemSource = null;
        DepoSIL depoSIL;
        CertStore certStore = null;
        try 
        {
        	certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
            CertStoreCRLOps si = new CertStoreCRLOps(certStore);
            silItemSource = si.listStoreCRL(new CRLSearchTemplate(), new SILTipi[]{SILTipi.DELTA});
            depoSIL = silItemSource.nextItem();

            while (depoSIL != null) {
                try {
                    ECRL crl = new ECRL(depoSIL.getValue());
                    crls.add(crl);
                } catch (ESYAException e) {
                    logger.warn("Depodan alinan sil olusturulurken hata olustu", e);
                }
                depoSIL = silItemSource.nextItem();
            }

        } catch (CertStoreException aEx) {
            logger.error("Depodan siller alınırken hata oluştu", aEx);
            return null;
        } catch (ESYAException aEx) {
            logger.error("İlk depo sil nesnesi alınırken hata oluştu", aEx);
            return null;
        } finally {
            try {
                if (silItemSource != null) silItemSource.close();
                if (certStore != null) certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.error("Connection couldn't closed", e);
            }
        }

        return crls;
    }
}
