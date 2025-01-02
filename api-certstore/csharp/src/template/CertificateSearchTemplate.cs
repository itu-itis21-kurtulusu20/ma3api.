using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.template
{
    public class CertificateSearchTemplate
    {
        protected byte[] mValue = null;
        protected byte[] mHash = null;
        protected OzetTipi mHashType = null;
        protected byte[] mIssuer = null;
        protected DateTime? mStartDate = null;
        protected DateTime? mEndDate = null;
        protected byte[] mSubject = null;
        protected byte[] mSerialNumber = null;
        protected KeyUsageSearchTemplate mAnahtarKullanim = null;
        protected byte[] mSubjectKeyIdentifier = null;
        protected String mEPosta = null;
        protected String mX400Adress = null;
        protected long? mDizinNo;

        //protected StringBuilder mSorgu = new StringBuilder("");
        //protected List<Object> mParams = null;


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
            try
            {
                mHash = Convert.FromBase64String(aHash);//Base64.decode(aHash);
            }
            catch (Exception aEx)
            {
                throw new CertStoreException("Hash degeri base64 decode edilirken hata olustu.", aEx);
            }
        }
        public void setHashType(OzetTipi aHashType)
        {
            mHashType = aHashType;
        }

        public void setIssuer(byte[] aIssuer)
        {
            mIssuer = aIssuer;
        }

        public void setIssuer(EName aIssuer)
        {
            mIssuer = mSubject = CertStoreUtil.getNormalizeName(aIssuer.getObject()); ;
        }
        public void setStartDate(DateTime? aStartDate)
        {
            mStartDate = aStartDate;
        }

        public void setEndDate(DateTime? aEndDate)
        {
            mEndDate = aEndDate;
        }


        public void setSubject(byte[] aSubject)
        {            
            mSubject = aSubject;
        }

        public void setSubject(EName aSubject)
        {
            mSubject = CertStoreUtil.getNormalizeName(aSubject.getObject());
        }

        public void setSerial(byte[] aSerialNumber)
        {
            mSerialNumber = aSerialNumber;
        }

        public void setSerial(Asn1BigInteger aSerialNumber)
        {
            mSerialNumber = aSerialNumber.mValue.GetData();
        }


        public void setAnahtarKullanimi(KeyUsageSearchTemplate aAnahtarKullanim)
        {
            mAnahtarKullanim = aAnahtarKullanim;
        }

        public void setSubjectKeyID(byte[] aSubjectKeyID)
        {
            mSubjectKeyIdentifier = aSubjectKeyID;
        }

        public void setEPosta(String aEPosta)
        {
            mEPosta = aEPosta;
        }

        public void setDizin(DepoDizin aDizin)
        {
            mDizinNo = aDizin.getDizinNo();
        }

        public void setDizin(long aDizinNo)
        {
            mDizinNo = aDizinNo;
        }

        public void setX400Adress(String aX400Address)
        {
            mX400Adress = aX400Address;
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

        public byte[] getSerialNumber()
        {
            return mSerialNumber;
        }

        public byte[] getIssuer()
        {
            return mIssuer;
        }
        public DateTime? getStartDate()
        {
            return mStartDate;
        }

        public DateTime? getEndDate()
        {
            return mEndDate;
        }

        public byte[] getSubject()
        {
            return mSubject;
        }

        public KeyUsageSearchTemplate getAnahtarKullanim()
        {
            return mAnahtarKullanim;
        }

        public String getEPosta()
        {
            return mEPosta;
        }

        public byte[] getSubjectKeyID()
        {
            return mSubjectKeyIdentifier;
        }

        public long? getDizinNo()
        {
            return mDizinNo;
        }

        public String getX400Address()
        {
            return mX400Adress;
        }

    }
}
