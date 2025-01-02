using System;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class ByteUtil
    {

        public static int findIndex(byte[] content, int startIndex, byte[] searching)
        {
            int searchingIndex = 0;
            for (int i = startIndex; i < content.Length; i++)
            {
                if (content[i] == searching[searchingIndex])
                {
                    if (searching.Length == searchingIndex + 1)
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


        public static byte[] concatAll(byte[] first, params byte[][] rest)
        {
            int totalLength = first.Length;
            foreach (byte[] array in rest)
            {
                totalLength += array.Length;
            }
            byte[] result = new byte[totalLength];
            Array.Copy(first, 0, result, 0, Math.Min(first.Length, totalLength));
            int offset = first.Length;

            foreach (byte[] array in rest)
            {
                Array.Copy(array, 0, result, offset, array.Length);
                offset += array.Length;
            }

            return result;
        }

        public static byte[] copyofRange(byte[] source, int startIndex, int endIndex)
        {
            int len = endIndex - startIndex;
            byte[] dest = new byte[len];
            Array.Copy(source, startIndex, dest, 0, len);
            return dest;
        }


        public static int getHashCode(byte[] aBytes)
        {
            if (aBytes == null)
            {
                return 0;
            }

            int i = aBytes.Length;
            int hc = i + 1;

            while (--i >= 0)
            {
                hc *= 257;
                hc ^= aBytes[i];
            }

            return hc;

        }

        public static bool isAllZero(byte[] data)
        {
            if (data == null)
                return false;
           
            foreach (byte item in data)
              if (item != 0) { return false; }

            return true;                      
        }
    }
}
