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

import gnu.crypto.sig.ecdsa.ecmath.field.Field;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.EllipticCurve;

/**
 * @author mss
 * 
 */
public abstract class Curve
          extends EllipticCurve
{

     protected final Field mField;
     protected final BigInteger mA, mB;
     protected final byte[] mSeed;


     public Curve(Field aField, BigInteger aA, BigInteger aB)
     {
          super(aField, aA, aB);

          mA = aA;
          mB = aB;
          mField = aField;
          mSeed = null;
     }


     public Curve(Field aField, BigInteger aA, BigInteger aB, byte[] aSeed)
     {
          super(aField, aA, aB);

          mA = aA;
          mB = aB;
          mField = aField;
          mSeed = aSeed;
     }


     public Field getMField()
     {
          return mField;
     }


     public ECField getField()
     {
          return mField;
     }


     public BigInteger getMA()
     {
          return mA;
     }


     public BigInteger getA()
     {
          return mA;
     }


     public BigInteger getMB()
     {
          return mB;
     }


     public BigInteger getB()
     {
          return mB;
     }


     public byte[] getSeed()
     {
          return mSeed;
     }


     public abstract boolean onCurve(BigInteger aX, BigInteger aY);


     public boolean equals(Object aObject)
     {
          if (aObject == null)
               return false;

          if (this.getClass().isInstance(aObject))
          {
               Curve obj = (Curve) aObject;

               if (!obj.mField.equals(mField))
                    return false;
               if (!obj.mA.equals(mA))
                    return false;
               if (!obj.mB.equals(mB))
                    return false;
               return true;
          } else
               return false;
     }


     public int hashCode()
     {
          return mField.hashCode() ^ mA.hashCode() ^ mB.hashCode();
     }
}
