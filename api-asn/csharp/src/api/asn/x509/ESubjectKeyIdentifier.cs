using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ESubjectKeyIdentifier : BaseASNWrapper<SubjectKeyIdentifier>, ExtensionType
    {
        public ESubjectKeyIdentifier(SubjectKeyIdentifier aObject)
            : base(aObject)
        {
        }

        public ESubjectKeyIdentifier(byte[] aKeyID)
            : base(new SubjectKeyIdentifier(aKeyID))
        {
        }
        public byte[] getValue()
        {
            return mObject.mValue;
        }
        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_subjectKeyIdentifier, aCritic, getBytes());
        }
    }
}
