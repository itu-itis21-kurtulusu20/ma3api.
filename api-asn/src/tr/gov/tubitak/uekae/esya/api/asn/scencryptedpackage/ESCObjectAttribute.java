package tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.SCObjectAttribute;

import java.io.InputStream;

public class ESCObjectAttribute extends BaseASNWrapper<SCObjectAttribute> {

    public ESCObjectAttribute(SCObjectAttribute aObject) {
        super(aObject);
    }

    public ESCObjectAttribute(byte[] aBytes, SCObjectAttribute aObject) throws ESYAException {
        super(aBytes, aObject);
    }

    public ESCObjectAttribute(String aBase64Encoded, SCObjectAttribute aObject) throws ESYAException {
        super(aBase64Encoded, aObject);
    }

    public ESCObjectAttribute(InputStream aStream, SCObjectAttribute aObject) throws ESYAException {
        super(aStream, aObject);
    }

    public ESCObjectAttribute(byte[] type, byte[] value){
        super(new SCObjectAttribute(type,value));
    }

    public byte[] getType(){
        return mObject.type.value;
    }

    public byte[] getValue(){
        return mObject.value.value;
    }
}
