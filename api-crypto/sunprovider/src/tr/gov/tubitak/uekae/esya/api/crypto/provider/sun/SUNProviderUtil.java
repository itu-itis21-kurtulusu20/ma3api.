package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author ayetgin
 */
public class SUNProviderUtil
{
    private static Map<String, String> mEsya2SunCipherNames = new HashMap<String, String>();
    private static Map<String, String> mEsya2SunPaddingNames = new HashMap<String, String>();

    static {
        mEsya2SunCipherNames.put("AES128", "AES");
        mEsya2SunCipherNames.put("AES192", "AES");
        mEsya2SunCipherNames.put("AES256", "AES");
        mEsya2SunCipherNames.put("3DES", "DESede");
        mEsya2SunCipherNames.put("RSA", "RSA");
        mEsya2SunCipherNames.put("RC2", "RC2");

        mEsya2SunPaddingNames.put("PKCS1", "PKCS1Padding");
        mEsya2SunPaddingNames.put("PKCS5", "PKCS5Padding");
        mEsya2SunPaddingNames.put("PKCS7", "PKCS5Padding");
        mEsya2SunPaddingNames.put("", "NoPadding");
    }

    public static String getTransformName(CipherAlg aCipherAlg){
        String[] parts = aCipherAlg.getName().toUpperCase(Locale.US).split("/");
        String alg  = mEsya2SunCipherNames.get(parts[0]);
        String mode = parts[1];
        String padding  = mEsya2SunPaddingNames.get(parts[2]);
        return alg + '/' + mode + '/' + padding;
    }

    public static String getCipherName(CipherAlg aCipherAlg){
        String[] parts = aCipherAlg.getName().toUpperCase(Locale.US).split("/");
        return mEsya2SunCipherNames.get(parts[0]);
    }

    public static String getKeyPairGenName(AsymmetricAlg aAsymmetricAlg) throws CryptoException {
        if (aAsymmetricAlg!=null)
        switch (aAsymmetricAlg){
            case RSA:   return "RSA";
            case DSA:   return "DSA";
            case ECDSA: return "EC";
        }
        throw new CryptoException("Unsupported op for asymmetric alg: "+aAsymmetricAlg);
    }

    public static String getSignatureName(SignatureAlg aSignatureAlg){
        String digestName;
        if (aSignatureAlg.getDigestAlg()!=null){
            digestName = aSignatureAlg.getDigestAlg().getName();
            digestName =  digestName.replace("-", "");
        }
        else {
            digestName = "NONE";
        }
        
        return  digestName + "with" + aSignatureAlg.getAsymmetricAlg().getName();
    }

    public static String getMACName(MACAlg aMACAlg){
        String rawName = aMACAlg.getName().toUpperCase();
        String macType = rawName.substring(0, rawName.indexOf('-'));
        String digestAlg = aMACAlg.getDigestAlg().getName();
        digestAlg =  digestAlg.replace("-", "");
        return macType+'-'+digestAlg;
    }

    public static String getPBEName(PBEAlg aPbeAlg){
        String cipher = SUNProviderUtil.getTransformName(aPbeAlg.getCipherAlg()).split("/")[0];
        String digest = aPbeAlg.getDigestAlg().getName().replace("-", "");
        return "PBEWith"+ digest +"And"+cipher;
    }
}
