package tr.gov.tubitak.uekae.esya.api.cvc;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 6/14/11
 * Time: 4:07 PM
 */
public class CVCVerifierException extends ESYAException {
    public CVCVerifierException (String mesaj)
     {
          super(mesaj);
     }

     public CVCVerifierException (String mesaj, Throwable cause)
     {
          super(mesaj, cause);
     }
}
