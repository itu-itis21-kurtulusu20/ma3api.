package tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.SCObject;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.SCObjectAttribute;

import java.io.InputStream;

public class ESCObject extends BaseASNWrapper<SCObject> {
    public ESCObject(SCObject aObject) {
        super(aObject);
    }

    public ESCObject(byte[] aBytes, SCObject aObject) throws ESYAException {
        super(aBytes, aObject);
    }

    public ESCObject(String aBase64Encoded, SCObject aObject) throws ESYAException {
        super(aBase64Encoded, aObject);
    }

    public ESCObject(InputStream aStream, SCObject aObject) throws ESYAException {
        super(aStream, aObject);
    }

    public ESCObject(ESCObjectAttribute[] escObjectAttribute){
        super(null);
        SCObject scObject = new SCObject(convertESCObjectAttributeToSCObjectAttribute(escObjectAttribute));
        super.mObject = scObject;
    }

    private SCObjectAttribute[] convertESCObjectAttributeToSCObjectAttribute(ESCObjectAttribute[] escObjectAttribute){
        SCObjectAttribute[] scObjectAttributes = new SCObjectAttribute[escObjectAttribute.length];
        for (int i = 0; i < escObjectAttribute.length; i++) {
            scObjectAttributes[i] = escObjectAttribute[i].getObject();
        }
        return scObjectAttributes;
    }

    public ESCObjectAttribute[] getESCObjectAttributes(){
        int length = mObject.getLength();
        ESCObjectAttribute[] escObjectAttributes = new ESCObjectAttribute[length];

        for (int i = 0; i < length; i++) {
            escObjectAttributes[i] = new ESCObjectAttribute(mObject.elements[i]);
        }

        return escObjectAttributes;
    }
}
