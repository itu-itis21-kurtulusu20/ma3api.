using System;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.exceptions
{
    public class UnknownElement : CryptoException
    {
        public UnknownElement() : base(Resource.message(Resource.ARGUMAN_BILINMIYOR)) { }
        public UnknownElement(String mesaj) : base(mesaj) { }
        public UnknownElement(String mesaj, Exception ex) : base(mesaj, ex) { }
    }
}
