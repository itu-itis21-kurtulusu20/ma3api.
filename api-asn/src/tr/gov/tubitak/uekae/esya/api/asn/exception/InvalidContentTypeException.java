package tr.gov.tubitak.uekae.esya.api.asn.exception;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public class InvalidContentTypeException extends ESYAException
{
    public InvalidContentTypeException(String aMsg)
    {
        super(aMsg);
    }

    public InvalidContentTypeException(String aMsg, Throwable aThrowable)
    {
        super(aMsg, aThrowable);
    }
}
