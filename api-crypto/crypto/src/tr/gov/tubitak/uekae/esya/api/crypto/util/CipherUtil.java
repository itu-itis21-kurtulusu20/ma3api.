package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.PBEAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.interfaces.PBEKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import static tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg.fromAlgorithmIdentifier;

/**
 * @author ayetgin
 */

public class CipherUtil {
    public static byte[] encrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, byte[] aData, final byte[] aSecretKey)
            throws CryptoException {
        BufferedCipher encryptor = Crypto.getEncryptor(aCipherAlg);
        encryptor.init(aSecretKey, aParams);
        return encryptor.doFinal(aData);
    }

    public static byte[] decrypt(EAlgorithmIdentifier aAlgorithm, final byte[] aData, final byte[] aSecretKey)
            throws CryptoException{
        Pair<CipherAlg, AlgorithmParams> alg = fromAlgorithmIdentifier(aAlgorithm);
        return decrypt(alg.first(), alg.second(), aData, aSecretKey);
    }

    public static byte[] decrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, final byte[] aData, final byte[] aSecretKey)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg);
        cipher.init(aSecretKey, aParams);
        return cipher.doFinal(aData);
    }

    public static byte[] decrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, final byte[] aData, final SecretKey aSecretKey)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg);
        cipher.init(aSecretKey, aParams);
        return cipher.doFinal(aData);
    }


    public static void encrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, InputStream aToBeEncrypted, OutputStream aEncrypted, final byte[] aKey)
            throws CryptoException, IOException {
        BufferedCipher cipher = Crypto.getEncryptor(aCipherAlg);
        cipher.init(aKey, aParams);
        int available, read;
        while ((available = aToBeEncrypted.available()) != 0) {
            byte[] bytes = new byte[available];
            read = aToBeEncrypted.read(bytes);
            cipher.process(bytes, 0, read);
        }
        aEncrypted.write(cipher.doFinal(null));
    }


    public static void decrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, InputStream aEncrypted, OutputStream aDecrypted, final byte[] aKey)
            throws CryptoException, IOException {
        BufferedCipher encryptor = Crypto.getDecryptor(aCipherAlg);
        encryptor.init(aKey, aParams);
        int available, read;
        while ((available = aEncrypted.available()) != 0) {
            byte[] bytes = new byte[available];
            read = aEncrypted.read(bytes);
            encryptor.process(bytes, 0, read);
        }
        aDecrypted.write(encryptor.doFinal(null));
    }

    public static byte[] encrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, byte[] aData, PublicKey aSifPubKey)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getEncryptor(aCipherAlg);
        cipher.init(aSifPubKey, aParams);
        return cipher.doFinal(aData);
    }

    public static byte[] wrap(CipherAlg cipherAlg, AlgorithmParams aParams, SecretKey aData, PublicKey aSifPubKey) throws CryptoException {
        //return wrap(WrapAlg.fromCipherAlg(cipherAlg), aParams, aData, aSifPubKey);
        return wrap(new WrapAlg(cipherAlg), aParams, aData, aSifPubKey);
    }

    public static byte[] wrap(WrapAlg wrapperAlg, AlgorithmParams aParams, SecretKey aData, PublicKey aSifPubKey) throws CryptoException {
        if(wrapperAlg == null)
            throw new CryptoException("Null Args");
        Wrapper wrapper = Crypto.getWrapper(wrapperAlg);
        wrapper.init(aSifPubKey, aParams);
        return wrapper.wrap(aData);
    }

    public static byte[] encrypt(EAlgorithmIdentifier aAlgorithm, byte[] aData, PublicKey aSifPubKey) throws CryptoException {
        Pair<CipherAlg, AlgorithmParams> alg = fromAlgorithmIdentifier(aAlgorithm);

        BufferedCipher cipher = Crypto.getEncryptor(alg.first());
        cipher.init(aSifPubKey, alg.second());
        return cipher.doFinal(aData);
    }


    public static byte[] encrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, byte[] aData, ECertificate aCertificate)
            throws CryptoException {
        if (!isEnciphermentCertificate(aCertificate))
            throw new ArgErrorException(GenelDil.mesaj(GenelDil.CERT_HATALI));
        return encrypt(aCipherAlg, aParams, aData, KeyUtil.decodePublicKey(aCertificate.getSubjectPublicKeyInfo()));
    }

    public static byte[] decrypt(CipherAlg aCipherAlg, AlgorithmParams aParams, byte[] aData, PrivateKey aSignPrivateKey)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg);
        cipher.init(aSignPrivateKey, aParams);
        return cipher.doFinal(aData);
    }

    /**
     * use this method when the encoding is not known...
     */
    public static byte[] decryptRSA(byte[] aData, PrivateKey aKey)
            throws CryptoException {
        CipherAlg alg;

        alg = CipherAlg.RSA_PKCS1;
        try {
            return decrypt(alg, null, aData, aKey);
        } catch (CryptoException cx) {
            // not rsa pkcs1
        }

        alg = CipherAlg.RSA_OAEP_SHA256;
        try {
            return decrypt(alg, null, aData, aKey);
        } catch (CryptoException ex) {
            // not rsa oaep
        }

        throw new CryptoException("Cant decrypt data, possibly encoding can not be determined.");
    }

    public static byte[] decryptRSA(byte[] aData, PrivateKey aKey, EAlgorithmIdentifier algorithmIdentifier) throws CryptoException {

        Pair<CipherAlg, AlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(algorithmIdentifier);
        return decrypt(cipherAlg.first(), cipherAlg.second(), aData, aKey);
    }


    public static byte[] decrypt(PBEAlg aCipherAlg, AlgorithmParams aParams, PBEKeySpec aKeySpec, byte[] aData)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg.getCipherAlg());
        PBEKey key = (PBEKey) Crypto.getKeyFactory().generateSecretKey(aKeySpec);
        cipher.init(key, aParams);
        return cipher.doFinal(aData);
    }

    public static byte[] encrypt(PBEAlg aCipherAlg, AlgorithmParams aParams, PBEKeySpec aKeySpec, byte[] aData)
            throws CryptoException {
        BufferedCipher cipher = Crypto.getEncryptor(aCipherAlg.getCipherAlg());
        SecretKey key = (SecretKey) Crypto.getKeyFactory().generateSecretKey(aKeySpec);
        cipher.init(key, aParams);
        return cipher.doFinal(aData);
    }

    public static boolean isEnciphermentCertificate(ECertificate aCertificate) {
        EKeyUsage keyUsage = aCertificate.getExtensions().getKeyUsage();
        return (keyUsage != null) && (keyUsage.isDataEncipherment() || keyUsage.isKeyEncipherment());
    }


}
