package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import tr.gov.tubitak.uekae.esya.api.crypto.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.core.PBEDecryptor;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.core.PBEEncryptor;

/**
 * @author ayetgin
 */

public class GNUCryptoProvider implements CryptoProvider
{
    public Cipher getEncryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        if (aCipherAlg instanceof PBEAlg){
            return new PBEEncryptor((PBEAlg)aCipherAlg);
        }
        if (aCipherAlg.getName().toLowerCase().startsWith("rsa")){
            return new GNUAsymmetricEncryptor(aCipherAlg);
        }
        return new GNUEncryptor(aCipherAlg);
    }

    public Cipher getDecryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        if (aCipherAlg instanceof PBEAlg){
            return new PBEDecryptor((PBEAlg)aCipherAlg);
        }
        if (aCipherAlg.getName().toLowerCase().startsWith("rsa")){
            return new GNUAsymmetricDecryptor(aCipherAlg);
        }
        return new GNUDecryptor(aCipherAlg);
    }

    public Digester getDigester(DigestAlg aDigestAlg) throws CryptoException
    {
        return new GNUDigester(aDigestAlg);
    }

    public Signer getSigner(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return new GNUSigner(aSignatureAlg);
    }

    public Verifier getVerifier(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return new GNUVerifier(aSignatureAlg);
    }

    public MAC getMAC(MACAlg aMACAlg) throws CryptoException
    {
        return new GNUMAC(aMACAlg);  
    }

    public Wrapper getWrapper(WrapAlg aWrapAlg) throws CryptoException
    {
        return new GNUAESWrapper(aWrapAlg, true);
    }

    public Wrapper getUnwrapper(WrapAlg aWrapAlg) throws CryptoException
    {
        return new GNUAESWrapper(aWrapAlg,false);
    }

    public KeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException
    {
        return new GNUKeyAgreement(aKeyAgreementAlg);
    }

    public KeyFactory getKeyFactory() throws CryptoException
    {
        return new GNUKeyFactory();  
    }

    public KeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg) throws CryptoException
    {
        return new GNUKeyPairGenerator(aAsymmetricAlg);
    }

    public RandomGenerator getRandomGenerator()
    {
        return new GNURandomGenerator();  
    }

    public boolean isFipsMode() {
        return false;
    }

    public void destroyProvider() {

    }
}
