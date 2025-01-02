package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.MA3APIEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpsUtil {

    private static Logger logger = LoggerFactory.getLogger(OpsUtil.class);

    private static List<String> mSystemPathList = new ArrayList<String>();

    static
    {
        String os = System.getProperty("os.name").toLowerCase();

        if(os.indexOf("windows") >=0)
        {
            String winSystemPath = System.getenv("SystemRoot") +
                    System.getProperty("file.separator") +
                    "system32" +
                    System.getProperty("file.separator");
            mSystemPathList.add(winSystemPath);
        }
        else if(os.indexOf("mac") >=0)
        {
            String macSystemPath1 = System.getProperty("file.separator") +
                    "usr" + System.getProperty("file.separator") +
                    "local" + System.getProperty("file.separator") +
                    "lib" + System.getProperty("file.separator");
            mSystemPathList.add(macSystemPath1);

            String macSystemPath2 = System.getProperty("file.separator") +
                    "usr" + System.getProperty("file.separator") +
                    "lib" + System.getProperty("file.separator");
            mSystemPathList.add(macSystemPath2);

            // MAC OS Big Sur'da aşağıdaki ayar olmadığı zaman PKCS11 işlemleri yapılamıyormuş.
            // Başka MAC sürümlerinde bu ayarın yapılmasından dolayı hata alınması durumda bu ayar pasifize edilebilsin diye bir değişkene bağlandı.
            if (MA3APIEnvironment.SET_MAC_OS_PCSC_PATH)
                System.setProperty("sun.security.smartcardio.library", "/System/Library/Frameworks/PCSC.framework/Versions/Current/PCSC");
        }
        else
        {
            String otherLinuxBasedOSSystemPath = System.getProperty("file.separator") +
                    "lib" +
                    System.getProperty("file.separator");
            mSystemPathList.add(otherLinuxBasedOSSystemPath);
        }
    }

    public static String getLibPath(String aLibName)
    {
        String userDir = System.getProperty("user.dir");
        logger.debug("Working dir :" + userDir);
        logger.debug("Looking for pkcs11 library in path:" + userDir);
        if (new File(aLibName).exists()) {
            logger.info(aLibName + " found in working directory");
            return aLibName;
        }

        String path = System.getProperty("java.library.path");
        String pathSep = System.getProperty("path.separator");
        String[] items = path.split(pathSep);

        for(String item:items)
        {
            String absPath = item + System.getProperty("file.separator") + System.mapLibraryName(aLibName);
            logger.debug("Looking for pkcs11 library in path:" + absPath);
            if (new File(absPath).exists()) {
                logger.info("Will work with library path :" + absPath);
                return absPath;
            }
        }

        for (String systemPath : mSystemPathList) {
            String libPath = systemPath + System.mapLibraryName(aLibName);
            logger.debug("Looking for pkcs11 library in path:" + libPath);
            if (new File(libPath).exists()) {
                logger.info("Will work with library path :" + libPath);
                return libPath;
            } else {
                logger.debug("Can not find library at path :" + libPath);
            }
        }
        logger.info("Will work with library path :" + System.mapLibraryName(aLibName));
        return System.mapLibraryName(aLibName);
    }
}
