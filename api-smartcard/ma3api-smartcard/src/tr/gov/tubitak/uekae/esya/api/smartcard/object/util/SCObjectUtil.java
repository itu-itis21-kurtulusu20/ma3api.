package tr.gov.tubitak.uekae.esya.api.smartcard.object.util;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.ESCObject;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.ESCObjectAttribute;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteConversionUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

public class SCObjectUtil {

    public static long getClassInESCObject(ESCObject escObject) throws SmartCardException {
        for (ESCObjectAttribute escObjectAttribute : escObject.getESCObjectAttributes()) {
            long type = ByteConversionUtil.bigEndianBytesToLong(escObjectAttribute.getType());
            if(type == PKCS11Constants.CKA_CLASS){
                return ByteConversionUtil.littleEndianBytesToLong(escObjectAttribute.getValue());
            }
        }
        throw new SmartCardException("Object's class was not found");
    }

    public static ESCObject encodeESCObject(byte[] wrappedKey, CK_ATTRIBUTE[] ck_attributes) throws ESYAException {
        ESCObjectAttribute[] escObjectAttributesWithWrappedKey = new ESCObjectAttribute[ck_attributes.length+1];

        ESCObjectAttribute[] escObjectAttributes = convertCK_ATTRIBUTEArrayToESCObjectAttributeArray(ck_attributes);

        System.arraycopy(escObjectAttributes, 0, escObjectAttributesWithWrappedKey, 0, escObjectAttributes.length);

        byte[] type = ByteConversionUtil.objectToBytes(PKCS11Constants.CKA_VALUE);
        escObjectAttributesWithWrappedKey[ck_attributes.length] = new ESCObjectAttribute(type, wrappedKey);

        ESCObject scObject = new ESCObject(escObjectAttributesWithWrappedKey);
        return scObject;
    }

    public static ESCObject encodeESCObject(CK_ATTRIBUTE[] ck_attributes) throws ESYAException {
        ESCObjectAttribute[] escObjectAttributes = convertCK_ATTRIBUTEArrayToESCObjectAttributeArray(ck_attributes);
        ESCObject scObject = new ESCObject(escObjectAttributes);
        return scObject;
    }

    public static ESCObjectAttribute[] convertCK_ATTRIBUTEArrayToESCObjectAttributeArray(CK_ATTRIBUTE[] ck_attributes) throws ESYAException {
        ESCObjectAttribute[] scObjectAttributes = new ESCObjectAttribute[ck_attributes.length];

        for (int i = 0; i < ck_attributes.length; i++) {
            byte[] type = ByteConversionUtil.objectToBytes(ck_attributes[i].type);
            byte[] value = ByteConversionUtil.objectToBytes(ck_attributes[i].pValue);

            scObjectAttributes[i] = new ESCObjectAttribute(type, value);
        }

        return scObjectAttributes;
    }
}
