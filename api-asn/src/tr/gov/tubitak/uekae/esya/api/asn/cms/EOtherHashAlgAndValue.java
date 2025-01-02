package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;

public class EOtherHashAlgAndValue extends BaseASNWrapper<OtherHashAlgAndValue> 
{
	public EOtherHashAlgAndValue(OtherHashAlgAndValue aObject)
	{
		super(aObject);
	}
	
	public EOtherHashAlgAndValue(byte[] aBytes) throws ESYAException
	{
		super(aBytes,new OtherHashAlgAndValue());
	}
	
	
	public EAlgorithmIdentifier getHashAlg()
	{
		return new EAlgorithmIdentifier(mObject.hashAlgorithm);
	}
	
	public byte [] getHashValue()
	{
		return mObject.hashValue.value;
	}
}
