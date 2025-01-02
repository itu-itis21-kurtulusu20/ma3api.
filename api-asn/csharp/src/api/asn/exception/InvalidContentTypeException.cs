
using System;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.asn.exception
{
    public class InvalidContentTypeException : ESYAException
    {

        public InvalidContentTypeException(String aMessage) : base(aMessage)
        {
            
        }

        public InvalidContentTypeException(String aMessage, Exception aCause) : base(aMessage, aCause)
        {
            
        }

    }
}
