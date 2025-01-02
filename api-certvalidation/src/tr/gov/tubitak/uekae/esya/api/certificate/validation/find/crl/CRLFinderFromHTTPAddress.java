package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate from the given HTTP address 
 */
public class CRLFinderFromHTTPAddress extends CRLFinder
{
	private static Logger logger = LoggerFactory.getLogger(CRLFinderFromHTTPAddress.class);
    private static final String HTTP_ADRESI = "httpadresi";

    /**
     * Find CRL at http address for given certificate
     */
    protected List<ECRL> _findCRL(ECertificate aCertificate) throws ESYAException
    {
    	String address = mParameters.getParameterAsString(HTTP_ADRESI);
    	try
    	{
    		logger.debug("Getting CRL from address: " + address);
	    	URL url = new URL(address);
	    	URLConnection conn = url.openConnection();
	    	setTimeOut(conn);
			InputStream crlStream = conn.getInputStream();
			ECRL crl = new ECRL(crlStream);
			
			List<ECRL> crlList = new ArrayList<ECRL>(1);
			crlList.add(crl);
			return crlList;
    	}
		catch (MalformedURLException e)
		{
			throw new ESYAException("CRL can not read, url is not valid" + address, e);
		}
		catch (IOException e)
		{
			throw new ESYAException("CRL can not read", e);
		}
        
    }

}
