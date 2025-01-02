package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;



/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class UnknownElement
    extends CryptoException
{

     public UnknownElement ()
     {
          super(GenelDil.mesaj(GenelDil.ARGUMAN_BILINMIYOR)); //Ver\u0131len arguman bilinmiyor
     }


     public UnknownElement (String mesaj)
     {
          super(mesaj);
     }


     public UnknownElement (String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }

}