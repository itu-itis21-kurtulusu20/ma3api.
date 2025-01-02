package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.AttributeUtil;

public class P11Object {

    private long objectId;
    //Label can be defined in different types like byte [], char [], String. If we convert it to String, the object can not be found for different types while searching.
    private Object label;
    private ObjectType type;


    public P11Object(CK_ATTRIBUTE[] attrs) {
        init(0, attrs);
    }

    public P11Object(long objectId, CK_ATTRIBUTE[] attrs) {
        init(objectId, attrs);
    }

    public P11Object(String label, ObjectType type) {
        this.label = label;
        this.type = type;
    }

    public P11Object(String label, long classCode) {
        init(label, classCode);
    }

    public void init(long objectId, CK_ATTRIBUTE[] attrs) {
        this.objectId = objectId;
        for (CK_ATTRIBUTE attr : attrs) {
            if (attr.type == PKCS11Constants.CKA_LABEL) {
                label = attr.pValue;
            } else if (attr.type == PKCS11Constants.CKA_CLASS) {
                type = ObjectType.get((long) attr.pValue);
            } else if (attr.type == PKCS11Constants.CKA_OBJECT_ID) {
                this.objectId = (long) attr.pValue;
            }
        }
    }

    public void init(String label, long classCode) {
        this.label = label;
        this.type = ObjectType.get(classCode);
    }

    public String getLabel() {
        return AttributeUtil.getStringValue(label);
    }

    public Object getLabelObj() {
        return this.label;
    }

    public long getObjectId() {
        return this.objectId;
    }

    public ObjectType getType() {
        return type;
    }

    public CK_ATTRIBUTE [] getAsAttributes() {
        CK_ATTRIBUTE [] attrs;

        if(type == ObjectType.UnKnown){
            attrs = new CK_ATTRIBUTE[1];
        } else {
            attrs = new CK_ATTRIBUTE[2];
            attrs[1] = new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, type.value);
        }

        attrs[0] = new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, label);


        return attrs;
    }

    public static CK_ATTRIBUTE [] getAttributesToFilled() {
        CK_ATTRIBUTE[] attrs = new CK_ATTRIBUTE[2];
        attrs[0] = new CK_ATTRIBUTE();
        attrs[0].type = PKCS11Constants.CKA_LABEL;
        attrs[1] = new CK_ATTRIBUTE();
        attrs[1].type = PKCS11Constants.CKA_CLASS;
        return attrs;
    }

    public enum ObjectType {
        Data(0), // CKO_DATA
        Certificate(1), // PKCS11Constants_Fields.CKO_CERTIFICATE
        PublicKey(2), // CKO_PUBLIC_KEY
        PrivateKey(3), // CKO_PRIVATE_KEY
        SecretKey(4), // CKO_SECRET_KEY
        HwFeature(5), // CKO_HW_FEATURE
        DomainParameters(6), // CKO_DOMAIN_PARAMETERS
        Mechanism(7), // CKO_MECHANISM
        UnKnown(-1);

        private long value;

        ObjectType(long l) {
            value = l;
        }

        public static ObjectType get(Long l) {
            ObjectType[] types = ObjectType.values();
            for(ObjectType type : types) {
                if(type.value == l)
                    return type;
            }
            return UnKnown;
        }
    }
}
