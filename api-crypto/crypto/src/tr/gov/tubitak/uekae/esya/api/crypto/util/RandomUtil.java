package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.RandomGenerator;

/**
 * @author ayetgin
 */

public class RandomUtil
{
    public static RandomGenerator mRandomGenerator;

    public static void generateRandom(byte[] aBytesToFill)
    {
        if (mRandomGenerator==null)
            mRandomGenerator = Crypto.getRandomGenerator();
        mRandomGenerator.nextBytes(aBytesToFill);
    }

    public static byte[] generateRandom(int aLengh)
    {
        byte[] result = new byte[aLengh];
        if (mRandomGenerator==null)
            mRandomGenerator = Crypto.getRandomGenerator();
        mRandomGenerator.nextBytes(result);
        return result;
    }

    public static String generateRandomHexString(int byteLen){
        byte[] bytes = generateRandom(byteLen);
        return StringUtil.toHexString(bytes);
    }

    public static void generateRandom(byte[] aBytesToFill, int aOffset, int aLength)
    {
        if (mRandomGenerator==null)
            mRandomGenerator = Crypto.getRandomGenerator();
        mRandomGenerator.nextBytes(aBytesToFill, aOffset, aLength);
    }
}
