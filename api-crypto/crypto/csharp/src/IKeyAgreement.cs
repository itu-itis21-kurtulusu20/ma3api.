using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IKeyAgreement
    {
        /**
	 * 
	 * @param aKey private key for ECDH
	 * @param aParams parameters that may be needed in the KeyGeneration
	 */
        void init(IKey aKey, IAlgorithmParams aParams);

        //TO-DO geri döndürme tipi byte []'den SecretKey'e çevrilebilir.
        /**
         * @param aKey public key for ECDH
         * @param alg the requested secret key algorithm
         * @return symmetric key bytes
         */
        byte[] generateKey(IKey aKey, IAlgorithm alg);
    }
}
