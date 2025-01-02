package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.infra.directory.DirectoryBase;
import tr.gov.tubitak.uekae.esya.api.infra.directory.SearchDirectory;
import tr.gov.tubitak.uekae.esya.api.infra.directory.StaticDirectoryInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

/**
 * Utility class for LDAP and HTTP access operations
 *
 * @author IH
 */
public class EBaglantiUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EBaglantiUtil.class);

	private static final String SM_SERTIFIKASI = "cacertificate";
    private static final String CRL = "certificaterevocationlist";

    private static String[] msAttributes = null;

    /**
     * Reads data from the given HTTP address.
     * @param aURLAddress address
     * @return read data
     */
    public static byte[] urldenVeriOku(String aURLAddress)
    {
    	return urldenVeriOku(aURLAddress, null);
    }
    
    /**
     * 
     * @param aURLAddress
     * @param timeOut timeout in miliseconds. If it is null, default timeout value is used. And a timeout of zero is
     * intreped as an infinite timeout. 
     * @return
     */
    public static byte[] urldenVeriOku(String aURLAddress, String timeOut)
    {
    	ByteArrayOutputStream ba = new ByteArrayOutputStream();
    	URL url;
		try
		{
			url = new URL(aURLAddress);
		} catch (MalformedURLException e)
		{
			LOGGER.error(aURLAddress + " adresinden URL objesi yaratilamadi",e);
			return null;
		}
		try
		{
			URLConnection conn = url.openConnection();
			if(timeOut != null)
			{
				LOGGER.debug("Time out is set to " + timeOut);
				conn.setConnectTimeout(Integer.parseInt(timeOut));
				conn.setReadTimeout(Integer.parseInt(timeOut));
			}
			InputStream is = conn.getInputStream();
			byte [] block = new byte[4096];
			while(true)
			{
				int lenght = is.read(block);
				if(lenght == -1)
					break;
				ba.write(block, 0, lenght);
			}
	       
		} catch (IOException aEx)
		{
			LOGGER.warn(aURLAddress +  " adresinden veri alınamadı", aEx);
			return null;
		}
        return ba.toByteArray();
    }

    public static InputStream urldenStreamOku(String aURLAdresi)
    {
    	try
        {
        	URL url = new URL(aURLAdresi);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
			return input;
        }
        catch (Exception aEx)
        {
            LOGGER.warn(aURLAdresi +  " adresinden veri alınamadı", aEx);
            return null;
        }
    }

    public static byte[] dizindenVeriOku(String aDizinAdresi)
    {
    	return dizindenVeriOku(aDizinAdresi, null);
    }

    /**
     * Reads data from the given LDAP address.
     *
     * @param aDizinAdresi LDAP address
     * @param aDizinTipi  LDAP Type defined inf DirectoryBase
     * @return read data
     * @see tr.gov.tubitak.uekae.esya.api.infra.directory.DirectoryBase
     */
    public static byte[] dizindenVeriOku(String aDizinAdresi, String aDizinTipi)
    {
    	Object[][] smcrl = _getLDAP(aDizinAdresi, aDizinTipi);
    	if (smcrl == null || smcrl[0][0] == null)
        {
            LOGGER.error("Dizinden veri okunamadı");
            return null;
        }
    	byte[] crlArr = new byte[smcrl.length];
        crlArr = (byte[])smcrl[0][0];
        return crlArr;
    }

    private static Object[][] _getLDAP(String aDizinAdresi, String aDizinTipi)
    {
    	aDizinAdresi = aDizinAdresi.replaceAll("%20", " "); //bosluklari duzelt
        StringTokenizer token = new StringTokenizer(aDizinAdresi, "/");
        String host = "";
        //default port
        int port = 389;
        String remain = "";

        if (token.countTokens() >= 2)
        { //host tanimli
            token.nextToken();//ldap
            host = token.nextToken();//adres
            StringTokenizer portToken = new StringTokenizer(host, ":");
            if (portToken.countTokens() == 2)
            { //port tanimli
                host = portToken.nextToken();//host
                port = Integer.parseInt(portToken.nextToken());//port
            }
        } else
        {
            LOGGER.error("LDAP adresinde host tanımlı değil: " + aDizinAdresi);
            return null;
        }
        if(token.hasMoreTokens())
        {
            remain = token.nextToken();
        }
        else
        {
            LOGGER.error("LDAP uzantısından TKA alınamadı: " + aDizinAdresi);
            return null;
        }
        String[] others = _getElements(remain, "?");
        if (others.length == 0)
        {
            LOGGER.error("LDAP uzantısında TKA bulunamadı: " + aDizinAdresi);
            return null;
        }
        String tka = others[0]; //tka
        LOGGER.debug("LDAP uzantısından TKA alındı: " + tka);
        String attr = null;
        String[] attrs = null;
        if (others.length == 1) //? yok
        {
            attrs = msAttributes;
        } else
        {
            attr = others[1]; //attributes
            attrs = _getAttributes(attr, ",");
        }
        //String scope = others[2]; //scope
        //String filter = others[3]; //filter
       if(aDizinTipi == null)
       {
    	   aDizinTipi = DirectoryBase.ACTIVE_DIRECTORY;
       }
        SearchDirectory da = new SearchDirectory(new StaticDirectoryInfo(host,
                port,
                aDizinTipi,
                "",
                ""), "");
        if (!da.isConnected())
        {
            LOGGER.error("LDAP adresine bağlanamadı:" + host + ":" + port);
            return null;
        }
        Object[][] userc = da.getAttributes(tka, attrs);
        return userc;
    }

	/**
     * String yapının içinden elemanları çıkarır.
     * @param aParcalanacak String
     * @param aDelim String
     * @return String[]
     */
    private static String[] _getElements(String aParcalanacak, String aDelim)
    {
        StringTokenizer token = new StringTokenizer(aParcalanacak, aDelim);
        String[] parcalar = new String[token.countTokens()];
        int i = 0;
        while (token.hasMoreTokens())
        {
            parcalar[i++] = token.nextToken();
        }
        return parcalar;

    }

    /**
     * String yapının içinden attributeları çıkarır.
     * @param aParcalanacak String
     * @param aDelim String
     * @return String[]
     */
    private static String[] _getAttributes(String aParcalanacak, String aDelim)
    {
        StringTokenizer token = new StringTokenizer(aParcalanacak, aDelim);
        String[] parcalar = new String[token.countTokens()];
        int i = 0;
        String temp = "";
        while (token.hasMoreTokens())
        {
            temp = token.nextToken();
            if (temp.equalsIgnoreCase(SM_SERTIFIKASI))
            {
                parcalar[i++] = DirectoryBase.ATTR_SMSERTIFIKASI;
            } else if (temp.equalsIgnoreCase(CRL))
            {
                parcalar[i++] = DirectoryBase.ATTR_CRL;
            } else
            {
                parcalar[i++] = temp;
            }
        }
        return parcalar;
    }


}
