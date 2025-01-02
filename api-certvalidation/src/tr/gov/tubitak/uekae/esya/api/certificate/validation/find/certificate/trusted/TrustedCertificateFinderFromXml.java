package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ConnectionUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.cache.FixedSizedCache;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStore;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TrustedCertificateFinderFromXml extends TrustedCertificateFinder
{
	private static final Logger logger = LoggerFactory.getLogger(TrustedCertificateFinderFromXml.class);
	private static Object syncObject = new Object();

	private static FixedSizedCache<String, List<ECertificate>> cache = new FixedSizedCache<>(20, Duration.ofHours(6));
	
	@Override
	protected List<ECertificate> _findTrustedCertificate()
	{
		String finderConfig = getParameters().toString();
		synchronized (syncObject)
		{
			List<ECertificate> trustedCertificates = cache.getItem(finderConfig);
			if(trustedCertificates == null || trustedCertificates.size() == 0) {
				trustedCertificates = new ArrayList<>();
				try {
					String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
					XMLStore store = null;
					if (storePath != null) {
						logger.debug(storePath + " adresinden XML sertifika depo okunacak.");
						InputStream is = ConnectionUtil.urldenStreamOku(storePath);
						if (is != null)
							store = new XMLStore(is);
						else {
							logger.debug(PARAM_STOREPATH + " : " + storePath + " adresindeki XML depo okunamadi!");
							store = new XMLStore();
						}
					} else {
						Object storeStreamParam = mParameters.getParameter(PARAMA_STORE_STREAM);
						if ((storeStreamParam != null) && (storeStreamParam instanceof InputStream)) {
							store = new XMLStore((InputStream) storeStreamParam);
						} else {
							String storeStreamParamName = mParameters.getParameterAsString(PARAMA_STORE_STREAM);
							logger.debug(PARAMA_STORE_STREAM + " : " + storeStreamParamName + " adresindeki XML depo okunamadi!");
							store = new XMLStore();
						}
					}

					List<DepoKokSertifika> depoKokSertifikaList = store.getTrustedCertificates();

					List<GuvenlikSeviyesi> guvenlikSeviyeleri = getGuvenlikSeviyesi();

					String trustAnchorHash = mParameters.getParameterAsString(PARAM_TA);

					for (DepoKokSertifika depoKokSertifika : depoKokSertifikaList) {
						GuvenlikSeviyesi guvenSeviyesi = depoKokSertifika.getKokGuvenSeviyesi();
						if (guvenlikSeviyeleri.contains(guvenSeviyesi)) {
							boolean sonuc = CertStoreUtil.verifyDepoKokSertifika(depoKokSertifika, trustAnchorHash);
							if (sonuc == true)
								trustedCertificates.add(new ECertificate(depoKokSertifika.getValue()));
						}
					}
					cache.put(finderConfig, trustedCertificates);
				} catch (ESYAException e) {
					logger.error("Xml sertifikası okunurken hata oluştu", e);
				} catch (CertStoreException e) {
					logger.error("Kök sertifika doğrulanırken hata oluştu", e);
				}
			}
			return trustedCertificates;
		}
	}
}
