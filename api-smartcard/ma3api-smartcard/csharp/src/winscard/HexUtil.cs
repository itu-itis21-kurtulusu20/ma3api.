using System;
using System.Text;


namespace tr.gov.tubitak.uekae.esya.api.smartcard.winscard
{
    public class HexUtil
    {

        private HexUtil()
        {
            // private ctor prevents instantiation
        }

        public static String HexToAscii(String hex)
        {
            StringBuilder sb = new StringBuilder();

            if (hex.Length % 2 != 0)
            {
                return "";
            }
            for (int i = 0; i < hex.Length; i += 2)
            {
                sb.Append((char)byte.Parse(hex.Substring(i, 2), System.Globalization.NumberStyles.HexNumber));
            }
            return sb.ToString();
        }

        public static byte[] HexToBin(String hex)
        {
            hex = hex.Replace(" ", "");

            byte[] buffer = new byte[hex.Length / 2];

            if (hex.Length % 2 != 0)
            {
                throw new Exception("Uneven hex string length.");
            }
            for (int i = 0, j = 0; i < hex.Length; i += 2, j++)
            {
                buffer[j] = byte.Parse(hex.Substring(i, 2), System.Globalization.NumberStyles.HexNumber);
            }
            return buffer;
        }

        public static String BinToHex(byte[] buffer)
        {
            return BinToHex(buffer, 0, buffer.Length);
        }

        public static String BinToHex(byte[] buffer, int offset, int length)
        {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < length; i++)
            {
                sb.Append(BinToHex(buffer[i + offset]));
            }

            return sb.ToString();
        }

        public static String BinToHex(byte b)
        {
            return String.Format("{0:X2}", b);
        }

    }
}

