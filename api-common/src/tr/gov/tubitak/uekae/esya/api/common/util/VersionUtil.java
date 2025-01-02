package tr.gov.tubitak.uekae.esya.api.common.util;

public class VersionUtil {

    public static String getAPIVersion() {
        String versionString = VersionUtil.class.getPackage().getImplementationVersion();
        return versionString;
    }
}
