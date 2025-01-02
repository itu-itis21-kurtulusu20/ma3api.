package tr.gov.tubitak.uekae.esya.api.asn.pkcs10;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.EPrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.Attributes;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.util.UtilExtensions;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:06 AM
 */
public class EAttributes extends BaseASNWrapper<Attributes> {

    public EAttributes(Attributes aObject)
    {
        super(aObject);
    }

    public EAttributes(byte[] aBytes) throws ESYAException {
        super(aBytes, new Attributes());
    }

    public EAttributes(EAttribute[] attributes) {
        super(new Attributes());
        mObject.elements = BaseASNWrapper.unwrapArray(attributes);
    }

    public boolean isAttributeExists(Asn1ObjectIdentifier attribute) {

        if ((mObject == null) || (mObject.elements == null))
            return false;

        for (Attribute element : mObject.elements)
            if (UtilEsitlikler.esitMi(attribute, element.type))
                return true;

        return false;
    }

    public void addAttribute(EAttribute attribute){
        if(mObject == null)
            mObject = new Attributes();

        if(mObject.elements == null)
            mObject.elements = new Attribute[]{attribute.getObject()};
        else{
            mObject.elements = BaseASNWrapper.extendArray(mObject.elements,attribute.getObject());
        }
    }
}
