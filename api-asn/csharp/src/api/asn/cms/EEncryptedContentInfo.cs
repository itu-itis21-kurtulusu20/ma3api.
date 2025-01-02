using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;
/**
 * @author ayetgin
 */
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class EEncryptedContentInfo : BaseASNWrapper<EncryptedContentInfo>
    {
        public EEncryptedContentInfo(EncryptedContentInfo aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public EEncryptedContentInfo(Asn1ObjectIdentifier aContentType,
                                     EAlgorithmIdentifier aEncryptionAlgorithm,
                                     byte[] aEncryptedContent)
            : base(new EncryptedContentInfo())
        {
            //super(new EncryptedContentInfo());
            setContentType(aContentType);
            setEncryptionAlgorithm(aEncryptionAlgorithm);
            setEncryptedContent(aEncryptedContent);
        }

        public EAlgorithmIdentifier getEncryptionAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.contentEncryptionAlgorithm);
        }

        public void setEncryptionAlgorithm(EAlgorithmIdentifier aEncryptionAlgorithm)
        {
            mObject.contentEncryptionAlgorithm = aEncryptionAlgorithm.getObject();
        }

        public Asn1ObjectIdentifier getContentType()
        {
            return mObject.contentType;
        }

        public void setContentType(Asn1ObjectIdentifier aContentType)
        {
            mObject.contentType = aContentType;
        }

        public byte[] getEncryptedContent()
        {
            if (mObject.encryptedContent != null)
            {
                return mObject.encryptedContent.mValue;
            }
            return null;
        }

        public void setEncryptedContent(byte[] aEncryptedContent)
        {
            mObject.encryptedContent = new Asn1OctetString(aEncryptedContent);
        }
    }
}
