package tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

import java.sql.Date;


public class DepoOCSP {

	private Long mOCSPNo;
	private byte[] mOCSPResponderID;
	private Date mOCSPProducedAt;
	private byte[] mBasicOCSPResponse;
	
	public Long getOCSPNo()
	{
		return mOCSPNo;
	}
	
	public void setOCSPNo(Long aOCSPNo)
	{
		mOCSPNo = aOCSPNo;
	}
	
	public byte[] getOCSPResponderID()
	{
		return mOCSPResponderID;
	}
	
	public void setOCSPResponderID(byte[] aOCSPResponderID)
	{
		mOCSPResponderID = aOCSPResponderID;
	}
	
	public Date getOCSPProducedAt()
	{
		return mOCSPProducedAt;
	}
	
	public void setOCSPProducedAt(Date aOCSPProducedAt)
	{
		mOCSPProducedAt = aOCSPProducedAt;
	}
	
	public byte[] getBasicOCSPResponse()
	{
		return mBasicOCSPResponse;
	}
	
	public void setBasicOCSPResponse(byte[] aBasicOCSPResponse)
	{
		mBasicOCSPResponse = aBasicOCSPResponse;
	}
	
}
