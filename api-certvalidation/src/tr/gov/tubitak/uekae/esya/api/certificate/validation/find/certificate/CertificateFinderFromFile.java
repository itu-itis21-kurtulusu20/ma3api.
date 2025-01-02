package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds issuer certificate of a given certificate from local file.
 */
public class CertificateFinderFromFile extends CertificateFinder {

	private static final Logger logger = LoggerFactory.getLogger(CertificateFinderFromFile.class);

	/**
	 * Find issuer certificate from file
	 */
	protected List<ECertificate> _findCertificate() {
		return _findCertificate(null);
	}

	/**
	 * Find issuer certificate from file
	 */
	protected List<ECertificate> _findCertificate(ECertificate aSertifika) {
		List<ECertificate> certList = new ArrayList<ECertificate>();
		
		String dosyaYolu;
		if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null)) 
		{
			try {
				dosyaYolu = PathUtil.getRawPath(dosyaYolu);
				certList.add(ECertificate.readFromFile(dosyaYolu));
			} catch (Exception x) {
				logger.error("Dosyadan sertifika bilgisi alınamadı", x);
			}
		}

		String dizin;
		if ((mParameters != null) && ((dizin = mParameters.getParameterAsString(DIZIN)) != null)) 
		{
			dizin = PathUtil.getRawPath(dizin);
			File folder = new File(dizin);
			String [] certFileNames = folder.list();
			if(certFileNames != null)
			{
				for (String certFileName : certFileNames) 
				{
					File cert = new File(folder + File.separator + certFileName);
					if(cert.isFile())
					{
						try
						{
							certList.add(new ECertificate(cert));
						}
						catch(Exception e)
						{
							logger.error("Dosyadan sertifika bilgisi alınamadı", e);
						}
					}
				}
			}
		}

		return certList;
	}

    /*
	@Override
	public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
	{
		List<ECertificate> certs = findCertificate();

		if (certs!=null && certs.size()>0){
			// finds only one cert from file anyway
			ECertificate cert = certs.get(0);

			if (mMatcher.match(aCriteria, cert))
				return certs;
		}
		return null;
	}     */


}
