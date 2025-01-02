/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public static class RandomUtil
    {
        public static IRandomGenerator mRandomGenerator;

        public static void generateRandom(byte[] aBytesToFill)
        {
            if (mRandomGenerator == null)
                mRandomGenerator = Crypto.getRandomGenerator();
            mRandomGenerator.nextBytes(aBytesToFill);
        }

        public static byte[] generateRandom(int aLengh)
        {
            byte[] result = new byte[aLengh];
            if (mRandomGenerator == null)
                mRandomGenerator = Crypto.getRandomGenerator();
            mRandomGenerator.nextBytes(result);
            return result;
        }

        public static void generateRandom(byte[] aBytesToFill, int aOffset, int aLength)
        {
            if (mRandomGenerator == null)
                mRandomGenerator = Crypto.getRandomGenerator();
            mRandomGenerator.nextBytes(aBytesToFill, aOffset, aLength);
        }
    }
}
