package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import java.io.Serializable;

public class PhoneNumberAndOperator implements UserIdentifier, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String mPhoneNumber;
	Operator mOperator;
	
	public PhoneNumberAndOperator(String aPhoneNumber, Operator aOperator)
	{
		mPhoneNumber = aPhoneNumber;
		mOperator = aOperator;
	}
	
	public String getPhoneNumber()
	{
		return mPhoneNumber;
	}
	
	public Operator getOperator()
	{
		return mOperator;
	}
	
	
}

