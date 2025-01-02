package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 12/17/12 - 11:43 PM <p>
 * <b>Description</b>: <br>
 */
public class NSSProviderUtil {
    // find better way for these conversions
    public static String getNssWrapperName(WrapAlg algorithm) {
        if(algorithm.equals(WrapAlg.RSA_PKCS1) || algorithm.equals(WrapAlg.RSA_ECB_PKCS1))
            return "RSA/ECB/PKCS1Padding";
        else return algorithm.getName();


    }

    public static String getMACName(MACAlg aMACAlg) {
        if(aMACAlg == MACAlg.HMAC_SHA1)
            return "HmacSHA1";
        else if(aMACAlg == MACAlg.HMAC_SHA256)
            return "HmacSHA256";
        else if(aMACAlg == MACAlg.HMAC_SHA384)
            return "HmacSHA384";
        else if(aMACAlg == MACAlg.HMAC_SHA512)
            return "HmacSHA512";
        else if(aMACAlg == MACAlg.HMAC_MD5)
            return "HmacMD5";
        else
            return aMACAlg.getName();
    }
}
