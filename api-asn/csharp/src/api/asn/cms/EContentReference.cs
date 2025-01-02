using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EContentReference : BaseASNWrapper<ContentReference>
    {
        public EContentReference(byte[] aBytes)
            : base(aBytes, new ContentReference())
        {
        }

        public Asn1ObjectIdentifier getcontentType()
        {
            return mObject.contentType;
        }

        public byte[] getSignedContentIdentifier()
        {
            return mObject.signedContentIdentifier.mValue;
        }

        public byte[] getOriginatorSignatureValue()
        {
            return mObject.originatorSignatureValue.mValue;
        }
    }
}