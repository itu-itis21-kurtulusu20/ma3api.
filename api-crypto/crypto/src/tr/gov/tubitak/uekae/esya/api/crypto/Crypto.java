package tr.gov.tubitak.uekae.esya.api.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;

/**
 * Central class for Crypto operations.
 *
 * @author ayetgin
 */

public class Crypto
{
    private static final Logger logger = LoggerFactory.getLogger(Crypto.class);

    public static final String PROVIDER_SUN = "tr.gov.tubitak.uekae.esya.api.crypto.provider.sun.SUNCryptoProvider";
    public static final String PROVIDER_GNU = "tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUCryptoProvider";

    private static CryptoProvider mProvider;
    private static ThreadLocal<CryptoProvider> threadLocalCryptoProvider=new ThreadLocal<CryptoProvider>();

    static {
        try {

            // todo make configurative
            setProvider(PROVIDER_GNU);
        }
        catch (Throwable x){
            x.printStackTrace();
            logger.error("Cant init crpto!", x);
        }
    }
    /**
     * Set Crypto provider with crypto provider
     * @param aProvider
     */
    public static void setProvider(CryptoProvider aProvider){
        mProvider = aProvider;
    }
    /**
     * Set Crypto provider with Fully Qualified ClassName
     * @param aFullyQualifiedClassName
     */
    public static void setProvider(String aFullyQualifiedClassName) throws CryptoException {
        try {
            mProvider = (CryptoProvider)Class.forName(aFullyQualifiedClassName).newInstance();
        } catch (Throwable t){
            throw new CryptoException("Cant set crypto provider : "+aFullyQualifiedClassName, t);
        }
        threadLocalCryptoProvider.set(mProvider);
    }

    public static void setThreadCryptoProvider(CryptoProvider cryptoProvider){
        threadLocalCryptoProvider.set(cryptoProvider);
    }

    public static CryptoProvider getProvider(){
        if(threadLocalCryptoProvider !=null && threadLocalCryptoProvider.get()!=null){
            return threadLocalCryptoProvider.get();
        }
        return mProvider;
    }

    public static boolean isFipsMode(){
        return mProvider.isFipsMode();
    }

    public static Digester getDigester(DigestAlg aDigestAlg) throws CryptoException
    {
        return getProvider().getDigester(aDigestAlg);
    }

    public static BufferedCipher getEncryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        return new BufferedCipher(getProvider().getEncryptor(aCipherAlg));
    }

    public static BufferedCipher getDecryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        return new BufferedCipher(getProvider().getDecryptor(aCipherAlg));
    }

    public static Signer getSigner(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return getProvider().getSigner(aSignatureAlg);
    }

    public static Verifier getVerifier(SignatureAlg aSignatureAlg) throws CryptoException
    {
        return getProvider().getVerifier(aSignatureAlg);
    }

    public static MAC getMAC(MACAlg aMACAlg) throws CryptoException
    {
        return getProvider().getMAC(aMACAlg);
    }

    public static Wrapper getWrapper(WrapAlg aWrapAlg) throws CryptoException{
        return getProvider().getWrapper(aWrapAlg);
    }

    public static Wrapper getUnwrapper(WrapAlg aWrapAlg) throws CryptoException{
        return getProvider().getUnwrapper(aWrapAlg);
    }

    public static KeyFactory getKeyFactory() throws CryptoException
    {
        return getProvider().getKeyFactory();
    }

    public static KeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException
    {
        return getProvider().getKeyAgreement(aKeyAgreementAlg);
    }

    public static KeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg) throws CryptoException
    {
        return getProvider().getKeyPairGenerator(aAsymmetricAlg);
    }

    public static RandomGenerator getRandomGenerator(){
        return getProvider().getRandomGenerator();
    }

}
