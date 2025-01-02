package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

public class DepoOzet {
	
	private Long mHashNo;
	private Integer mObjectType;
	private Long mObjectNo;
	private int mHashType;
	private byte[] mHashValue;
	
	public Long getHashNo()
	{
		return mHashNo;
	}
	
	public void setHashNo(Long aHashNo)
	{
		mHashNo = aHashNo;
	}
	
	public Integer getObjectType()
	{
		return mObjectType;
	}
	
	public void setObjectType(Integer aObjectType)
	{
		mObjectType = aObjectType;
	}
	
	
	public Long getObjectNo()
	{
		return mObjectNo;
	}
	
	public void setObjectNo(Long aObjectNo)
	{
		mObjectNo = aObjectNo;
	}
	
	public int getHashType()
	{
		return mHashType;
	}
	
	public void setHashType(Integer aHashType)
	{
		mHashType = aHashType;
	}
	
	public byte[] getHashValue()
	{
		return mHashValue;
	}
	
	public void setHashValue(byte[] aHashValue)
	{
		mHashValue = aHashValue;
	}

}
