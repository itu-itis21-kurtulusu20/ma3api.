package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;



public enum CommitmentType 
{
	/*Proof of origin indicates that the signer recognizes to have created, approved and sent the message.*/
	ORIGIN(AttributeOIDs.id_cti_ets_proofOfOrigin),
	
	/*Proof of receipt indicates that signer recognizes to have received the content of the message.*/
	RECEIPT(AttributeOIDs.id_cti_ets_proofOfReceipt),
	
	/*Proof of delivery indicates that the TSP providing that indication has delivered a message in a 
	 * local store accessible to the recipient of the message.*/
	DELIVERY(AttributeOIDs.id_cti_ets_proofOfDelivery),
	
	/*Proof of sender indicates that the entity providing that indication has sent the message (but not 
	 * necessarily created it).*/
	SENDER(AttributeOIDs.id_cti_ets_proofOfSender),
	
	/*Proof of approval indicates that the signer has approved the content of the message.*/
	
	APPROVAL(AttributeOIDs.id_cti_ets_proofOfApproval),
	
	/*Proof of creation indicates that the signer has created the message (but not necessarily approved, 
	 * nor sent it).*/
	CREATION(AttributeOIDs.id_cti_ets_proofOfCreation),

	/*Test signature indicates that the signer has created the message (but not necessarily approved,
	 * nor sent it).*/
	TEST(AttributeOIDs.id_test_signature);


	private Asn1ObjectIdentifier mOid;
	
	CommitmentType(Asn1ObjectIdentifier oid)
	{
		mOid = oid;
	}
	
	public Asn1ObjectIdentifier getOid()
	{
		return mOid;
	}
	
}
