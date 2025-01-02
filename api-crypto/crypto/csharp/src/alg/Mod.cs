/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class Mod
    {

        public static readonly Mod CBC = new Mod(_enum.CBC);
        public static readonly Mod CFB = new Mod(_enum.CFB);
        public static readonly Mod ECB = new Mod(_enum.ECB);
        public static readonly Mod OFB = new Mod(_enum.OFB);
        public static readonly Mod GCM = new Mod(_enum.GCM);
        public static readonly Mod NONE = new Mod(_enum.NONE);

        enum _enum
        {
            NONE,
            CBC,
            CFB,
            ECB,
            OFB,
            GCM
        }
        private readonly _enum mValue;
        private Mod(_enum aValue)
        {
            mValue = aValue;
        }
    }



}
