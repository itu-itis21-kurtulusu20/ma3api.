package tr.gov.tubitak.uekae.esya.api.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


//Tekrar derleme ihtiyacı olmadan ayarlar değişebilsin diye dosyadan okundu.
public class MA3APIEnvironment
{
    protected static Logger logger = LoggerFactory.getLogger(LDAPDNUtil.class);

    public static boolean SET_MAC_OS_PCSC_PATH = true;

    static
    {
        initialize();
    }

    private static void initialize()
    {
        try
        {
            File propertiesFile = new File("MA3APIEnvironment.properties");

            if(!propertiesFile.exists())
            {
                logger.warn("MA3APIEnvironment.properties file could not found.");
                return;
            }

            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("MA3APIEnvironment.properties");
            properties.load(fis);
            fis.close();

            SET_MAC_OS_PCSC_PATH = Boolean.parseBoolean(properties.getProperty("SET_MAC_OS_PCSC_PATH", Boolean.toString(SET_MAC_OS_PCSC_PATH)));
        }
        catch (FileNotFoundException e)
        {
            logger.warn("MA3APIEnvironment.properties file could not found.", e);
        }
        catch (IOException e)
        {
            logger.error("MA3APIEnvironment.properties file could not load.", e);
        }
    }

}
