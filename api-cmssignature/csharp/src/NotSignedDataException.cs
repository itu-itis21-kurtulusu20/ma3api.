using System;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public class NotSignedDataException : CMSSignatureException
    {
        public NotSignedDataException(String aMsg)
            : base(aMsg)
        {
        }

        public NotSignedDataException(String aMsg, Exception aThrowable)
            : base(aMsg, aThrowable)
        {
        }
    }
}
