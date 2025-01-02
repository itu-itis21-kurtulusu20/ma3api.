package tr.gov.tubitak.uekae.esya.api.common.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.util.Arrays;

public class ByteConversionUtil {

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static long bigEndianBytesToLong(final byte[] b) {
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result = (result << 8) | (b[i] & 0xFF);
        }
        return result;
    }

    public static long littleEndianBytesToLong(final byte[] b) {
        long result = 0;
        for (int i = 0; i < b.length; i++) {
            result |= (b[i] & 0xFFL) << (8*i);
        }
        return result;
    }

    public static long[] littleEndianBytesToLongArray(final byte[] b) {
        long[] result = new long[b.length/8];
        int j = 0;
        for (int i = 0; i < b.length/8; i++) {
            byte[] subArray = new byte[8];
            System.arraycopy(b, j, subArray, 0, 8);
            long bytesToLong = littleEndianBytesToLong(subArray);
            result[i] = bytesToLong;
            j = j+8;
        }
        return result;
    }

    public static byte[] booleanToBytes(boolean b) {
        byte[] result = new byte[1];
        result[0] = (byte) (b ? 0xFF : 0x00);
        return result;
    }

    public static boolean bytesToBoolean(final byte[] b) {
        return (b[0] & 0xFF) != 0;
    }

    public static byte[] objectToBytes(Object object) throws ESYAException {
        if(object instanceof Long){
            return ByteConversionUtil.longToBytes((Long)object);
        }else if(object instanceof Boolean){
            return ByteConversionUtil.booleanToBytes((Boolean) object);
        }else if(object instanceof byte[]){
            return (byte[]) object;
        }else if (object == null){
            return null;
        } else {
            throw new ESYAException("not implemented");
        }
    }
}
