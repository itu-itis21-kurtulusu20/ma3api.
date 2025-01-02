package tr.gov.tubitak.uekae.esya.api.common.asn1;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.Arrays;

public class TLVUtil {

    public static TLVInfo parseField(byte[] asn1Data, int startIndex) throws ESYAException {
        int tag = toUnsignedInt(asn1Data[startIndex++]);
        return parseLen(tag, asn1Data, startIndex);
    }

    public static TLVInfo parseField(byte [] asn1Data, int startIndex, int fieldIndex) throws ESYAException {
        int index = startIndex;
        int tag = toUnsignedInt(asn1Data[index++]);
        index = parseLen(tag, asn1Data, index).getDataStartIndex();
        for(int i = 0; i < fieldIndex; i++){
            index = skipField(asn1Data, index);
        }

        return parseField(asn1Data, index);
    }

    public static TLVInfo parseFieldInsideTag(byte [] asn1Data, int startIndex, int fieldIndex) throws ESYAException {
        int index = startIndex;
        for(int i = 0; i < fieldIndex; i++){
            index = skipField(asn1Data, index);
        }
        return parseField(asn1Data, index);
    }

    private static TLVInfo parseLen(int tag, byte[] asn1Data, int index) {
        int len = 0;
        int firstLenByte = toUnsignedInt(asn1Data[index++]);
        if(firstLenByte < 0x80){
            len = firstLenByte;
        } else {
            int lenLen = firstLenByte & 0x7F;
            for(int i=0; i < lenLen ; i++){
                len = len * 256;
                len = len + toUnsignedInt(asn1Data[index++]);
            }
        }

        return new TLVInfo(tag, index, len);
    }


    /*
    * TLV info tag and encoding tag may differ. So tag is also taken. give -1 for tag in order to use tag inside TLVInfo.
    * */
    public static byte [] encode(int tag, byte [] asn1Data, TLVInfo tlvInfo) {
        byte[] encoded = Arrays.copyOfRange(asn1Data, tlvInfo.getTagStartIndex(), tlvInfo.getDataEndIndex());
        if(tag != -1)
            encoded[0] = (byte) tag;
        return encoded;
    }

    private static int skipField(byte[] asn1Data, int index) {
        int tag = toUnsignedInt(asn1Data[index++]);
        return parseLen(tag, asn1Data, index).getDataEndIndex();
    }

    //Android 5,6,7 desteği için Byte.toUnsignedInt fonksiyonu yerine bu fonksiyon eklendi.
    private static int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }

}
