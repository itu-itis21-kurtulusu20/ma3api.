/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class DerivationFunctionType
    {
        public static readonly DerivationFunctionType ECDHKEK = new DerivationFunctionType(_enum.ECDHKEK);        
        public static readonly DerivationFunctionType MGF1 = new DerivationFunctionType(_enum.MGF1);
        public static readonly DerivationFunctionType DHKEK = new DerivationFunctionType(_enum.X9_63);
        enum _enum
        {
            ECDHKEK,
            MGF1,
            X9_63
        }
        private readonly _enum mValue;
        private DerivationFunctionType(_enum aValue)
        {
            mValue = aValue;
        }
    }
}
