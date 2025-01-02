package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.KeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;

import javax.crypto.SecretKey;
import java.lang.reflect.Constructor;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */

public class KeyUtil
{
    private static Map<Algorithm, Integer> msAlgoKeyLength = new HashMap<Algorithm, Integer>();
    private static Map<CipherAlg, WrapAlg> msCipherAlgToWrapAlg = new HashMap<CipherAlg, WrapAlg>();

    static {
        msAlgoKeyLength.put(CipherAlg.AES128_CBC, 128);
        msAlgoKeyLength.put(CipherAlg.AES128_CFB, 128);
        msAlgoKeyLength.put(CipherAlg.AES128_OFB, 128);
        msAlgoKeyLength.put(CipherAlg.AES128_ECB, 128);
        msAlgoKeyLength.put(CipherAlg.AES128_GCM, 128);
        msAlgoKeyLength.put(CipherAlg.AES192_CBC, 192);
        msAlgoKeyLength.put(CipherAlg.AES192_CFB, 192);
        msAlgoKeyLength.put(CipherAlg.AES192_OFB, 192);
        msAlgoKeyLength.put(CipherAlg.AES192_ECB, 192);
        msAlgoKeyLength.put(CipherAlg.AES192_GCM, 192);
        msAlgoKeyLength.put(CipherAlg.AES256_CBC, 256);
        msAlgoKeyLength.put(CipherAlg.AES256_CFB, 256);
        msAlgoKeyLength.put(CipherAlg.AES256_OFB, 256);
        msAlgoKeyLength.put(CipherAlg.AES256_ECB, 256);
        msAlgoKeyLength.put(CipherAlg.AES256_GCM, 256);
        msAlgoKeyLength.put(CipherAlg.RC2_CBC, 128);    //It can be different.This is default value.
        msAlgoKeyLength.put(CipherAlg.DES_EDE3_CBC, 192);
        msAlgoKeyLength.put(WrapAlg.AES128, 128);
        msAlgoKeyLength.put(WrapAlg.AES128_CBC, 128);
        msAlgoKeyLength.put(WrapAlg.AES192, 192);
        msAlgoKeyLength.put(WrapAlg.AES192_CBC, 192);
        msAlgoKeyLength.put(WrapAlg.AES256, 256);
        msAlgoKeyLength.put(WrapAlg.AES256_CBC, 256);
    }
    
    static
    {
    	msCipherAlgToWrapAlg.put(CipherAlg.AES128_CBC, WrapAlg.AES128_CBC);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES128_CFB, WrapAlg.AES128);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES128_OFB, WrapAlg.AES128);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES128_ECB, WrapAlg.AES128);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES192_CBC, WrapAlg.AES192_CBC);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES192_CFB, WrapAlg.AES192);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES192_OFB, WrapAlg.AES192);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES192_ECB, WrapAlg.AES192);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES256_CBC, WrapAlg.AES256_CBC);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES256_CFB, WrapAlg.AES256);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES256_OFB, WrapAlg.AES256);
    	msCipherAlgToWrapAlg.put(CipherAlg.AES256_ECB, WrapAlg.AES256);
    }

    public static PublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException
    {
        return Crypto.getKeyFactory().decodePublicKey(aAsymmetricAlg, aBytes);
    }

    public static PublicKey decodePublicKey(ESubjectPublicKeyInfo aSubjectPublicKeyInfo) throws CryptoException
    {
        AsymmetricAlg alg = AsymmetricAlg.fromOID(aSubjectPublicKeyInfo.getAlgorithm().getAlgorithm().value);

        return decodePublicKey(alg, aSubjectPublicKeyInfo.getEncoded());
    }

    public static PrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException
    {
        return Crypto.getKeyFactory().decodePrivateKey(aAsymmetricAlg, aBytes);
    }

    public static int getKeyLength(ECertificate certificate) throws CryptoException {
        PublicKey publicKey = decodePublicKey(certificate.getSubjectPublicKeyInfo());
        int keyLength = getKeyLength(publicKey);
        return keyLength;
    }

    public static int getKeyLength(PublicKey aKey) throws CryptoException
    {
         if(aKey instanceof RSAKey)
              return ((RSAKey)aKey).getModulus().bitLength();

         if(aKey instanceof DSAKey)
              return ((DSAKey)aKey).getParams().getG().bitLength();

         if(aKey instanceof ECKey)
              return ((ECKey)aKey).getParams().getCurve().getField().getFieldSize();

        if(aKey.getClass().getName().equals("sun.security.x509.X509Key")){
            ECPublicKey ecpubkey = null;
            try {
                ecpubkey = (ECPublicKey)aKey;
                return ecpubkey.getParams().getCurve().getField().getFieldSize();
            }
            catch (ClassCastException exc) {
                try {
                    Class ecPublicKeyImpClass = Class.forName("sun.security.ec.ECPublicKeyImpl");
                    Constructor declaredConstructor  = ecPublicKeyImpClass.getConstructor(byte[].class);
                    ecpubkey = (ECPublicKey)declaredConstructor.newInstance(aKey.getEncoded());
                    return ecpubkey.getParams().getCurve().getField().getFieldSize();
                }catch (Exception exc3){
                }
            }
        }

         throw new ArgErrorException("");
    }

    public static int getKeyLength(java.security.spec.KeySpec publicKeySpec) throws CryptoException
    {
        if (publicKeySpec instanceof ECPublicKeySpec){
            ECPublicKeySpec ecPublicKeySpec = (ECPublicKeySpec) publicKeySpec;
            return ecPublicKeySpec.getParams().getCurve().getField().getFieldSize();

        } else if (publicKeySpec instanceof RSAPublicKeySpec){
            RSAPublicKeySpec rsaPublicKeySpec = (RSAPublicKeySpec) publicKeySpec;
            return rsaPublicKeySpec.getModulus().bitLength();
        } else
            throw new CryptoException("Key algorithm not supported..!");
    }

    public static KeyPair generateKeyPair(AsymmetricAlg aAsymmetricAlg, int aLength) throws CryptoException
    {
    	ParamsWithLength lengthParam = new ParamsWithLength(aLength);
        return Crypto.getKeyPairGenerator(aAsymmetricAlg).generateKeyPair(lengthParam);
    }

    public static SecretKey generateSecretKey(CipherAlg mSymmetricAlgorithm, int keyLength) throws CryptoException {
        return Crypto.getKeyFactory().generateSecretKey(mSymmetricAlgorithm,keyLength); // check
    }

    public static SecretKey generateSecretKey(KeySpec aSpec) throws CryptoException
    {
        return Crypto.getKeyFactory().generateSecretKey(aSpec);
    }

    public static byte[] generateKey(CipherAlg aAlg, int aBitLength) throws CryptoException
    {
        return Crypto.getKeyFactory().generateKey(aAlg, aBitLength);
    }


    public static int getKeyLength(PrivateKey aKey) throws CryptoException
    {
         if(aKey instanceof RSAKey)
              return ((RSAKey)aKey).getModulus().bitLength();

         if(aKey instanceof DSAKey)
              return ((DSAKey)aKey).getParams().getG().bitLength();

         if(aKey instanceof ECKey)
              return ((ECKey)aKey).getParams().getCurve().getField().getFieldSize();

         throw new ArgErrorException("");
    }

    /**
     * @return required known key length in bits if applicable like some Cipher and Wrap algs.
     *  -1 if algo not known
     */
    public static int getKeyLength(Algorithm aCipheralg)
    {
        if (msAlgoKeyLength.containsKey(aCipheralg))
            return msAlgoKeyLength.get(aCipheralg);
        return -1;
    }
    
    public static WrapAlg getConvenientWrapAlg(CipherAlg aCipherAlg)
    {
    	if (msCipherAlgToWrapAlg.containsKey(aCipherAlg))
            return msCipherAlgToWrapAlg.get(aCipherAlg);
        return null;
    }
    
    public static PublicKey generatePublicKey(java.security.spec.KeySpec aKeySpec) throws CryptoException
    {
	return Crypto.getKeyFactory().generatePublicKey(aKeySpec);
    }
    
    public static PrivateKey generatePrivateKey(java.security.spec.KeySpec aKeySpec) throws CryptoException
    {
	return Crypto.getKeyFactory().generatePrivateKey(aKeySpec);
    }

}
