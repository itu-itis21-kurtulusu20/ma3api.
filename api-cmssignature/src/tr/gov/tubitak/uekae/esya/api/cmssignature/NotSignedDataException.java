package tr.gov.tubitak.uekae.esya.api.cmssignature;

public class NotSignedDataException extends CMSSignatureException 
{
	public NotSignedDataException(String aMsg)
	{
		super(aMsg);
	}
	
	public NotSignedDataException(String aMsg, Throwable aThrowable)
	{
		super(aMsg, aThrowable);
	}
}
