
using System;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    public class NotEnvelopeDataException : CMSException
    {
        public NotEnvelopeDataException(String msg) : base(msg)
        {
            
        }

        public NotEnvelopeDataException(String msg, Exception e) : base(msg, e)
        {
            
        }
    }
}
