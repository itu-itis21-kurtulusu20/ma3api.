package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

public class DepoNitelikSertifikasi {
	private Long mNitelikSertifikaNo;
	private Long mSertifikaNo;
	private byte[] mValue;
	private String mHolderDNName;
	
	public Long getNitelikSertifikaNo()
	{
		return mNitelikSertifikaNo;
	}
	public void setNitelikSertifikaNo(Long aNitelikSertifikaNo)
	{
		mNitelikSertifikaNo = aNitelikSertifikaNo;
	}
	public Long getSertifikaNo()
	{
		return mSertifikaNo;
	}
	public void setSertifikaNo(Long aSertifikaNo)
	{
		mSertifikaNo = aSertifikaNo;
	}
	public byte[] getValue()
	{
		return mValue;
	}
	public void setValue(byte[] aValue)
	{
		mValue = aValue;
	}
	public String getHolderDNName()
	{
		return mHolderDNName;
	}
	public void setHolderDNName(String aHolderDNName)
	{
		mHolderDNName = aHolderDNName;
	}
	
	

	
}
