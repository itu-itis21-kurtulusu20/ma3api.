package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

// todo bu sinifin .NET'te eslenigi yok
// todo en azindan isim olarak, method olarak belki farkli siniflara
// todo dagilmis bir sekilde vardir

public class ConnectionUtil 
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtil.class);
	
	
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
    		URL url = null;
			try
			{
				url = new URL(aURLAdresi);
			}
			catch(MalformedURLException ex)
			{
				aURLAdresi = "file:" + aURLAdresi;
				url = new URL(aURLAdresi);
			}
            URLConnection con = url.openConnection();
            return con.getInputStream();
        }
        catch (Exception aEx)
        {
            LOGGER.warn(aURLAdresi +  " adresinden veri alınamadı", aEx);
            return null;
        }
    }
}
