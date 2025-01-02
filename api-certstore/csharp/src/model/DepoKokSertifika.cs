using System;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.model
{
    public class DepoKokSertifika
    {
        public long? mKokSertifikaNo;
        public DateTime? mKokEklenmeTarihi;
        public byte[] mValue;
        //private byte[] mHash;
        public byte[] mSerialNumber;
        public byte[] mIssuerName;
        public DateTime? mStartDate;
        public DateTime? mEndDate;
        public byte[] mSubjectName;
        public String mKeyUsageStr;
        public byte[] mSubjectKeyIdentifier;
        public CertificateType mKokTipi;
        public SecurityLevel mKokGuvenSeviyesi;
        public byte[] mSatirImzasi;


        public long? getKokSertifikaNo()
        {
            return mKokSertifikaNo;
        }
        public void setKokSertifikaNo(long? aKokSertifikaNo)
        {
            mKokSertifikaNo = aKokSertifikaNo;
        }

        public DateTime? getKokEklenmeTarihi()
        {
            return mKokEklenmeTarihi;
        }
        public void setKokEklenmeTarihi(DateTime? aKokEklenmeTarihi)
        {
            mKokEklenmeTarihi = aKokEklenmeTarihi;
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

        public byte[] getSerialNumber()
        {
            return mSerialNumber;
        }
        public void setSerialNumber(byte[] aSerialNumber)
        {
            mSerialNumber = aSerialNumber;
        }

        public byte[] getIssuerName()
        {
            return mIssuerName;
        }
        public void setIssuerName(byte[] aIssuerName)
        {
            mIssuerName = aIssuerName;
        }

        public DateTime? getStartDate()
        {
            return mStartDate;
        }
        public void setStartDate(DateTime? aStartDate)
        {
            mStartDate = aStartDate;
        }

        public DateTime? getEndDate()
        {
            return mEndDate;
        }
        public void setEndDate(DateTime? aEndDate)
        {
            mEndDate = aEndDate;
        }

        public byte[] getSubjectName()
        {
            return mSubjectName;
        }
        public void setSubjectName(byte[] aSubjectName)
        {
            mSubjectName = aSubjectName;
        }

        public String getKeyUsageStr()
        {
            return mKeyUsageStr;
        }
        public void setKeyUsageStr(String aKeyUsageStr)
        {
            mKeyUsageStr = aKeyUsageStr;
        }

        public byte[] getSubjectKeyIdentifier()
        {
            return mSubjectKeyIdentifier;
        }
        public void setSubjectKeyIdentifier(byte[] aSubjectKeyIdentifier)
        {
            mSubjectKeyIdentifier = aSubjectKeyIdentifier;
        }

        public /*SertifikaTipi*/CertificateType getKokTipi()
        {
            return mKokTipi;
        }
        public void setKokTipi(/*SertifikaTipi*/CertificateType aKokTipi)
        {
            mKokTipi = aKokTipi;
        }

        public /*GuvenlikSeviyesi*/SecurityLevel getKokGuvenSeviyesi()
        {
            return mKokGuvenSeviyesi;
        }
        public void setKokGuvenSeviyesi(/*GuvenlikSeviyesi*/SecurityLevel aKokGuvenSeviyesi)
        {
            mKokGuvenSeviyesi = aKokGuvenSeviyesi;
        }

        public byte[] getSatirImzasi()
        {
            return mSatirImzasi;
        }
        public void setSatirImzasi(byte[] aSatirImzasi)
        {
            mSatirImzasi = aSatirImzasi;
        }
    }
}
