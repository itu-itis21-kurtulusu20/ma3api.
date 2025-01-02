using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes
{
    public class KEKRecipient : RecipientInfo
    {
        private readonly KEKRecipientInfo ri = new KEKRecipientInfo();

        public KEKRecipient()
            : base()
        {
            //super();
            _ilkIsler();
        }

        public KEKRecipient(Certificate aSertifika)
            : base()
        {
            //super();
            _ilkIsler();
            setKeyEncryptionAlgorithm(aSertifika.tbsCertificate.subjectPublicKeyInfo.algorithm);
        }

        public void setKeyEncryptionAlgorithm(AlgorithmIdentifier aAlgorithmIdentifier)
        {
            ri.keyEncryptionAlgorithm = aAlgorithmIdentifier;
        }

        public CMSVersion getCMSVersion()
        {
            return ri.version;
        }

        private void _ilkIsler()
        {
            SetElement(_KEKRI, ri);
            ri.version = new CMSVersion(4);
        }
    }
}
