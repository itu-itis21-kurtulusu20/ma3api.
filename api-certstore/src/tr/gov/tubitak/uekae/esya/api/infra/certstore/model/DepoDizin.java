package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import java.sql.Date;

public class DepoDizin
{
	private Long mDizinNo;
	private Date mEklenmeTarihi;
	private String mDizinAdi;
	
	public Long getDizinNo()
	{
		return mDizinNo;
	}
	public void setDizinNo(Long aDizinNo)
	{
		mDizinNo = aDizinNo;
	}
	
	public Date getEklenmeTarihi()
	{
		return mEklenmeTarihi;
	}
	
	public void setEklenmeTarihi(Date aEklenmeTarihi)
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