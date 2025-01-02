package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.Key;

/**
 * @author ayetgin
 */
public interface Wrapper
{
    /**
     * @param aKey to be used for cipher process
     */
    void init(Key aKey) throws CryptoException;

    void init(Key aKey, AlgorithmParams aParams) throws CryptoException;


    /**
     *
     * @param aKey to be wrapped.
     * @return wrapped key
     */
    byte[] wrap(Key aKey) throws CryptoException;

    /**
     * Unwrap Key
     * @param data
     * @return
     * @throws CryptoException
     */
    Key unwrap(byte[] data) throws CryptoException;

    /**
     * Unwrap key with specified Key Template. keyTemplateObj must be instance of KeyTemplate
     * @param data
     * @param keyTemplateObj
     * @return
     * @throws CryptoException
     */
    Key unwrap(byte[] data,Object keyTemplateObj) throws CryptoException;

    /**
     * @return if this instance wraps or unwraps.
     */
    boolean isWrapper();

}
