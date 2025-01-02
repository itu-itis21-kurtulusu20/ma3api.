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
import java.util.Calendar;
import java.util.List;

/**
 * Finds trusted certificates from local Certificate Store.
 *
 * @author isilh
 */
public class TrustedCertificateFinderFromECertStoreV2 extends TrustedCertificateFinder {

    private static final Logger logger = LoggerFactory.getLogger(TrustedCertificateFinderFromECertStoreV2.class);
   
    private static Calendar readTime = Calendar.getInstance(); 
    List<ECertificate> trustedCertificates = null;
    
    protected List<ECertificate> _findTrustedCertificate()
    {
    	Calendar now = Calendar.getInstance();
    	
    	Calendar oneDayLaterAfterReading = (Calendar) readTime.clone();
    	oneDayLaterAfterReading.add(Calendar.DATE, 1);
    	
    	if(now.after(oneDayLaterAfterReading))
    	{
    		trustedCertificates = null;
    	}
    	
    	if(trustedCertificates == null)
    	{
    		trustedCertificates = new ArrayList<ECertificate>();
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
	        }
	        catch (CertStoreException aEx) 
	        {
	            logger.error("Sertifikalar listelenirken hata oluştu", aEx);
	            return trustedCertificates;
	        }
	        
	        for (DepoKokSertifika sertifika : ds) 
	        {
	            try 
	            {
	                ECertificate certificate = new ECertificate(sertifika.getValue());
	                trustedCertificates.add(certificate);
	            }
	            catch (Exception aEx) 
	            {
	                logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
	            }
	        }
	        readTime = Calendar.getInstance();
    	}

        return trustedCertificates;
    }
}
