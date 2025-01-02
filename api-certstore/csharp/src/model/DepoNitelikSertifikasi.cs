using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoNitelikSertifikasi
    {

        private long? mNitelikSertifikaNo;
        private long? mSertifikaNo;
        private byte[] mValue;
        private String mHolderDNName;

        public long? getNitelikSertifikaNo()
        {
            return mNitelikSertifikaNo;
        }

        public void setNitelikSertifikaNo(long? aNitelikSertifikaNo)
        {
            mNitelikSertifikaNo = aNitelikSertifikaNo;
        }

        public long? getSertifikaNo()
        {
            return mSertifikaNo;
        }

        public void setSertifikaNo(long? aSertifikaNo)
        {
            mSertifikaNo = aSertifikaNo;
        }

        public byte[] getValue()
        {
            return mValue;
        }

        public void setValue(byte[] aValue)
        {
            mValue = aValue;
        }

        public String getHolderDNName()
        {
            return mHolderDNName;
        }

        public void setHolderDNName(String aHolderDNName)
        {
            mHolderDNName = aHolderDNName;
        }
    }
}
