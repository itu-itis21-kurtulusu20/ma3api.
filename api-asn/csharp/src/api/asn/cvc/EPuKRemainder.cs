using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class EPuKRemainder : BaseASNWrapper<PuKRemainder>
    {
        public EPuKRemainder(byte[] aEncoded)
            : base(aEncoded, new PuKRemainder())
        {
        }

        public EPuKRemainder()
            : base(new PuKRemainder())
        {
        }

        public void setPuKRemainder(byte[] aPukRemainderValue)
        {
            getObject().mValue = aPukRemainderValue;
        }

    }
}
