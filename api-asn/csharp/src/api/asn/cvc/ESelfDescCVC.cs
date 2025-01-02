using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ESelfDescCVC : BaseASNWrapper<SelfDescCVC>
    {
        public ESelfDescCVC(byte[] aEncoded)
            : base(aEncoded, new SelfDescCVC())
        {
        }

        public ESelfDescCVC(SelfDescCVC aObject)
            : base(aObject)
        {
        }
    }
}
