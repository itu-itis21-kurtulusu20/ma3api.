package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ConnectionUtil;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStore;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CertificateFinderFromXml  extends CertificateFinder 
{
	private static final Logger logger = LoggerFactory.getLogger(CertificateFinderFromXml.class);
	private static List<ECertificate> certificates;
	private static Calendar readTime = Calendar.getInstance();

    /**
     * Find issuer certificate from file
     */
	protected List<ECertificate> _findCertificate() 
	{
        return _findCertificate(null);
    }

	@Override
	protected List<ECertificate> _findCertificate(ECertificate aCertificate) 
	{
        EName name = null;

		if (aCertificate != null) 
		{
			name = aCertificate.getIssuer();
		}
		return searchCertificates(name);
	}

	/*sadece Issuer için çalışıyor.
	@Override
	public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria) throws ESYAException 
	{
		 // search criteria
        CertificateSearchTemplate certSearchTemplate = createSearchTemplate(aCriteria);
        return searchCertificates(certSearchTemplate);
	} */
	
	protected List<ECertificate> searchCertificates(EName issuer)
	{
		Calendar now = Calendar.getInstance();

		Calendar oneDayLaterAfterReading = (Calendar) readTime.clone();
		oneDayLaterAfterReading.add(Calendar.DATE, 1);

		if(now.after(oneDayLaterAfterReading))
		{
			certificates = null;
		}

		if(certificates == null)
		{
			String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
			try 
			{
				XMLStore store = null;
				if(storePath != null)
				{
					InputStream is = ConnectionUtil.urldenStreamOku(storePath);
                    if(is != null)
                        store = new XMLStore(is);
                    else {
                        logger.debug(PARAM_STOREPATH + " : " + storePath + " adresindeki XML depo okunamadi!");
                        store = new XMLStore();
                    }
				}
				else
				{
                    Object storeStreamParam = mParameters.getParameter(PARAMA_STORE_STREAM);
                    if((storeStreamParam != null) && (storeStreamParam instanceof InputStream)){
                        store = new XMLStore((InputStream)storeStreamParam);
                    }
                    else
                    {
					    store = new XMLStore();
                    }
				}
				certificates = store.getCertificates();
			} 
			catch (ESYAException e) 
			{
				logger.error("Xml sertifikası okunurken hata oluştu",e);
			}

			readTime = Calendar.getInstance();
		}

		if(certificates == null){
			return new ArrayList<>();
		}

		List<ECertificate> properCerts = new ArrayList<ECertificate>(certificates);
		for(ECertificate cert : certificates)
		{
			byte [] subject = issuer.getEncoded();
			if(subject != null)
			{
				if(!Arrays.equals(cert.getSubject().getEncoded(),subject))
					properCerts.remove(cert);
			}
		}
		
		return properCerts;
	}

}
