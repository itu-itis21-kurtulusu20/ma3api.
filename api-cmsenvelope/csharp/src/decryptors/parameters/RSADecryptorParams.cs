//@ApiClass

using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters
{
    public class RSADecryptorParams : IDecryptorParams
    {
        private readonly byte[] encryptedKey;
        private EAlgorithmIdentifier algorithmIdentifier;

        public RSADecryptorParams(byte[] encryptedKey)
        {
            this.encryptedKey = encryptedKey;
        }

        public byte[] getEncryptedKey()
        {
            return encryptedKey;
        }

        public EAlgorithmIdentifier GetAlgorithmIdentifier()
        {
            return algorithmIdentifier;
        }

        public void setAlgorithmIdentifier(EAlgorithmIdentifier algorithmIdentifier)
        {
            this.algorithmIdentifier = algorithmIdentifier;
        }

        public Pair<CipherAlg, IAlgorithmParams> getCipherAlg()
        {
            return CipherAlg.fromAlgorithmIdentifier(GetAlgorithmIdentifier());
        }
    }
}
