package gnu.crypto.sig.ecdsa.ecmath.exceptions;

public class EllipticCurveRuntimeException
          extends RuntimeException
{

     public EllipticCurveRuntimeException()
     {
          super();
     }


     public EllipticCurveRuntimeException(String aMessage)
     {
          super(aMessage);
     }


     public EllipticCurveRuntimeException(String aMessage, Throwable aCause)
     {
          super(aMessage, aCause);
     }


     public EllipticCurveRuntimeException(Throwable aCause)
     {
          super(aCause);
     }

}

