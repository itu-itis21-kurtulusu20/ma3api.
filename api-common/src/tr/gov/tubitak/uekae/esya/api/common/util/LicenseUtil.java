package tr.gov.tubitak.uekae.esya.api.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * sets configurations about license
 * @author orcun.ertugrul
 *
 */
public class LicenseUtil
{
	private static Logger logger = LoggerFactory.getLogger(LicenseUtil.class);
	private static byte [] licenseBytes;
	private static String mPassword;

	/**
	 * sets license.
	 * @param aIs
	 * @throws ESYAException
	 */
	public static void setLicenseXml(InputStream aIs) throws ESYAException
	{
		try
		{
			try
			{
				licenseBytes = readInputStream(aIs);
			}
			catch(IOException IOex)
			{
				throw new ESYAException("License file can not be read", IOex.getMessage());
			}

			Class<?> aClass = Class.forName("tr.gov.tubitak.uekae.esya.api.common.lcns.LV");
			aClass.getMethod("reset").invoke(null);
			aClass.getMethod("getInstance").invoke(null);
		}
		catch(Exception ex)
		{
			logger.error("Error in setting license", ex.getCause().getMessage());
			throw new ESYAException("Error in setting license: " + ex.getCause().getMessage());
		}
	}

	/**
	 * Gets the expiration date of the current licence.
	 *
	 * @return the expiration date of the current licence.
	 */
	public static Date getExpirationDate() throws ESYAException {

		try {
			Class<?> classLV = Class.forName("tr.gov.tubitak.uekae.esya.api.common.lcns.LV");
			Object objectLV = classLV.getMethod("getInstance").invoke(null);
			Method methodGetExpirationDate = classLV.getMethod("getLicenseExpirationDate", int.class);

			// kullanılan sınıfın gizlenmesi için 40 değeri gömülü olarak verildi.
			Date date = (Date) methodGetExpirationDate.invoke(objectLV, 40);

			return date;
		} catch (InvocationTargetException ex) {
			Throwable cause = ex.getCause();
			String message = (cause != null && cause.getMessage() != null) ? cause.getMessage() : ex.getMessage();

			throw new ESYAException("Problem in getting expiration date. " + message, ex);
		} catch (Exception ex) {
			throw new ESYAException("Problem in getting expiration date. " + ex.getMessage(), ex);
		}
	}

	/**
	 * Gets the start date of the current licence.
	 *
	 * @return the start date of the current licence.
	 */
	public static Date getStartDate() throws ESYAException {

		try {
			Class<?> classLV = Class.forName("tr.gov.tubitak.uekae.esya.api.common.lcns.LV");
			Object objectLV = classLV.getMethod("getInstance").invoke(null);
			Method methodGetStartDate = classLV.getMethod("getLicenseStartDate", int.class);

			// kullanılan sınıfın gizlenmesi için 40 değeri gömülü olarak verildi.
			Date date = (Date) methodGetStartDate.invoke(objectLV, 40);

			return date;
		}catch (Exception e) {
			throw new ESYAException("Problem in getting expiration date" + e.getMessage());
		}
	}

	public static void setLicenseXml(InputStream aIs, String aPassword) throws ESYAException
	{
		setLicensePassword(aPassword);
		setLicenseXml(aIs);
	}

	public static void setLicensePassword(String aPassword)
	{
		mPassword = aPassword;
	}

	public static byte [] getLicense()
	{
		return licenseBytes;
	}

	public static String getPassword()
	{
		return mPassword;
	}

	private static byte [] readInputStream(InputStream is) throws IOException
	{
		ByteArrayOutputStream ba = null;
		ba = new ByteArrayOutputStream();
		byte [] block = new byte[4096];
		while(true)
		{
			int lenght = is.read(block);
			if(lenght == -1)
				break;
			ba.write(block, 0, lenght);
		}

		return ba.toByteArray();
	}

}
