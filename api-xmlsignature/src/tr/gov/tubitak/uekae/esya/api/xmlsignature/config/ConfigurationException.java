package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 * @author ahmety
 * date: Dec 11, 2009
 */
public class ConfigurationException extends tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException
{

    public ConfigurationException(String aMessageId)
    {
        super(I18n.translate(aMessageId));
    }

    public ConfigurationException(Throwable aCause, String aMessageId)
    {
        super(I18n.translate(aMessageId), aCause);
    }

    public ConfigurationException(String aMessageId, Object... aMessageArgs)
    {
        super(I18n.translate(aMessageId, aMessageArgs));
    }

    public ConfigurationException(Throwable aCause, String aMessageId, Object... aMessageArgs)
    {
        super(I18n.translate(aMessageId, aMessageArgs), aCause);
    }
}
