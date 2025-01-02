using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    class StoreFinder
    {
        public static readonly String PARAM_STOREPATH = "storepath";
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
	
	    public static CertStore createCertStore(ParameterList aParams, String aDefaultPath)
	    {
		    String path = null;
            CertStore certStore = null;
            try 
            {
        	    if(aParams != null)
        	    {
        		    String storePath = aParams.getParameterAsString(PARAM_STOREPATH);
	                if (!string.IsNullOrEmpty(storePath))
	                {
	            	    path = storePath.Trim();
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
                logger.Error("Sertifika deposuna ulaşılamadı", aEx);
                throw;
            }
            return certStore;
	    }
    }
}
