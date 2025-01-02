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

public class EncodeDecodeException
    extends CryptoException
{
     public EncodeDecodeException ()
     {
          super(GenelDil.mesaj(GenelDil.ENCODEDECODE_HATASI));
     }


     public EncodeDecodeException (Throwable aEx)
     {
          super(GenelDil.mesaj(GenelDil.ENCODEDECODE_HATASI),aEx);
     }

     public EncodeDecodeException (String mesaj)
     {
          super(mesaj);
     }


     public EncodeDecodeException (String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }

}
