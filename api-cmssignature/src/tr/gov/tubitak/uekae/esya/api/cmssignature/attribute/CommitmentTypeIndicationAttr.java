package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECommitmentTypeIndication;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CommitmentTypeIndication;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * There may be situations where a signer wants to explicitly indicate to a verifier that by signing the data, it illustrates a
 * type of commitment on behalf of the signer. The commitment-type-indication attribute conveys such
 * information. The commitment-type-indication attribute shall be a signed attribute.(etsi 101733v010801 5.11.1)
 * @author aslihan.kubilay
 *
 */

public class CommitmentTypeIndicationAttr
extends AttributeValue
{
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_commitmentType;

	private CommitmentType mTip;

	 /**
		 * Create CommitmentTypeIndicationAttr with commitment type
		  * @param aTip CommitmentType
		  * @throws CMSSignatureException
		  */
	public CommitmentTypeIndicationAttr (CommitmentType aTip) throws CMSSignatureException
	{
		super();
		mTip = aTip;
		if(mTip == null)
			throw new NullParameterException("Commitment type can not be null");
	}
	 /**
   	 * Set Commitment Type Indication value
   	 */
	public void setValue () throws CMSSignatureException
	{
		Asn1ObjectIdentifier oid = mTip.getOid();
		CommitmentTypeIndication cti = new CommitmentTypeIndication(oid);
		_setValue(cti);
	}
	/**
	 * Checks whether attribute is signed or not.
	 * @return True 
	 */   
	public boolean isSigned() 
	{
		return true;
	}

	/**
	 * Returns AttributeOID of Commitment Type Indication attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	
	/**
	 * Returns  commitment type
	 * @param aAttribute EAttribute
	 * @return 
	 * @throws ESYAException
	 */
	public static CommitmentType toCommitmentType(EAttribute aAttribute) throws ESYAException
	{
		ECommitmentTypeIndication indication = new ECommitmentTypeIndication(aAttribute.getValue(0));
		CommitmentType [] values = CommitmentType.values();
		for (CommitmentType value : values) 
		{
			if(value.getOid().equals(indication.getOid()))
				return value;
		}
		
		throw new ESYAException(CMSSignatureI18n.getMsg(E_KEYS.UNKNOWN_COMMITMENT_TYPE));
	}
	
	
}