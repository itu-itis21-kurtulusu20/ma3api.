using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EAttribute : BaseASNWrapper<Attribute>
    {
        public EAttribute(Attribute aObject)
            : base(aObject)
        {
        }

        public EAttribute(byte[] aBytes)
            : base(aBytes, new Attribute())
        {
        }
        public EAttribute(Asn1ObjectIdentifier type, Asn1OpenType[] elements)
            : base(new Attribute(type, new _SetOfAttributeValue(elements)))
        {
            //super(new Attribute(type, new _SetOfAttributeValue(elements)));
        }

        public Asn1ObjectIdentifier getType()
        {
            return mObject.type;
        }

        public void setType(Asn1ObjectIdentifier aType)
        {
            mObject.type = aType;
        }

        public int getValueCount()
        {
            if (mObject.values == null || mObject.values.elements == null)
                return 0;

            return mObject.values.elements.Length;
        }

        public byte[] getValue(int aIndex)
        {
            if (mObject.values == null || mObject.values.elements == null)
                return null;

            return mObject.values.elements[aIndex].mValue;
        }

        public void setValue(int aIndex, byte[] aValue)
        {
            if (mObject.values == null || mObject.values.elements == null)
                return;

            mObject.values.elements[aIndex].mValue = aValue;
        }

        public void addValue(byte[] aValue)
        {
            if (mObject.values == null)
            {
                mObject.values = new _SetOfAttributeValue();
                mObject.values.elements = new Asn1OpenType[0];
            }

            mObject.values.elements = extendArray(mObject.values.elements, new Asn1OpenType(aValue));
        }
    }
}
