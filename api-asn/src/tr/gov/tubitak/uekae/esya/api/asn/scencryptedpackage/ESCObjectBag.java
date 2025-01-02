package tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.SCObject;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.SCObjectBag;

import java.io.InputStream;

public class ESCObjectBag extends BaseASNWrapper<SCObjectBag> {
    public ESCObjectBag(SCObjectBag aObject) {
        super(aObject);
    }

    public ESCObjectBag(byte[] aBytes, SCObjectBag aObject) throws ESYAException {
        super(aBytes, aObject);
    }

    public ESCObjectBag(String aBase64Encoded, SCObjectBag aObject) throws ESYAException {
        super(aBase64Encoded, aObject);
    }

    public ESCObjectBag(InputStream aStream, SCObjectBag aObject) throws ESYAException {
        super(aStream, aObject);
    }

    public ESCObjectBag(ESCObject[] escObjects) {
        super(null);
        SCObjectBag scObjectBag = new SCObjectBag(convertESCObjectToSCObject(escObjects));
        super.mObject = scObjectBag;
    }

    public ESCObjectBag(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new SCObjectBag());
    }

    private SCObject[] convertESCObjectToSCObject(ESCObject[] escObjects){
        SCObject[] scObjects = new SCObject[escObjects.length];
        for (int i = 0; i < escObjects.length; i++) {
            scObjects[i] = escObjects[i].getObject();
        }
        return scObjects;
    }

    public ESCObject[] getESCObjects(){
        int length = mObject.getLength();
        ESCObject[] escObjects = new ESCObject[length];

        for (int i = 0; i < length; i++) {
             escObjects[i] = new ESCObject(mObject.elements[i]);
        }

        return escObjects;
    }
}
