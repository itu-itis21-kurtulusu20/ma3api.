package tr.gov.tubitak.uekae.esya.api.crypto.provider;

import tr.gov.tubitak.uekae.esya.api.crypto.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;


/**
 * @author ayetgin
 */

public interface CryptoProvider
{
    Cipher getEncryptor(CipherAlg aCipherAlg) throws CryptoException;
    Cipher getDecryptor(CipherAlg aCipherAlg) throws CryptoException;
    Digester getDigester(DigestAlg aDigestAlg) throws CryptoException;
    Signer getSigner(SignatureAlg aSignatureAlg) throws CryptoException;
    Verifier getVerifier(SignatureAlg aSignatureAlg) throws CryptoException;
    MAC getMAC(MACAlg aMACAlg) throws CryptoException;
    Wrapper getWrapper(WrapAlg aWrapAlg) throws CryptoException;
    Wrapper getUnwrapper(WrapAlg aWrapAlg) throws CryptoException;
    KeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException;
    KeyFactory getKeyFactory() throws CryptoException;
    KeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg) throws CryptoException;
    RandomGenerator getRandomGenerator();
    boolean isFipsMode();
    void destroyProvider();
}
