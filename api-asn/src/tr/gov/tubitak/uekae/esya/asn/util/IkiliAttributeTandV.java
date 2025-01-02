/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1Choice;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Type;

/**
 * @author mss
 *
 */
public class IkiliAttributeTandV
{

     public String mst;
     public Asn1Type mType;
     public Asn1ObjectIdentifier mOid;

     IkiliAttributeTandV (String ast, Asn1Type aType, Asn1ObjectIdentifier aOid)
     {
          this.mst = ast;
          this.mType = aType;
          this.mOid = aOid;
     }


     String stringValue ()
     {
          if (mType == null)
               return "";
          if (mType instanceof Asn1Choice)
               return ( (Asn1Choice) mType).getElement().toString();
          return mType.toString();
     }

}
