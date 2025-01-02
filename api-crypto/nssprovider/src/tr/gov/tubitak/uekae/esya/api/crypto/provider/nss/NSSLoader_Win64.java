package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import java.io.File;

/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 12/6/12 - 5:41 PM <p/>
 * <b>Description</b>: <br/>
 */
class NSSLoader_Win64 extends NSSLoader {
    NSSLoader_Win64(String tempNSSDir, boolean fipsMode) {
        super(tempNSSDir, fipsMode);
    }

    @Override
    protected String convertToOSPath(String path) {
        return "\""+path+"\"";
    }

    @Override
    protected String getLibrary() {
        return "/org/nss/"+NSS_VERSION+"/win64/lib.zip";
    }

    @Override
    protected String getTargetLibraryDir() {
        return System.getenv("SystemRoot") + File.separator + "System32";
//        return "D:\\Projects\\MA3API\\nss\\temp";
    }
}
