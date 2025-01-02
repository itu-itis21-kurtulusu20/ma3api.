using System;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    public class ConfigurationException : SignatureException
    {
        public ConfigurationException(String aMessageId)
            : base(aMessageId)
        {
        }

        public ConfigurationException(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {
        }
    }
}
