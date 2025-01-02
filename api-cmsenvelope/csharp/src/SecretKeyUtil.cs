using System;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    static internal class SecretKeyUtil
    {

        public static void eraseSecretKey(byte[] key)
        {
            try
            {
                //clear the underlying key from memory
                //it was done due to meet the requirements of crypto analysis
                //this is so ugly
                fill(key, (byte) 0XCC);
                fill(key, (byte) 0XCC);
            }
            catch (Exception e)
            {
                throw new Exception("Cannot erase the key " + e.Message);
            }
        }

        public static void fill(byte[] a, byte val)
        {
            for (int i = 0, len = a.Length; i < len; i++)
                a[i] = val;
        }
    }
}
