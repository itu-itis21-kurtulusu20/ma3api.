using System;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public class NullParameterException : CMSSignatureException
    {
        public NullParameterException()
            : base("Aranan parametre değeri null")
        {            
        }

        public NullParameterException(String mesaj):base(mesaj)
        {            
        }
    }
}
