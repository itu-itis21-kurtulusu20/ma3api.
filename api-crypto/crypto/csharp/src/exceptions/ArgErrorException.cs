using System;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.exceptions
{
    public class ArgErrorException : CryptoException
    {
        public ArgErrorException() : base(Resource.message(Resource.ARGUMAN_BILINMIYOR)) { }
        public ArgErrorException(String mesaj) : base(mesaj) { }
        public ArgErrorException(String mesaj, Exception ex) : base(mesaj, ex) { }
    }
}
