using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IMAC
    {
        void init(IKey aKey, IAlgorithmParams aParams);
        void init(byte[] aKey, IAlgorithmParams aParams);

        void process(byte[] aData);

        byte[] doFinal(byte[] aData);

        MACAlg getMACAlgorithm();

        int getMACSize();
    }
}
