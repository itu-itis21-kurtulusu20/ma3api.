/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithIV : IAlgorithmParams
    {
        private readonly byte[] _mIv;

        public ParamsWithIV(byte[] aIV)
        {
            _mIv = aIV;
        }

        public byte[] getIV()
        {
            return _mIv;
        }

        public virtual byte[] getEncoded()
        {
            return _mIv;
        }
    }
}
