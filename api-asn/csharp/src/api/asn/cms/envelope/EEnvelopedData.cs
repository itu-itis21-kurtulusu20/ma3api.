using asn.src.tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms.envelope
{
    public class EEnvelopedData : EnvelopeASNWrapper<EnvelopedData>
    {
        public EEnvelopedData(EnvelopedData aObject)
            : base(aObject) { }

        public EEnvelopedData(byte[] aBytes)
            : base(aBytes, new EnvelopedData()) { }

        public override long getVersion() { return mObject.version.mValue; }
        public override void setVersion(long aVersion) { mObject.version = new CMSVersion(aVersion); }
        public override OriginatorInfo getOriginatorInfo() { return mObject.originatorInfo; }
        public override void setOriginatorInfo(OriginatorInfo aOriginatorInfo) { mObject.originatorInfo = aOriginatorInfo; }
        public override RecipientInfos getRecipientInfos() { return mObject.recipientInfos; }
        public override void setRecipientInfos(RecipientInfos recipientInfos) { mObject.recipientInfos = recipientInfos; }
        public override UnprotectedAttributes getUnprotectedAttributes(){ return mObject.unprotectedAttrs; }
        public override void setUnprotectedAttributes(UnprotectedAttributes aAttr) { mObject.unprotectedAttrs = aAttr;}
        public override Asn1ObjectIdentifier getOID() { return new Asn1ObjectIdentifier(_cmsValues.id_envelopedData); }
        public override byte [] getMac(){ throw new ESYAException("Not supported"); }
        public override void setMac(byte[] mac) { throw new ESYAException("Not supported.."); }
        public override void readAttrs(Asn1BerInputStream sifrelenmisVeri) 
        {
            int available = sifrelenmisVeri.Available();
            if(available > 0) {
            Asn1Tag tag = sifrelenmisVeri.PeekTag();
            Asn1Tag unProtectedAttributeTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
            if (tag != null && tag.Equals(unProtectedAttributeTag)) {
                int len = sifrelenmisVeri.DecodeTagAndLength(unProtectedAttributeTag);
                UnprotectedAttributes ua = new UnprotectedAttributes();
                ua.Decode(sifrelenmisVeri, false, len);
                mObject.unprotectedAttrs = ua;
            }
          }
        }
        public override void writeAttr(Asn1BerOutputStream sifrelenmisVeri) 
        {
            if(mObject.unprotectedAttrs != null)
            {
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mObject.unprotectedAttrs.Encode(buffer, false);
                buffer.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
                sifrelenmisVeri.Write(buffer.MsgCopy);
            }
        }
        public override EEncryptedContentInfo getEncryptedContentInfo() { return new EEncryptedContentInfo(mObject.encryptedContentInfo); }
        public override void setEncryptedContentInfo(EEncryptedContentInfo aEncryptedContentInfo) { mObject.encryptedContentInfo = aEncryptedContentInfo.getObject(); } 
    }
}
