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
    public class AlreadyInitializedException : CryptoException
    {
        public AlreadyInitializedException() : base(Resource.message(Resource.INIT_EDILMIS)) { }
        public AlreadyInitializedException(Exception ex) : base(Resource.message(Resource.INIT_EDILMIS), ex) { }
        public AlreadyInitializedException(String mesaj) : base(mesaj) { }
    }
}
