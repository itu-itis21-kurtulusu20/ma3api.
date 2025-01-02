using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoDizin
    {        
        private long? mDizinNo;
        private DateTime? mEklenmeTarihi;
        private String mDizinAdi;

        public long? getDizinNo()
        {
            return mDizinNo;
        }
        public void setDizinNo(long? aDizinNo)
        {
            mDizinNo = aDizinNo;
        }

        public DateTime? getEklenmeTarihi()
        {
            return mEklenmeTarihi;
        }

        public void setEklenmeTarihi(DateTime? aEklenmeTarihi)
        {
            mEklenmeTarihi = aEklenmeTarihi;
        }

        public String getDizinAdi()
        {
            return mDizinAdi;
        }
        public void setDizinAdi(String aDizinAdi)
        {
            mDizinAdi = aDizinAdi;
        }

    }
}
