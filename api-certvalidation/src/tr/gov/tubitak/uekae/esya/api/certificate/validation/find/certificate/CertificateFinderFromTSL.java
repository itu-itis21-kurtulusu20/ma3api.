package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Find issuer certificate of the input certificate from TSL document.
 *
 * @author BY
 */
public class CertificateFinderFromTSL extends CertificateFinder {

    private static final Logger logger = LoggerFactory.getLogger(CertificateFinderFromTSL.class);
	private static CertificateFinder certfinder;
	private static List<ECertificate> certificates;
	private static Calendar readTime = Calendar.getInstance(); 
	
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
		try {
			certfinder= (CertificateFinder)Class.forName("tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder.CertificateFinderFromTSL").newInstance();
		} catch (Exception e) {
			logger.error("Dynamic Binding of CertificateFinderFromTSL is not working" + e.getMessage(), e);
		}
		certificates= certfinder.findCertificate(null);
		readTime = Calendar.getInstance();
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
