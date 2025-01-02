using System;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoSIL
    {
        private long? mSILNo;
        private DateTime? mEklenmeTarihi;
        private byte[] mValue;
        //private byte[] mHash;
        private byte[] mIssuerName;
        private DateTime? mThisUpdate;
        private DateTime? mNextUpdate;
        private byte[] mSILNumber;
        private byte[] mBaseSILNumber;
        private CRLType mSILTipi;

        public long? getSILNo()
        {
            return mSILNo;
        }
        public void setSILNo(long? aSILNo)
        {
            mSILNo = aSILNo;
        }

        public DateTime? getEklenmeTarihi()
        {
            return mEklenmeTarihi;
        }
        public void setEklenmeTarihi(DateTime? aEklenmeTarihi)
        {
            mEklenmeTarihi = aEklenmeTarihi;
        }

        public byte[] getValue()
        {
            return mValue;
        }
        public void setValue(byte[] aValue)
        {
            mValue = aValue;
        }

        /*public byte[] getmHash()
        {
            return mHash;
        }
        public void setmHash(byte[] aHash)
        {
            mHash = aHash;
        }*/

        public byte[] getIssuerName()
        {
            return mIssuerName;
        }
        public void setIssuerName(byte[] aIssuerName)
        {
            mIssuerName = aIssuerName;
        }

        public DateTime? getThisUpdate()
        {
            return mThisUpdate;
        }
        public void setThisUpdate(DateTime? aThisUpdate)
        {
            mThisUpdate = aThisUpdate;
        }

        public DateTime? getNextUpdate()
        {
            return mNextUpdate;
        }
        public void setNextUpdate(DateTime? aNextUpdate)
        {
            mNextUpdate = aNextUpdate;
        }

        public byte[] getSILNumber()
        {
            return mSILNumber;
        }
        public void setSILNumber(byte[] aSILNumber)
        {
            mSILNumber = aSILNumber;
        }

        public byte[] getBaseSILNumber()
        {
            return mBaseSILNumber;
        }
        public void setBaseSILNumber(byte[] aBaseSILNumber)
        {
            mBaseSILNumber = aBaseSILNumber;
        }

        public CRLType getSILTipi()
        {
            return mSILTipi;
        }
        public void setSILTipi(CRLType aSILTipi)
        {
            mSILTipi = aSILTipi;
        }
    }
}
