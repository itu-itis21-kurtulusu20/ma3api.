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
import gnu.crypto.util.Prime;

import java.math.BigInteger;



/**
 * @author mss
 * 
 * For Jacobian coordibate computations, Software Implementation of the NIST
 * Elliptic Curves Over Prime Fields, by M. Brown, D. Hankerson, J. Lopez, a.
 * Menezes paper is used.
 */
public class ECPointFp
          extends ECGNUPoint
{

     private static final int MOD = 1;

     BigInteger mX, mY, mZ;


     public ECPointFp(Curve aCurve, byte[] aOctet)
               throws EllipticCurveException
     {
          super(aCurve);
          BigInteger[] temp = fromOctetString(aOctet);
          mX = temp[0];
          mY = temp[1];
          mZ = BigInteger.ONE;
          if (!( aCurve instanceof CurveFp))
               throw new EllipticCurveException("Not a prime field curve!");
     }


     public ECPointFp(Curve aCurve, BigInteger aX, BigInteger aY)
     {
          super(aCurve);
          mX = aX;
          mY = aY;
          mZ = BigInteger.ONE;
     }



     /**
      * @param aCurve
      * @param aX
      * @param aY
      */
     public ECPointFp(CurveFp aCurve, BigInteger aX, BigInteger aY)
     {
          super(aCurve);
          mX = aX;
          mY = aY;
          mZ = BigInteger.ONE;
     }


     public ECPointFp(CurveFp aCurve, BigInteger aX, BigInteger aY, BigInteger aZ)
     {
          super(aCurve);
          mX = aX;
          mY = aY;
          mZ = aZ;
     }


     protected ECGNUPoint _add(ECGNUPoint aElem)
     {
          ECPointFp elem = (ECPointFp) aElem;

          BigInteger[] x;

          switch (MOD)
          {
          case 1:
               x = _addJacobian(elem.mX,
                                elem.mY,
                                elem.mZ);
               return new ECPointFp((CurveFp) mCurve, x[0], x[1], x[2]);
          default:
          {
               x = _addAffine(aElem.getAffineX(),
                              aElem.getAffineY());
               return new ECPointFp((CurveFp) mCurve, x[0], x[1]);
          }
          }
     }


     private BigInteger[] _addJacobian(BigInteger aX2, BigInteger aY2, BigInteger aZ2)
     {
          BigInteger p = mField.getMSize();
          BigInteger x3, y3, z3;



          if (mX.equals(aX2))
          {
               if (mY.equals(aY2))
               {
                    if (mZ.equals(aZ2))
                    {
                         // doubling...
                         BigInteger S, M, T;
                         BigInteger y1sqr, z1sqr;
                         BigInteger tmp;

                         y1sqr = mY.multiply(mY).mod(p);
                         z1sqr = mZ.multiply(mZ).mod(p);

                         S = mX.multiply(y1sqr).shiftLeft(2).mod(p);

                         tmp = mX.multiply(mX).mod(p);
                         M = tmp.shiftLeft(1).add(tmp).add(mCurve.mA.multiply(z1sqr).mod(p).multiply(z1sqr).mod(p)).mod(p);
                         T = M.multiply(M).mod(p).subtract(S.shiftLeft(1)).mod(p);

                         x3 = T;
                         tmp = y1sqr.multiply(y1sqr).mod(p);
                         // tmp = tmp.add(tmp);
                         // tmp = tmp.add(tmp);
                         // tmp = tmp.add(tmp).mod(p); // now tmp = 8*y1^4
                         tmp = tmp.shiftLeft(3).mod(p);
                         y3 = M.multiply(S.subtract(T).mod(p)).subtract(tmp).mod(p);

                         tmp = mY.multiply(mZ).mod(p);
                         z3 = tmp.shiftLeft(1).mod(p);

                    } else
                    {
                         /*
                          * TODO 26.Oca.2006 inverse'i ile toplayip
                          * pointatinfinity donme ihtimali kontrol edilmeli
                          */
                         throw new IllegalArgumentException();
                    }
               } else
               {
                    /*
                     * TODO 26.Oca.2006 inverse'i ile toplayip pointatinfinity
                     * donme ihtimali kontrol edilmeli
                     */
                    throw new IllegalArgumentException();
               }
          } else
          {
               // Addition
               BigInteger U1, U2, S1, S2, H, r;
               BigInteger z1sqr, z2sqr, hsqr, hcube;
               BigInteger u1hsqr;

               z1sqr = mZ.multiply(mZ).mod(p);
               z2sqr = aZ2.multiply(aZ2).mod(p);

               U1 = mX.multiply(z2sqr).mod(p);
               U2 = aX2.multiply(z1sqr).mod(p);
               S1 = mY.multiply(z2sqr).multiply(aZ2).mod(p);
               S2 = aY2.multiply(z1sqr).multiply(mZ).mod(p);
               H = U2.subtract(U1).mod(p);
               r = S2.subtract(S1).mod(p);

               hsqr = H.multiply(H).mod(p);
               hcube = hsqr.multiply(H);

               u1hsqr = hsqr.multiply(U1).mod(p);
               x3 = r.multiply(r).subtract(hcube).subtract(u1hsqr.shiftLeft(1).mod(p)).mod(p);
               y3 = r.multiply(u1hsqr.subtract(x3)).mod(p).subtract(hcube.multiply(S1).mod(p)).mod(p);
               z3 = mZ.multiply(aZ2).mod(p).multiply(H).mod(p);
          }

          return new BigInteger[] { x3, y3, z3 };

     }


     private BigInteger[] _addAffine(BigInteger aX2, BigInteger aY2)
     {
          BigInteger x1 = getAffineX();
          BigInteger y1 = getAffineY();
          BigInteger p = mField.getMSize();
          BigInteger x3, y3, lambda;

          // X9.62 Page 61 Section B.3
          // 4. (Rule for adding two distinct points that are not inverses of
          // each other)
          // Let (x1,y1) \in E(Fp) and (x2,y2) \in E(Fp) be two points such that
          // x1 != x2.
          // Then (x1,y1) + (x2,y2) = (x3,y3), where:
          // x3 = lambda^2 ? x1 ? x2, y3 = lambda (x1 ? x3) ? y1,
          // and lambda =(y2 - y1) / (x2-x1)
          // 5. (Rule for doubling a point)
          // Let (x1, y1) \in E(Fp) be a point with y1 != 0.
          // Then 2(x1, y1) = (x3, y3), where:
          // x3 = lambda^2?2x1, y3 = lambda (x1 ? x3) ? y1,
          // and lambda = (3 x1^2 + a) / 2y1

          BigInteger up, down;

          if (x1.equals(aX2))
          {
               if (y1.equals(aY2))
               {
                    BigInteger xsqr = x1.multiply(x1).mod(p);
                    up = xsqr.add(xsqr).add(xsqr).add(mCurve.mA).mod(p);
                    down = y1.add(y1).mod(p);
               } else
               {
                    // System.out.println(mCurve.onCurve(new
                    // FieldElementFp((FieldFp)mCurve.mField,x1),
                    // new FieldElementFp((FieldFp)mCurve.mField,y1)
                    // ));
                    // System.out.println(mCurve.onCurve(new
                    // FieldElementFp((FieldFp)mCurve.mField,aX2),
                    // new FieldElementFp((FieldFp)mCurve.mField,aY2)
                    // ));
                    // System.out.println(y1.add(aY2).toString(16)) ;
                    throw new IllegalArgumentException();
               }
          } else
          {
               up = aY2.subtract(y1).mod(p);
               down = aX2.subtract(x1).mod(p);
          }
          lambda = up.multiply(down.modInverse(p)).remainder(p);

          x3 = lambda.multiply(lambda).mod(p).subtract(x1).subtract(aX2).mod(p);
          y3 = lambda.multiply(x1.subtract(x3).mod(p)).subtract(y1).mod(p);

          return new BigInteger[] { x3, y3 };
     }


     protected void _addToThis(ECGNUPoint aElem)
     {
          ECPointFp elem = (ECPointFp) aElem;

          BigInteger[] x;
          switch (MOD)
          {
          case 1:
               x = _addJacobian(elem.mX,
                                elem.mY,
                                elem.mZ);
               mX = x[0];
               mY = x[1];
               mZ = x[2];
               break;
          default:
          {
               x = _addAffine(aElem.getAffineX(),
                              aElem.getAffineY());
               mX = x[0];
               mY = x[1];
               mZ = BigInteger.ONE;
          }
          }
          ;
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#negate()
      */
     public ECGNUPoint negate()
     {
          ECPointFp x = (ECPointFp) clone();
          x.mY = x.mY.negate();
          return x;
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#negateThis()
      */
     public void negateThis()
     {
          mY.negate();
     }


     protected boolean _ypTilda()
     {
          // X9.62 Page 17 Section 4.2.1
          return ( getAffineY().testBit(0));
     }


     protected BigInteger _convert(BigInteger aXP, int aYTilda)
               throws EllipticCurveException
     {
          BigInteger p = mField.getMSize();
          BigInteger a = mCurve.mA;
          BigInteger b = mCurve.mB;

          // X9.62 Page 17 Section 4.2.1
          // 1. Compute the field element alfa = xp^3 + a xp +b mod p.
          BigInteger alfa = aXP.pow(3).remainder(p).add(a.multiply(aXP)).add(b).remainder(p);
          // 2. Compute a square root beta of alfa mod p. (See Annex D.1.4.) It
          // is an error if the output of
          // Annex D.1.4 is "no square roots exist".
          BigInteger beta = Prime.modPrimeSqrt(alfa,
                                               p);
          // 3. If the rightmost bit of beta is equal to ypTilda , then set yp =
          // beta. Otherwise,
          // set yp = p-beta.
          int betaTilda = beta.testBit(0) ? 1 : 0;
          if (betaTilda == aYTilda)
               return beta;
          else
               return p.subtract(beta);

     }


     public Object clone()
     {
          return new ECPointFp((CurveFp) mCurve, mX, mY, mZ);
     }


     public boolean equals(Object aObject)
     {
          if (( aObject == null) || !( aObject instanceof ECPointFp))
               return false;

          ECPointFp obj = (ECPointFp) aObject;
          if (( mX.equals(obj.mX)) && ( mY.equals(obj.mY)) && ( mZ.equals(obj.mZ)))
               return true;

          BigInteger p = mField.getMSize();
          if (!p.equals(obj.mField.getMSize()))
               return false;

          BigInteger z1sqr = mZ.multiply(mZ).mod(p);
          BigInteger z1cube = mZ.multiply(z1sqr).mod(p);

          BigInteger z2sqr = obj.mZ.multiply(obj.mZ).mod(p);
          BigInteger z2cube = obj.mZ.multiply(z2sqr).mod(p);

          return ( ( mX.multiply(z2sqr).mod(p).equals(obj.mX.multiply(z1sqr).mod(p))) && ( mY.multiply(z2cube).mod(p).equals(obj.mY.multiply(z1cube)
                    .mod(p))));

     }


     public BigInteger getAffineX()
     {
          BigInteger p = mField.getMSize();

          return mX.multiply(mZ.multiply(mZ).modInverse(p)).mod(p);
     }


     public BigInteger getAffineY()
     {
          BigInteger p = mField.getMSize();

          return mY.multiply(mZ.multiply(mZ).mod(p).multiply(mZ).modInverse(p)).mod(p);
     }


     public String toString()
     {
          String st = "(" + mX + "," + mY + "," + mZ + ")\n";
          st += "   " + getAffineX() + "," + getAffineY();
          return st;
     }

}
