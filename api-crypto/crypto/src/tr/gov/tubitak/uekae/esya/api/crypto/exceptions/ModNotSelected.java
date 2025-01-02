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

public class ModNotSelected
    extends CryptoException
{

     public ModNotSelected ()
     {
          super(GenelDil.mesaj(GenelDil.MOD_SECILMEMIS)); //Kullanilacak mod secilmemis!
     }


     public ModNotSelected (String mesaj)
     {
          super(mesaj);
     }

}