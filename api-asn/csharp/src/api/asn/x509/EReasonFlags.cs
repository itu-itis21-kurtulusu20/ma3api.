using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EReasonFlags : BaseASNWrapper<ReasonFlags>
    {
        public EReasonFlags(ReasonFlags aObject)
            : base(aObject)
        {
        }

        public EReasonFlags(bool[] bitValues)
            : base(new ReasonFlags(bitValues))
        {
        }

        public EReasonFlags(int numbits, byte[] data)
            : base(new ReasonFlags(numbits, data))
        {
        }

        public EReasonFlags(byte[] aBytes)
            : base(aBytes, new ReasonFlags())
        {
        }

    }
}
