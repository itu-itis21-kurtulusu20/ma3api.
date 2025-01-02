package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

/**
 * @author ahmety
 *         date: Jul 2, 2009
 */
public class StringUtil
{
    public static String substring(String aSource, int aCharCount){
        if (aSource==null)
            return null;
        String subStr = aSource.substring(
                                   0, Math.min(aSource.length(), aCharCount-3));
        if (aSource.length()>aCharCount)
            subStr.concat("...");

        return subStr;
    }

    public static String substring(byte[] aSource, int aCharCount){
        if (aSource==null)
            return null;

        int len = Math.min(aSource.length, aCharCount-3);
        byte[] bytes = new byte[len];

        System.arraycopy(aSource, 0, bytes, 0, len);

        String subStr = new String(bytes);

        if (aSource.length>aCharCount)
            subStr.concat("...");

        return subStr;
    }

}
