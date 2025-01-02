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

import gnu.crypto.Registry;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;

import java.security.Key;
import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;


/**
 * @author mss
 *
 */
public abstract class ECDSAKey
          implements Key,ECKey
{

     private ECDomainParameter mParameters;
     
     /**
      * 
      */
     public ECDSAKey(ECDomainParameter aParameters)
     {
          super();

          mParameters = aParameters;
     }


     /* (non-Javadoc)
      * @see java.security.Key#getEncoded()
      */
     public abstract byte[] getEncoded();


     /* (non-Javadoc)
      * @see java.security.Key#getAlgorithm()
      */
     public String getAlgorithm()
     {
          return Registry.ECDSA_KPG;
     }


     /* (non-Javadoc)
      * @see java.security.Key#getFormat()
      */
     public String getFormat()
     {
          throw new RuntimeException();
     }

     

     public ECDomainParameter getMParameters()
     {
          return mParameters;
     }
     
     public ECParameterSpec getParams() //NOPMD
     {
          return mParameters;
     }

}
