 package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Type;


public abstract class AttributeValue implements IAttribute
{
     private EAttribute mAttribute;
     protected Map<String,Object> mAttParams = new HashMap<String, Object>();
     protected Logger logger = null;
     /**
      * Create AttributeValue,default constructor
      */
     public AttributeValue ()
     {
    	  logger = LoggerFactory.getLogger(getClass());
    	  mAttribute = new EAttribute(new Attribute());
          mAttribute.setType(getAttributeOID());
     }
     /**
   	 * Set parameters
   	 * @param aParameterAndValue 
   	 */
 	public void setParameters(Map<String, Object> aParameterAndValue) {
 		mAttParams = aParameterAndValue;
 	}
 	
 	protected void _setValue(Asn1Type aValue) throws CMSSignatureException
    {
         _setValue(new Asn1Type[]
                      {aValue});
    }
     
 	protected void _setValue(Asn1Type... aValue)
 	throws CMSSignatureException
 	{
          for (int i = 0; i < aValue.length; i++)
          {
               Asn1Type deger = aValue[i];
               Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
               try
               {
                    deger.encode(encBuf);
               } catch (Asn1Exception ex1)
               {
                    throw new CMSSignatureException("Asn1 encode error", ex1);
               }
               
               mAttribute.addValue(encBuf.getMsgCopy());
          }
          
 	}
    /**
     * @return attribute
     */
 	public EAttribute getAttribute()
 	{
 		return mAttribute;
 	}
}