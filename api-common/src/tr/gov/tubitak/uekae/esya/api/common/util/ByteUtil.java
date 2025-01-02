package tr.gov.tubitak.uekae.esya.api.common.util;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by orcun.ertugrul on 15-Dec-17.
 */
public class ByteUtil
{
    public static int findIndex(byte [] content, int startIndex, byte [] searching)
    {
        int searchingIndex = 0;
        for (int i = startIndex; i < content.length; i++) {
            if (content[i] == searching[searchingIndex])
            {
                if (searching.length == searchingIndex + 1)
                    return i - searchingIndex;
                searchingIndex++;
            }
            else
            {
                searchingIndex = 0;
            }
        }

        return -1;
    }

    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        System.arraycopy(first, 0, result, 0, Math.min(first.length, totalLength));
        int offset = first.length;

        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static byte[] byteToByteArray(byte first, byte... others){
        int totalLength = 1 ; // first her zaman 1
        int index = 1;
        for(byte number : others){
            totalLength += 1;
        }
        byte[] result = new byte[totalLength]; // toplam concat edilecek byte sayisi kadar array
        result[0] = first;

        for(byte a : others){
            result[index] = a;
            index++;
        }

        return result;
    }

    public static byte [] xor(byte [] b1, byte[] b2){
        if(b1.length != b2.length)
            throw new IllegalArgumentException("Byte length must be equal");

        byte [] result = xor(b1,0,b2,0,b1.length);

        return result;
    }


    public static byte [] xor(byte [] b1, int b1StartIndex, byte[] b2, int b2StartIndex, int len){

        byte [] result = new byte[len];

        for(int i=0; i < result.length; i++){
            result[i] = (byte) (b1[i+b1StartIndex]^ b2[i+b2StartIndex]);
        }

        return result;
    }

    /**
     * Verilen bir sayıyı byte array'e dönüştürür.
     * @param input long cinsinden veri
     * @return
     */
    public static byte[] numberToByteArray(long input, int byteLen)
    {
        ByteBuffer buf = ByteBuffer.allocate(byteLen); // Long değer 8 byte
        buf.putLong(0, input);
        buf.flip();
        return buf.array();
    }


    public static boolean isAllZero(final byte[] data) {
        if(data == null)
            return false;
        else{
            for (byte item : data)
                if (item != 0) { return false;}
            return true;
        }
    }

    public static byte[] concatAllObjects(Object...bytes){
        List<byte []> byteArrayList = new ArrayList<byte[]>();

        for (Object aByte:bytes) {
            if(aByte instanceof String){
                byte[] stringBytes = StringUtil.hexToByte((String) aByte);
                byteArrayList.add(stringBytes);
            } else if(aByte instanceof Byte){
                byte [] oneByte = new byte []{(Byte)aByte};
                byteArrayList.add(oneByte);
            }else if(aByte instanceof byte []){
                byteArrayList.add((byte [])aByte);
            }else if(aByte instanceof Integer){
                Integer integer = (Integer) aByte;
                byte [] oneByte = new byte []{integer.byteValue()};
                byteArrayList.add(oneByte);
            }
            else{
                throw new ESYARuntimeException("Unsupported type to concat for byte array: " + aByte.getClass());
            }
        }

        int totalLength = 0;
        for (byte[] byteArray : byteArrayList) {
            totalLength += byteArray.length;
        }

        byte [] combinedArray = new byte[totalLength];

        int offset = 0;
        for (byte[] byteArray : byteArrayList) {
            System.arraycopy(byteArray, 0, combinedArray, offset, byteArray.length);
            offset += byteArray.length;
        }

        return combinedArray;
    }
}
