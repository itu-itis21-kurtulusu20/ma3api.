package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.x509.AttributeTypeAndValue;

/**
 * @author ayetgin
 */
public class EAttributeTypeAndValue extends BaseASNWrapper<AttributeTypeAndValue>
{
    public EAttributeTypeAndValue(AttributeTypeAndValue aObject)
    {
        super(aObject);
    }

    public Asn1ObjectIdentifier getType(){
        return mObject.type;
    }

    public byte[] getValue(){
        return mObject.value.value;
    }
    
}
