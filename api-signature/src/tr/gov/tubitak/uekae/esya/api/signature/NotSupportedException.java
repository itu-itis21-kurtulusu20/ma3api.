package tr.gov.tubitak.uekae.esya.api.signature;

/**
 * @author ayetgin
 */
public class NotSupportedException extends SignatureRuntimeException
{
    public NotSupportedException()
    {
    }

    public NotSupportedException(String aMessage)
    {
        super(aMessage);
    }

    public NotSupportedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NotSupportedException(Throwable aCause)
    {
        super(aCause);
    }
}
