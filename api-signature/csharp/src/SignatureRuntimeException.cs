using System;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    public class SignatureRuntimeException : ESYARuntimeException
    {
        public SignatureRuntimeException()
        {
        }

        public SignatureRuntimeException(String aMessage)
            : base(aMessage)
        {
        }

        public SignatureRuntimeException(Exception aCause, String aMessage)
            : base(aMessage, aCause)
        {
        }

        public SignatureRuntimeException(Exception aCause)
            : base(aCause)
        {
        }
    }
}
