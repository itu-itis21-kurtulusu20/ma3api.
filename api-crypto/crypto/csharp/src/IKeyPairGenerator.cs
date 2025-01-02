using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IKeyPairGenerator
    {
        /**
     * @param aParams to be used for key pair generation
     * @throws CryptoException if anything goes wrong
     */
        KeyPair generateKeyPair(IAlgorithmParams aParams);

    }
}
