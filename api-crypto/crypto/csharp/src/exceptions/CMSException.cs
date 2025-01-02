using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.crypto.exceptions
{
    public class CMSException : CryptoException
    {
        public CMSException()
            : base("Mode has not been selected!")
        {
            //super(GenelDil.mesaj(GenelDil.MOD_SECILMEMIS)); //Kullanilacak mod secilmemis!
        }


        public CMSException(String mesaj)
            : base(mesaj)
        {
            //super(mesaj);
        }


        public CMSException(String mesaj, Exception ex)
            : base(mesaj, ex)
        {
            //super(mesaj, ex);
        }
    }
}
