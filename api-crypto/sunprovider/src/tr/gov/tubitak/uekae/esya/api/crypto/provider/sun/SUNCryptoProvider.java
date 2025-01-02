package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;

/**
 * Minimal Crypto Provider based on SUN crypto APIs. Note that if Sun JCE provider is altered,
 * classes that will be used by this provider will be altered too.
 *
 * <p>Below algorithms are supported:
 * <ul>
 * <li>ciphers: AES, 3DES
 *
 * <li>signatures: RSA, DSA
 *
 * <li>message digests: SHA, MD5
 *
 * <li>random number generation is done via SecureRandom.
 * </ul>
 *
 * @author ayetgin
 */

public class SUNCryptoProvider implements CryptoProvider
{

    public Cipher getEncryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        return new SUNCipher(aCipherAlg, true);
    }

    public Cipher getDecryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        return new SUNCipher(aCipherAlg, false);
    }

    public Digester getDigester(DigestAlg aDigestAlg) throws CryptoException
    {
        return new SUNDigester(aDigestAlg);  
    }

    public Signer getSigner(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return new SUNSigner(aSignatureAlg);
    }

    public Verifier getVerifier(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return new SUNVerifier(aSignatureAlg);
    }

    public MAC getMAC(MACAlg aMACAlg) throws CryptoException
    {
        return new SUNMAC(aMACAlg);
    }

    public Wrapper getWrapper(WrapAlg aWrapAlg) throws CryptoException
    {
        return new SUNWrapper(aWrapAlg, true);
    }

    public Wrapper getUnwrapper(WrapAlg aWrapAlg) throws CryptoException
    {
        return new SUNWrapper(aWrapAlg, false);  
    }

    public KeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException
    {
        return new SUNKeyAgreement(aKeyAgreementAlg);
    }

    public KeyFactory getKeyFactory() throws CryptoException
    {
        return new SUNKeyFactory();  
    }

    public KeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg) throws CryptoException
    {
        return new SUNKeyPairGenerator(aAsymmetricAlg);  
    }

    public RandomGenerator getRandomGenerator()
    {
        return new SUNRandomGenerator();  
    }

    public boolean isFipsMode() {
        return false;
    }

    public void destroyProvider() {

    }
}
