namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class ArrayUtil
    {
        public static bool Equals(byte[] expected, byte[] actual)
        {
            if (expected == actual)
                return true;

            bool result = ((expected != null) && (actual != null));
            if (result)
            {
                int l = expected.Length;
                result = (l == actual.Length);
                if (result)
                {
                    for (int i = 0; i < l; i++)
                    {
                        if (expected[i] != actual[i])
                            return false;
                    }
                }
            }
            return result;
        }
    }
}
