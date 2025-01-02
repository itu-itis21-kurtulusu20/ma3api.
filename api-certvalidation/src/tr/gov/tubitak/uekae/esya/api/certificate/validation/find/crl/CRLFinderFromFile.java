package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate from local file system 
 */
public class CRLFinderFromFile extends CRLFinder {

    private static final Logger logger = LoggerFactory.getLogger(CRLFinderFromFile.class);

    /**
     * Dosyadan SİL okur
     */
    protected List<ECRL> _findCRL(ECertificate aCertificate)
    {
    	List<ECRL> crlList = new ArrayList<ECRL>();

    	String dosyaYolu;
        if ((mParameters != null) && ((dosyaYolu = mParameters.getParameterAsString(DOSYA_YOLU)) != null)) 
		{
        	 try 
        	 {
        		 dosyaYolu = PathUtil.getRawPath(dosyaYolu);
                 ECRL crl = new ECRL(new File(dosyaYolu));
                 crlList.add(crl);
             }
             catch (Exception x) 
             {
                 logger.error("Dosyadan sertifika bilgisi alınamadı", x);
             }
		}
        
        String dizinYolu;
        if ((mParameters != null) && ((dizinYolu = mParameters.getParameterAsString(DIZIN)) != null)) 
		{
        	dizinYolu = PathUtil.getRawPath(dizinYolu);
			File folder = new File(dizinYolu);
			String [] fileNames = folder.list();
			if(fileNames != null)
			{
				for (String fileName : fileNames) 
				{
					File file = new File(folder + File.separator + fileName);
					if(file.isFile())
					{
						try
						{
							crlList.add(new ECRL(file));
						}
						catch(Exception e)
						{
							logger.error("Dosyadan sertifika bilgisi alınamadı", e);
						}
					}
				}
			}
		}
       
        return crlList;
    }

}
