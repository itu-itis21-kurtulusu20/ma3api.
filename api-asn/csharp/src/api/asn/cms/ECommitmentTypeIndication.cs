using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECommitmentTypeIndication : BaseASNWrapper<CommitmentTypeIndication>
    {
        public ECommitmentTypeIndication(byte[] aBytes)
            : base(aBytes, new CommitmentTypeIndication())
        {
        }

        public Asn1ObjectIdentifier getOid()
        {
            return mObject.commitmentTypeId;
        }
    }
}