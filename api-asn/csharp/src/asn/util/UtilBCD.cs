using System;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilBCD
    {
        public static byte[] convertToBCD(int aValue)
        {
            StringBuilder stringBuilder = new StringBuilder(aValue.ToString());
            if (stringBuilder.Length < 2)
                stringBuilder.Insert(0, 0);
            byte[] bytes = new byte[2];
            for (int i = 0; i < stringBuilder.Length; i++)
            {
                bytes[i] = Byte.Parse(stringBuilder.ToString().Substring(i, 1));
            }
            return bytes;
        }

        public static byte[] date(int aYear, int aMonth, int aDay)
        {

            int year = aYear - 2000;

            byte[] yearBytes = convertToBCD(year);
            byte[] monthBytes = convertToBCD(aMonth);
            byte[] dayBytes = convertToBCD(aDay);
            byte[] date = new byte[yearBytes.Length + monthBytes.Length + dayBytes.Length];
            Array.Copy(yearBytes, 0, date, 0, yearBytes.Length);

            int offset = yearBytes.Length;
            Array.Copy(monthBytes, 0, date, offset, monthBytes.Length);
            offset += monthBytes.Length;
            Array.Copy(dayBytes, 0, date, offset, dayBytes.Length);

            return date;

        }

        public static byte year(int aYear)
        {
            String year = aYear.ToString();
            year = year.Substring(year.Length - 2);
            byte highNibble = Byte.Parse(year[0].ToString());
            highNibble <<= 4;
            byte lowNibble = Byte.Parse(year[0].ToString());
            return (byte)(highNibble | lowNibble);
        }
    }
}
