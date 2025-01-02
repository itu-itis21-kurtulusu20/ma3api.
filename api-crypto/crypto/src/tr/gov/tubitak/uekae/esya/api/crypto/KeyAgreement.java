package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * @author ayetgin
 */

public interface KeyAgreement
{
    public static long CKD_SHA224_KDF = 5L;
    public static long CKD_SHA256_KDF = 6L;
    public static long CKD_SHA384_KDF = 7L;
    public static long CKD_SHA512_KDF = 8L;
    /**
	 * 
	 * @param aKey private key for ECDH
	 * @param aParams parameters that may be needed in the KeyGeneration
	 */
    void init(Key aKey, AlgorithmParams aParams) throws CryptoException;

    //TO-DO geri döndürme tipi byte []'den SecretKey'e çevrilebilir.
    /**
     * @param aKey public key for ECDH
     * @param alg the requested secret key algorithm
     * @return symmetric key bytes
     */
    SecretKey generateKey(Key aKey, Algorithm alg) throws CryptoException;
}
