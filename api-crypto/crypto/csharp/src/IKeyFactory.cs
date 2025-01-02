using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using System.Security.Cryptography;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IKeyFactory
    {
        IPublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes);
        IPrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes);
        ISecretKey generateSecretKey(IKeySpec aKeySpec);

        PrivateKey convertCSharpPrivateKey(RSACryptoServiceProvider aKey);

        /**
     * Generate raw secret key
     * @param aAlg which the key is for
     * @param aBitLength in bits
     * @return raw key in bytes
     */
        byte[] generateKey(CipherAlg aAlg, int aBitLength);

        int getKeyLength(IPublicKey aKey);
    }
}
