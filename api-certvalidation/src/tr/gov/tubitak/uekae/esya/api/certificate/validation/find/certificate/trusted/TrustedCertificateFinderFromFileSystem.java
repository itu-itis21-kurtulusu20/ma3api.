package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds trusted certificates from the local file system. 
 */
public class TrustedCertificateFinderFromFileSystem extends TrustedCertificateFinder {
    
    private static final Logger logger = LoggerFactory.getLogger(TrustedCertificateFinderFromFileSystem.class);

    //private static final String DIZIN = "dizin";

    /**
     * Dosyadan Güvenilir Sertifika okur
     */
    protected List<ECertificate> _findTrustedCertificate()
    {
    	List<ECertificate> certList = new ArrayList<ECertificate>();
		
		String dosyaYolu;
		if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null)) 
		{
			try {
				dosyaYolu = PathUtil.getRawPath(dosyaYolu);
				certList.add(ECertificate.readFromFile(dosyaYolu));
			} catch (Exception x) {
				logger.error("Dosyadan (" + dosyaYolu + ")sertifika bilgisi alınamadı", x);
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

}
