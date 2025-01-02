package tr.gov.tubitak.uekae.esya.api.crypto.exceptions;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author IH
 * @version 1.0
 */

public class NullParameterException
    extends CMSException
{
     public NullParameterException()
     {
          super("Aranan parametre değeri null");
     }


     public NullParameterException(String mesaj)
     {
          super(mesaj);
     }
}