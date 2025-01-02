package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.SunPKCS11;
import sun.security.pkcs11.wrapper.PKCS11;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.*;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 12/6/12 - 5:41 PM <p>
 * <b>Description</b>: <br>
 * Here is the class of alot of magic happens.
 *  <br>  {@literal ->} create NSS DB with modutil.
 *  <br>  {@literal ->} init to Fipsmode if it requires.
 *  <br>  {@literal ->} change pin of "NSS FIPS 140-2 Certificate DB"
 *  <br>  {@literal ->} create SunPKCS11
 *  <br>  {@literal ->}extract PKCS11 wrapper for NSS Softtoken from SunPKCS11
 *  <br>  {@literal ->} zeroize
 *
 */
public abstract class NSSLoader {
    /**
     * NSS 3.11.4 is Fips validated at 2007, NSS 3.12.4 is validated at 2012. or pending.
     */
    protected final static String NSS_VERSION = "3.12.4";
    private static Logger logger = LoggerFactory.getLogger(NSSLoader.class);
    /**
     * temp NSS token directory
     */
    protected String tempNSSDBDir;
    /**
     * wheter we should run on fipsmode or not
     */
    private boolean fipsMode;
    /**
     * it can be only used to generate NSS softtoken pin
     */
    private final SecureRandom random;

    /**
     * token pin.
     */
    private char[] pin;


    public static CryptoProvider loadPlatformNSS(String tempNSSDir, boolean fipsMode) throws CryptoException {
        NSSLoader platformLoader = getPlatformLoader(tempNSSDir, fipsMode);
        return platformLoader.loadProvider();
    }

    /**
     * Determine platform loader for Win32, Lin32-64.
     * Windows 64 (which mean java64 on windows 64) does not support atm
     * @param tempNSSDir
     * @param fipsMode
     * @return
     * @throws CryptoException
     */
    private static NSSLoader getPlatformLoader(String tempNSSDir, boolean fipsMode) throws CryptoException {
        String os = System.getProperty("os.name").toLowerCase();
        String bit = System.getProperty("os.arch");
        if (os.contains("windows")) {
            if (bit.contains("86"))
                return new NSSLoader_Win32(tempNSSDir, fipsMode);
            if (bit.contains("64"))
                return new NSSLoader_Win64(tempNSSDir, fipsMode);
        } else if (os.contains("linux")) {
            if (bit.contains("86"))
                return new NSSLoader_Lin32(tempNSSDir, fipsMode);
            else if (bit.contains("64"))
                return new NSSLoader_Lin64(tempNSSDir, fipsMode);
        }
        throw new CryptoException("Unknown Platform:" + os + "-" + bit + " NSS only supports:Win32-Lin32-Lin64");
    }

    /**
     * init directories etc.
     * @param tempNSSDir
     * @param fipsMode
     */
    protected NSSLoader(String tempNSSDir, boolean fipsMode) {
        if (!tempNSSDir.endsWith(File.separator))
            tempNSSDir += File.separator;
        this.fipsMode = fipsMode;
        random = new SecureRandom();
        tempNSSDBDir = tempNSSDir + "nssdb-"+(random.nextInt()<<2);
    }

    /**
     * Herein main loader
     * @return
     * @throws CryptoException
     */
    public NSSCryptoProvider loadProvider() throws CryptoException {

        try {
            createDirs(tempNSSDBDir);    // create nss db directories if it's not exists
            copyLibraries();
            calculatePin();
            initCrypoDB();
            return createSunPKCS11WithNSs(fipsMode);
        } finally {
            zeroize();
        }

    }

    /**
     * Zeroize pin
     */
    protected void zeroize(){
        for (int i = 0; i < pin.length; i++) {
            pin[i] = 0;
        }
    }

    /**
     * Calculates pin with Base64Char(10) + LowerCase(1) + UpperCase(1) + Number(1)
     */
    protected void calculatePin(){
        byte[] bytes = new byte[12];
        random.nextBytes(bytes);
        char[] lowLetters = "abcdefghijklmnoprqstvuwxyz".toCharArray();
        char[] upLetters = "ABCDEFGHIJKLMNOPRQSTUVWXYZ".toCharArray();
        char[] symbols = ".,!-*$%^&+@".toCharArray();
        String pinStart = Base64.encode(bytes).substring(0, 12);
        pinStart += lowLetters[random.nextInt(lowLetters.length)]
                + upLetters[random.nextInt(upLetters.length)]
                + symbols[random.nextInt(symbols.length)]
                + random.nextInt(10);
        this.pin = pinStart.toCharArray();
//        this.pin = "Test'!123456".toCharArray();
    }

    protected abstract String convertToOSPath(String path);
    public void doPlatformDependentJobs(String libraryDirPath)
            throws CryptoException {
    }

    /**
     * init Crypto DB with SunPKCS11 and login
     * @param fipsMode
     * @return
     * @throws CryptoException
     */
    private NSSCryptoProvider createSunPKCS11WithNSs(boolean fipsMode) throws CryptoException {
        Locale defaultLocale = Locale.getDefault();
        try {
            String targetLibraryDir = getTargetLibraryDir();
            System.setProperty("java.library.path",targetLibraryDir );
            doPlatformDependentJobs(targetLibraryDir);
            String pkcs11Cfg = "name = NSSfips\n" +
                    "nssLibraryDirectory = " + targetLibraryDir + "\n" +
                    "nssSecmodDirectory = "+convertToOSPath(tempNSSDBDir) + "\n" +
                    "nssDbMode = readWrite \n" +
                    "showInfo = true"+"\n" +
                    "nssModule = " + (fipsMode ? "fips" : "crypto") + "\n";
            pkcs11Cfg = pkcs11Cfg.replace("\\","/");
            logger.info("Creating SunPkcs11 provider with j2pkcs11");
            logger.info("Config :"+pkcs11Cfg);

            logger.info("SunPKCS11 ilklendirilecek.");
            SunPKCS11 sunPKCS11 = new SunPKCS11(new ByteArrayInputStream(pkcs11Cfg.getBytes()));
            logger.info("SunPKCS11 ilklendirildi.");

            logger.info("SunPKCS11 ile Login olucak.");
            CallbackHandler loginCallbackHandler = new  CallbackHandler() {
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof javax.security.auth.callback.PasswordCallback)
                            ((javax.security.auth.callback.PasswordCallback) callback).setPassword(pin);
                        else
                            logger.warn("Different Callback:" + callback);
                    }
                }
            };

            Locale.setDefault(Locale.US);
            sunPKCS11.setCallbackHandler(loginCallbackHandler);
            sunPKCS11.login(null,loginCallbackHandler);
            logger.info("SunPKCS11 ile Login olundu.");

            logger.info("PKCS11 Wrapper oluşturulacak.");
            Pair<SmartCard, Long> params = getPKCS11Wrapper(sunPKCS11);
            logger.info("SunPkcs11 provider constructing completed.");
            NSSCryptoProvider nssCryptoProvider = new NSSCryptoProvider(sunPKCS11, fipsMode, params.getObject1(), params.getObject2());
            logger.info("PKCS11 Wrapper oluşturuldu");
            return nssCryptoProvider;
        } catch (Exception e) {
            logger.error("Error while Logging in NSS via SunPKCS11(j2pkcs11):",e);
            logger.error("Please check your j2pkcs11 library in java directory. If your system is 64 bit then please make sure that you copied sunpkcs11 library for 64 bit jre to java directory");
            throw new CryptoException("Error while Logging in NSS via SunPKCS11(j2pkcs11):"+e.getMessage(),e);
        }
        finally {
            Locale.setDefault(defaultLocale);
        }
    }

    /**
     * Extracts Pkcs11Wrapper, and SlotID from SunPKCS11
     */
    private Pair<SmartCard, Long> getPKCS11Wrapper(SunPKCS11 sunPKCS11) throws ESYAException {
        try {
            Field p11Field = SunPKCS11.class.getDeclaredField("p11");
            p11Field.setAccessible(true);
            PKCS11 pkcs11 = (PKCS11) p11Field.get(sunPKCS11);
            p11Field.setAccessible(false);

            Field slotIDField = SunPKCS11.class.getDeclaredField("slotID");
            slotIDField.setAccessible(true);
            long slotID = slotIDField.getLong(sunPKCS11);
            slotIDField.setAccessible(false);


/*        Field tokenField = SunPKCS11.class.getDeclaredField("token");
        tokenField.setAccessible(true);
        Object token = tokenField.get(sunPKCS11);
        tokenField.setAccessible(false);

        Class<?> tokenClass = Class.forName("sun.security.pkcs11.Token");
        Method getOpSession = tokenClass.getDeclaredMethod("getOpSession");
        getOpSession.setAccessible(true);
        Object session = getOpSession.invoke(token);

        Class<?> sessionClass = Class.forName("sun.security.pkcs11.Session");
        Method idMethod = sessionClass.getDeclaredMethod("id");
        idMethod.setAccessible(true);
        Long nssSessionID = (Long) idMethod.invoke(session);*/

            return new Pair<SmartCard, Long>(new SmartCard(new NSSCardType(pkcs11)),slotID);
        }catch (Exception e){
            throw new ESYAException(e);
        }
    }

    /**
     * Initialize CryptoDB in given directory using ModUtil
     * Note: take care of Linux init.
     * @throws CryptoException
     */
    private void initCrypoDB( ) throws CryptoException {
        File pwdFile = null;
        try {
            String libDir = getTargetLibraryDir();
            String pwd = libDir +"/pwd";
            pwdFile = new File(pwd);
            pwdFile.deleteOnExit();
            writeToTarget(new ByteArrayInputStream(new String(pin).getBytes("UTF-8")), pwdFile);
//            String enter = libDir+"\\enter";
//            writeToTarget(new ByteArrayInputStream(("\n\n\n").getBytes()), new File(enter));

            String command = "-create -dbdir "+ convertToOSPath(tempNSSDBDir)+" -force";
            execute(libDir, command);

            command = "-dbdir "+convertToOSPath(tempNSSDBDir)+" -fips true -force";
            execute(libDir,command);

            command = "-dbdir "+convertToOSPath(tempNSSDBDir)+" -changepw \"NSS FIPS 140-2 Certificate DB\" -newpwfile "+pwd+" -force";
            execute(libDir,command);

//            command = libDir+"\\modutil -dbdir "+dbDir+" -changepw \"NSS Generic Crypto Services\" \n"+pin+" \n "+pin+"\n";
//            Runtime.getRuntime().exec(command);


        } catch (Exception e) {
            logger.error("Error while creating NSS DB:"+e.getMessage(),e);
            throw new CryptoException("Error while creating NSS DB:"+e.getMessage(),e);
        }
        finally {
            try {
                if(pwdFile != null && pwdFile.exists())
                    pwdFile.delete();
            } catch (Exception e) {
                logger.warn("Couldnot deleteFile pwdFile:"+e.getMessage(),e);
            }
        }
    }

    /**
     * Executes command.
     * Note: Implement for Linuxxx
     * @param libdir
     * @param command
     * @throws ESYAException
     */
    protected void execute(String libdir, String command) throws ESYAException {
        try {
            logger.info("Execute:"+command);
            Process process = Runtime.getRuntime().exec(libdir + "/modutil " + command);
            //ProcessBuilder builder = new ProcessBuilder("cmd","/c "+libdir+"\\modutil "+command); // implement for linux...
            //ProcessBuilder builder = new ProcessBuilder(libdir+"/modutil ",command); // implement for linux...
            //Process process = builder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while( (line = reader.readLine()) != null){
                logger.info(line);
            }
        }catch (Exception e){
            throw new ESYAException(e);
        }
    }

    /**
     * Extract and copy NSS libraries, executables and chk files to System.
     * @throws CryptoException
     */
    private void copyLibraries() throws CryptoException {

        try {
            String zipLibrary = getLibrary();
            ZipInputStream zipInputStream = new ZipInputStream(getClass().getResourceAsStream(zipLibrary));
            ZipEntry zipEntry ;
            while( (zipEntry = zipInputStream.getNextEntry()) != null){
                try {
                    String target = getTargetLibraryDir() + File.separator + zipEntry.getName();
                    File targetFile = new File(target);
                    long sourceFileSize = zipEntry.getSize();
                    long targetFileSize = targetFile.length();
                    if(targetFile.exists() && (sourceFileSize == targetFileSize)){
                        logger.debug("Source and target file size is same. File:" + target + ", Size :" + targetFileSize);
                        continue;
                    }
                    if (targetFile.exists()) {
                        targetFile.delete();
                    }
                    writeToTarget(zipInputStream,targetFile);
                }
                catch (Exception exc){
                    logger.error("Error while copying NSS Library files to system directory.Exception:" + exc.getMessage(), exc);
                }
                finally {
                    zipInputStream.closeEntry();
                }
            }
            zipInputStream.close();
        } catch (Exception e) {
            logger.error("Error while preparing NSS Library Exception:" + e.getMessage(), e);
            throw new CryptoException("Error while preparing NSS Library Exception:" + e.getMessage(), e);
        }
    }

    /**
     * write gicen InputStream to targetFile
     * @param is
     * @param targetFile
     * @throws IOException
     */
    private void writeToTarget(InputStream is, File targetFile) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(targetFile);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
/*            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    logger.warn("Error while closing Resource Input Stream:" + e.getMessage());
                }*/
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    logger.warn("Error while closing Library Output Stream:" + e.getMessage(), e);
                }
        }
    }

    /**
     * Platform specific lib.zip in Jar File
     * @return
     */
    protected abstract String getLibrary();

    /**
     * platform specific target Library Directory.
     * @return
     */
    protected abstract String getTargetLibraryDir();

    private void createDirs(String... dirs) {
        for (String dir : dirs)
            createDir(dir);
    }

    protected void createDir(String tempNSSDir) {
        File file = new File(tempNSSDir);
        if (!file.exists())
            file.mkdirs();
    }


    public static void clearNSSDirectory(String tempNSSDir){
        try {
            deleteFile(new File(tempNSSDir));
        } catch (IOException e) {
            logger.error("Error in NSSLoader", e);
        }
    }

    private static void deleteFile(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                deleteFile(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to deleteFile file: " + f);
    }
}
