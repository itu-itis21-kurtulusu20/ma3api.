using System;
using tr.gov.tubitak.uekae.esya.api.common;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class SmartCardException : ESYAException
    {
        public SmartCardException(ESYAException ex)
            : base(ex)
        { }
        public SmartCardException(String mesaj) : base(mesaj) { }

        public SmartCardException(String mesaj, Exception ex) : base(mesaj, ex) { }

        public SmartCardException(String mesaj, String[] args) : base(mesaj) { }

    }
}
