package tr.gov.tubitak.uekae.esya.api.common.crypto;

/**
 * Interface for cipher implementations
 */
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;


public interface BaseCipher 
{
    /**
     *  Encrypts or decrypts data depending on the implementation.
     * @param aData input to be encrypted/decrypted
     * @return result of encryption/decryption
     * @throws ESYAException
     */
    byte[] doFinal(byte[] aData) throws ESYAException;
	

    /**
     * Returns the name of cipher algorithm. To avoid ambiguity, names must be the same with the ones in {@link Algorithms} class.
     * @return the name of cipher algorithm
     */
    public String getCipherAlgorithmStr();
    
    
	
}
