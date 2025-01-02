using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    class EAttributeTypeAndValue : BaseASNWrapper<AttributeTypeAndValue>
    {
        public EAttributeTypeAndValue(AttributeTypeAndValue aObject)
            : base(aObject)
        {
        }

        public Asn1ObjectIdentifier getType()
        {
            return mObject.type;
        }

        public byte[] getValue()
        {
            return mObject.value_.mValue;
        }

    }
}
