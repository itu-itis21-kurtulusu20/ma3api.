package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignaturePolicy;
import tr.gov.tubitak.uekae.esya.asn.cms.SignaturePolicyId;

public class ESignaturePolicy extends BaseASNWrapper<SignaturePolicy>
{
	public ESignaturePolicy(SignaturePolicy aObject)
	{
		super(aObject);
	}
	
	public ESignaturePolicy(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new SignaturePolicy());
	}

    public ESignaturePolicy(ESignaturePolicyId policyId){
        super(new SignaturePolicy());
        mObject.set_signaturePolicyId(policyId.getObject());
    }

	public ESignaturePolicyId getSignaturePolicyId()
	{
		if(mObject.getChoiceID()==SignaturePolicy._SIGNATUREPOLICYID)
			return new ESignaturePolicyId((SignaturePolicyId)mObject.getElement());

		return null;
	}
}
