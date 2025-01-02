//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithSharedInfo : IAlgorithmParams
    {
        private readonly byte[] _mSharedInfo;

        public ParamsWithSharedInfo(byte[] aSharedInfo)
        {
            _mSharedInfo = aSharedInfo;
        }

        public byte[] getSharedInfo()
        {
            return _mSharedInfo;
        }

        public byte[] getEncoded()
        {
            return _mSharedInfo;
        }
    }
}
