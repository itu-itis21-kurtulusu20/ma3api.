using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignedAttributes : BaseASNWrapper<SignedAttributes>
    {
        public ESignedAttributes(SignedAttributes aObject)
            : base(aObject)
        {
        }

        public ESignedAttributes(byte[] aBytes)
            : base(aBytes, new SignedAttributes())
        {
        }

        public int getAttributeCount()
        {
            if (mObject.elements == null)
                return 0;

            return mObject.elements.Length;
        }

        public EAttribute getAttribute(int aIndex)
        {
            if (mObject.elements == null)
                return null;

            return new EAttribute(mObject.elements[aIndex]);
        }

        public void addAttribute(EAttribute aAttribute)
        {
            if (mObject.elements == null)
                mObject.elements = new Attribute[0];
            mObject.elements = extendArray(mObject.elements, aAttribute.getObject());
        }
    }
}
