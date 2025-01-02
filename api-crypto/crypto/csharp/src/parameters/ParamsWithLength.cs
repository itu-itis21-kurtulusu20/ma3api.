
//todo Annotation!
//@ApiClass

using System;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithLength : IAlgorithmParams
    {
        private readonly int _mLength;

        public ParamsWithLength(int aLength)
        {
            _mLength = aLength;
        }

        public int getLength()
        {
            return _mLength;
        }

        public byte[] getEncoded()
        {
            throw new NotImplementedException("Not applicable!");
        }
    }
}
