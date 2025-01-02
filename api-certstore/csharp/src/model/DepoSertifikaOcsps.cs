using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoSertifikaOcsps
    {
        private long? mOcspNo;
        private long? mSertifikaNo;
        private DateTime? mThisUpdate;
        private long? mStatus;
        private DateTime? mRevocationTime;
        private long? mRevocationReason;

        public long? getOcspNo()
        {
            return mOcspNo;
        }

        public void setOcspNo(long? aOcspNo)
        {
            mOcspNo = aOcspNo;
        }

        public long? getSertifikaNo()
        {
            return mSertifikaNo;
        }

        public void setSertifikaNo(long? aSertifikaNo)
        {
            mSertifikaNo = aSertifikaNo;
        }

        public DateTime? getThisUpdate()
        {
            return mThisUpdate;
        }

        public void setThisUpdate(DateTime? aThisUpdate)
        {
            mThisUpdate = aThisUpdate;
        }

        public long? getStatus()
        {
            return mStatus;
        }

        public void setStatus(long? aStatus)
        {
            mStatus = aStatus;
        }

        public DateTime? getRevocationTime()
        {
            return mRevocationTime;
        }

        public void setRevocationTime(DateTime? aRevocationTime)
        {
            mRevocationTime = aRevocationTime;
        }

        public long? getRevocationReason()
        {
            return mRevocationReason;
        }

        public void setRevocationReason(long? aRevocationReason)
        {
            mRevocationReason = aRevocationReason;
        }
    }
}
