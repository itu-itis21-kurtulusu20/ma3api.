package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author IH
 * @version 1.0
 */

public class CMSException
    extends CryptoException
{

     public CMSException ()
     {
          super(GenelDil.mesaj(GenelDil.MOD_SECILMEMIS)); //Kullanilacak mod secilmemis!
     }


     public CMSException (String mesaj)
     {
          super(mesaj);
     }


     public CMSException (String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }

}