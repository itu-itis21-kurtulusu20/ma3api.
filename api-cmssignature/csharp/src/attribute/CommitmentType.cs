using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    public class CommitmentType
    {
        /*Proof of origin indicates that the signer recognizes to have created, approved and sent the message.*/
        public static readonly CommitmentType ORIGIN = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfOrigin);

        /*Proof of receipt indicates that signer recognizes to have received the content of the message.*/
        public static readonly CommitmentType RECEIPT = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfReceipt);

        /*Proof of delivery indicates that the TSP providing that indication has delivered a message in a 
	 * local store accessible to the recipient of the message.*/
        public static readonly CommitmentType DELIVERY = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfDelivery);

        /*Proof of sender indicates that the entity providing that indication has sent the message (but not 
	 * necessarily created it).*/
        public static readonly CommitmentType SENDER = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfSender);

        /*Proof of approval indicates that the signer has approved the content of the message.*/

        public static readonly CommitmentType APPROVAL = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfApproval);

        /*Proof of creation indicates that the signer has created the message (but not necessarily approved, 
	 * nor sent it).*/
        public static readonly CommitmentType CREATION = new CommitmentType(AttributeOIDs.id_cti_ets_proofOfCreation);

        private readonly Asn1ObjectIdentifier mOid;

        private CommitmentType(Asn1ObjectIdentifier oid)
        {
            mOid = oid;
        }

        public Asn1ObjectIdentifier getOid()
        {
            return mOid;
        }
    }
}