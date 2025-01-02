package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class LoginException extends ESYAException 
{
	boolean mFinalTry;
	boolean mPinLocked;
	
	public LoginException(String aMsg, Throwable aEx, boolean aFinalTry, boolean aPinLocked)
	{
		super(aMsg, aEx);
		mFinalTry = aFinalTry;
		mPinLocked = aPinLocked;
	}
	
	public boolean isFinalTry()
	{
		return mFinalTry;
	}
	
	public boolean isPinLocked()
	{
		return mPinLocked;
	}
}
