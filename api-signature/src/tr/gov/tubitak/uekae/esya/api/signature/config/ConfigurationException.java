package tr.gov.tubitak.uekae.esya.api.signature.config;


import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

/**
 * @author ayetgin
 */
public class ConfigurationException extends SignatureException
{

    public ConfigurationException(String aMessage)
    {
        super(aMessage);
    }

    public ConfigurationException(String aMessage, Throwable aCause)
    {
        super(aMessage, aCause);
    }


}
