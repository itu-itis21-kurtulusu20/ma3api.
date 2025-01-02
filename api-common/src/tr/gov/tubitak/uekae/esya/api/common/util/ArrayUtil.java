package tr.gov.tubitak.uekae.esya.api.common.util;

import java.util.Arrays;

public class ArrayUtil {

    public static long[] concatArrays(long[] array1, long[] array2) {
        long[] resultArray = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    public static <T> T[] concatArrays(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
