using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EClaimedAttributes : BaseASNWrapper<ClaimedAttributes>
    {
        public EClaimedAttributes(EAttribute[] aAttributes)
            : base(new ClaimedAttributes(unwrapArray<Attribute, EAttribute>(aAttributes)))
        {
        }

        public EClaimedAttributes(ClaimedAttributes aObject)
            : base(aObject)
        {
        }

        public EClaimedAttributes(byte[] aBytes)
            : base(aBytes, new ClaimedAttributes())
        {
        }

        public EAttribute[] getElements()
        {
            EAttribute[] attrs = new EAttribute[mObject.elements.Length];

            for (int i = 0; i < attrs.Length; i++)
                attrs[i] = new EAttribute(mObject.elements[i]);

            return attrs;
        }
    }
}