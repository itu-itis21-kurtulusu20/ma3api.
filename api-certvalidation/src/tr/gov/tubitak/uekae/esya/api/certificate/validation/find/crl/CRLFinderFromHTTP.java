package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLDistributionPoints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds CRL for a given certificate according to the CRL Distribution Points
 * (CDP) extension information in the certificate. It only searches in HTTP
 * addresses specified in the CDP extension. 
 */
public class CRLFinderFromHTTP extends CRLFinderFromCDP
{
	private static Logger logger = LoggerFactory.getLogger(CRLFinderFromHTTP.class);

    /**
     * Sertifikanın HTTP üzerindeki SİL dağıtıcı adreslerini döner
     */
    protected List<String> _getAddresses(ECertificate aCertificate)
    {
    	ECRLDistributionPoints crlDistributionPoints = aCertificate.getExtensions().getCRLDistributionPoints(); 
    	if(crlDistributionPoints == null)
    		return new ArrayList<String>();
    	
        List<String> addresses = crlDistributionPoints.getHttpAddresses();
        if ((addresses != null) && !addresses.isEmpty()) {
            return addresses;
        }
        return new ArrayList<String>();
    }

    /**
     * verilen HTTP adresinden SİL bilgisini okur
     */
    ECRL _getCRL(String aAddress)
    {
		try 
		{
			logger.debug("Getting CRL from address: " + aAddress);
			URL url = new URL(aAddress);
			URLConnection conn = url.openConnection();
			setTimeOut(conn);
			InputStream crlStream = conn.getInputStream();
			ECRL crl = new ECRL(crlStream);
			
			return crl;
		}
		catch (MalformedURLException e) 
		{
			 logger.error("The URL address is not valid: " + aAddress, e);
		} 
		catch (IOException e) 
		{
			logger.error("CRL can not be read from " + aAddress, e);
		} 
		catch (ESYAException e) 
		{
			logger.error("CRL Asn1 decode error for CRL from " + aAddress, e);
		}
		return null;
    }

	/**
     * Sertifikanın HTTP üzerindeki SİL dağıtıcısını döner
     */
    protected EName _getCRLIssuer(ECertificate aCertificate)
    {
        List<EName> issuers = aCertificate.getExtensions().getCRLDistributionPoints().getHttpIssuers();
        if ((issuers != null) && !issuers.isEmpty()) {
            return issuers.get(0);
        }
        return null;
    }

}
