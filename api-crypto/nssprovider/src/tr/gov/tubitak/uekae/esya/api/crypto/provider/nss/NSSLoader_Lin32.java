package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 12/6/12 - 5:41 PM <p/>
 * <b>Description</b>: <br/>
 */
class NSSLoader_Lin32 extends NSSLoader {
    private static Logger logger = LoggerFactory.getLogger(NSSLoader_Lin32.class);

    NSSLoader_Lin32(String tempNSSDir, boolean fipsMode) {
        super(tempNSSDir, fipsMode);
    }



    @Override
    protected String getTargetLibraryDir() {
        //return File.separator + "lib";
        String targetDir =  tempNSSDBDir+"/libNSS";
        createDir(targetDir);
        return targetDir;
    }

    @Override
    protected String convertToOSPath(String path) {
        //do nothing in linux
        return path;
    }

    @Override
    protected String getLibrary() {
        return "/org/nss/"+NSS_VERSION+"/lin32/lib.zip";
    }

    @Override
    public void doPlatformDependentJobs(String libraryDirPath){
        System.load(libraryDirPath+"/libsqlite3.so");
        System.load(libraryDirPath+"/libplc4.so");
        System.load(libraryDirPath+"/libplds4.so");
        System.load(libraryDirPath+"/libnspr4.so");
        System.load(libraryDirPath+"/libnssutil3.so");
        System.load(libraryDirPath+"/libnssdbm3.so");
        System.load(libraryDirPath+"/libfreebl3.so");
        System.load(libraryDirPath+"/libnssckbi.so");
        System.load(libraryDirPath+"/libnss3.so");
        System.load(libraryDirPath+"/libsmime3.so");
        System.load(libraryDirPath+"/libnssutil3.so");
        System.load(libraryDirPath+"/libnssckbi.so");

    }

    private void addExecutePrivilage(String commandPath) throws Exception{
        Process process = Runtime.getRuntime().exec("chmod +x "+commandPath);
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while( (line = reader.readLine()) != null){
            logger.info(line);
            System.out.println(line);
        }
    }

    @Override
    protected void execute(String libdir, String command) throws ESYAException {
        try {
            logger.info("Execute:" + command);
            addExecutePrivilage(libdir + "/modutil");
            String tmpCommand = command;

            List<String> cmdParams = new ArrayList<String>();
            cmdParams.add("sh");
            cmdParams.add(libdir + "/modutil.sh");
            String[] allParam = tmpCommand.split(" \"");
            for (String param : allParam) {
                if (param.contains("NSS FIPS")) {
                    String[] nssParams = param.split("\" ");
                    for (String nssParam : nssParams) {
                        if (nssParam.contains("NSS FIPS")) {
                            cmdParams.add(nssParam);
                        } else {
                            String[] otherParams = nssParam.split(" ");
                            for (String otherParam : otherParams) {
                                cmdParams.add(otherParam);
                            }
                        }
                    }
                } else {
                    String[] otherParams = param.split(" ");
                    for (String otherParam : otherParams) {
                        cmdParams.add(otherParam);
                    }
                }
            }

            String[] cmdParamArray = cmdParams.toArray(new String[0]);
            //Process process = Runtime.getRuntime().exec("sh "+libdir + "/modutil.sh " + command);
            Process process = Runtime.getRuntime().exec(cmdParamArray);
            //Process process = Runtime.getRuntime().exec(libdir + "/modutil " + command);
            //ProcessBuilder builder = new ProcessBuilder(libdir+"/modutil",command); // implement for linux...
            //ProcessBuilder builder = new ProcessBuilder(libdir+"/modutil ",command); // implement for linux...
            //Process process = builder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

}
