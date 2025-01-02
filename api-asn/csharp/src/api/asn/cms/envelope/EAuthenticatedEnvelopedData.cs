using asn.src.tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms.envelope
{
    public class EAuthenticatedEnvelopedData : EnvelopeASNWrapper<AuthEnvelopedData>
    {
        public EAuthenticatedEnvelopedData(AuthEnvelopedData aObject) : base(aObject) { }
        public EAuthenticatedEnvelopedData(byte[] aBytes) : base(aBytes, new AuthEnvelopedData()) { }
        public override long getVersion() { return mObject.version.mValue; }
        public override void setVersion(long version) { mObject.version = new CMSVersion(version); }
        public override RecipientInfos getRecipientInfos() { return mObject.recipientInfos; }
        public override void setRecipientInfos(RecipientInfos recipientInfos) { mObject.recipientInfos = recipientInfos; }
        public override EEncryptedContentInfo getEncryptedContentInfo() { return new EEncryptedContentInfo(mObject.authEncryptedContentInfo); }
        public override void setEncryptedContentInfo(EEncryptedContentInfo aEncryptedContentInfo) { mObject.authEncryptedContentInfo = aEncryptedContentInfo.getObject(); }
        public override UnprotectedAttributes getUnprotectedAttributes() { return null; }
        public override void setUnprotectedAttributes(UnprotectedAttributes unprotectedAttributes) { throw new ESYAException("Not implemented..");}
        public override Asn1ObjectIdentifier getOID() { return new Asn1ObjectIdentifier(_cmsValues.id_ct_authEnvelopedData);}
        public override OriginatorInfo getOriginatorInfo() { return mObject.originatorInfo; }
        public override void setOriginatorInfo(OriginatorInfo originatorInfo) { mObject.originatorInfo = originatorInfo;}
        public override byte [] getMac() { return mObject.mac.mValue; }
        public override void setMac(byte[] mac) { mObject.mac = new Asn1OctetString(mac); }

        public override void readAttrs(Asn1BerInputStream sifrelenmisVeri) 
        {
            int available = sifrelenmisVeri.Available();
                if(available > 0) {
                Asn1Tag tag;
                Asn1Tag attrTag;

                tag = sifrelenmisVeri.PeekTag();
                attrTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
                if (tag != null && tag.Equals(attrTag)) {
                    int len = sifrelenmisVeri.DecodeTagAndLength(attrTag);
                    AuthAttributes aa = new AuthAttributes();
                    aa.Decode(sifrelenmisVeri, false, len);
                    mObject.authAttrs = aa;
                }

                tag = sifrelenmisVeri.PeekTag();
                attrTag = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 4);
                if (tag != null && tag.Equals(attrTag))
                {                
                    Asn1OctetString mac = new Asn1OctetString();
                    mac.Decode(sifrelenmisVeri);
                    mObject.mac = mac;
                }

                if (sifrelenmisVeri.Available() > 0)
                { 
                    tag = sifrelenmisVeri.PeekTag();
                    attrTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 2);
                    if (tag != null && tag.Equals(attrTag))
                    {
                        int len = sifrelenmisVeri.DecodeTagAndLength(attrTag);
                        UnauthAttributes uaa = new UnauthAttributes();
                        uaa.Decode(sifrelenmisVeri, false, len);
                        mObject.unauthAttrs = uaa;
                    }   
                }
             }
          }

        public override void writeAttr(Asn1BerOutputStream sifrelenmisVeri) 
        {
            if(mObject.authAttrs != null){
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mObject.authAttrs.Encode(buffer, false);
                buffer.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
                sifrelenmisVeri.Write(buffer.MsgCopy);
            }

            if (mObject.mac != null)
            {
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                mObject.mac.Encode(buffer, true);             
                sifrelenmisVeri.Write(buffer.MsgCopy);
            }

            if (mObject.unauthAttrs != null){
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mObject.unauthAttrs.Encode(buffer, false);
                buffer.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 2, len);
                sifrelenmisVeri.Write(buffer.MsgCopy);
            }
        }
    }
}
