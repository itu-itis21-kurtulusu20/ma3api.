package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaTipi;

import java.sql.Date;


public class DepoKokSertifika
{
	public Long mKokSertifikaNo;
	public Date mKokEklenmeTarihi;
	public byte[] mValue;
	//private byte[] mHash;
	public byte[] mSerialNumber;
	public byte[] mIssuerName;
	public Date mStartDate;
	public Date mEndDate;
	public byte[] mSubjectName;
	public String mKeyUsageStr;
	public byte[] mSubjectKeyIdentifier;
	public SertifikaTipi mKokTipi;
	public GuvenlikSeviyesi mKokGuvenSeviyesi;
	public byte[] mSatirImzasi;
	
	
	public Long getKokSertifikaNo()
    {
    	return mKokSertifikaNo;
    }
	public void setKokSertifikaNo(Long aKokSertifikaNo)
    {
    	mKokSertifikaNo = aKokSertifikaNo;
    }
	
	public Date getKokEklenmeTarihi()
    {
    	return mKokEklenmeTarihi;
    }
	public void setKokEklenmeTarihi(Date aKokEklenmeTarihi)
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
	
	public Date getStartDate()
    {
    	return mStartDate;
    }
	public void setStartDate(Date aStartDate)
    {
    	mStartDate = aStartDate;
    }
	
	public Date getEndDate()
    {
    	return mEndDate;
    }
	public void setEndDate(Date aEndDate)
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
	
	public SertifikaTipi getKokTipi()
    {
    	return mKokTipi;
    }
	public void setKokTipi(SertifikaTipi aKokTipi)
    {
    	mKokTipi = aKokTipi;
    }
	
	public GuvenlikSeviyesi getKokGuvenSeviyesi()
    {
    	return mKokGuvenSeviyesi;
    }
	public void setKokGuvenSeviyesi(GuvenlikSeviyesi aKokGuvenSeviyesi)
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
