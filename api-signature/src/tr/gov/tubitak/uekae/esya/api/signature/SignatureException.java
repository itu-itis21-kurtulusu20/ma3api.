package tr.gov.tubitak.uekae.esya.api.signature;

/**
 * Base exception class for signature API related errors.
 * @author ayetgin
 */
public class SignatureException extends Exception
{
    public SignatureException()
    {
    }

    public SignatureException(String aMessage)
    {
        super(aMessage);
    }

    public SignatureException(Throwable aCause)
    {
        super(aCause);
    }

    public SignatureException(String aMessage, Throwable cause)
    {
        super(aMessage, cause);
    }


}
