using System;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    /**
     * This class provides the functionality of a cryptographic cipher for encryption and decryption.
     * 
     * @author ayetgin
     */
    public abstract class Cipher : BaseCipher
    {
        public abstract byte[] doFinal(byte[] aData);

        /**
         * @param aKey that will be used for cipher process
         * @throws CryptoException if key is not suitable
         */
        public abstract void init(IKey aKey, IAlgorithmParams aParams);

        /**
         * @param aKey material that will be used for cipher process.
         * @throws CryptoException if key is not suitable
         */
        public abstract void init(byte[] aKey, IAlgorithmParams aParams);

        /**
        * Reset underlying cipher for reuse 
        * @throws CryptoException if init fails
        */
        public abstract void reset();

        /**
         * Process subsequent block of data
         * @param aData to be processed. If underlying cipher processes fixed amount on block than this data length must be
         *      multiple of block size.
         * @return processed data.
         * @throws CryptoException if anything goes wrong 
         */
        public abstract byte[] process(byte[] aData);


        /**
         * @return block size for the cipher.
         */
        public abstract int getBlockSize();

        /**
         *  @return if this cipher is for either encription or decryption process
         */
        public abstract bool isEncryptor();

        public abstract CipherAlg getCipherAlgorithm();

        public String getCipherAlgorithmStr()
        {
            return getCipherAlgorithm().getName();
        }


    }
}
