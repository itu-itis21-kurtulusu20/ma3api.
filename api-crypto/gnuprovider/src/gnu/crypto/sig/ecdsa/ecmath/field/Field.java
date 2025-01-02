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
import java.security.spec.ECField;
import java.util.Hashtable;

/**
 * @author mss
 * 
 */
public abstract class Field
          implements ECField
{
     protected static final Hashtable INSTANCES = new Hashtable();

     protected final BigInteger mSize;
     protected final BigInteger mFieldSizeMinusOne;


     protected Field(BigInteger aSize)
     {
          mSize = aSize;
          mFieldSizeMinusOne = mSize.subtract(BigInteger.ONE);
          INSTANCES.put(aSize,
                        this);
     }


     public BigInteger getMSize()
     {
          return mSize;
     }


     public int getFieldSize()
     {
          return mFieldSizeMinusOne.bitLength();
     }


     public boolean equals(Object aObject)
     {
          if (( aObject != null) && ( this.getClass().isInstance(aObject)) && ( ( aObject == this) || ( ( (Field) aObject).mSize.equals(mSize)))) { return true; }

          return false;
     }


     public int hashCode()
     {
          return mSize.hashCode();
     }


     abstract public BigInteger fromOctetToFieldElement(byte[] aOctet);

}
