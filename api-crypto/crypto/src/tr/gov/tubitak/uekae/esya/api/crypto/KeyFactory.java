package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.KeySpec;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author ayetgin
 */

public interface KeyFactory
{
    PublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException;

    PrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException;

    SecretKey generateSecretKey(KeySpec aKeySpec) throws CryptoException;

    SecretKey generateSecretKey(CipherAlg alg, int keyLength) throws CryptoException;
    /**
     * Generate raw secret key
     * @param aAlg which the key is for
     * @param aBitLength in bits
     * @return raw key in bytes
     */
    byte[] generateKey(CipherAlg aAlg, int aBitLength);

    PublicKey generatePublicKey(java.security.spec.KeySpec aKeySpec) throws CryptoException;

    PrivateKey generatePrivateKey(java.security.spec.KeySpec aKeySpec) throws CryptoException;


}
