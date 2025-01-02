using System;
using tr.gov.tubitak.uekae.esya.api.common;
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
    public class CryptoException : ESYAException
    {
        public CryptoException(String mesaj) : base(mesaj) { }

        public CryptoException(String mesaj, Exception ex) : base(mesaj, ex) { }

        public CryptoException(String mesaj, String[] args) : base(mesaj) { }

    }
}
