package tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinder;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.TSL;

import java.util.List;

public class CertificateFinderFromTSL extends CertificateFinder {

	private static final Logger logger = LoggerFactory.getLogger(CertificateFinderFromTSL.class);
	private static final String DEFAULT_DEPO_DOSYA_ADI = "tsl.xml";
	private static String filePath = System.getProperty("user.home") +
			System.getProperty("file.separator") +
			CertStoreUtil.DEPO_DIZIN_ADI +
			System.getProperty("file.separator")+
			DEFAULT_DEPO_DOSYA_ADI;
	
	protected List<ECertificate> _findCertificate() 
	{
        return _findCertificate(null);
    }

	@Override
	protected List<ECertificate> _findCertificate(ECertificate aCertificate) 
	{
		return getAllCertificates();
	}
	
	protected List<ECertificate> getAllCertificates() {

			String storePath = mParameters.getParameterAsString(PARAM_STOREPATH);
			if(storePath == null)
			{
				logger.debug("TSL storepath is not defined. "+filePath+" is used to find TSL file as default path");
				storePath=filePath;
			}
			
			TSL tsl=null;
			try{
			tsl = TSL.parse(storePath);
			}
	        catch (Exception e)
	        {
	        	logger.error("An error occurred while reading TSL file", e);
	        }
			boolean validationRslt=false;
			try{
			validationRslt = tsl.validateTSL();
			}
	        catch (Exception e)
	        {
	        	logger.error("An error occurred while validating TSL file", e);
	        }
			if(!validationRslt){
				logger.error("TSL file cannot be validated");
				return null;
			}
		return tsl.getValidCertificates();
	}
	
}

