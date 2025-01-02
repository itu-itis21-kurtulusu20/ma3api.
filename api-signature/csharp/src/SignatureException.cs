using System;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
 * Base exception class for signature API related errors.
 * @author ayetgin
 */
    public class SignatureException : Exception
    {
        public SignatureException()
        {
        }

        public SignatureException(String aMessage)
            : base(aMessage)
        {
        }
        
        public SignatureException(Exception aCause)
            : base(null,aCause)
        {
        }

        public SignatureException(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {
        }
    }
}
