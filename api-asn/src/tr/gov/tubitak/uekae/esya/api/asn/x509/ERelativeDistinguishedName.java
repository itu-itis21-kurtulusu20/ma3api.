package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.asn.util.UtilAtav;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;
import tr.gov.tubitak.uekae.esya.asn.x509.RelativeDistinguishedName;

/**
 * @author ayetgin
 */
public class ERelativeDistinguishedName extends BaseASNWrapper<RelativeDistinguishedName>
{

    public ERelativeDistinguishedName(RelativeDistinguishedName aObject)
    {
        super(aObject);
    }

    public AttributeTypeAndValue[] getAttibutes() 
    {
	    return mObject.elements;
    }

    public String getCommonNameAttribute()
    {
    	 for (AttributeTypeAndValue av : mObject.elements)
         {
             if (av.type.equals(Constants.EXP_ID_COMMON_NAME))
             {
            	 return UtilAtav.atavValue2String(av);
             }
         }
         return null;
    }
    
/*    public String getEmailAttribute()
    {
        for (AttributeTypeAndValue av : mObject.elements)
        {
            if (av.type.equals(Constants.EXP_ID_EMAIL_ADDRESS))
                return new String(av.value.value).trim();
        }
        return null;
    }

    public String getSerialNumberAttribute()
    {
        for (AttributeTypeAndValue av : mObject.elements)
        {
            if (av.type.equals(Constants.EXP_ID_AT_SERIAL_NUMBER))
                return new String(av.value.value).trim();
        }
        return null;
    }*/

    public String getStringAttribute(Asn1ObjectIdentifier oid) {
        for (AttributeTypeAndValue av : mObject.elements)
        {
            if (av.type.equals(oid))
            	return UtilAtav.atavValue2String(av);
        }
        return null;
    }
}
