using System;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.src.util
{
    public class AttributeUtil
    {

        public static String getStringValue(Object value)
        {
            if (value == null)
                return null;

            if (value is String)
                return (String)value;

            if (value is byte[])
                return System.Text.Encoding.Default.GetString((byte[]) value);

            if (value is char[])
                return new String((char[])value);

            return value.ToString();
        }

        public static long byteArrayToLong(byte[] arr)
        {
            long value = 0;
            if (arr.Length == 4)
                value = BitConverter.ToInt32(arr, 0);
            else if (arr.Length == 8)
                value = BitConverter.ToInt64(arr, 0);
            else
                throw new SmartCardException("UnKnown Long Len!");

            return value;
        }
    }
}
