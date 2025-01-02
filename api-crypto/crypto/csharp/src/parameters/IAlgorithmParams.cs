using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public interface IAlgorithmParams : IAlgorithmParameterSpec
    {
        byte[] getEncoded();
    }
}