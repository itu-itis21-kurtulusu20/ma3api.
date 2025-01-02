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
import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveRuntimeException;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;

import java.math.BigInteger;

import gnu.crypto.util.PRNG;


/**
 * @author mss
 * 
 */
public class ECPointF2mPolynomial
          extends ECGNUPoint
{

     private ElementF2mPolynomial mX, mY, mZ;
     private final CurveF2m mCurveF2m;


     public ECPointF2mPolynomial(Curve aCurve, byte[] aOctet)
               throws EllipticCurveException
     {
          super(aCurve);
          if (!( mCurve instanceof CurveF2m))
               throw new EllipticCurveException("Not a 2^m field curve!");
          if (!( mField instanceof FieldF2mPolynomial))
               throw new EllipticCurveException("Not a polynomial bases field!");

          mCurveF2m = (CurveF2m) mCurve;
          BigInteger[] temp = fromOctetString(aOctet);
          mX = new ElementF2mPolynomial((FieldF2mPolynomial) mField, temp[0]);
          mY = new ElementF2mPolynomial((FieldF2mPolynomial) mField, temp[1]);
          mZ = new ElementF2mPolynomial((FieldF2mPolynomial) mField, BigInteger.ONE);
     }


     /**
      * @param aCurve
      * @param aX
      * @param aY
      * @throws EllipticCurveRuntimeException
      */
     public ECPointF2mPolynomial(CurveF2m aCurve, BigInteger aX, BigInteger aY)
     {
          super(aCurve);
          mCurveF2m = aCurve;
          mX = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aX);
          mY = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aY);
          mZ = new ElementF2mPolynomial((FieldF2mPolynomial) mField, BigInteger.ONE);
          if (!( mField instanceof FieldF2mPolynomial))
               throw new EllipticCurveRuntimeException("Not a polynomial bases field!");
     }


     public ECPointF2mPolynomial(CurveF2m aCurve, BigInteger aX, BigInteger aY, BigInteger aZ)
     {
          super(aCurve);
          mCurveF2m = aCurve;
          mX = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aX);
          mY = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aY);
          mZ = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aZ);
          if (!( mField instanceof FieldF2mPolynomial))
               throw new EllipticCurveRuntimeException("Not a polynomial bases field!");
     }





     /*------------------------------------------------------*/
     /*------------------------------------------------------*/
     /*------------------------------------------------------*/
     /*------------------------------------------------------*/




     /*------------------------------------------------------*/
     /*------------------------------------------------------*/
     /*------------------------------------------------------*/
     /*------------------------------------------------------*/


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#_add(gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint)
      */
     protected ECGNUPoint _add(ECGNUPoint aElem)
     {
          ECPointF2mPolynomial elem = (ECPointF2mPolynomial) aElem;

          ElementF2mPolynomial[] x;

          x = _addJacobian(elem.mX,
                           elem.mY,
                           elem.mZ);
          return new ECPointF2mPolynomial(mCurveF2m, x[0].getMElem(), x[1].getMElem(), x[2].getMElem());
     }


     private ElementF2mPolynomial[] _addJacobian(ElementF2mPolynomial aX2, ElementF2mPolynomial aY2, ElementF2mPolynomial aZ2)
     {
          ElementF2mPolynomial x3, y3, z3;
          if (mX.equals(aX2))
          {
               if (mY.equals(aY2))
               {
                    if (mZ.equals(aZ2))
                    {
                         // doubling...
                         // kronometre.baslat();

                         // //**************++++++++++++//////
                         ElementF2mPolynomial z1sqr = mZ.sqr();
                         z3 = mX.unsafeMultiply(z1sqr);
                         // x3 =
                         // mX.sqr().sqr().unsafeAdd(z1sqr.sqr().sqr().unsafeMultiply(mCurveF2m.mEB));
                         x3 = z1sqr.sqr().sqr();
                         x3.unsafeMultiplyThisBy(mCurveF2m.mEB);
                         x3.unsafeAddToThis(mX.sqr().sqr());

                         ElementF2mPolynomial alfa = mX.sqr();
                         ElementF2mPolynomial tmp2 = alfa.sqr();

                         alfa.unsafeAddToThis(mY.unsafeMultiply(mZ));

                         alfa.unsafeAddToThis(z3);
                         y3 = x3.unsafeMultiply(alfa);
                         tmp2.unsafeMultiplyThisBy(z3);
                         y3.unsafeAddToThis(tmp2);
                         // //**************-------------//////


                         // ////**************++++++++++++//////
                         // // Burasi cok iyi calisiyor....
                         // ElementF2mPolynomial z1sqr = mZ.sqr();
                         // z3 = mX.unsafeMultiply(z1sqr);
                         // x3 =
                         // mX.sqr().sqr().unsafeAdd(z1sqr.sqr().sqr().unsafeMultiply(mCurveF2m.mEB));
                         //
                         //                         
                         // ElementF2mPolynomial x1sqr = mX.sqr();
                         // ElementF2mPolynomial alfa =
                         // x1sqr.unsafeAdd(mY.unsafeMultiply(mZ));
                         // ElementF2mPolynomial alfaPlusz3 =
                         // alfa.unsafeAdd(z3);
                         // ElementF2mPolynomial tmp =
                         // x3.unsafeMultiply(alfaPlusz3);
                         // ElementF2mPolynomial tmp2 = x1sqr.sqr();
                         // y3 = tmp2.unsafeMultiply(z3).unsafeAdd(tmp);
                         // ////**************-------------//////


                         // //**************++++++++++++//////
                         // ElementF2mPolynomial x1sqr = mX.sqr();
                         // ElementF2mPolynomial alfa =
                         // x1sqr.unsafeAdd(mY.unsafeMultiply(mZ));
                         // ElementF2mPolynomial z1sqr = mZ.sqr();
                         // z3 = mX.unsafeMultiply(z1sqr);
                         //                         
                         // // ElementF2mPolynomial alfasqr = alfa.sqr();
                         // ElementF2mPolynomial alfaPlusz3 =
                         // alfa.unsafeAdd(z3);
                         //                         
                         // ElementF2mPolynomial z3sqr = z3.sqr();
                         //                         
                         //                         
                         // // x3 = alfasqr.unsafeAdd(
                         // alfa.unsafeMultiply(z3)).unsafeAdd(mCurveF2m.mEA.unsafeMultiply(z3sqr)
                         // );
                         // // ElementF2mPolynomial tmp =
                         // x3.unsafeMultiply(alfa.unsafeAdd(z3));
                         // x3 = alfa.unsafeMultiply(
                         // alfaPlusz3).unsafeAdd(mCurveF2m.mEA.unsafeMultiply(z3sqr)
                         // );
                         // ElementF2mPolynomial tmp =
                         // x3.unsafeMultiply(alfaPlusz3);
                         //                         
                         // ElementF2mPolynomial tmp2 = x1sqr.sqr();
                         // y3 = tmp2.unsafeMultiply(z3).unsafeAdd(tmp);
                         // //**************-------------//////
                         // ElementF2mPolynomial x,y,zsqr,zcube;
                         // zsqr = z3.sqr();
                         // zcube = zsqr.unsafeMultiply(z3);
                         // x = x3.unsafeMultiply(zsqr.inverse());
                         // y = y3.unsafeMultiply(zcube.inverse());
                         // x3 = x;
                         // y3 = y;
                         // z3 = mCurveF2m.mOne;



                         // kronometre.durdur();
                         // ElementF2mPolynomial affineX =
                         // x3.unsafeMultiply(z3.sqr().inverse());
                         // ElementF2mPolynomial affineY =
                         // y3.unsafeMultiply(z3.sqr().unsafeMultiply(z3).inverse());
                         // ElementF2mPolynomial ll =
                         // alfa.unsafeMultiply(z3.inverse());


                         // ElementF2mPolynomial x,y;
                         // if(! mZ.equals(mCurveF2m.mOne))
                         // {
                         // x=new
                         // ElementF2mPolynomial((FieldF2mPolynomial)mField,getAffineX());
                         // y=new
                         // ElementF2mPolynomial((FieldF2mPolynomial)mField,getAffineY());
                         // }
                         // else
                         // {
                         // x=mX;
                         // y=mY;
                         // }
                         // ElementF2mPolynomial lambda =
                         // x.unsafeAdd(y.unsafeMultiply(x.inverse()));
                         // x3 =
                         // lambda.sqr().unsafeAdd(lambda).unsafeAdd(mCurveF2m.mEA);
                         // y3 =
                         // x.sqr().unsafeAdd(lambda.unsafeMultiply(x3)).unsafeAdd(x3);
                         // z3 = mCurveF2m.mOne;

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

               kronometre.start();






               // //**************++++++++++++//////
               ElementF2mPolynomial z1toPown = mZ.sqr();
               ElementF2mPolynomial z2toPown = aZ2.sqr();
               ElementF2mPolynomial U1 = mX.unsafeMultiply(z2toPown);
               ElementF2mPolynomial alfa = aX2.unsafeMultiply(z1toPown); // =U2

               z1toPown.unsafeMultiplyThisBy(mZ);
               z2toPown.unsafeMultiplyThisBy(aZ2);

               alfa.unsafeAddToThis(U1);
               if (alfa.getMElem().equals(BigInteger.ZERO)) { return _addJacobian(mX,
                                                                                  mY,
                                                                                  mZ); }
               z2toPown.unsafeMultiplyThisBy(mY); // =S1
               z1toPown.unsafeMultiplyThisBy(aY2); // =S2
               z1toPown.unsafeAddToThis(z2toPown); // =beta
               ElementF2mPolynomial betasqr = z1toPown.sqr();
               z3 = alfa.unsafeMultiply(mZ);
               z3.unsafeMultiplyThisBy(aZ2);
               ElementF2mPolynomial z3sqr = z3.sqr();
               z3sqr.unsafeMultiplyThisBy(mCurveF2m.mEA); // =mCurveF2m.mEA.unsafeMultiply(z3sqr)

               ElementF2mPolynomial alfatoPown = alfa.sqr();
               y3 = alfatoPown.unsafeMultiply(U1);
               alfatoPown.unsafeMultiplyThisBy(alfa);
               z2toPown.unsafeMultiplyThisBy(alfatoPown); // =alfacube.unsafeMultiply(z2toPown)
                                                            // =
                                                            // alfacube.unsafeMultiply(S1)

               // x3 =
               // betasqr.unsafeAdd(z3.unsafeMultiply(z1toPown)).unsafeAdd(alfacube).unsafeAdd(z3sqr);
               x3 = z3.unsafeMultiply(z1toPown);
               x3.unsafeAddToThis(betasqr);
               x3.unsafeAddToThis(alfatoPown);
               x3.unsafeAddToThis(z3sqr);

               // y3 =
               // z1toPown.unsafeMultiply(alfasqr.unsafeMultiply(U1).unsafeAdd(x3)).unsafeAdd(
               // z3.unsafeMultiply(x3)
               // ).unsafeAdd(
               // z2toPown
               // );
               y3.unsafeAddToThis(x3);
               y3.unsafeMultiplyThisBy(z1toPown);
               y3.unsafeAddToThis(z3.unsafeMultiply(x3));
               y3.unsafeAddToThis(z2toPown);
               // //**************-------------//////




               // ////**************++++++++++++//////
               // ElementF2mPolynomial z1sqr = mZ.sqr();
               // ElementF2mPolynomial z2sqr = aZ2.sqr();
               // ElementF2mPolynomial z1cube = z1sqr.unsafeMultiply(mZ);
               // ElementF2mPolynomial z2cube = z2sqr.unsafeMultiply(aZ2);
               // ElementF2mPolynomial U1 = mX.unsafeMultiply(z2sqr);
               // ElementF2mPolynomial U2 = aX2.unsafeMultiply(z1sqr);
               // ElementF2mPolynomial alfa = U1.unsafeAdd(U2);
               // if(alfa.getMElem().equals(BigInteger.ZERO))
               // {
               // return _addJacobian(mX,mY,mZ);
               // }
               // ElementF2mPolynomial S1 = mY.unsafeMultiply(z2cube);
               // ElementF2mPolynomial S2 = aY2.unsafeMultiply(z1cube);
               // ElementF2mPolynomial beta = S1.unsafeAdd(S2);
               // ElementF2mPolynomial betasqr = beta.sqr();
               // z3 = alfa.unsafeMultiply(mZ).unsafeMultiply(aZ2);
               // ElementF2mPolynomial z3sqr = z3.sqr();
               // ElementF2mPolynomial alfasqr = alfa.sqr();
               // ElementF2mPolynomial alfacube = alfasqr.unsafeMultiply(alfa);
               // x3 =
               // betasqr.unsafeAdd(z3.unsafeMultiply(beta)).unsafeAdd(alfacube).unsafeAdd(mCurveF2m.mEA.unsafeMultiply(z3sqr));
               // y3 =
               // beta.unsafeMultiply(alfasqr.unsafeMultiply(U1).unsafeAdd(x3)).unsafeAdd(
               // z3.unsafeMultiply(x3)
               // ).unsafeAdd(
               // alfacube.unsafeMultiply(S1)
               // );
               // ////**************-------------//////


               // ElementF2mPolynomial x,y,zsqr,zcube;
               // zsqr = z3.sqr();
               // zcube = zsqr.unsafeMultiply(z3);
               // x = x3.unsafeMultiply(zsqr.inverse());
               // y = y3.unsafeMultiply(zcube.inverse());
               // x3 = x;
               // y3 = y;
               // z3 = mCurveF2m.mOne;



               // if(! mZ.equals(mCurveF2m.mOne))
               // throw new IllegalStateException("Not affine coordinates");
               // ElementF2mPolynomial lambda =
               // mY.unsafeAdd(aY2).unsafeMultiply(mX.unsafeAdd(aX2).inverse());
               // x3 =
               // lambda.sqr().unsafeAdd(lambda).unsafeAdd(mX).unsafeAdd(aX2).unsafeAdd(mCurveF2m.mEA);
               // y3 =
               // lambda.unsafeMultiply(mX.unsafeAdd(x3)).unsafeAdd(x3).unsafeAdd(mY);
               // z3 = new ElementF2mPolynomial(mZ);

               kronometre.stop();
          }

          return new ElementF2mPolynomial[] { x3, y3, z3 };

     }




     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#_addToThis(gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint)
      */
     protected void _addToThis(ECGNUPoint aElem)
     {
          ECPointF2mPolynomial elem = (ECPointF2mPolynomial) aElem;

          ElementF2mPolynomial[] x;

          x = _addJacobian(elem.mX,
                           elem.mY,
                           elem.mZ);
          mX = x[0];
          mY = x[1];
          mZ = x[2];

     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#negate()
      */
     public ECGNUPoint negate()
     {
          ECPointF2mPolynomial x = (ECPointF2mPolynomial) clone();
          x.negateThis();
          return x;
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#negateThis()
      */
     public void negateThis()
     {
          mY = mX.unsafeMultiply(mZ).unsafeAdd(mY);
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#equals(java.lang.Object)
      */
     public boolean equals(Object aObject)
     {
          if (( aObject == null) || !( aObject instanceof ECPointF2mPolynomial))
               return false;

          ECPointF2mPolynomial obj = (ECPointF2mPolynomial) aObject;
          if (( mX.equals(obj.mX)) && ( mY.equals(obj.mY)) && ( mZ.equals(obj.mZ)) && mCurveF2m.equals(obj.mCurveF2m))
               return true;

          ElementF2mPolynomial z1sqr = mZ.unsafeMultiply(mZ);
          ElementF2mPolynomial z1cube = mZ.unsafeMultiply(z1sqr);

          ElementF2mPolynomial z2sqr = obj.mZ.unsafeMultiply(obj.mZ);
          ElementF2mPolynomial z2cube = obj.mZ.unsafeMultiply(z2sqr);

          return ( ( mX.unsafeMultiply(z2sqr).equals(obj.mX.unsafeMultiply(z1sqr))) && ( mY.unsafeMultiply(z2cube).equals(obj.mY
                    .unsafeMultiply(z1cube))));
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#clone()
      */
     public Object clone()
     {
          return new ECPointF2mPolynomial(mCurveF2m, mX.getMElem(), mY.getMElem(), mZ.getMElem());
     }


     public BigInteger getAffineX()
     {
          if (mZ.equals(mCurveF2m.mOne))
               return mX.getMElem();
          return mX.unsafeMultiply(mZ.sqr().inverse()).getMElem();
     }


     public BigInteger getAffineY()
     {
          if (mZ.equals(mCurveF2m.mOne))
               return mY.getMElem();
          return mY.unsafeMultiply(mZ.sqr().unsafeMultiply(mZ).inverse()).getMElem();
     }


     public String toString()
     {
          String st = "(" + mX.getMElem() + "," + mY.getMElem() + "," + mZ.getMElem() + ")\n";
          st += "   " + getAffineX() + "," + getAffineY();
          return st;
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#_ypTilda()
      */
     protected boolean _ypTilda()
     {
          // X9.62 Page 18 Section 4.2.2
          ElementF2mPolynomial x = new ElementF2mPolynomial((FieldF2mPolynomial) mField, getAffineX());
          ElementF2mPolynomial y = new ElementF2mPolynomial((FieldF2mPolynomial) mField, getAffineY());
          return y.unsafeMultiply(x.inverse()).getMElem().testBit(0);
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint#_convert(java.math.BigInteger,
      *      int)
      */
     protected BigInteger _convert(BigInteger aXP, int aYTilda)
               throws EllipticCurveException
     {
          // X9.62 Page 18 Section 4.2.2
          // 1. If xp = 0, then yp = b^(2^(m-1)). (yp is the square root of b in
          // F2m.)
          if (aXP.equals(BigInteger.ZERO))
               return mCurveF2m.mEB.pow(new BigInteger("2").pow(( (FieldF2mPolynomial) mCurveF2m.mField).getMM() - 1)).getMElem();
          // 2. If xp != 0, then do the following:
          ElementF2mPolynomial xp = new ElementF2mPolynomial((FieldF2mPolynomial) mField, aXP);
          ElementF2mPolynomial xpInverse = xp.inverse2();
          ElementF2mPolynomial beta, z;
          // 2.1. Compute the field element beta = xp + a + b xp^-2 in F2m.
          beta = xp.unsafeAdd(mCurveF2m.mEA).unsafeAdd(mCurveF2m.mEB.unsafeMultiply(xpInverse.unsafeMultiply(xpInverse)));
          // 2.2. Find a field element z such that z^2 + z = beta using the
          // algorithm described
          // in Annex D.1.6. It is an error if the output of Annex D.1.6 is "no
          // solutions exist".
          z = _solveQuadratic(beta);
          // 2.3. Let zTilda be the rightmost bit of z.
          int zTilda = z.getMElem().testBit(0) ? 1 : 0;
          // 2.4. If yTilda_p != zTilda , then set z = z + 1, where 1 is the
          // multiplicative identity.
          if (aYTilda != zTilda)
               z = z.unsafeAdd(new ElementF2mPolynomial((FieldF2mPolynomial) mField, BigInteger.ONE));
          // 2.5. Compute yp = xp z.
          return xp.unsafeMultiply(z).getMElem();
     }


     private ElementF2mPolynomial _solveQuadratic(ElementF2mPolynomial aBeta)
               throws EllipticCurveException
     {
          // X9.62 Page 93 Section D.1.6
          // Input: A field F2m along with a basis for representing its
          // elements; and an element beta != 0.
          // Output: An element z for which z^2 + z = beta if any exist;
          // otherwise the message
          // "no solutions exist".

          // Algorithm 1: for normal basis representation.
          // 1. Let ( beta0 beta1... beta(m-1)) be the representation of beta.
          // 2. Set z0 = 0.
          // 3. For i from 1 to m-1 do
          // 3.1. Set zi = z(i-1) xor betai
          // 4. Set z = (z0 z1...z(m-1)).
          // 5. Compute gamma = z^2 + z.
          // 6. If gamma = beta, then output z. Otherwise, output the message
          // "no solutions exist".

          if (( ( ( (FieldF2mPolynomial) mCurveF2m.mField).getMM()) & 1) != 0)
          // if( ((((FieldF2mPolynomial)mCurveF2m.mField).getMM()) & 1) == 10)
          {
               // Algorithm 2: for polynomial basis representation, with m odd.
               // 1. Compute z = half-trace of beta via Annex D.1.5.
               ElementF2mPolynomial z = _halfTrace(aBeta);
               // 2. Compute gamma = z^2 + z.
               ElementF2mPolynomial gamma = z.unsafeMultiply(z).unsafeAdd(z);
               // 3. If gamma = beta, then output z. Otherwise, output the
               // message "no solutions exist".
               if (gamma.equals(aBeta))
                    return z;
               else
                    throw new EllipticCurveException("no solutions exist");
          } else
          {
               // Algorithm 3: works in any polynomial basis.
               while (true)
               {
                    // 1. Choose a random toe \in F2m.
                    int m = ( (FieldF2mPolynomial) mCurveF2m.mField).getMM();
                    int i;
                    ElementF2mPolynomial toe = new ElementF2mPolynomial((FieldF2mPolynomial) mField, rastgeleSayi(m));
                    // 2. Set z = 0 and w = beta.
                    // 3. For i from 1 to m - 1 do
                    // 3.1. Set z = z^2 + w^2 toe.
                    // 3.2. Set w = w^2 + beta.
                    /***********************************************************
                     * ElementF2mPolynomial w = aBeta; ElementF2mPolynomial wsqr =
                     * w.sqr(); ElementF2mPolynomial z =
                     * wsqr.unsafeMultiply(toe); w = wsqr.unsafeAdd(aBeta);
                     * for(i=m-1;i>1;i--) { wsqr = w.sqr(); z =
                     * z.sqr().unsafeAdd(wsqr.unsafeMultiply(toe)); w =
                     * wsqr.unsafeAdd(aBeta); }
                     **********************************************************/
                    ElementF2mPolynomial gamma;
                    ElementF2mPolynomial w = toe;
                    ElementF2mPolynomial wsqr;
                    ElementF2mPolynomial z = new ElementF2mPolynomial((FieldF2mPolynomial) mField, BigInteger.ZERO);
                    for (i = 1 ; i < m ; i++)
                    {
                         wsqr = w.sqr();
                         z = z.sqr().unsafeAdd(wsqr.unsafeMultiply(aBeta));
                         w = wsqr.unsafeAdd(toe);
                    }

                    // 4. If w != 0, then output the message "no solutions
                    // exist" and stop.
                    // if(! w.getMElem().equals(BigInteger.ZERO))
                    // throw new EllipticCurveException("no solutions exist");
                    // 5. Compute gamma = z^2 + z.
                    gamma = z.sqr().unsafeAdd(z);
                    // 6. If gamma = 0, then go to Step 1.
                    // 7. Output z.
                    if (!gamma.getMElem().equals(BigInteger.ZERO))
                         return z;
               }
          }

     }


     public static BigInteger rastgeleSayi(int aBitLen)
     {
          BigInteger n = BigInteger.ONE.shiftLeft(aBitLen);
          BigInteger k;
          int bitLen = aBitLen;
          int byteLen = bitLen / 8 + ( ( bitLen % 8) == 0 ? 0 : 1);
          byte[] kbytes = new byte[byteLen];

          do
          {
               PRNG.nextBytes(kbytes);
               k = new BigInteger(1, kbytes);
          } while (( k.compareTo(n) >= 0) || ( k.compareTo(BigInteger.ONE) < 0));

          return k;
     }


     private ElementF2mPolynomial _halfTrace(ElementF2mPolynomial aAlfa)
               throws EllipticCurveException
     {
          int end;
          end = ( (FieldF2mPolynomial) mCurveF2m.mField).getMM();
          if (( end & 1) == 0)
               throw new EllipticCurveException("m is not odd, can not calculate half-trace");

          end = ( end + 1) >> 1;

          ElementF2mPolynomial t = new ElementF2mPolynomial(aAlfa);
          int i;

          for (i = 1 ; i < end ; i++)
          {
               t.unsafeMultiplyThisBy(t);
               t.unsafeMultiplyThisBy(t);
               t = t.unsafeAdd(aAlfa);
          }
          return t;
     }

}
