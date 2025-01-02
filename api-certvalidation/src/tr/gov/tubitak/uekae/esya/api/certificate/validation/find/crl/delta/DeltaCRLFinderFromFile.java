package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds delta-CRL from a specific file location 
 */
public class DeltaCRLFinderFromFile extends DeltaCRLFinder {

    private static final Logger logger = LoggerFactory.getLogger(DeltaCRLFinderFromFile.class);

    @Override
    protected List<ECRL> _findDeltaCRL(ECertificate aCertificate) {
        return _findDeltaCRL((ECRL)null);
    }

    /**
     * Dosyadan delta-SİL okur
     */
    protected List<ECRL> _findDeltaCRL(ECRL aBaseCRL)
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
