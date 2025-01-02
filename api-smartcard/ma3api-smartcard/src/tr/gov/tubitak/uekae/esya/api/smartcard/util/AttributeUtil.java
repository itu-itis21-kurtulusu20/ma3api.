package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class AttributeUtil {

    public static String getStringValue(Object value) {
        if(value == null)
            return null;

        if(value instanceof String)
            return (String) value;

        if(value instanceof byte [])
            return new String((byte[]) value);

        if (value instanceof char[]) {
            char[] chars = (char[]) value;

            CharBuffer charBuff = CharBuffer.wrap(chars);

            if (StandardCharsets.US_ASCII.newEncoder().canEncode(charBuff)) {
                return new String(chars);
            }

            if (StandardCharsets.UTF_8.newEncoder().canEncode(charBuff)) {
                byte[] bytes = new byte[chars.length];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) chars[i];
                }

                return new String(bytes);
            }
        }

        return value.toString();
    }

    public static CK_ATTRIBUTE[] createAttributesFromTypes(long[] attributeTypes){
        CK_ATTRIBUTE[] ckAttributes = new CK_ATTRIBUTE[attributeTypes.length];

        for (int i = 0; i < attributeTypes.length; i++) {
            ckAttributes[i] = new CK_ATTRIBUTE(attributeTypes[i]);
        }
        return ckAttributes;
    }
}
