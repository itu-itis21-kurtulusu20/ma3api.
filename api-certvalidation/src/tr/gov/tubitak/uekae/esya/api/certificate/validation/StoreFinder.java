package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.common.util.PathUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;

public class StoreFinder 
{
	public static final String PARAM_STOREPATH = "storepath";
	private static Logger logger = LoggerFactory.getLogger(StoreFinder.class);
	
	public static CertStore createCertStore(ParameterList aParams, String aDefaultPath) throws CertStoreException
	{
		String path = null;
        CertStore certStore = null;
        try 
        {
        	if(aParams != null)
        	{
        		String storePath = aParams.getParameterAsString(PARAM_STOREPATH);
	            if (storePath!=null && storePath.length()>0)
	            {
	            	path = storePath.trim();
	            	path = PathUtil.getRawPath(path);
	            }
        	}
        	
        	if(path == null && aDefaultPath != "")
        		path = aDefaultPath;
        	
        	if(path == null)
        		certStore = new CertStore();
        	else
        		certStore = new CertStore(path, null, null);
        	
        }
        catch (CertStoreException aEx)
        {
            logger.error("Sertifika deposuna ulaşılamadı", aEx);
            throw aEx;
        }
        return certStore;
	}
}
