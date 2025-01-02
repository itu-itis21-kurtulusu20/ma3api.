package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 * @author ayetgin
 */
public class XMLSignatureRuntimeException extends SignatureRuntimeException
{

    public XMLSignatureRuntimeException(String message)
    {
        super(I18n.translate(message));
    }

    public XMLSignatureRuntimeException(Throwable aCause, String aMessage)
    {
        super(I18n.translate(aMessage), aCause);
    }


    public XMLSignatureRuntimeException(String aMessage,  Object... aMessageArgs)
    {
        super(I18n.translate(aMessage, aMessageArgs));
    }

    public XMLSignatureRuntimeException(Throwable aCause, String aMessage,  Object... aMessageArgs)
    {
        super(I18n.translate(aMessage, aMessageArgs), aCause);
    }

}
