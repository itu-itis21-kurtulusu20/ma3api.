using System.Text;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    /**
 * Conversion util between int[] OID s and their urn representation.
 *
 * @author ahmety
 * date: Aug 28, 2009
 */
    public static class OIDUtil
    {
        public static string concat(int[] aOID)
        {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < aOID.Length; i++)
            {
                b.Append(aOID[i]);
                if (i < aOID.Length - 1)
                    b.Append('.');
            }
            return b.ToString();
        }

        public static string toURN(int[] aOID)
        {
            StringBuilder b = new StringBuilder("urn:oid:");
            b.Append(concat(aOID));
            return b.ToString();
        }

        public static int[] fromURN(string aOID)
        {
            return parse(aOID.Substring("urn:oid:".Length));
        }

        public static int[] parse(string aInput)
        {
            string[] numbers = aInput.Split('.');
            int[] oid = new int[numbers.Length];
            for (int i = 0; i < numbers.Length; i++)
            {
                oid[i] = int.Parse(numbers[i]);
            }
            return oid;
        }

        public static bool Equals(int[] oid1, int[] oid2)
        {
            bool result = ((oid1 != null) && (oid2 != null));
            if (!result) return false;
            int l = oid1.Length;
            result = (l == oid2.Length);
            if (!result) return false;
            for (int i = 0; i < l; i++)
            {
                if (oid1[i] != oid2[i])
                    return false;
            }
            return true;
        }

        //public static void main(String[] args)
        //{
        //    int[] oid = fromURN("urn:oid:1.2.840.10045.3.1.7");
        //    String oidStr = toURN(oid);
        //    int[] parsedOid = fromURN(oidStr);
        //    String finalStr = toURN(parsedOid);
        //    Console.WriteLine("> " + finalStr);
        //}
    }
}
