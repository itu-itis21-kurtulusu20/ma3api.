/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package gnu.crypto.sig.ecdsa;

import gnu.crypto.Registry;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.BaseSignature;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.util.TLV;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.tools.Chronometer;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;


/**
 * @author mss
 *
 */
public class ECDSASignature extends BaseSignature
{

     static Chronometer kronometre = new Chronometer("gnu   ECDSASignature");

     public static void yaz()
     {
          System.out.println(kronometre.toString("gnu   icin "));
          kronometre.reset();
     }
     
     private static final BigInteger ONE = BigInteger.ONE;
     private static final BigInteger ZERO = BigInteger.ZERO;

     /**
      * 
      */
     public ECDSASignature()
     {
          this(Registry.SHA160_HASH);
     }

     public ECDSASignature(final String mdName) {
          super(Registry.ECDSA_SIG, HashFactory.getInstance(mdName));
       }
     

     public Object clone()
     {
          return null;
     }
     
     protected void setupForVerification(PublicKey aKey)
     throws IllegalArgumentException
     {
          if(aKey instanceof  ECDSAPublicKey)
               this.publicKey = aKey;
          else if(aKey instanceof ECPublicKey)
               this.publicKey = ECDSAPublicKey.fromECPublicKey((ECPublicKey)aKey);
          else
               throw new IllegalArgumentException();
     }
     
     protected void setupForSigning(PrivateKey aKey)
     throws IllegalArgumentException
     {
        if (!(aKey instanceof ECDSAPrivateKey)) {
           throw new IllegalArgumentException();
        }
        
        this.privateKey = aKey;
     }
     
     private final static byte[] BITMASK = new byte [] {
          (byte)0x7F,(byte)0xBF,(byte)0xDF,(byte)0xEF,(byte)0xF7,(byte)0xFB,(byte)0xFD,(byte)0xFE
     };
     
     protected Object generateSignature() throws IllegalStateException {
          
          BigInteger n = ((ECDSAPrivateKey)privateKey).getMParameters().getMN();
          BigInteger d = ((ECDSAPrivateKey)privateKey).getMD();
          BigInteger s,r;
          ECGNUPoint[][] params = ((ECDSAPrivateKey)privateKey).getMParameters().getMPreComputaion();

          //X9.62-1998 Page 28, Section 5.3
          //5.3.1 Message Digesting
          //Compute the hash value e = H(M)
          BigInteger e = new BigInteger(1, getDigest(privateKey));
          BigInteger k;
          int bitLen = n.bitLength();
          int byteLen = bitLen/8 + ((bitLen%8)==0 ? 0 : 1 );
          int zeroBitLen = (byteLen << 3) - bitLen;
          byte[] kbytes = new byte[byteLen];
          //5.3.2 Elliptic Curve Computation
          ECGNUPoint G = ((ECDSAPrivateKey)privateKey).getMParameters().getMG();
          ECGNUPoint xy;
          while (true)
          {
               //1)Select a statistically unique and unpredictable integer k in the
               //interval [1,n-1]
               do
               {
                    for(int i = 0 ; i < zeroBitLen ; i++)
                         kbytes[0] = ((byte) (kbytes[0] & BITMASK[i]) ); 
                    nextRandomBytes(kbytes);
                    k = new BigInteger(1,kbytes);
               } while ( (k.compareTo(n) >= 0) || (k.compareTo(ONE) < 0) );


               //k=new BigInteger("1542725565216523985789236956265265265235675811949404040041",10);
               //2)Compute the elliptic curve point (x_1,y_1) = kG
               xy = G.multiply(k,params);
               
               //5.3.3 Modular Computation
               //1)Convert the field element x_1 to an integer x_1Bar as described in Section 4.3.5
               //2)Set r = x_1Bar mod n
               r = xy.getAffineX().mod(n);
               //3)if r=0, then go to step 1 of Section 5.3.2
               if(r.compareTo(ZERO)==0)
                    continue;
               //4)Compute s=k^-1(e+dr) mod n
               s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);
               //5)if s=0, then go to step 1 of Section 5.3.2
               if(s.compareTo(ZERO)==0)
                    continue;
               
               //5.3.4 The Signature
               // r,s is the signature
               break;
          }
          return encodeSignature(r,s);
     }
     

     private Object encodeSignature(BigInteger r, BigInteger s) {
          byte[] a = TLV.makeTLV((byte)0x02,r.toByteArray());
          byte[] b = TLV.makeTLV((byte)0x02,s.toByteArray());
          byte[] x = new byte[a.length+b.length];
          System.arraycopy(a,0,x,0,a.length);
          System.arraycopy(b,0,x,a.length,b.length);
          return TLV.makeTLV((byte)0x30, x);
          //return new BigInteger[] {r, s};
     }
     
     protected boolean verifySignature(Object sig) throws IllegalStateException {
          final BigInteger[] rs = decodeSignature(sig);
          
         BigInteger r = rs[0];
         BigInteger s = rs[1];
         BigInteger n = ((ECDSAPublicKey)publicKey).getMParameters().getMN();
         ECGNUPoint Q = ((ECDSAPublicKey)publicKey).getMQ();
         ECGNUPoint G = ((ECDSAPublicKey)publicKey).getMParameters().getMG();
//         ECGNUPoint[][] paramsG = ((ECDSAPublicKey)publicKey).getMParameters().getMPreComputaion();
         ECGNUPoint[] oddPowersG = ((ECDSAPublicKey)publicKey).getMParameters().getMOddPowers();

         //X9.62-1998 Page 30, Section 5.4
         //5.4.1 Message Digesting
         //Compute the hash value e = H(M)
         BigInteger e = new BigInteger(1,getDigest(publicKey));
         //5.4.2 Modular Computations
         //1)if r is not an integer in the interval [1,n-1], then reject the signature.
         if( (r.compareTo(ONE)<0) || (r.compareTo(n)>=0) )
              return false;
         //2)if s is not an integer in the interval [1,n-1], then reject the signature.
         if( (s.compareTo(ONE)<0) || (s.compareTo(n)>=0) )
              return false;
         //3)Compute c=s^-1 mod n
         BigInteger c = s.modInverse(n);
         //4)Compute u_1=ec mod n and u_2=rc mod n
         BigInteger u1 = e.multiply(c).mod(n);
         BigInteger u2 = r.multiply(c).mod(n);
         //5.4.3 Elliptic Curve Computations
         //1)Compute the elliptic curve point (x_1,y_1)=u_1G+u_2Q
         ECGNUPoint xy;
//         try
//         {
              kronometre.start();
//              xy = G.multiply(u1,paramsG).add(Q.multiply(u2));
              //ECGNUPoint u1G = G.multiply(u1);
              //ECGNUPoint u2Q = Q.multiply(u2);
              //xy = G.multiply(u1).add(Q.multiply(u2));
              //ElementF2mPolynomial.say=true;
              xy = G.multiplyAndAdd(u1,oddPowersG,Q,u2);
              //ElementF2mPolynomial.say=false;
              kronometre.stop();
//         } catch (EllipticCurveException ex)
//         {
//              return false;
//         }
         //5.4.4. Signature Checking
         //1)Convert the field element x_1 to an integer x_1Bar
         //2)Compute v=x_1Bar mod n
         BigInteger v = xy.getAffineX().mod(n);
         //3)if r=v then the signature is verified
         return (r.compareTo(v) == 0);
     }

     private byte[] getDigest(Key key) {

         /*When the length of the output of the hash function is greater than the bit length of n,
         then the leftmost n bits of the hash function output block shall be used in any calculation using the hash function output
         during the generation and verification of a digital signature. NIST-FIPS 186-4 Section 6.4*/

         try {
             int keySize = 0;

             if(key instanceof ECDSAPublicKey)
               keySize = KeyUtil.getKeyLength(publicKey) / 8;
             else if(key instanceof ECDSAPrivateKey)
               keySize = KeyUtil.getKeyLength(privateKey) / 8;

             byte[] digest = md.digest();
             int hashSize = md.hashSize();

             if (hashSize > keySize) {
                 byte[] truncatedDigest = new byte[keySize];
                 System.arraycopy(digest, 0, truncatedDigest, 0, keySize);
                 return truncatedDigest;
             } else
                 return digest;
         } catch (Exception e) {
             throw new ESYARuntimeException("Error in getting digest..");
         }
     }
     
     private BigInteger[] decodeSignature(Object aSig) {
          byte[] x = (byte[])aSig;
          //ilk tag 30 olmali...
          if(x[0] != (byte)0x30)//SEQUENCE
            return null;
          //icerigi alalim
          int[] sinir = TLV.getIcerik(x,0);
          if(sinir[1]+1 != x.length)
            return null;
        //icerikte ardarda iki int olmali
          if(x[sinir[0]] != 0x02) //INTEGER
            return null;
          int[] sinir_r = TLV.getIcerik(x,sinir[0]);
          if(x[sinir_r[1]+1] != 0x02) //INTEGER
            return null;
          int[] sinir_s = TLV.getIcerik(x,sinir_r[1]+1);

          byte[] r_array = new byte[sinir_r[1]-sinir_r[0]+1];
          byte[] s_array = new byte[sinir_s[1]-sinir_s[0]+1];

          System.arraycopy(x,sinir_r[0],r_array,0,r_array.length);
          System.arraycopy(x,sinir_s[0],s_array,0,s_array.length);

          BigInteger r = new BigInteger(r_array);
          BigInteger s = new BigInteger(s_array);

          return new BigInteger[]{r,s};

     }
     
     public static void main(String[] args)
     {
          byte[] xx = new byte[] {56,(byte)255,2};
          //BigInteger x = new BigInteger("1110001111111100000010",2);
          BigInteger x = new BigInteger(1,xx);
          
          System.out.println("0  "+x.testBit(0));
          System.out.println("1  "+x.testBit(1));
          System.out.println("16 "+x.testBit(16));
          System.out.println("19 "+x.testBit(19));
          System.out.println("21 "+x.testBit(21));
          System.out.println("22 "+x.testBit(22));

          
          BigInteger n = x;
          int bitLen = n.bitLength();
          int byteLen = bitLen/8 + ((bitLen%8)==0 ? 0 : 1 );
          int zeroBitLen = (byteLen << 3) - bitLen;
          
          System.out.println("bitLen="+bitLen + " byteLen="+ byteLen + " zeroBitLen=" + zeroBitLen);
          
          BigInteger aa = new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951");
          System.out.println("aa mod 8 = "+aa.mod(new BigInteger("4")));
     }
}
