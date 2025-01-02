package tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

import java.sql.Date;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;

import com.objsys.asn1j.runtime.Asn1BigInteger;

public class CRLSearchTemplate
{
	private byte[] mValue = null;
	private byte[] mHash = null;
	private OzetTipi mHashType = null;
	private byte[] mIssuer = null;
	private byte[] mSILNumber = null;
	private byte[] mBaseSILNumber = null;
    private Date mPublishedAfter;
    private Date mPublishedBefore;
    private Date mValidAt = null;
	private Long mDizinNo = null;
	
	
	public void setValue(byte[] aValue)
	{
		mValue = aValue;
	}
	
	public void setHash(byte[] aHash)
	{
		mHash = aHash;
	}
	
	public void setHash(String aHash)
	throws CertStoreException
	{
		byte[] hash = null;
		try
		{
			hash = Base64.decode(aHash);
		}
		catch(Exception aEx)
		{
			throw new CertStoreException("Hash degeri base64 decode edilirken hata olustu.",aEx);
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
		mSILNumber = aSILNumber.value.toByteArray();
	}
	
	public void setBaseSILNumber(byte[] aBaseSILNumber)
	{
		mBaseSILNumber = aBaseSILNumber;
	}
	
	public void setBaseSILNumber(Asn1BigInteger aBaseSILNumber)
	{
		mBaseSILNumber = aBaseSILNumber.value.toByteArray();
	}
	
	public void setValidAt(java.util.Date aThisUpdate)
	{
		mValidAt = new java.sql.Date(aThisUpdate.getTime());
	}

    public void setPublishedAfter(java.util.Date aPublishedAfter)
    {
        mPublishedAfter = new java.sql.Date(aPublishedAfter.getTime());
    }

    public void setPublishedBefore(java.util.Date aPublishedBefore)
    {
        mPublishedBefore = new java.sql.Date(aPublishedBefore.getTime());
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
	
	public Date getValidAt()
	{
		return mValidAt;
	}

    public Date getPublishedAfter()
    {
        return mPublishedAfter;
    }

    public Date getPublishedBefore()
    {
        return mPublishedBefore;
    }

	public Long getDizinNo()
	{
		return mDizinNo;
	}
	
}
