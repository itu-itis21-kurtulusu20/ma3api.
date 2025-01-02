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

public class NotInitializedException
    extends CryptoException
{

     public NotInitializedException()
     {
          super(GenelDil.mesaj(GenelDil.INIT_EDILMEMIS)); //"Initialize" edilmeden kullanilmak isteniyor!
     }


     public NotInitializedException(String mesaj)
     {
          super(mesaj);
     }


     public NotInitializedException(String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }

}