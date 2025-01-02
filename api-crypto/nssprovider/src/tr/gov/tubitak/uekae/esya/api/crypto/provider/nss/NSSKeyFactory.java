package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import tr.gov.tubitak.uekae.esya.api.crypto.KeyFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.KeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.PBEKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.sun.SUNProviderUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.*;
import java.security.spec.*;

/**
 * @author ayetgin
 */
public class NSSKeyFactory implements KeyFactory {

    Provider mProvider = null;


   public NSSKeyFactory(Provider aProvider) {
        mProvider = aProvider;
    }

    public PublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes)
            throws CryptoException {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(aBytes);
            return getKeyFactory(aAsymmetricAlg).generatePublic(spec);
        } catch (Exception x) {
            throw new CryptoException("Acn not decode public key ", x);
        }
    }

    private java.security.KeyFactory getKeyFactory(AsymmetricAlg aAsymmetricAlg) throws NoSuchAlgorithmException, CryptoException {
        if (mProvider == null)
            return java.security.KeyFactory.getInstance(SUNProviderUtil.getKeyPairGenName(aAsymmetricAlg));
        else {
            try {
                return java.security.KeyFactory.getInstance(SUNProviderUtil.getKeyPairGenName(aAsymmetricAlg), mProvider);
            }catch (NoSuchAlgorithmException exc){//CentOS 64 bit'te EC'yi görmediği için böyle bir şey yaptık. İleride vakit olduğunda Centos 7deki soruna bakılması lazım.
                exc.printStackTrace();
                return java.security.KeyFactory.getInstance(SUNProviderUtil.getKeyPairGenName(aAsymmetricAlg));
            }
        }
    }

    public PrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes) throws CryptoException {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(aBytes);
            return getKeyFactory(aAsymmetricAlg).generatePrivate(spec);
        } catch (Exception x) {
            throw new CryptoException("Acn not decode private key ", x);
        }
    }

    public SecretKey generateSecretKey(KeySpec aKeySpec) throws CryptoException {
        if (aKeySpec instanceof PBEKeySpec) {
            try {
                PBEKeySpec aSpec = (PBEKeySpec) aKeySpec;
                javax.crypto.spec.PBEKeySpec spec =
                        new javax.crypto.spec.PBEKeySpec(
                                aSpec.getPassword(),
                                aSpec.getSalt(),
                                aSpec.getIterationCount(),
                                aSpec.getKeyLength() * 8);

                return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec);   // not available in nss
            } catch (Exception x) {
                throw new CryptoException("Error generating PBE key", x);
            }
        }
        else if (aKeySpec instanceof SecretKeySpec){
            try {
                SecretKeySpec secretKeySpec = (SecretKeySpec) aKeySpec;

                KeyGenerator keyGenerator = KeyGenerator.getInstance(SUNProviderUtil.getCipherName(((SecretKeySpec) aKeySpec).getmAlgorithm()), mProvider);

                keyGenerator.init(secretKeySpec.getmLen());

                SecretKey secretKey =  keyGenerator.generateKey();


                KeyStore ks = KeyStore.getInstance("PKCS11", mProvider);
                ks.load(null, null);

                ks.setEntry(secretKeySpec.getmLabel(), new KeyStore.SecretKeyEntry(secretKey), null);

                return secretKey;
            }
            catch (Exception e){
                throw new CryptoException("Error generating Secretkey", e);
            }

        }

        throw new UnknownElement("Unknown key spec " + aKeySpec);

    }

    public SecretKey generateSecretKey(CipherAlg alg, int keyLength) throws CryptoException {
        try {
            KeyGenerator keyGenerator;
            keyGenerator = KeyGenerator.getInstance(SUNProviderUtil.getCipherName(alg), mProvider);
            keyGenerator.init(keyLength);
            SecretKey secretKey =  keyGenerator.generateKey();
            KeyStore ks = KeyStore.getInstance("PKCS11", mProvider);
            ks.load(null, null);
            String keyLabel = "Secret-"+System.currentTimeMillis();
            ks.setEntry(keyLabel, new KeyStore.SecretKeyEntry(secretKey), null);
            return secretKey;
        } catch (Exception e) {
            throw new CryptoException("Error while generation Secret Key:" + e.getMessage(), e);
        }
    }


    public byte[] generateKey(CipherAlg aAlg, int aBitLength) {
        return RandomUtil.generateRandom(aBitLength / 8);
    }

    public PublicKey generatePublicKey(java.security.spec.KeySpec aKeySpec)
            throws CryptoException {
        if (aKeySpec instanceof RSAPublicKeySpec) {
            try {
                return getKeyFactory(AsymmetricAlg.RSA).generatePublic(aKeySpec);
            } catch (Exception aEx) {
                throw new CryptoException("Error generating rsa public key", aEx);
            }
        } else if (aKeySpec instanceof ECPublicKeySpec) {
            try {
                return getKeyFactory(AsymmetricAlg.ECDSA).generatePublic(aKeySpec);
            } catch (Exception aEx) {
                throw new CryptoException("Error generating rsa public key", aEx);
            }
        } else
            throw new UnknownElement("Unknown key spec " + aKeySpec);
    }

    public PrivateKey generatePrivateKey(java.security.spec.KeySpec aKeySpec)
            throws CryptoException {
        if (aKeySpec instanceof RSAPrivateCrtKeySpec || aKeySpec instanceof RSAPrivateKeySpec) {
            try {
                return getKeyFactory(AsymmetricAlg.RSA).generatePrivate(aKeySpec);
            } catch (Exception aEx) {
                throw new CryptoException("Error generating rsa private key", aEx);
            }
        } else if (aKeySpec instanceof ECPrivateKeySpec) {
            try {
                return getKeyFactory(AsymmetricAlg.ECDSA).generatePrivate(aKeySpec);
            } catch (Exception aEx) {
                throw new CryptoException("Error generating ecdsa private key", aEx);
            }
        } else
            throw new UnknownElement("Unknown key spec " + aKeySpec);
    }

}
