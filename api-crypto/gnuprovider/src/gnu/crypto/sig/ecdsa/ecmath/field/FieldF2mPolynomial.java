/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package gnu.crypto.sig.ecdsa.ecmath.field;

import java.math.BigInteger;

/**
 * @author mss
 * 
 */
public class FieldF2mPolynomial
          extends Field
{

     private final int mM;
     private final BigInteger mReductionP;
     private final byte[] mReductionPArray;
     private final int[] mReductionPOnes;
     private final BigInteger mFieldSizeMinusTwo;
     private final int mFlen;

     private static final BigInteger ONE = BigInteger.ONE;
     private static final BigInteger TWO = new BigInteger("2");


     /**
      * @param aSize
      */
     private FieldF2mPolynomial(int aM, BigInteger aReductionP)
     {
          super(ONE.shiftLeft(aM));
          mM = aM;
          mReductionP = aReductionP;
          mReductionPArray = mReductionP.toByteArray();
          mFieldSizeMinusTwo = mSize.subtract(TWO);

          int i, k, ones, l;
          byte[] tmpf = aReductionP.toByteArray();
          byte[] f;
          if (tmpf[0] != 0)
               f = tmpf;
          else
          {
               f = new byte[tmpf.length - 1];
               System.arraycopy(tmpf,
                                1,
                                f,
                                0,
                                f.length);
          }

          mReductionPOnes = new int[aReductionP.bitCount()];
          for (i = f.length - 1, k = 0, ones = 0 ; i >= 0 ; i--, k += 8)
          {
               if (f[i] != 0)
                    for (l = 0 ; l < 8 ; l++)
                    {
                         if (( ( ( f[i] & 0x00ff) >> l) & 1) == 1)
                         {
                              mReductionPOnes[ones++] = k + l;
                         }
                    }
          }

          if (mReductionPArray[0] == 0 || mReductionPArray[0] == 1)
          {
               mFlen = mReductionPArray.length - 1;
          } else
          {
               mFlen = mReductionPArray.length;
          }
     }


     public static synchronized FieldF2mPolynomial getInstance(int aM, BigInteger aReductionP)
     {

          FieldF2mPolynomial f = (FieldF2mPolynomial) INSTANCES.get(ONE.shiftLeft(aM));
          if (f == null)
          {
               f = new FieldF2mPolynomial(aM, aReductionP);
          }
          return f;
     }


     public int getMM()
     {
          return mM;
     }


     public boolean equals(Object aObject)
     {
          if (super.equals(aObject))
          {
               // Size is equal. Test the bases
               return mReductionP.equals(( (FieldF2mPolynomial) aObject).mReductionP);
          }
          return false;
     }


     public BigInteger getMReductionP()
     {
          return mReductionP;
     }


     public byte[] getMReductionPArray()
     {
          return mReductionPArray;
     }


     public int[] getMReductionPOnes()
     {
          return mReductionPOnes;
     }


     public int hashCode()
     {
          return mSize.hashCode() ^ mReductionP.hashCode();
     }


     /*
      * (non-Javadoc)
      * 
      * @see gnu.crypto.sig.ecdsa.ecmath.field.Field#fromOctetToFieldElement(byte[])
      */
     public BigInteger fromOctetToFieldElement(byte[] aOctet)
     {
          return new BigInteger(1, aOctet);
     }


     public BigInteger getMFieldSizeMinusOne()
     {
          return mFieldSizeMinusOne;
     }


     public BigInteger getMFieldSizeMinusTwo()
     {
          return mFieldSizeMinusTwo;
     }


     public BigInteger reduce(BigInteger aPol)
     {
          return reduce(aPol.toByteArray());
     }



     public BigInteger reduce(byte[] aPol)
     {
          int i, k, l;

          int pos;
          pos = ( ( aPol.length << 3) - 8) - ( mM);
          int leftBitCount;
          byte leftByte;
          int rightByteNo;
          int fromBit;

          for (i = 0 ; i < aPol.length - mFlen ; i++, pos -= 8)
          {
               if (aPol[i] != 0)
               {
                    for (k = mReductionPOnes.length - 2 ; k >= 0 ; k--)
                    {
                         fromBit = pos + mReductionPOnes[k];
                         leftBitCount = fromBit & 0x07;
                         leftByte = (byte) ( ( aPol[i] & 0x00ff) >> ( 8 - leftBitCount));
                         rightByteNo = aPol.length - ( fromBit >> 3) - 1;
                         aPol[rightByteNo] ^= (byte) ( aPol[i] << leftBitCount);
                         if (leftByte > 0)
                              aPol[rightByteNo - 1] ^= leftByte;
                    }
               }
          }
          // It is assumed that remaining bits do not overflow...

          l = ( ( mReductionP.bitLength() - 1) % 8);

          if (l > 0)
          {
               byte b = (byte) ( ( aPol[i] & 0x00ff) >> l);
               if (b != 0)
               {
                    for (k = mReductionPOnes.length - 2 ; k >= 0 ; k--)
                    {
                         fromBit = mReductionPOnes[k];
                         leftBitCount = fromBit & 0x07;
                         leftByte = (byte) ( ( b & 0x00ff) >> ( 8 - leftBitCount));
                         rightByteNo = aPol.length - ( fromBit >> 3) - 1;

                         aPol[rightByteNo] ^= (byte) ( b << leftBitCount);
                         if (leftByte > 0)
                              aPol[rightByteNo - 1] ^= leftByte;
                    }
               }
               aPol[i] = (byte) ( ( ( (byte) ( aPol[i] << ( 8 - l))) & 0x00ff) >> ( 8 - l));
          }

          byte[] r = new byte[aPol.length < mFlen ? aPol.length : mFlen];
          System.arraycopy(aPol,
                           aPol.length - r.length,
                           r,
                           0,
                           r.length);

          return new BigInteger(1, r);
     }




     // public BigInteger reducehatali(byte[] aPol)
     // {
     // // if(true)
     // // return _reduce1(new BigInteger(1,aPol));
     //          
     // //kronometre.baslat();
     // int i,k,l;
     // byte[] c = aPol;
     //               
     // int flen = mReductionPArray.length;
     // if(mReductionPArray[0]==0)
     // {
     // flen--;
     // }
     // if((mM%8) == 0)
     // flen--;
     // //
     // //
     // // int[] fones = new int[fBig.bitCount()];
     // // for(i=f.length-1,k=0,ones=0;i>=0;i--,k+=8)
     // // {
     // // if(f[i]!=0)
     // // for(l=0;l<8;l++)
     // // {
     // // if(( ((f[i]&0x00ff) >> l) & 1) == 1)
     // // fones[ones++]=k+l;
     // // }
     // // }
     //          
     // int pos;
     // pos = ( (c.length << 3) - 8 ) - (mReductionP.bitLength() - 1);
     // int leftBitCount;
     // byte leftByte;
     // int rightByteNo;
     // int fromBit;
     //
     // for(i=0;i < c.length - flen;i++,pos-=8)
     // {
     // for(k=0;k<mReductionPOnes.length-1;k++)
     // {
     // // _addByteStartingFromBitX(c,c[i],pos+fones[k]);
     // if(c[i]!=0)
     // {
     // fromBit = pos+mReductionPOnes[k];
     // leftBitCount = fromBit & 0x07;
     // leftByte = (byte) ((c[i]&0x00ff) >> (8 - leftBitCount));
     // rightByteNo = c.length - (fromBit >> 3) - 1;
     //                         
     // if(rightByteNo<0 || rightByteNo>=c.length)
     // System.out.println("jkasdhasjkdhfadjk");
     // c[rightByteNo] ^= (byte) (c[i] << leftBitCount);
     // if(leftByte>0)
     // c[rightByteNo-1] ^= leftByte;
     // }
     // }
     // }
     // //It is assumed that remaining bits do not overflow...
     //          
     // l = ((mReductionP.bitLength()-1) % 8 );
     //          
     // if(l>0)
     // {
     // byte b = (byte)((c[i]&0x00ff) >> l);
     //
     // if(b!=0)
     // {
     // for(k=0;k<mReductionPOnes.length-1;k++)
     // {
     // // _addByteStartingFromBitX(c,b,fones[k]);
     // fromBit = mReductionPOnes[k];
     // leftBitCount = fromBit & 0x07;
     // leftByte = (byte) ((b&0x00ff) >> (8 - leftBitCount));
     // rightByteNo = c.length - (fromBit >> 3) - 1;
     //                         
     // c[rightByteNo] ^= (byte) (b << leftBitCount);
     // if(leftByte>0)
     // c[rightByteNo-1] ^= leftByte;
     // }
     // }
     // c[i] = (byte)( (((byte) (c[i] << (8-l)) ) & 0x00ff) >> (8-l));
     // //c[i] = (byte)( (((byte) (c[i] << (8-l)) ) ) >> (8-l));
     // }
     // // else if(l==0 && (mM%8) == 0)
     // // {
     // // byte b = (byte)((c[i]&0x00ff));
     // //
     // // if(b!=0)
     // // {
     // // for(k=0;k<mReductionPOnes.length-1;k++)
     // // {
     // //// _addByteStartingFromBitX(c,b,fones[k]);
     // // fromBit = mReductionPOnes[k];
     // // rightByteNo = c.length - (fromBit >> 3) - 1;
     // //
     // // c[rightByteNo] ^= (b);
     // // }
     // // }
     // // c[i]=0;
     // // }
     //
     //          
     //          
     // byte[] r = new byte[c.length<flen?c.length:flen];
     // System.arraycopy(c,c.length-r.length,r,0,r.length);
     //          
     // //kronometre.durdur();
     //          
     // return new BigInteger(1,r);
     // }


     public BigInteger _reduce1(BigInteger aPol)
     {
          // kronometre.baslat();

          BigInteger t, rem = aPol;
          while (rem.bitLength() > mM)
          {
               t = mReductionP.shiftLeft(rem.bitLength() - mReductionP.bitLength());
               rem = rem.xor(t);
          }

          // kronometre.durdur();
          return rem;
     }

}
