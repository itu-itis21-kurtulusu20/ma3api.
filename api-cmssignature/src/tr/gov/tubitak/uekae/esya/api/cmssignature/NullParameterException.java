package tr.gov.tubitak.uekae.esya.api.cmssignature;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author IH
 * @version 1.0
 */
@SuppressWarnings("serial")

public class NullParameterException
    extends CMSSignatureException
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