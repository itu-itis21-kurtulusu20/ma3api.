package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentReference;

public class EContentReference extends BaseASNWrapper<ContentReference>
{
	public EContentReference(byte [] aBytes) throws ESYAException
	{
		super(aBytes, new ContentReference());
	}
	
	public Asn1ObjectIdentifier getcontentType()
	{
		return mObject.contentType;
	}
	
	public byte [] getSignedContentIdentifier()
	{
		return mObject.signedContentIdentifier.value;
	}
	
	public byte []  getOriginatorSignatureValue()
	{
		return mObject.originatorSignatureValue.value;
	}
}
