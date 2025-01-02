package tr.gov.tubitak.uekae.esya.api.asn1viewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by orcun.ertugrul on 06-Oct-17.
 */
public class AppSettings
{
    protected static Logger logger = LoggerFactory.getLogger(AppSettings.class);

    static Properties mProp;
    static String configFilePath;
    static String configFilePathTDrivePath= "T:\\api-parent\\resources\\asn1viewer\\asn1viewer.config.properties";
    static String configFilePathLocalPath= "asn1viewer.config.properties";

    static
    {
        try
        {
            mProp = new Properties();
            InputStream input;
            if(new java.io.File(configFilePathLocalPath).exists())
                configFilePath = configFilePathLocalPath;
            else
                configFilePath = configFilePathTDrivePath;

            input = new FileInputStream(configFilePath);
            mProp.load(input);
        }
        catch (Exception ex)
        {
            logger.warn("Warning in AppSettings", ex);
        }
    }


    public AppSettings()
    {
    }

    public static String getClassForOid(int [] oid)
    {
        String prefix = "ObjectIndetifier.";
        String suffix = OIDUtil.concat(oid);

        return mProp.getProperty(prefix + suffix);
    }

    public static String getOidFieldForType(String clazz, String field)
    {
        return mProp.getProperty(clazz + "." + field);
    }

    public static void setLatestFilePath(String filePath){
        mProp.setProperty("latestFilePath", filePath);
        save();
    }

    public static String getLatestFilePath(){
        return mProp.getProperty("latestFilePath");
    }

    public static void setLatestAsn1Type(String asn1Type){
        mProp.setProperty("latestAsn1Type", asn1Type);
        save();
    }

    public static String getLatestAsn1Type(){
        return mProp.getProperty("latestAsn1Type");
    }

    private static void save() {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(configFilePath);
            mProp.store(fileOutputStream, null);
            fileOutputStream.close();
        }catch (Exception ex){
            logger.error("Error in AppSettings", ex);
        }
    }

}
