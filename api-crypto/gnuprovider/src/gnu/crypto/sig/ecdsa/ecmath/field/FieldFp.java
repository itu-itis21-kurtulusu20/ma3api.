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
public class FieldFp
          extends Field
{

     private BigInteger mP;


     private FieldFp(BigInteger aP)
     {
          super(aP);
          mP = aP;
     }


     public static synchronized FieldFp getInstance(BigInteger aP)
     {
          FieldFp f = (FieldFp) INSTANCES.get(aP);
          if (f == null)
          {
               f = new FieldFp(aP);
          }
          return f;
     }


     public BigInteger getMP()
     {
          return mP;
     }


     public BigInteger fromOctetToFieldElement(byte[] aOctet)
     {
          return new BigInteger(1, aOctet);
     }

}
