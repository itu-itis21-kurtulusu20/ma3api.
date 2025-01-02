package tr.gov.tubitak.uekae.esya.api.asn.cms;

import java.util.Arrays;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;
import tr.gov.tubitak.uekae.esya.asn.cms.SignaturePolicyId;
import tr.gov.tubitak.uekae.esya.asn.cms.SignaturePolicyId_sigPolicyQualifiers;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;


public class ESignaturePolicyId extends BaseASNWrapper<SignaturePolicyId> 
{
	public ESignaturePolicyId(SignaturePolicyId aObject)
	{
		super(aObject);
	}
	
	public ESignaturePolicyId(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new SignaturePolicyId());
	}

    public ESignaturePolicyId(int[] oid, int[] digestOid, byte[] digestValue)
    {
        super(new SignaturePolicyId());
        mObject.sigPolicyHash = new OtherHashAlgAndValue(new AlgorithmIdentifier(digestOid), digestValue);
        mObject.sigPolicyId = new Asn1ObjectIdentifier(oid);
    }

    public Asn1ObjectIdentifier getPolicyObjectIdentifier()
	{
		return mObject.sigPolicyId;
	}
		
	public EOtherHashAlgAndValue getHashInfo()
	{
		return new EOtherHashAlgAndValue(mObject.sigPolicyHash);
	}

    public void addPolicyQualifier(ESigPolicyQualifierInfo qualifier){
        if (mObject.sigPolicyQualifiers==null)
            mObject.sigPolicyQualifiers = new SignaturePolicyId_sigPolicyQualifiers();
        mObject.sigPolicyQualifiers.elements = extendArray(mObject.sigPolicyQualifiers.elements, qualifier.getObject());
    }

    public ESigPolicyQualifierInfo[] getPolicyQualifiers(){
        if (mObject.sigPolicyQualifiers==null)
            return new ESigPolicyQualifierInfo[0];
        return wrapArray(mObject.sigPolicyQualifiers.elements, ESigPolicyQualifierInfo.class);
    }
}
