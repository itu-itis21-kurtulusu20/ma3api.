package tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;

import java.util.Date;

public class OCSPSearchTemplate {
	protected byte[] mOCSPResponderID = null;
	protected byte[] mOCSPValue = null;
	protected byte[] mHash = null;
	protected OzetTipi mHashType = null;
	protected Date mProducedAtAfter = null;
	protected Date mProducedAtBefore = null;

    protected Date mProducedAt = null;

    private byte[] mCertSerialNumber;
	
	
	public void setOCSPResponderID(byte[] aOCSPResponderID)
	{
		mOCSPResponderID = aOCSPResponderID;
	}
	
	public void setOCSPValue(byte[] aValue)
	{
		mOCSPValue = aValue;
	}
	
	public void setHash(byte[] aHash)
	{
		mHash = aHash;
	}
	
	public void setHashType(OzetTipi aHashType)
	{
		mHashType = aHashType;
	}

    public void setProducedAt(Date aProducedAt) {
        this.mProducedAt = aProducedAt;
    }

    public void setProducedAtAfter(java.util.Date aDate)
	{
		mProducedAtAfter = new Date(aDate.getTime());
	}
	
	public void setProducedAtBefore(java.util.Date aDate)
	{
		mProducedAtBefore = new Date(aDate.getTime());
	}

    public void setCertSerialNumber(byte[] aCertSerialNumber)
    {
        mCertSerialNumber = aCertSerialNumber;
    }

    public byte[] getOCSPResponderID()
	{
		return mOCSPResponderID;
	}
	
	public byte[] getOCSPValue()
	{
		return mOCSPValue;
	}
	
	public byte[] getHash()
	{
		return mHash;
	}
	
	public OzetTipi getHashType()
	{
		return mHashType;
	}

    public Date getProducedAt() {
        return mProducedAt;
    }

    public Date getProducedAtAfter()
	{
		return mProducedAtAfter;
	}
	
	public Date getProducedAtBefore()
	{
		return mProducedAtBefore;
	}

    public byte[] getCertSerialNumber()
    {
        return mCertSerialNumber;
    }
}
