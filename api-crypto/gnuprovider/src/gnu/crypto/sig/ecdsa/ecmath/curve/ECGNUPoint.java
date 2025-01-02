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
import java.security.spec.ECPoint;

import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;



import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.Field;

/**
 * @author mss
 * 
 */
public abstract class ECGNUPoint
          extends ECPoint
{

     static Chronometer kronometre = new Chronometer("------ ECGNUOPoint");


     public static void sifirla()
     {
          kronometre.reset();
     }


     public static void yaz()
     {
          System.out.println(kronometre.toString("Point icinde "));
          kronometre.reset();
     }


     public static final int COMPRESSED = 0;
     public static final int UNCOMPRESSED = 1;
     public static final int HYBRID = 2;


     protected static final BigInteger TWO = new BigInteger("2");
     protected static final BigInteger THREE = new BigInteger("3");
     protected static final BigInteger FOUR = new BigInteger("4");
     protected static final BigInteger EIGHT = new BigInteger("8");

     protected final Curve mCurve;
     protected final Field mField;


     public ECGNUPoint(Curve aCurve)
     {
          super(TWO, TWO);
          mCurve = aCurve;
          mField = mCurve.mField;
     }


     protected BigInteger[] fromOctetString(byte[] aOctet)
               throws EllipticCurveException
     {
    	  BigInteger X, Y;
          int aCompressionMode;
          // X9.62 Page 22 Section 4.3.7
          // 1. If the compressed form is used, then parse PO as follows: PO =
          // PC || X1, where PC is a
          // single octet, and X1 is an octet string of length l octets. If
          // uncompressed or hybrid form is
          // used, then parse PO as follows: PO = PC || X1 ||Y1, where PC is a
          // single octet, and X1 and
          // Y1 are octet strings each of length l octets.
          // 2. Convert X1 to a field element xp. (See Section 4.3.4.)
          byte PC = aOctet[0];
          if (( PC == ( (byte) 0x02)) || ( PC == ( (byte) 0x03)))
               aCompressionMode = COMPRESSED;
          else if (( PC == ( (byte) 0x04)))
               aCompressionMode = UNCOMPRESSED;
          else
               throw new EllipticCurveException("PC error ??");

          if (aCompressionMode == COMPRESSED)
          {
               byte[] xBytes = new byte[aOctet.length - 1];
               System.arraycopy(aOctet,
                                1,
                                xBytes,
                                0,
                                xBytes.length);
               X = mField.fromOctetToFieldElement(xBytes);

               // 3. If the compressed form is used, then do the following:
               // 3.1. Verify that PC is either 02 or 03. (It is an error if
               // this is not the case.)
               if (( PC != (byte) 0x02) && ( PC != (byte) 0x03))
                    throw new EllipticCurveException("PC error");
               // 3.2. Set the bit ypTilda to be equal to 0 if PC = 02, or 1 if
               // PC = 03.
               // 3.3. Convert (xp, ypTilda ) to an elliptic curve point (xp
               // ,yp). (See Section 4.2.)
               Y = _convert(X,
                            PC - 2);
          } else if (aCompressionMode == UNCOMPRESSED)
          {
               // throw new EllipticCurveException();
               // 4. If the uncompressed form is used, then do the following:
               // 4.1. Verify that PC is 04. (It is an error if this is not the
               // case.)
               if (PC != (byte) 0x04)
                    throw new EllipticCurveException("PC error");
               // 4.2. Convert Y1 to a field element yp. (See Section 4.3.4.)
               int len = aOctet.length - 1;
               if (( len & 1) == 1)
                    throw new EllipticCurveException("Length is wrong");
               len >>= 1;

               byte[] xBytes = new byte[len];
               byte[] yBytes = new byte[len];
               System.arraycopy(aOctet,
                                1,
                                xBytes,
                                0,
                                xBytes.length);
               X = mField.fromOctetToFieldElement(xBytes);

               System.arraycopy(aOctet,
                                xBytes.length + 1,
                                yBytes,
                                0,
                                yBytes.length);
               Y = mField.fromOctetToFieldElement(yBytes);
          } else
          {
               throw new EllipticCurveException();
          }
          // 5. If the hybrid form is used, then do the following:
          // 5.1. Verify that PC is either 06 or 07. (It is an error if this is
          // not the case.)
          // 5.2. Perform either step 5.2.1 or step 5.2.2:
          // 5.2.1. Convert Y1 to a field element yp. (See Section 4.3.4.)
          // 5.2.2. Set the bit ~y p to be equal to 0 if PC = 06, or 1 if PC =
          // 07. Convert (xp, ~y p)
          // to an elliptic curve point (xp, yp). (See Section 4.2.)
          // 6. If q is a prime, verify that yp^2 = xp^3 + a xp + b (mod p). (It
          // is an error if this is not the case.)
          // If q = 2^m, verify that yp^2 + xp yp = xp^3 + a xp^2 + b in F2m.
          // (It is an error if this is not the case.)
          if (!mCurve.onCurve(X,
                              Y))
               throw new EllipticCurveException("Point not on Curve");
          // 7. The result is P = (xp ,yp).

          return new BigInteger[] { X, Y };
     }


     // public ECGNUPoint (Curve aCurve,BigInteger aX,BigInteger aY)
     // throws EllipticCurveException
     // {
     // if( !aCurve.onCurve(aX,aY))
     // throw new EllipticCurveException("Point not on Curve");
     //          
     // mCurve = aCurve;
     // mX = aX;
     // mY = aY;
     // }

     public final ECGNUPoint add(ECGNUPoint aElem)
               throws EllipticCurveException
     {
          if (!this.getClass().isInstance(aElem))
               throw new EllipticCurveException("Not same type point");

          if (( mCurve == aElem.mCurve) || ( mField.equals(aElem.mField) && ( mCurve.onCurve(aElem.getAffineX(),
                                                                                             aElem.getAffineY() /*
                                                                                                                    * TODO
                                                                                                                    * 04.Oca.2006
                                                                                                                    * elem.mZ
                                                                                                                    */))))
               return _add(aElem);
          else
               throw new EllipticCurveException("Point not on curve");
     }


     abstract protected ECGNUPoint _add(ECGNUPoint aElem);


     public void addToThis(ECGNUPoint aElem)
               throws EllipticCurveException
     {
          if (!this.getClass().isInstance(aElem))
               throw new EllipticCurveException("Not same type point");

          if (( mCurve == aElem.mCurve) || ( mField.equals(aElem.mField) && ( mCurve.onCurve(aElem.getAffineX(),
                                                                                             aElem.getAffineY() /*
                                                                                                                    * TODO
                                                                                                                    * 04.Oca.2006
                                                                                                                    * elem.mZ
                                                                                                                    */))))
               _addToThis(aElem);
          else
               throw new EllipticCurveException("Point not on curve");
     }


     abstract protected void _addToThis(ECGNUPoint aElem);


     abstract public ECGNUPoint negate();


     abstract public void negateThis();


     public ECGNUPoint multiply(BigInteger aKatsayi)
     {
          // X9.62 Page 101 Section D.3.2
          // 1. Set e = k mod n, where n is the order of P. (If n is unknown,
          // then set e = k instead.)
          // return multiply(aKatsayi,(BigInteger)null);


          int w = 5;

          ECGNUPoint[] odd2 = new ECGNUPoint[1 << ( w - 1)];
          ECGNUPoint q = (ECGNUPoint) clone();
          odd2[0] = q;
          ECGNUPoint q2 = q.multiplyBy2();
          for (int i = 1 ; i < odd2.length ; i++)
          {
               odd2[i] = odd2[i - 1]._add(q2);
          }

          return multiply(aKatsayi,
                          odd2);
     }


     public ECGNUPoint multiply(BigInteger aKatsayi, BigInteger aN)
     {
          // P is this !

          // X9.62 Page 101 Section D.3.2
          // 1. Set e = k mod n, where n is the order of P. (If n is unknown,
          // then set e = k instead.)
          BigInteger e;
          if (aN == null)
               e = aKatsayi;
          else
               e = aKatsayi.mod(aN);
          // 2. Let hr hr-1 ...h1 h0 be the binary representation of 3e, where
          // the leftmost bit hr is 1.
          BigInteger hr = e.add(e).add(e);
          // 3. Let er er-1...e1 e0 be the binary representation of e.

          // 4. Set R = P.
          ECGNUPoint R = (ECGNUPoint) clone();
          ECGNUPoint negative = negate();
          // 5. For i from r-1 down to 1 do
          for (int i = hr.bitLength() - 2 ; i > 0 ; i--)
          {
               // 5.1. Set R = 2R.
               R.multiplyThisBy2();
               // 5.2. If hi = 1 and ei = 0, then set R = R + P.
               if (hr.testBit(i) && !e.testBit(i))
                    R._addToThis(this);
               // 5.3. If hi = 0 and ei = 1, then set R = R - P.
               else if (!hr.testBit(i) && e.testBit(i))
                    R._addToThis(negative);

          }
          // 6. Output R.

          return R;
     }


     public ECGNUPoint multiply(BigInteger aKatsayi, ECGNUPoint[][] aPreComputation)
     {
          // Find the base 16 representation of aKatsayi
          byte[] katsayi = _toBase16(aKatsayi);
          int i = 0;

          while (katsayi[i] == 0)
               i++; // We assume that aKatsayi is not zero
          ECGNUPoint r = (ECGNUPoint) aPreComputation[i][katsayi[i] - 1].clone();
          i++;
          for ( ; i < katsayi.length ; i++)
          {
               if (katsayi[i] != 0)
                    r._addToThis(aPreComputation[i][katsayi[i] - 1]);
          }
          return r;
     }


     private byte[] _toBase16(BigInteger aX)
     {
          int len = aX.bitLength() / 4 + 1;
          byte[] base16 = new byte[len];

          int i;
          int k = 0;
          byte temp;

          for (i = 0 ; i < base16.length ; i++)
          {
               temp = aX.testBit(k++) ? (byte) 1 : (byte) 0;
               if (aX.testBit(k++))
                    temp += 2;
               if (aX.testBit(k++))
                    temp += 4;
               if (aX.testBit(k++))
                    temp += 8;
               base16[i] = temp;
          }

          return base16;
     }


     public ECGNUPoint multiply(BigInteger aKatsayi, ECGNUPoint[] aOddPowers)
     {
          int b = aKatsayi.bitLength();
          int[] N = new int[b + 1];
          determineNAF(N,
                       aKatsayi,
                       5 /* TODO 03.Oca.2006 */);

          int j = b;
          while (N[j] == 0)
               j--;
          ECGNUPoint r;
          if (N[j] > 0)
               r = (ECGNUPoint) ( aOddPowers[( N[j] - 1) >> 1]).clone();
          else
               r = ( aOddPowers[( 0 - N[j] - 1) >> 1].negate());
          j--;
          for ( ; j >= 0 ; j--)
          {

               r.multiplyThisBy2();

               if (N[j] > 0)
               {
                    r._addToThis(aOddPowers[( N[j] - 1) >> 1]);
               } else if (N[j] < 0)
               {
                    r._addToThis(aOddPowers[( 0 - N[j] - 1) >> 1].negate());
               }
          }

          return r;
     }


     public ECGNUPoint multiplyAndAdd(BigInteger aKatsayi, ECGNUPoint[] aOddPowers, ECGNUPoint aQ, BigInteger aKatsayi2)
     {

          int w = 5;
          int b, tempint;

          ECGNUPoint[] odd2 = new ECGNUPoint[1 << ( w - 1)];
          ECGNUPoint q = (ECGNUPoint) aQ.clone();
          odd2[0] = q;
          ECGNUPoint q2 = q.multiplyBy2();
          // try
          // {
          for (int i = 1 ; i < odd2.length ; i++)
          {
               odd2[i] = odd2[i - 1]._add(q2);
          }
          // } catch (EllipticCurveException ex)
          // {
          // //never
          // throw new IllegalArgumentException();
          // }

          // find the greatest bit length
          b = aKatsayi.bitLength();
          tempint = aKatsayi2.bitLength();
          if (tempint > b)
               b = tempint;

          // arrays for NAF represantation
          int[] N1 = new int[b + 1];
          int[] N2 = new int[b + 1];
          determineNAF(N1,
                       aKatsayi,
                       w);
          determineNAF(N2,
                       aKatsayi2,
                       w);

          // System.out.print("N1 =");
          // for(int k = b; k>=0; k--)
          // System.out.print(N1[k]+" ");
          // System.out.println("");
          // System.out.print("N2 =");
          // for(int k = b; k>=0; k--)
          // System.out.print(N2[k]+" ");
          // System.out.println("");

          // set initial value
          int j = b;
          while (( N1[j] == 0) && ( N2[j] == 0))
               j--;
          ECGNUPoint r = null;
          ECGNUPoint tmpPoint = null;
          if (N1[j] > 0)
               r = (ECGNUPoint) aOddPowers[( N1[j] - 1) >> 1].clone();
          else if (N1[j] < 0)
               r = aOddPowers[( 0 - N1[j] - 1) >> 1].negate();

          if (N2[j] > 0)
               tmpPoint = (ECGNUPoint) odd2[( N2[j] - 1) >> 1].clone();
          else if (N2[j] < 0)
               tmpPoint = odd2[( 0 - N2[j] - 1) >> 1].negate();

          if (( r == null))
               r = tmpPoint;
          else if (tmpPoint != null)
               r._addToThis(tmpPoint);
          j--;
          // kronometre.baslat();

          for ( ; j >= 0 ; j--)
          {

               ElementF2mPolynomial.say = true;
               r.multiplyThisBy2();
               ElementF2mPolynomial.say = false;

               if (N1[j] > 0)
               {
                    r._addToThis(aOddPowers[( N1[j] - 1) >> 1]);
               } else if (N1[j] < 0)
               {
                    r._addToThis(aOddPowers[( 0 - N1[j] - 1) >> 1].negate());
               }

               if (N2[j] > 0)
               {
                    r._addToThis(odd2[( N2[j] - 1) >> 1]);
               } else if (N2[j] < 0)
               {
                    r._addToThis(odd2[( 0 - N2[j] - 1) >> 1].negate());
               }
          }

          // kronometre.durdur();

          return r;
     }


     public void multiplyThisBy(BigInteger aKatsayi)
     {
          throw new RuntimeException();
     }


     public void multiplyThisBy(BigInteger aKatsayi, BigInteger aN)
     {
          throw new RuntimeException();
     }


     public ECGNUPoint multiplyBy2()
     {
          return _add(this);
     }


     public void multiplyThisBy2()
     {
          _addToThis(this);
     }


     abstract public boolean equals(Object aObject);


     abstract public Object clone();


     abstract public BigInteger getAffineX();


     abstract public BigInteger getAffineY();


     protected abstract boolean _ypTilda();


     protected abstract BigInteger _convert(BigInteger aX, int aYTilda)
               throws EllipticCurveException;


     public byte[] toOctetString(int aCompressionMode)
     {
    	  // X9.62 Page 21 Section 4.3.6
          // 1) Convert the field element x_p to an octet string X_1
    	 
          byte[] X_1 = _integerToBytes(getAffineX());
          // 2) If the compressed form is used:
          if (aCompressionMode == COMPRESSED)
          {
               // 2.1) Compute the bit y_pTilda
               // 2.2) Assign the value 02 to the single octet PC if y_pTilda is
               // 0,
               // or the value 03 if y_pTilda is 1
               // 2.3) The result is the octet string PO = PC X_1
               byte[] result = new byte[X_1.length + 1];
               result[0] = _ypTilda() ? (byte) 0x03 : (byte) 0x02;
               System.arraycopy(X_1,
                                0,
                                result,
                                1,
                                X_1.length);
               return result;
          }
          // 3. If the uncompressed form is used
          else if(aCompressionMode == UNCOMPRESSED)
          {
        	  // 3.1. Convert the field element yp to an octet string Y1.
        	  byte[] Y_1 = _integerToBytes(getAffineY());
        	  //3.2. Assign the value 04 to the single octet PC.
        	  byte[] result = new byte[X_1.length + Y_1.length + 1];
        	  result[0] = (byte) 0x04;
        	  //3.3. The result is the octet string PO = PC || X1 ||Y1.
        	  System.arraycopy(X_1,
                      0,
                      result,
                      1,
                      X_1.length);
        	  System.arraycopy(Y_1,
                      0,
                      result,
                      X_1.length + 1,
                      Y_1.length);
        	  return result;
        	  //PO = PC || X1 ||Y1.
          }
          //4. If the hybrid form is used,
          else if(aCompressionMode == HYBRID)
          {
        	  //4.1. Convert the field element yp to an octet string Y1.
        	  byte[] Y_1 = _integerToBytes(getAffineY());
        	  //4.2. Compute the bit ~y p .
        	  //4.3. Assign the value 06 to the single octet if ~y p is 0, or the value 07 if ~y p is 1.
        	  byte[] result = new byte[X_1.length + Y_1.length + 1];
        	  result[0] = _ypTilda() ? (byte) 0x07 : (byte) 0x06;
        	  //4.4. The result is the octet string PO = PC || X1 ||Y1.
        	  System.arraycopy(X_1,
                      0,
                      result,
                      1,
                      X_1.length);
        	  System.arraycopy(Y_1,
                      0,
                      result,
                      X_1.length + 1,
                      Y_1.length);
        	  return result;
          }
          else
          {
               throw new RuntimeException();
          }
     }

     private byte[] _integerToBytes(BigInteger aB)
     {
    	 //number of bytes of the biginteger
    	// int i = (int)Math.ceil(((double)aB.bitLength() / (double)8));
    	 int i = (mCurve.getField().getFieldSize()+7)/8;
    	 
         byte toByte[] = aB.toByteArray();
         
         if(i < toByte.length)
         {
             byte kucultulmus[] = new byte[i];
             System.arraycopy(toByte, toByte.length - kucultulmus.length, kucultulmus, 0, kucultulmus.length);
             return kucultulmus;
         }
         if(i > toByte.length)
         {
             byte buyumus[] = new byte[i];
             System.arraycopy(toByte, 0, buyumus, buyumus.length - toByte.length, toByte.length);
             return buyumus;
         } else
         {
             return toByte;
         }
     }

     protected static final void determineNAF(int[] N, BigInteger e, int wi)
     {

          int power2wi = 1 << wi;
          int j, u;
          BigInteger c = e.abs();
          int s = e.signum();

          j = 0;
          while (c.compareTo(BigInteger.ZERO) > 0)
          {
               if (c.testBit(0))
               {
                    u = ( c.intValue()) & ( ( power2wi << 1) - 1);
                    if (( u & power2wi) != 0)
                    {
                         u = u - ( power2wi << 1);
                    }

                    c = c.subtract(BigInteger.valueOf(u));
               } else
               {
                    u = 0;
               }
               try
               {
                    N[j++] = ( s > 0) ? u : -u;
               } catch (ArrayIndexOutOfBoundsException AIOBExc)
               {
                    throw new ArrayIndexOutOfBoundsException("Point.determineNAF(int[] N, BigInteger e, int wi):" + " problem at index " + j);
               }
               c = c.shiftRight(1);
          }

          // fill with zeros
          while (j < N.length)
          {
               N[j++] = 0;
          }
     }
}
