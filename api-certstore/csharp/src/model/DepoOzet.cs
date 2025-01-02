
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoOzet
    {
        private long? mHashNo;
        private int? mObjectType;
        private long? mObjectNo;
        private int mHashType;
        private byte[] mHashValue;

        public long? getHashNo()
        {
            return mHashNo;
        }

        public void setHashNo(long? aHashNo)
        {
            mHashNo = aHashNo;
        }

        public int? getObjectType()
        {
            return mObjectType;
        }
        
        public void setObjectType(int? aObjectType)
        {
            mObjectType = aObjectType;
        }

        public void setObjectType(long? aObjectType)
        {
            mObjectType = (int?)aObjectType;
        }

        public long? getObjectNo()
        {
            return mObjectNo;
        }

        public void setObjectNo(long? aObjectNo)
        {
            mObjectNo = aObjectNo;
        }

        public int getHashType()
        {
            return mHashType;
        }

        public void setHashType(int aHashType)
        {
            mHashType = aHashType;
        }

        public void setHashType(long aHashType)
        {
            mHashType = (int)aHashType;
        }
        public byte[] getHashValue()
        {
            return mHashValue;
        }

        public void setHashValue(byte[] aHashValue)
        {
            mHashValue = aHashValue;
        }
    }
}
