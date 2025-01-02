package tr.gov.tubitak.uekae.esya.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy ayarları
 * @author isilh
 *
 */

public class ProxyUtil
{
	/**
	 * Sets proxy settings for api. 
	 * @param aProxyHost  host
	 * @param aProxyPort  port
	 * @param aDomain     domain
	 * @param aUserName   client user
	 * @param aPassword   client user password
	 */

	private static Logger logger = LoggerFactory.getLogger(ProxyUtil.class);

	public static void setProxyConfig(String aProxyHost, int aProxyPort, String aDomain, String aUserName, String aPassword)
	{
    	System.setProperty("http.proxyHost", aProxyHost);
    	System.setProperty("http.proxyPort", aProxyPort + "");

    	if(aDomain != null)
    	{
			System.setProperty("http.auth.ntlm.domain", aDomain);
		}
    	if(aUserName != null)
    	{
    		System.setProperty("http.proxyUser", aUserName);
    	}
        if(aPassword != null)
        {
        	System.setProperty("http.proxyPassword", aPassword);
        }
	}
}
