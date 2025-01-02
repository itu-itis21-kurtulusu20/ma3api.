package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SILTipi;

import java.sql.Date;

public class DepoSIL
{
	private Long mSILNo;
	private Date mEklenmeTarihi;
	private byte[] mValue;
	//private byte[] mHash;
	private byte[] mIssuerName;
	private Date mThisUpdate;
	private Date mNextUpdate;
	private byte[] mSILNumber;
	private byte[] mBaseSILNumber;
	private SILTipi mSILTipi;
	
	public Long getSILNo()
	{
		return mSILNo;
	}
	public void setSILNo(Long aSILNo)
	{
		mSILNo = aSILNo;
	}
	
	public Date getEklenmeTarihi()
	{
		return mEklenmeTarihi;
	}
	public void setEklenmeTarihi(Date aEklenmeTarihi)
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
	
	public byte[] getIssuerName()
	{
		return mIssuerName;
	}
	public void setIssuerName(byte[] aIssuerName)
	{
		mIssuerName = aIssuerName;
	}
	
	public Date getThisUpdate()
	{
		return mThisUpdate;
	}
	public void setThisUpdate(Date aThisUpdate)
	{
		mThisUpdate = aThisUpdate;
	}
	
	public Date getNextUpdate()
	{
		return mNextUpdate;
	}
	public void setNextUpdate(Date aNextUpdate)
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
	
	public SILTipi getSILTipi()
	{
		return mSILTipi;
	}
	public void setSILTipi(SILTipi aSILTipi)
	{
		mSILTipi = aSILTipi;
	}
}
