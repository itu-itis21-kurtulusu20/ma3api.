using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms.envelope
{
    public interface IEnvelopeData
    {
        long getVersion();
        void setVersion(long version);

        OriginatorInfo getOriginatorInfo();
        void setOriginatorInfo(OriginatorInfo originatorInfo);

        RecipientInfos getRecipientInfos();
        void setRecipientInfos(RecipientInfos recipientInfos);

        RecipientInfo getRecipientInfo(int aIndex);

        int getRecipientInfoCount();

        void addRecipientInfo(RecipientInfo aRecipientInfo);

        EEncryptedContentInfo getEncryptedContentInfo();
        void setEncryptedContentInfo(EEncryptedContentInfo encryptedContentInfo);

        UnprotectedAttributes getUnprotectedAttributes();
        void setUnprotectedAttributes(UnprotectedAttributes unprotectedAttributes);

        byte[] getEncoded();

        Asn1ObjectIdentifier getOID();

        byte [] getMac();
        void setMac(byte[] mac);

        void readAttrs(Asn1BerInputStream mSifrelenmisVeri);

        void writeAttr(Asn1BerOutputStream mSifrelenmisVeri);
    }
}
