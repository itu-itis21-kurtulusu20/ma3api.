package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.UnsignedAttributes;

public class EUnsignedAttributes extends BaseASNWrapper<UnsignedAttributes>
{
	public EUnsignedAttributes(UnsignedAttributes aObject)
	{
		super(aObject);
	}
	
	public EUnsignedAttributes(byte[] aBytes)
	throws ESYAException
	{
		super(aBytes,new UnsignedAttributes());
	}
	
	//TODO
	public int getAttributeCount()
	{
		if(mObject==null || mObject.elements==null)
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
		mObject.elements = extendArray(mObject.elements, aAttribute.getObject());
	}
	
	
}
