package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;


public class SmartCardException extends ESYAException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public SmartCardException(Exception ex)
	{
		super(ex);
	}
	
	public SmartCardException (String mesaj)
    {
         super(mesaj);
    }

	public SmartCardException (String mesaj, Throwable ex)
	{
		super(mesaj, ex);
	}

	public SmartCardException (String mesajWithParams, Throwable ex, Object...args)
	{
		super(mesajWithParams, ex, args);
	}

	public SmartCardException (String mesajWithparams, Object...args)
	{
		super(mesajWithparams, args);
	}
}
