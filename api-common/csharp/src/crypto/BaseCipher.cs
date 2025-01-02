using System;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common.crypto
{
    /**
 * Interface for cipher implementations
 */
    public interface BaseCipher
    {
        /**
 *  Encrypts or decrypts data depending on the implementation.
 * @param aData input to be encrypted/decrypted
 * @return result of encryption/decryption
 * @throws ESYAException
 */
        byte[] doFinal(byte[] aData);
        /**
 * Returns the name of cipher algorithm. To avoid ambiguity, names must be the same with the ones in {@link Algorithms} class.
 * @return the name of cipher algorithm
 */
        String getCipherAlgorithmStr();
    }
}
