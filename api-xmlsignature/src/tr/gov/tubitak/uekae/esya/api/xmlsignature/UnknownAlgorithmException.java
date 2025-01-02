package tr.gov.tubitak.uekae.esya.api.xmlsignature;

/**
 * @author ahmety
 * date: May 29, 2009
 */
public class UnknownAlgorithmException extends XMLSignatureRuntimeException
{

    public UnknownAlgorithmException(Throwable aCause,
                                        String aMessageId,
                                        Object... aParams)
    {
        super(aCause, aMessageId, aParams);
    }
}
