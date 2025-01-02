package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;



/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */

public class ArgErrorException
    extends CryptoException
{

     public ArgErrorException ()
     {
          super(GenelDil.mesaj(GenelDil.ARGUMAN_BILINMIYOR)); //Ver\u0131len arguman bilinmiyor
     }


     public ArgErrorException (String mesaj)
     {
          super(mesaj);
     }


     public ArgErrorException (String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }

}