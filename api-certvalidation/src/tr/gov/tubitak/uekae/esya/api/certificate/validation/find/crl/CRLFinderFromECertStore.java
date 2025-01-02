package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Find CRL from ESYA Certificate Store
 * @author IH
 */

public class CRLFinderFromECertStore extends CRLFinder {

	private static final String GETACTIVECRL = "getactivecrl";
	private static final Logger logger = LoggerFactory.getLogger(CRLFinderFromECertStore.class);

	protected List<ECRL> _findCRL(ECertificate aCertificate) {
		List<ECRL> crls = new ArrayList<ECRL>();

		CRLSearchTemplate crlSearchParams = new CRLSearchTemplate();
		crlSearchParams.setIssuer(aCertificate.getCRLIssuer()); // Butun CRL leri almamak için bir optimizasyon

		if(mParameters != null)
		{
			boolean getActiveCrl = mParameters.getParameterBoolean(GETACTIVECRL);
			if(getActiveCrl == true)
			{
				crlSearchParams.setValidAt(new Date());
			}
			else
			{
                if (mParentSystem.isDoNotUsePastRevocationInfo())
                {
                    Calendar baseTime = mParentSystem.getBaseValidationTime();
                    crlSearchParams.setPublishedAfter(baseTime.getTime());
                }

                Calendar lastRevocTime = (mParentSystem.getLastRevocationTime() == null) ? aCertificate.getNotAfter() : mParentSystem.getLastRevocationTime();
                crlSearchParams.setPublishedBefore(lastRevocTime.getTime());
			}
		}

		ItemSource<DepoSIL> silItemSource = null;
		CertStore certStore = null;
		try 
		{
			certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
			CertStoreCRLOps certStoreCrlOps = new CertStoreCRLOps(certStore);
			silItemSource = certStoreCrlOps.listStoreCRL(crlSearchParams, new SILTipi[]{SILTipi.BASE});
			DepoSIL depoSIL = silItemSource.nextItem();

			while (depoSIL != null) {
				try {
					ECRL crl = new ECRL(depoSIL.getValue());
					crls.add(crl);
				} catch (Exception e) {
					logger.warn("Depodan alinan sil olusturulurken hata olustu", e);
				}
				depoSIL = silItemSource.nextItem();
			}

		} catch (CertStoreException aEx) {
			logger.error("Sil'ler listelenirken hata olustu", aEx);
			return null;
		} catch (ESYAException aEx) {
			logger.error("Ilk depo sil nesnesi alinirken hata olustu", aEx);
			return null;
		} finally {
			try {
				if (silItemSource != null) silItemSource.close();
				if (certStore != null)  certStore.closeConnection();
			} catch (CertStoreException e) {
				logger.error("Connection couldn't closed", e);
			}
		}
        logger.debug("Depodan " + crls.size() + " tane sil bulundu.");
		return crls;
	}
}
