package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import java.sql.Date;

public class DepoSilinecekKokSertifika
{
	private Long mKokSertifikaNo;
	private Date mKokEklenmeTarihi;
	private byte[] mValue;
	private byte[] mSerialNumber;
	private byte[] mIssuerName;
	private byte[] mSubjectName;
	private byte[] mSatirImzasi;
	
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
	
	public byte[] getSubjectName()
    {
    	return mSubjectName;
    }
	public void setSubjectName(byte[] aSubjectName)
    {
    	mSubjectName = aSubjectName;
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
