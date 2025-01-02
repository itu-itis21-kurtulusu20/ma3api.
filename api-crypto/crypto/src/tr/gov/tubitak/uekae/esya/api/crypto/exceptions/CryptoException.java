package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;


/**
 * <p>Title: CC</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class CryptoException
    extends ESYAException
{

     public CryptoException(String mesaj)
     {
          super(mesaj);
     }


     public CryptoException(String mesaj, Throwable ex)
     {
          super(mesaj, ex);
     }


     public CryptoException(String mesaj, String[] args)
     {
          super(mesaj);
     }

}