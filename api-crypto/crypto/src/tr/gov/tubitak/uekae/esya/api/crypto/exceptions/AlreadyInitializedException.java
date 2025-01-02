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

public class AlreadyInitializedException
    extends CryptoException
{

     public AlreadyInitializedException()
     {
          super(GenelDil.mesaj(GenelDil.INIT_EDILMIS)); // Daha once initialize edilmis
     }


     public AlreadyInitializedException(Throwable ex)
     {
          super(GenelDil.mesaj(GenelDil.INIT_EDILMIS), ex); // Daha once initialize edilmis
     }


     public AlreadyInitializedException(String mesaj)
     {
          super(mesaj);
     }

}