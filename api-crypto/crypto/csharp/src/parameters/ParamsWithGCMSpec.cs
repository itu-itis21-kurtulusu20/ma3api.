using System.IO;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithGCMSpec : ParamsWithIV
    {

        private readonly MemoryStream mAAD;
        protected byte[] mTag;

        public ParamsWithGCMSpec(byte[] aIV):this(aIV, null)
        {     
        }

        public ParamsWithGCMSpec(byte[] aIV, MemoryStream aAAD):base(aIV)
        {    
            mAAD = aAAD;
        }

        public ParamsWithGCMSpec(byte[] aIV, MemoryStream aAAD, byte[] aTag):base(aIV)
        {          
            mAAD = aAAD;
            mTag = aTag;
        }

        public MemoryStream getAAD()
        {
            return mAAD;
        }
      
        public byte[] getTag()
        {
            return mTag;
        }

        public void setTag(byte[] aTag)
        {
            mTag = aTag;
        }

        public override byte[] getEncoded()
        {
            throw new ESYARuntimeException("getEncoded is not applicable for GCM params");
        }
    }
}
