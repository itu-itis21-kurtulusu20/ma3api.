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

import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;

import java.math.BigInteger;

import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;


/**
 * @author mss
 * 
 */
public class ElementF2mPolynomial
{
     private final FieldF2mPolynomial mField;
     private BigInteger mElem;

     static Chronometer kronometre = new Chronometer("------ ElementF2mPolynomial");
     public static boolean say = false;


     public static void sifirla()
     {
          kronometre.reset();
     }


     public static void yaz()
     {
          System.out.println(kronometre.toString("Element icinde "));
          kronometre.reset();
     }


     private static byte[] TWOPOWMASK = { (byte) ( 1), (byte) ( 1 << 1), (byte) ( 1 << 2), (byte) ( 1 << 3), (byte) ( 1 << 4), (byte) ( 1 << 5),
               (byte) ( 1 << 6), (byte) ( 1 << 7) };


     public ElementF2mPolynomial(FieldF2mPolynomial aField, BigInteger aElem)
     {
          super();
          mField = aField;
          mElem = aElem;
     }


     /**
      * Copy constructor
      * 
      * @param aElement
      */
     public ElementF2mPolynomial(ElementF2mPolynomial aElement)
     {
          super();
          mField = aElement.mField;
          mElem = aElement.mElem;
     }


     // public ElementF2mPolynomial add(ElementF2mPolynomial aX)
     // {
     // if(mField.equals(aX.mField))
     // return new ElementF2mPolynomial(mField,mElem.xor(aX.mElem));
     // else throw new EllipticCurveRuntimeException("Elements not in same
     // field");
     // }

     public boolean equals(Object aObject)
     {
          if (aObject instanceof ElementF2mPolynomial)
          {
               ElementF2mPolynomial elem = (ElementF2mPolynomial) aObject;
               return ( mField.equals(elem.mField) && mElem.equals(elem.mElem));
          }

          return false;
     }


     public ElementF2mPolynomial unsafeAdd(ElementF2mPolynomial aX)
     {
          return new ElementF2mPolynomial(mField, mElem.xor(aX.mElem));
     }


     public void unsafeAddToThis(ElementF2mPolynomial aX)
     {
          mElem = mElem.xor(aX.mElem);
     }


     public ElementF2mPolynomial unsafeMultiply(ElementF2mPolynomial aX)
     {
          ElementF2mPolynomial elem = new ElementF2mPolynomial(this);
          elem.unsafeMultiplyThisBy(aX);
          return elem;
     }


     private byte[] _shiftLeftByteArray(byte[] aArray)
     {
          byte[] r;
          int i, j = 0;
          if (( aArray[0] & TWOPOWMASK[7]) != 0)
          {
               r = new byte[aArray.length + 1];
               r[0] = 1;
               i = 1;
          } else
          {
               r = new byte[aArray.length];
               i = 0;
          }
          while (i < r.length - 1)
          {
               r[i++] = (byte) ( aArray[j++] << 1);
               if (( aArray[j] & TWOPOWMASK[7]) != 0)
                    r[i - 1] |= 1;
          }
          r[i++] = (byte) ( aArray[j++] << 1);

          return r;
     }


     private byte[] _addByteArrays(byte[] aA, byte[] aB)
     {
          // int a = aA.length;
          // int b = aB.length;
          // int max = (a<b) ? b : a;
          // byte[] r = new byte[max];
          // for(int i=r.length-1;i>=0;i--)
          // {
          // if(a > 0)
          // r[i] = aA[--a];
          // if(b > 0)
          // r[i] ^= aB[--b];
          // }
          // return r;

          byte[] bigA;
          byte[] smallA;
          int dif, i;

          if (aA.length > aB.length)
          {
               bigA = aA;
               smallA = aB;
               dif = aA.length - aB.length;
          } else
          {
               bigA = aB;
               smallA = aA;
               dif = aB.length - aA.length;
          }

          byte[] r = new byte[bigA.length];
          for (i = 0 ; i < dif ; i++)
          {
               r[i] = bigA[i];
          }

          for ( ; i < bigA.length ; i++)
          {
               r[i] = (byte) ( bigA[i] ^ smallA[i - dif]);
          }

          return r;
     }


     public void unsafeMultiplyThisBy(ElementF2mPolynomial aX)
     {
          if (say)
               kronometre.start();

          // Left to right comb method from hankerson
          // Input: Polynomials a(x) and b(x)
          byte[] a = aX.mElem.toByteArray();
          byte[] b = mElem.toByteArray();

          // Output: c(x) = a(x) b(x)
          byte[] c = new byte[a.length + b.length + 1];
          // 1) Compute Bu = u(x) b(x) for all polynomials u(x) of degree at
          // most 3
          byte[][] B = new byte[16][];
          B[0] = new byte[] { 0 };
          B[1] = b;

          // for(int i=2;i<16;i++)
          // {
          // B[i] = _simpleMultiply(b,(byte)i);
          // }

          // B[2] = _shiftLeftByteArray(B[1]);
          // B[3] = _addByteArrays(B[2],B[1]);
          // B[4] = _shiftLeftByteArray(B[2]);
          // B[5] = _addByteArrays(B[4],B[1]);
          // B[6] = _addByteArrays(B[4],B[2]);
          // B[7] = _addByteArrays(B[6],B[1]);
          // B[8] = _shiftLeftByteArray(B[4]);
          // B[9] = _addByteArrays(B[8],B[1]);
          // B[10] = _addByteArrays(B[8],B[2]);
          // B[11] = _addByteArrays(B[10],B[1]);
          // B[12] = _addByteArrays(B[8],B[4]);
          // B[13] = _addByteArrays(B[12],B[1]);
          // B[14] = _addByteArrays(B[12],B[2]);
          // B[15] = _addByteArrays(B[14],B[1]);

          B[2] = _shiftLeftByteArray(B[1]);
          B[4] = _shiftLeftByteArray(B[2]);
          B[8] = _shiftLeftByteArray(B[4]);
          int lenDif = B[8].length - B[1].length;
          byte[] tempA;
          if (lenDif != 0)
          {
               tempA = B[1];
               B[1] = new byte[B[8].length];
               System.arraycopy(tempA,
                                0,
                                B[1],
                                lenDif,
                                tempA.length);
          }
          lenDif = B[8].length - B[2].length;
          if (lenDif != 0)
          {
               tempA = B[2];
               B[2] = new byte[B[8].length];
               System.arraycopy(tempA,
                                0,
                                B[2],
                                lenDif,
                                tempA.length);
          }
          lenDif = B[8].length - B[4].length;
          if (lenDif != 0)
          {
               tempA = B[4];
               B[4] = new byte[B[8].length];
               System.arraycopy(tempA,
                                0,
                                B[4],
                                lenDif,
                                tempA.length);
          }
          int i;

          i = B[8].length;
          B[3] = new byte[i];
          B[5] = new byte[i];
          B[6] = new byte[i];
          B[7] = new byte[i];
          B[9] = new byte[i];
          B[10] = new byte[i];
          B[11] = new byte[i];
          B[12] = new byte[i];
          B[13] = new byte[i];
          B[14] = new byte[i];
          B[15] = new byte[i];
          for (i = 0 ; i < B[8].length ; i++)
          {
               B[3][i] = (byte) ( B[2][i] ^ B[1][i]);
               B[5][i] = (byte) ( B[4][i] ^ B[1][i]);
               B[6][i] = (byte) ( B[4][i] ^ B[2][i]);
               B[7][i] = (byte) ( B[6][i] ^ B[1][i]);
               B[9][i] = (byte) ( B[8][i] ^ B[1][i]);
               B[10][i] = (byte) ( B[8][i] ^ B[2][i]);
               B[11][i] = (byte) ( B[10][i] ^ B[1][i]);
               B[12][i] = (byte) ( B[8][i] ^ B[4][i]);
               B[13][i] = (byte) ( B[12][i] ^ B[1][i]);
               B[14][i] = (byte) ( B[12][i] ^ B[2][i]);
               B[15][i] = (byte) ( B[14][i] ^ B[1][i]);
          }

          // //////////////////

          // 2) C = 0
          // 3) for k from 7 downto 0
          int j;
          int subindex, offset;
          byte[] elem;
          // ////////////-------------
          // 3.1) for j from 0 to t-1
          for (j = a.length - 1 ; j >= 0 ; j--)
          {
               elem = B[( a[j] & 0x00F0) >> 4];
               offset = c.length - ( a.length - j) - ( elem.length - 1);

               for (subindex = elem.length - 1 ; subindex >= 0 ; subindex--)
               {
                    c[offset + subindex] ^= elem[subindex];
               }
          }
          // 3.2) if k!=0 then C = C x^4
          for (j = 0 ; j < c.length - 1 ; j++)
          {
               c[j] = (byte) ( ( c[j] << 4) | ( ( c[j + 1] & 0x00F0) >> 4));
          }
          c[j] <<= 4;
          // 3.1) for j from 0 to t-1
          for (j = a.length - 1 ; j >= 0 ; j--)
          {
               elem = B[a[j] & 0x0F];
               offset = c.length - ( a.length - j) - ( elem.length - 1);

               for (subindex = elem.length - 1 ; subindex >= 0 ; subindex--)
               {
                    c[offset + subindex] ^= elem[subindex];
               }
          }
          // ////////////-------------
          // for(k=1;k>=0;k--)
          // {
          // // 3.1) for j from 0 to t-1
          // for(j=a.length-1;j>=0;j--)
          // {
          // // Let u = (u3,u2,u1,u0) where ui is bit 4k+i of A[j]. Add Bu to
          // C{j}
          // // if(k==0)
          // // _unsafeAdd(c,B[a[j]&0x0F],c.length-(a.length-j));
          // // else
          // // _unsafeAdd(c,B[(a[j]&0x00F0)>>4],c.length-(a.length-j));
          // if(k==0)
          // {
          // elem = B[a[j]&0x0F];
          // }
          // else
          // {
          // elem = B[(a[j]&0x00F0)>>4];
          // }
          // offset = c.length-(a.length-j) - (elem.length-1);
          //                    
          // for(subindex=elem.length-1;subindex>=0;subindex--)
          // {
          // c[offset+subindex] ^= elem[subindex];
          // }
          // }
          // // 3.2) if k!=0 then C = C x^4
          // if(k>0)
          // {
          // for(j=0;j<c.length-1;j++)
          // {
          // c[j]=(byte)((c[j]<<4) | ((c[j+1]& 0x00F0)>>4) );
          // }
          // c[j] <<= 4;
          // }
          // }
          // 4) Return C
          mElem = mField.reduce(c);

          if (say)
               kronometre.stop();


          // //--------------------------------------------------------------------------------
          // mElem = new BigInteger(1,b);
          //          
          //          
          //
          // byte[] carpan = aX.mElem.toByteArray();
          // byte[] carpilan = mElem.toByteArray();
          // byte[] carpim = new byte[carpan.length+carpilan.length+1];
          // byte[] temp;
          //          
          //          
          // for(i=carpim.length-1;i>=carpim.length-carpan.length;i--)
          // {
          // temp =
          // _simpleMultiply(carpilan,carpan[i-(carpim.length-carpan.length)]);
          // _unsafeAdd(carpim,temp,i);
          // }
          //          
          // // rem = new BigInteger(1,carpim);
          // // while(rem.bitLength()>mField.getMM())
          // // {
          // // t =
          // mField.getMReductionP().shiftLeft(rem.bitLength()-mField.getMReductionP().bitLength());
          // // rem = rem.xor(t);
          // // }
          // //
          // // //return new ElementF2mPolynomial(mField,rem);
          // // mElem = rem;
          // mElem = _reduce(carpim);

     }


     private void _addByteStartingFromBitX(byte[] aAddToThis, byte aByteToAdd, int aFromBit)
     {
          if (aByteToAdd == 0)
               return;


          int leftBitCount = aFromBit & 0x07;
          byte leftByte = (byte) ( ( aByteToAdd & 0x00ff) >> ( 8 - leftBitCount));
          int rightByteNo = aAddToThis.length - ( aFromBit >> 3) - 1;

          aAddToThis[rightByteNo] ^= (byte) ( aByteToAdd << leftBitCount);
          if (leftByte > 0)
               aAddToThis[rightByteNo - 1] ^= leftByte;
     }


     public ElementF2mPolynomial pow(BigInteger aPow)
     {
          // ElementF2mPolynomial elem = new ElementF2mPolynomial(this);
          // ElementF2mPolynomial carpim;
          // int i;
          //          
          // if(aPow.testBit(0))
          // carpim = new ElementF2mPolynomial(this);
          // else
          // carpim = new ElementF2mPolynomial(mField,BigInteger.ONE);
          //
          // for(i=1;i<aPow.bitLength();i++)
          // {
          // elem.unsafeMultiplyThisBy(this);
          // if(aPow.testBit(i))
          // carpim.unsafeMultiplyThisBy(elem);
          // }
          //          
          // return carpim;

          // X9.62 Page 89 Section D.1.1
          // 1. Set e = a mod (q-1). If e = 0, then output 1.
          BigInteger e = aPow.mod(mField.getMFieldSizeMinusOne());
          if (e.equals(BigInteger.ZERO))
               return new ElementF2mPolynomial(mField, BigInteger.ONE);
          // 2. Let e = er e(r-1)...e1 e0 be the binary representation of e,
          // where the most
          // significant bit er of e is 1.
          // 3. Set x = g.
          ElementF2mPolynomial x = new ElementF2mPolynomial(this);
          // 4. For i from r-1 down to 0 do
          int i;
          for (i = e.bitLength() - 2 ; i >= 0 ; i--)
          {
               // 4.1. Set x = x^2.
               x.unsafeMultiplyThisBy(x);
               // 4.2. If ei = 1, then set x = gx.
               if (e.testBit(i))
                    x.unsafeMultiplyThisBy(this);
          }
          // 5. Output x.
          return x;
     }


     public ElementF2mPolynomial sqr()
     {

          // kronometre.baslat();
          // ElementF2mPolynomial tmp = this.unsafeMultiply(this);
          // kronometre.durdur();
          // return tmp;

          byte[] x = mElem.toByteArray();
          byte[] y = new byte[x.length + x.length];
          int i, j;
          for (i = 0, j = 0 ; i < x.length ; i++, j += 2)
          {
               y[j + 1] = (byte) ( y[j + 1] | ( ( x[i] & TWOPOWMASK[0])));
               y[j + 1] = (byte) ( y[j + 1] | ( ( x[i] & TWOPOWMASK[1]) << 1));
               y[j + 1] = (byte) ( y[j + 1] | ( ( x[i] & TWOPOWMASK[2]) << 2));
               y[j + 1] = (byte) ( y[j + 1] | ( ( x[i] & TWOPOWMASK[3]) << 3));
               y[j + 1] &= 0x7f;

               y[j] = (byte) ( y[j] | ( ( x[i] & TWOPOWMASK[4]) >> 4));
               y[j] = (byte) ( y[j] | ( ( x[i] & TWOPOWMASK[5]) >> 3));
               y[j] = (byte) ( y[j] | ( ( x[i] & TWOPOWMASK[6]) >> 2));
               y[j] = (byte) ( y[j] | ( ( x[i] & TWOPOWMASK[7]) >> 1));
               y[j] &= 0x7f;
          }
          return new ElementF2mPolynomial(mField, mField.reduce(new BigInteger(1, y)));
     }


     public ElementF2mPolynomial inverse()
     {
          // Input: a \in F2m, a!=0
          // Output: a^-1 mod f(x)
          // 1) b=1, c=0, u=a, v=f
          ElementF2mPolynomial b = new ElementF2mPolynomial(mField, BigInteger.ONE);
          ElementF2mPolynomial c = new ElementF2mPolynomial(mField, BigInteger.ZERO);
          ElementF2mPolynomial u = new ElementF2mPolynomial(this);
          ElementF2mPolynomial v = new ElementF2mPolynomial(mField, mField.getMReductionP());
          ElementF2mPolynomial tmp;
          int j;
          // 2) while deg(u)!=0
          while (( u.mElem.compareTo(BigInteger.ONE) != 0) && ( u.mElem.compareTo(BigInteger.ZERO) != 0))
          {
               // 2.1) j = deg(u) - deg(v)
               j = u.mElem.bitLength() - v.mElem.bitLength();
               // 2.2) if j<0 then: swap(u,v), swap(b,c), j=-j
               if (j < 0)
               {
                    tmp = u;
                    u = v;
                    v = tmp;

                    tmp = b;
                    b = c;
                    c = tmp;

                    j = -j;
               }
               // 2.3) u = u + x^j v, b=b + x^j c
               u = u.unsafeAdd(v._shiftLeft(j));
               b = b.unsafeAdd(c._shiftLeft(j));
          }
          // 3) Return b
          return b;



          // // X9.62 Page 90 Section D.1.2
          // // Input: A field Fq, and a non-zero element g \in Fq
          // // Output: The inverse g^-1.
          // // 1. Compute c = g^(q-2) (see Annex D.1.1).
          // // 2. Output c.
          // return pow(mFieldSizeMinusTwo);
     }


     public ElementF2mPolynomial inverse2()
     {
          return pow(mField.getMFieldSizeMinusTwo());
     }


     private ElementF2mPolynomial _shiftLeft(int aX)
     {
          return new ElementF2mPolynomial(mField, mElem.shiftLeft(aX));
     }


     public BigInteger getMElem()
     {
          return mElem;
     }


     // private byte[] _simpleMultiplyEski(byte[] aCarpilan,byte aCarpan)
     // {
     // kronometre.baslat();
     //
     // byte elde = 0;
     // byte[] carpim = new byte[aCarpilan.length+1];
     // byte[] temp;
     // int i;
     //          
     // for(i=carpim.length-1;i>=carpim.length-aCarpilan.length;i--)
     // {
     // temp =
     // _simpleMultiply(aCarpilan[i-(carpim.length-aCarpilan.length)],aCarpan);
     // carpim[i] = temp[1];
     // carpim[i] ^= elde;
     // elde = temp[0];
     // }
     // carpim[i] = elde;
     //          
     //          
     // kronometre.durdur();
     // return carpim;
     // }

     private byte[] _simpleMultiply(byte[] aCarpilan, byte aCarpan)
     {
          byte elde = 0;
          byte[] carpim = new byte[aCarpilan.length + 1];
          int i;
          byte carpilan2;
          byte[] carpim2 = new byte[] { 0, 0 };
          byte[] temp2 = new byte[] { 0, 0 };
          int j;

          boolean[] carpanBits = new boolean[8];
          for (j = 0 ; j < 8 ; j++)
          {
               carpanBits[j] = ( ( aCarpan & TWOPOWMASK[j]) != 0);
          }
          int len = carpim.length - aCarpilan.length;

          for (i = carpim.length - 1 ; i >= len ; i--)
          {
               carpilan2 = aCarpilan[i - len];
               carpim2[0] = 0;
               carpim2[1] = 0;
               temp2[0] = 0;
               temp2[1] = carpilan2;

               if (carpanBits[0])
                    carpim2[1] = carpilan2;

               temp2[0] <<= 1;
               if (( temp2[1] & TWOPOWMASK[7]) != 0)
                    temp2[0] |= 1;
               temp2[1] <<= 1;

               for (j = 1 ; j < 8 ; j++)
               {
                    if (carpanBits[j])
                    {
                         carpim2[1] ^= temp2[1];
                         carpim2[0] ^= temp2[0];
                    }

                    temp2[0] <<= 1;
                    if (( temp2[1] & TWOPOWMASK[7]) != 0)
                         temp2[0] |= 1;
                    temp2[1] <<= 1;
               }


               //
               carpim[i] = carpim2[1];
               carpim[i] ^= elde;
               elde = carpim2[0];
          }
          carpim[i] = elde;
          return carpim;
     }


     // private byte[] _simpleMultiply(byte aCarpilan,byte aCarpan)
     // {
     // byte[] carpim = new byte[] {0,0};
     // byte[] temp = new byte[] {0,aCarpilan};
     // int i;
     //          
     // if((aCarpan & TWOPOWMASK[0]) != 0)
     // carpim[1] = aCarpilan;
     //          
     // temp[0] <<= 1;
     // if( (temp[1] & TWOPOWMASK[7]) != 0)
     // temp[0] |= 1;
     // temp[1] <<= 1;
     //          
     // for(i=1;i<8;i++)
     // {
     // if((aCarpan & TWOPOWMASK[i]) != 0)
     // {
     // carpim[1] ^= temp[1];
     // carpim[0] ^= temp[0];
     // }
     //               
     // temp[0] <<= 1;
     // if( (temp[1] & TWOPOWMASK[7]) != 0)
     // temp[0] |= 1;
     // temp[1] <<= 1;
     // }
     //          
     // return carpim;
     // }

     private void _unsafeAdd(byte[] aSum, byte[] aElem, int aPos)
     {
          int i;
          int offset = aPos - ( aElem.length - 1);

          for (i = aElem.length - 1 ; i >= 0 ; i--)
          {
               aSum[offset + i] ^= aElem[i];
          }
     }


     public String toString()
     {
          return "elem = " + mElem.toString(16) + "\nelem = " + mElem.toString(10) + "\nfield=" + mField;
     }
}
