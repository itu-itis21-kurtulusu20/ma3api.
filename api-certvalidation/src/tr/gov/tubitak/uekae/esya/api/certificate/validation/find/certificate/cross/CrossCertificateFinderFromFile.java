package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds cross certificate from a specific file location.
 */
public class CrossCertificateFinderFromFile extends CrossCertificateFinder {

    private static final Logger logger = LoggerFactory.getLogger(CrossCertificateFinderFromFile.class);

    /**
     * Dosyadan Çapraz sertifika okur
     */
    protected List<ECertificate> _findCrossCertificate() {
        String dosyaAdresi;
        List<ECertificate> caprazlar = new ArrayList<ECertificate>();
        if ((mParameters != null) && ((dosyaAdresi = mParameters.getParameterAsString(DOSYA_YOLU)) != null)) {
            try {
            	dosyaAdresi = PathUtil.getRawPath(dosyaAdresi);
                ECertificate capraz = ECertificate.readFromFile(dosyaAdresi);
                caprazlar.add(capraz);
            }
            catch (Exception x) {
                logger.error("Dosyadan (" + dosyaAdresi + ") sertifika bilgisi alınamadı", x);
            }

        }
        
        String dizinAdresi;
        if ((mParameters != null) && ((dizinAdresi = mParameters.getParameterAsString(DIZIN)) != null)) 
		{
        	dizinAdresi = PathUtil.getRawPath(dizinAdresi);
			File folder = new File(dizinAdresi);
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
							caprazlar.add(new ECertificate(cert));
						}
						catch(Exception e)
						{
							logger.error("Dosyadan sertifika bilgisi alınamadı", e);
						}
					}
				}
			}
		}
        return caprazlar;
    }
}