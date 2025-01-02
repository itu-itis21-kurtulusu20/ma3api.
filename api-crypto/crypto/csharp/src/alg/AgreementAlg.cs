/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.alg
{
    public class AgreementAlg
    {
        public static readonly AgreementAlg DIFFIE_HELLMAN = new AgreementAlg(Enum.DIFFIE_HELLMAN);
        public static readonly AgreementAlg ECDH = new AgreementAlg(Enum.ECDH);
        public static readonly AgreementAlg ECCDH = new AgreementAlg(Enum.ECCDH);
        public static readonly AgreementAlg ECMQV = new AgreementAlg(Enum.ECMQV);

        private enum Enum
        {
            DIFFIE_HELLMAN,
            ECDH,
            ECCDH,
            ECMQV
        }
        private readonly Enum _mValue;
        private AgreementAlg(Enum aValue)
        {
            _mValue = aValue;
        }

    }
}
