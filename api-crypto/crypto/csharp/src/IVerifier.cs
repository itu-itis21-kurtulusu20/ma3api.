using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IVerifier
    {
        void init(IPublicKey aPublicKey);
        void init(IPublicKey aPublicKey, IAlgorithmParams aParams);
        void reset();
        void update(byte[] aData);
        void update(byte[] aData, int aOffset, int aLength);
        bool verifySignature(byte[] aSignature);
    }
}
