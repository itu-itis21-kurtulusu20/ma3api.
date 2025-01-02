package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 * @author ahmety
 * date: May 8, 2009
 */
public class XMLSignatureException extends SignatureException
{

    public XMLSignatureException(String aMessageId)
    {
        super(I18n.translate(aMessageId));
    }

    public XMLSignatureException(Throwable aCause, String aMessageId)
    {
        super(I18n.translate(aMessageId), aCause);
    }

    public XMLSignatureException(String aMessageId, Object... aMessageArgs)
    {
        super(I18n.translate(aMessageId, aMessageArgs));
    }

    public XMLSignatureException(Throwable aCause, String aMessageId, Object... aMessageArgs)
    {
        super(I18n.translate(aMessageId, aMessageArgs), aCause);
    }
}
