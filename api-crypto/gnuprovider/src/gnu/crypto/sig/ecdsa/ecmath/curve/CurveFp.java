/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package gnu.crypto.sig.ecdsa.ecmath.curve;

import java.math.BigInteger;

import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.Field;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;

/**
 * @author mss
 * 
 */
public class CurveFp
          extends Curve
{

     /**
      * @param aA
      * @param aB
      * @throws EllipticCurveException
      */
     public CurveFp(Field aField, BigInteger aA, BigInteger aB)
               throws EllipticCurveException
     {
          super(aField, aA, aB);
          if (!( mField instanceof FieldFp))
               throw new EllipticCurveException("Not a prime field element");
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.Curve#onCurve(gnu.crypto.sig.ecdsa.ecmath.field.FieldElement,
      *      gnu.crypto.sig.ecdsa.ecmath.field.FieldElement)
      */
     public boolean onCurve(BigInteger aAffineX, BigInteger aAffineY)
     {
          BigInteger p = mField.getMSize();

          if (aAffineY.pow(2).mod(p).compareTo(aAffineX.pow(3).add(mA.multiply(aAffineX)).add(mB).mod(p)) == 0)
               return true;

          return false;
     }

}
