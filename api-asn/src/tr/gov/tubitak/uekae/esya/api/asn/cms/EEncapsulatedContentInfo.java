package tr.gov.tubitak.uekae.esya.api.asn.cms;

import java.io.InputStream;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.EncapsulatedContentInfo;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

public class EEncapsulatedContentInfo extends BaseASNWrapper<EncapsulatedContentInfo>
{
	public EEncapsulatedContentInfo(EncapsulatedContentInfo aObject)
	{
		super(aObject);
	}
	
	public EEncapsulatedContentInfo(Asn1ObjectIdentifier aContentType,Asn1OctetString aContent)
	{
		super(new EncapsulatedContentInfo());
		setContentType(aContentType);
		setContent(aContent);
	}
	
	public EEncapsulatedContentInfo(InputStream aIns)
	throws ESYAException
	{
		super(aIns,new EncapsulatedContentInfo());
	}
	
	
	public Asn1ObjectIdentifier getContentType()
	{
		return mObject.eContentType;
	}
	
	public void setContentType(Asn1ObjectIdentifier aContentType)
	{
		mObject.eContentType = aContentType;
	}
	
	public byte[] getContent()
	{
		if(mObject.eContent!=null)
			return mObject.eContent.value;
		
		return null;
	}
	
	public void setContent(Asn1OctetString aContent)
	{
		mObject.eContent = aContent;
	}
	
}
