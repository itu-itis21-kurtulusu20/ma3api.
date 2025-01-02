package tr.gov.tubitak.uekae.esya.api.common;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class ESYARuntimeException extends RuntimeException
{

    public ESYARuntimeException()
    {
    }

    public ESYARuntimeException(String aMessage)
    {
        super(aMessage);
    }

    public ESYARuntimeException(String aMessage, Throwable aCause)
    {
        super(aMessage, aCause);
    }

    public ESYARuntimeException(Throwable aCause)
    {
        super(aCause);
    }

}