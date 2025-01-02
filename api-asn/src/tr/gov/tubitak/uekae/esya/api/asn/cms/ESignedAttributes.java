package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedAttributes;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

public class ESignedAttributes extends BaseASNWrapper<SignedAttributes>{
	
	public ESignedAttributes(SignedAttributes aObject)
	{
		super(aObject);
	}
	
	public ESignedAttributes(byte[] aBytes)
	throws ESYAException
	{
		super(aBytes,new SignedAttributes());
	}
	
	public int getAttributeCount()
	{
		if(mObject.elements==null)
    		return 0;
    	
    	return mObject.elements.length;
	}
	
	public EAttribute getAttribute(int aIndex)
	{
		if(mObject.elements==null)
			return null;
		
		return new EAttribute(mObject.elements[aIndex]);
	}
	
	public void addAttribute(EAttribute aAttribute)
	{		
		if(mObject.elements == null)
			mObject.elements = new Attribute[0];
		mObject.elements = extendArray(mObject.elements, aAttribute.getObject());
	}

}
