/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class Padding
    {
        public static readonly Padding NONE = new Padding();
        public static readonly Padding PKCS1 = new Padding();
        public static readonly Padding PKCS7 = new Padding();

        internal Padding(){}        

    }

}
