using System;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 *
 * @author M. Serdar SORAN
 * @version 1.0
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.exceptions
{
    public class CryptoRuntimeException : ESYARuntimeException
    {
        public CryptoRuntimeException()
            : base(Resource.message(Resource.KRIPTO_GENEL_HATA)) { }

        public CryptoRuntimeException(String aMesaj)
            : base(aMesaj) { }

        public CryptoRuntimeException(String aMesaj, Exception aEx)
            : base(aMesaj, aEx) { }
       
    }
}
