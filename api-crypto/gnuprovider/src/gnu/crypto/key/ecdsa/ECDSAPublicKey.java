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
import gnu.crypto.sig.ecdsa.ecmath.curve.ECGNUPoint;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;

/**
 * @author mss
 *
 */
public class ECDSAPublicKey
          extends ECDSAKey
          implements ECPublicKey
{
     private final ECGNUPoint mQ;

     public static ECDSAPublicKey fromECPublicKey(ECPublicKey pubKey)
     {
          ECDSAKeyPairX509Codec ecdsaKeyPairX509Codec = new ECDSAKeyPairX509Codec();
          return (ECDSAPublicKey)ecdsaKeyPairX509Codec.decodePublicKey(pubKey.getEncoded());
     }


     /**
      * 
      */
     public ECDSAPublicKey(ECDomainParameter aParameters,ECGNUPoint aQ)
     {
          super(aParameters);
          mQ = aQ;
     }


     public ECGNUPoint getMQ()
     {
          return mQ;
     }
     
     public ECPoint getW()
     {
          return mQ;
     }
     
     public byte[] getEncoded() {
          return new ECDSAKeyPairX509Codec().encodePublicKey(this);
     }
}
