using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public abstract class Encryptor : Cipher
    {
        public Encryptor(CipherAlg aCipherAlg) : base(aCipherAlg) { }
        public override bool isEncryptor()
        {
            return true;
        }
        //public abstract int getBlockSize();
    }
}
