using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace asn.src.tr.gov.tubitak.uekae.esya.api.asn.cms.envelope
{
    public abstract class EnvelopeASNWrapper<T> : BaseASNWrapper<T>,IEnvelopeData where T : Asn1Type
    { 
        protected EnvelopeASNWrapper(T aObject) : base(aObject) { }

        protected EnvelopeASNWrapper(byte[] aBytes, T aObject) : base(aBytes, aObject) { }


        public RecipientInfo getRecipientInfo(int aIndex)
        {
            if (getRecipientInfos() == null)
                return null;
            return getRecipientInfos().elements[aIndex];
        }

        public int getRecipientInfoCount()
        {
            if (getRecipientInfos() == null || getRecipientInfos().elements == null)
                return 0;
            return getRecipientInfos().elements.Length;
        }

        public void addRecipientInfo(RecipientInfo aRecipientInfo)
        {
            RecipientInfos recipientInfos = getRecipientInfos();
            if (recipientInfos == null)
            {
                recipientInfos = new RecipientInfos();
                setRecipientInfos(recipientInfos);

                recipientInfos.elements = new RecipientInfo[0];
            }

            recipientInfos.elements = extendArray(recipientInfos.elements, aRecipientInfo);
        }

        public abstract long getVersion();
        public abstract void setVersion(long version);
        public abstract OriginatorInfo getOriginatorInfo();
        public abstract void setOriginatorInfo(OriginatorInfo originatorInfo);
        public abstract RecipientInfos getRecipientInfos();
        public abstract void setRecipientInfos(RecipientInfos recipientInfos);
        public abstract EEncryptedContentInfo getEncryptedContentInfo();
        public abstract void setEncryptedContentInfo(EEncryptedContentInfo encryptedContentInfo);
        public abstract UnprotectedAttributes getUnprotectedAttributes();
        public abstract void setUnprotectedAttributes(UnprotectedAttributes unprotectedAttributes);
        public abstract Asn1ObjectIdentifier getOID();
        public abstract byte [] getMac();
        public abstract void setMac(byte[] mac);
        public abstract void readAttrs(Asn1BerInputStream mSifrelenmisVeri);
        public abstract void writeAttr(Asn1BerOutputStream mSifrelenmisVeri);
    }
}
