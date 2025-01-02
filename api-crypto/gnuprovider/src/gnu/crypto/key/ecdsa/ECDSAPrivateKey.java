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

import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;

/**
 * @author mss
 *
 */
public class ECDSAPrivateKey
          extends ECDSAKey
          implements ECPrivateKey
{

     private BigInteger mD;

     public static ECDSAPrivateKey fromECPrivateKey(ECPrivateKey pubKey)
     {
          ECDSAKeyPairX509Codec ecdsaKeyPairX509Codec = new ECDSAKeyPairX509Codec();
          return (ECDSAPrivateKey) ecdsaKeyPairX509Codec.decodePrivateKey(pubKey.getEncoded());
     }
     /**
      * 
      */
     public ECDSAPrivateKey(ECDomainParameter aParameters,BigInteger aD)
     {
          super(aParameters);
          mD = aD;
     }
     

     public BigInteger getMD()
     {
          return mD;
     }
     
     public BigInteger getS()
     {
          return mD;
     }
     
     public byte[] getEncoded() {
          
          return new ECDSAKeyPairX509Codec().encodePrivateKey(this);
     }
}
