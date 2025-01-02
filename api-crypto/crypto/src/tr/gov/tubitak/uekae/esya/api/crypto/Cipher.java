package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.Key;

/**
 * This class provides the functionality of a cryptographic cipher for encryption and decryption.
 * 
 * @author ayetgin
 */

public abstract class Cipher implements BaseCipher
{
	public abstract byte[] doFinal(byte[] aData) throws CryptoException;
	
    /**
     * @param aKey that will be used for cipher process
     * @throws CryptoException if key is not suitable
     */
	public abstract void init(Key aKey, AlgorithmParams aParams) throws CryptoException;

    /**
     * @param aKey material that will be used for cipher process.
     * @throws CryptoException if key is not suitable
     */
	public abstract void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException;


    /**
     * Reset underlying cipher for reuse 
     * @throws CryptoException if init fails
     */
	public abstract void reset() throws CryptoException;



    /**
     * Process subsequent block of data
     * @param aData to be processed. If underlying cipher processes fixed amount on block than this data length must be
     *      multiple of block size.
     * @return processed data.
     * @throws CryptoException if anything goes wrong 
     */
	public abstract byte[] process(byte[] aData) throws CryptoException;


    /**
     * @return block size for the cipher.
     */
	public abstract int getBlockSize();

    /**
     *  @return if this cipher is for either encription or decryption process
     */
	public abstract boolean isEncryptor();
	
	public abstract CipherAlg getCipherAlgorithm();
	
	
	public String getCipherAlgorithmStr()
	{
		return getCipherAlgorithm().getName();
	}

}
