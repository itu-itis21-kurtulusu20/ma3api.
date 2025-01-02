using System;
using System.Text;
using System.Linq;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class StringUtil
    {
        /**
     * <p>Returns a string of hexadecimal digits from a byte array. Each byte is
     * converted to 2 hex symbols; zero(es) included.</p>
     *
     * <p>This method calls the method with same name and three arguments as:</p>
     *
     * <pre>
     *    toString(ba, 0, ba.length);
     * </pre>
     *
     * @param ba the byte array to convert.
     * @return a string of hexadecimal characters (two for each byte)
     * representing the designated input byte array.
     */
        public static string ToString(byte[] ba)
        {
            return ToString(ba, 0, ba.Length);
        }

        /**
         * <p>Returns a string of hexadecimal digits from a byte array, starting at
         * <code>offset</code> and consisting of <code>length</code> bytes. Each byte
         * is converted to 2 hex symbols; zero(es) included.</p>
         *
         * @param ba the byte array to convert.
         * @param offset the index from which to start considering the bytes to
         * convert.
         * @param length the count of bytes, starting from the designated offset to
         * convert.
         * @return a string of hexadecimal characters (two for each byte)
         * representing the designated input byte sub-array.
         */
        public static string ToString(byte[] ba, int offset, int length)
        {
            return BitConverter.ToString(ba, offset, length).Replace("-", String.Empty);
        }

        public static string ToHexString(byte[] ba, int offset, int length)
        {
            return ToString(ba, offset, length);
        }

        public static string ToHexString(byte[] ba)
        {
            return ToString(ba);
        }

        public static string FromByteArray(byte[] ba)
        {
            StringBuilder hex = new StringBuilder(ba.Length*2);

            foreach (byte b in ba)
                hex.Append(b.ToString("X2")); // <-- ToString is faster than AppendFormat   

            return hex.ToString();
        }

        /** 
         * Convert string to byte array
         * @param s
         */
        public static byte[] ToByteArray(string hex)
        {
            return Enumerable.Range(0, hex.Length).
                   Where(x => 0 == x % 2).
                   Select(x => Convert.ToByte(hex.Substring(x, 2), 16)).
                   ToArray();
        }

        public static string substring(string aSource, int aCharCount)
        {
            if (aSource == null)
            {
                return null;
            }
            string subStr = aSource.Substring(0, Math.Min(aSource.Length, aCharCount - 3));
            if (aSource.Length > aCharCount)
            {
                subStr = subStr + "...";
            }

            return subStr;
        }

        public static string substring(byte[] aSource, int aCharCount)
        {
            if (aSource == null)
            {
                return null;
            }

            int len = Math.Min(aSource.Length, aCharCount - 3);
            byte[] bytes = new byte[len];

            Array.Copy(aSource, 0, bytes, 0, len);

            string subStr = Encoding.UTF8.GetString(bytes);

            if (aSource.Length > aCharCount)
            {
                subStr = subStr + "...";
            }

            return subStr;
        }

        public static byte[] hexToByte(String s)
        {
            return ToByteArray(s);
        }
    }
}
