package tr.gov.tubitak.uekae.esya.api.common.util;

import java.math.BigInteger;

public class BigIntegerUtil {

    public static byte[] toByteArrayWithoutSignByte(BigInteger aX)
    {
        if(aX.equals(BigInteger.ZERO)){
            return new byte[]{0};
        }

        byte[] xx = aX.toByteArray();
        if (xx[0] == 0)
        {
            byte[] temp = new byte[xx.length - 1];
            System.arraycopy(xx,
                    1,
                    temp,
                    0,
                    temp.length);
            xx = temp;
        }
        return xx;
    }

}
