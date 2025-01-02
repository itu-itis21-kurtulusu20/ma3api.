using System;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    public class UniqueIdGenerator : IdGenerator
    {
        public override String uret(String aIdType)
        {
            return aIdType + "-Id-" + Guid.NewGuid().ToString();
        }

        public override void update(String id)
        {
        }

        public static void main(String[] args)
        {
            String id = new UniqueIdGenerator().uret("Signature");
            Console.WriteLine(id);
        }
    }
}
