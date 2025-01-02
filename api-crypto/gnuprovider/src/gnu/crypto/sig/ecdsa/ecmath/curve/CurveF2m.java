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

import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.Field;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;

import java.math.BigInteger;

/**
 * @author mss
 * 
 */
public class CurveF2m
          extends Curve
{


     final ElementF2mPolynomial mEA;
     final ElementF2mPolynomial mEB;
     final ElementF2mPolynomial mOne;


     /**
      * @param aA
      * @param aB
      * @throws EllipticCurveException
      */
     public CurveF2m(Field aField, BigInteger aA, BigInteger aB)
               throws EllipticCurveException
     {
          super(aField, aA, aB);
          if (!( mField instanceof FieldF2mPolynomial))
               throw new EllipticCurveException("Not a prime field element");
          mEA = new ElementF2mPolynomial((FieldF2mPolynomial) aField, aA);
          mEB = new ElementF2mPolynomial((FieldF2mPolynomial) aField, aB);
          mOne = new ElementF2mPolynomial((FieldF2mPolynomial) aField, BigInteger.ONE);


     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.Curve#onCurve(gnu.crypto.sig.ecdsa.ecmath.field.FieldElement,
      *      gnu.crypto.sig.ecdsa.ecmath.field.FieldElement)
      */
     public boolean onCurve(BigInteger aAffineX, BigInteger aAffineY)
     {

          ElementF2mPolynomial x = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aAffineX);
          ElementF2mPolynomial y = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aAffineY);
          ElementF2mPolynomial xsqr = x.unsafeMultiply(x);

          ElementF2mPolynomial left = y.unsafeMultiply(y).unsafeAdd(x.unsafeMultiply(y));
          ElementF2mPolynomial right = xsqr.unsafeMultiply(x).unsafeAdd(mEA.unsafeMultiply(xsqr)).unsafeAdd(mEB);
          return left.equals(right);
     }



}
