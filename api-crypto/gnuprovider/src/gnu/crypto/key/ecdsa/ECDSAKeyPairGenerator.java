/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package gnu.crypto.key.ecdsa;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Map;

import gnu.crypto.Registry;
import gnu.crypto.key.IKeyPairGenerator;
import gnu.crypto.sig.ecdsa.ecmath.curve.Curve;
import gnu.crypto.sig.ecdsa.ecmath.curve.CurveFp;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;
import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;
import gnu.crypto.util.PRNG;

/**
 * @author mss
 *
 */
public class ECDSAKeyPairGenerator
implements IKeyPairGenerator
{
     
     public static final String DOMAIN_PARAMETERS = "gnu.crypto.ecdsa.domainparameters";
     
     private static final FieldFp DEFAULDFIELD;
     private static final BigInteger DEFAULTA;
     private static final BigInteger DEFAULTB;
     private static final Curve DEFAULTCURVE; 
     
     private static final ECGNUPoint DEFAULTG;
     private static final ECDomainParameter DEFAULTPARAMETERS;
     
     private final static byte[] BITMASK = new byte [] {
          (byte)0x7F,(byte)0xBF,(byte)0xDF,(byte)0xEF,(byte)0xF7,(byte)0xFB,(byte)0xFD,(byte)0xFE
     };

     static {
          try
          {
        	   //prime256v1 is selected as default
               DEFAULDFIELD = FieldFp.getInstance(
                                                  new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
               DEFAULTA = new BigInteger("FFFFFFFF" +
                                         "00000001" +
                                         "00000000" +
                                         "00000000" +
                                         "00000000" +
                                         "FFFFFFFF" +
                                         "FFFFFFFF" +
                                         "FFFFFFFC",16);
               DEFAULTB = new BigInteger("5AC635D8" +
                                         "AA3A93E7" +
                                         "B3EBBD55" +
                                         "769886BC" +
                                         "651D06B0" +
                                         "CC53B0F6" +
                                         "3BCE3C3E" +
                                         "27D2604B",16);
               DEFAULTCURVE = new CurveFp(DEFAULDFIELD,DEFAULTA,DEFAULTB); 
               
               DEFAULTG = new ECPointFp(DEFAULTCURVE,new BigInteger("03" +
                                                                    "6B17D1F2" +
                                                                    "E12C4247" +
                                                                    "F8BCE6E5" +
                                                                    "63A440F2" +
                                                                    "77037D81" +
                                                                    "2DEB33A0" +
                                                                    "F4A13945" +
                                                                    "D898C296",16).toByteArray()
                                                                    );
               
               DEFAULTPARAMETERS = ECDomainParameter.getInstance(
                                                                 DEFAULTCURVE,
                                                                 DEFAULTG,
                                                                 new BigInteger(
                                                                                "FFFFFFFF" +
                                                                                "00000000" +
                                                                                "FFFFFFFF" +
                                                                                "FFFFFFFF" +
                                                                                "BCE6FAAD" +
                                                                                "A7179E84" +
                                                                                "F3B9CAC2" +
                                                                                "FC632551",
                                                                                16),
                                                                                BigInteger.ONE
                                                                           
               );
          } catch (EllipticCurveException ex)
          {
               throw new RuntimeException("Default values can not set!",ex);
          }
          
     }
     
     private ECDomainParameter mParameters; 
     
     /**
      * 
      */
     public ECDSAKeyPairGenerator()
     {
          super();
     }
     
     
     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairGenerator#name()
      */
     public String name()
     {
          return Registry.ECDSA_KPG;
     }
     
     
     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairGenerator#setup(java.util.Map)
      */
     public void setup(Map attributes)
     {
          mParameters = (ECDomainParameter) attributes.get(DOMAIN_PARAMETERS);
          if(mParameters == null)
               mParameters = DEFAULTPARAMETERS;
          
     }
     
     
     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairGenerator#generate()
      */
     public KeyPair generate()
     {
          BigInteger n = mParameters.getMN();
          BigInteger d;
          //1. Select a statistically unique and unpredictable integer d in the interval [1, n-1]. It is
          //acceptable to use a random or pseudorandom number.
          int bitLen = n.bitLength();
          int byteLen = bitLen/8 + ((bitLen%8)==0 ? 0 : 1 );
          int zeroBitLen = (byteLen << 3) - bitLen;
          byte[] kbytes = new byte[bitLen/8];
          do
          {
               for(int i = 0 ; i < zeroBitLen ; i++)
                    kbytes[0] = ((byte) (kbytes[0] & BITMASK[i]) ); 
               nextRandomBytes(kbytes);
               d = new BigInteger(1,kbytes);
          } while ( (d.compareTo(n) >= 0) || (d.compareTo(BigInteger.ONE) < 0) );
          //2. Compute the point Q = (xQ, yQ) = dG. (See Annex D.3.2.)
          
          ECGNUPoint Q = mParameters.getMG().multiply(d,mParameters.getMPreComputaion());
          //3. The key pair is (Q, d ), where Q is the public key, and d is the private key.
          return new KeyPair(new ECDSAPublicKey(mParameters,Q),
                             new ECDSAPrivateKey(mParameters,d));
     }
     
     private void nextRandomBytes(byte[] buffer) {
          
          PRNG.nextBytes(buffer);
     }
     
}
