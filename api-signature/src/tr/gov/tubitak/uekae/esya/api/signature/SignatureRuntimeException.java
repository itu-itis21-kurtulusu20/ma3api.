package tr.gov.tubitak.uekae.esya.api.signature;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

/**
 * @author ayetgin
 */
public class SignatureRuntimeException extends ESYARuntimeException
{
    public SignatureRuntimeException()
    {
    }

    public SignatureRuntimeException(String aMessage)
    {
        super(aMessage);
    }

    public SignatureRuntimeException(String aMessage, Throwable aCause)
    {
        super(aMessage, aCause);
    }

    public SignatureRuntimeException(Throwable aCause)
    {
        super(aCause);
    }
}
