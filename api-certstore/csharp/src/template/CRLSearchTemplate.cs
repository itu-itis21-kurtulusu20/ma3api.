using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.template
{
    public class CRLSearchTemplate
    {
        private byte[] mValue = null;
        private byte[] mHash = null;
        private OzetTipi mHashType = null;
        private byte[] mIssuer = null;
        private byte[] mSILNumber = null;
        private byte[] mBaseSILNumber = null;
        //private DateTime? mThisUpdate = null;
        private DateTime? mPublishedAfter;
        private DateTime? mPublishedBefore;

        private DateTime? mValidAt = null;

        //private DateTime? mNextUpDate = null;
        private long? mDizinNo = null;

        public void setValue(byte[] aValue)
        {
            mValue = aValue;
        }

        public void setHash(byte[] aHash)
        {
            mHash = aHash;
        }

        public void setHash(String aHash)
        {
            byte[] hash = null;
            try
            {
                hash = Convert.FromBase64String(aHash);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Hash degeri base64 decode edilirken hata olustu.", aEx);
            }
            mHash = hash;
        }

        public void setHashType(OzetTipi aOzetTipi)
        {
            mHashType = aOzetTipi;
        }

        public void setIssuer(byte[] aIssuer)
        {            
            mIssuer = aIssuer;
        }


        public void setIssuer(EName aIssuer)
        {
            mIssuer = CertStoreUtil.getNormalizeName(aIssuer.getObject());
        }

        public void setSILNumber(byte[] aSILNumber)
        {
            mSILNumber = aSILNumber;
        }

        public void setSILNumber(Asn1BigInteger aSILNumber)
        {
            mSILNumber = aSILNumber.mValue.GetData();
        }

        public void setBaseSILNumber(byte[] aBaseSILNumber)
        {
            mBaseSILNumber = aBaseSILNumber;
        }

        public void setBaseSILNumber(Asn1BigInteger aBaseSILNumber)
        {
            mBaseSILNumber = aBaseSILNumber.mValue.GetData();
        }

        public void setValidAt(DateTime? aThisUpdate)
        {
            mValidAt = aThisUpdate;
        }

        public void setPublishedAfter(DateTime? aPublishedAfter)
        {
            mPublishedAfter = aPublishedAfter;
        }

        public void setPublishedBefore(DateTime? aPublishedBefore)
        {
            mPublishedBefore = aPublishedBefore;
        }        

        public void setDizin(DepoDizin aDizin)
        {
            mDizinNo = aDizin.getDizinNo();
        }

        public void setDizin(long aDizinNo)
        {
            mDizinNo = aDizinNo;
        }

        public byte[] getValue()
        {
            return mValue;
        }

        public byte[] getHash()
        {
            return mHash;
        }

        public OzetTipi getHashType()
        {
            return mHashType;
        }

        public byte[] getIssuer()
        {
            return mIssuer;
        }

        public byte[] getSILNumber()
        {
            return mSILNumber;
        }

        public byte[] getBaseSILNumber()
        {
            return mBaseSILNumber;
        }

        public DateTime? getValidAt()
        {
            return mValidAt;
        }

        public DateTime? getPublishedAfter()
        {
            return mPublishedAfter;
        }

        public DateTime? getPublishedBefore()
        {
            return mPublishedBefore;
        }        

        public long? getDizinNo()
        {
            return mDizinNo;
        }
    }
}
