using System;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    public class NotSupportedException : SignatureRuntimeException
    {
        public NotSupportedException()
        {
        }

        public NotSupportedException(String aMessage)
            : base(aMessage)
        {
        }

        public NotSupportedException(String aMessage, Exception aCause)
            : base(aCause, aMessage)
        {
        }

        public NotSupportedException(Exception aCause)
            : base(aCause)
        {
        }
    }
}
