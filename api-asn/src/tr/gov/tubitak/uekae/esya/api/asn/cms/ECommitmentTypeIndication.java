package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CommitmentTypeIndication;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

public class ECommitmentTypeIndication extends BaseASNWrapper<CommitmentTypeIndication> 
{

	public ECommitmentTypeIndication(byte[] aBytes) 
	throws ESYAException 
	{
		super(aBytes, new CommitmentTypeIndication());
	}
	
	public Asn1ObjectIdentifier getOid()
	{
		return mObject.commitmentTypeId;
	}

}
