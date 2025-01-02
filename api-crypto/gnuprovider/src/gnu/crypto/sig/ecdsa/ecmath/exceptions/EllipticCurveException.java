package gnu.crypto.sig.ecdsa.ecmath.exceptions;

public class EllipticCurveException
          extends Exception
{

     public EllipticCurveException()
     {
          super();
     }


     public EllipticCurveException(String aMessage)
     {
          super(aMessage);
     }


     public EllipticCurveException(Throwable aCause)
     {
          super(aCause);
     }


     public EllipticCurveException(String aMessage, Throwable aCause)
     {
          super(aMessage, aCause);
     }

}
