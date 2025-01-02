using System;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.signature;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    /**
 * Base exception class for CMS-Signature API related errors.
 */
    public class CMSSignatureException : SignatureException
    {
        public CMSSignatureException()
        { }

        public CMSSignatureException(String aMessage)
            : base(aMessage)
        {
        }

        public CMSSignatureException(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {

        }

        public CMSSignatureException(Exception aCause)
            : base(aCause.Message, aCause)
        {
        }

        public override string Message
        {
            get
            {
                string msg = base.Message;
                if (InnerException is CertificateValidationException)
                {
                    msg = msg + " " + InnerException.Message;
                }
                return msg;
            }
        }
    }
}
