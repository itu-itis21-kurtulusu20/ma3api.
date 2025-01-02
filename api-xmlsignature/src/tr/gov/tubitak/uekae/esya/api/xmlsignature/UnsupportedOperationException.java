package tr.gov.tubitak.uekae.esya.api.xmlsignature;

/**
 * @author ahmety
 * date: Jul 8, 2009
 */
public class UnsupportedOperationException extends XMLSignatureException
{

    public UnsupportedOperationException(String aMessageId)
    {
        super(aMessageId);
    }

    public UnsupportedOperationException(Throwable aCause, String aMessageId)
    {
        super(aCause, aMessageId);
    }

    public UnsupportedOperationException(String aMessageId,
                                        Object... aMessageArgs)
    {
        super(aMessageId, aMessageArgs);
    }

    public UnsupportedOperationException(Throwable aCause, String aMessageId,
                                        Object... aMessageArgs)
    {
        super(aCause, aMessageId, aMessageArgs);
    }
}
