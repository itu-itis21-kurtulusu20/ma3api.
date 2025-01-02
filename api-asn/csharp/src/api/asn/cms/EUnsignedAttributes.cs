using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EUnsignedAttributes : BaseASNWrapper<UnsignedAttributes>
    {
        public EUnsignedAttributes(UnsignedAttributes aObject)
            : base(aObject)
        {
        }

        public EUnsignedAttributes(byte[] aBytes)
            : base(aBytes, new UnsignedAttributes())
        {
        }

        //TODO
        public int getAttributeCount()
        {
            if (mObject == null || mObject.elements == null)
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
            mObject.elements = extendArray(mObject.elements, aAttribute.getObject());
        }

    }
}
