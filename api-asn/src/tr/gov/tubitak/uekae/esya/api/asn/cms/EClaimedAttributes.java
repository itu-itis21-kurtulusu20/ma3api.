package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ClaimedAttributes;

public class EClaimedAttributes extends BaseASNWrapper<ClaimedAttributes>
{
	public EClaimedAttributes(EAttribute[] aAttributes)
	{
		super(new ClaimedAttributes(unwrapArray(aAttributes)));
	}
	
	public EClaimedAttributes(ClaimedAttributes aObject) 
	{
		super(aObject);
	}
	
	public EClaimedAttributes(byte [] aBytes) throws ESYAException
	{
		super(aBytes, new ClaimedAttributes());
	}
	
	public EAttribute [] getElements()
	{
		EAttribute [] attrs = new EAttribute[mObject.elements.length];
		
		for (int i = 0; i < attrs.length; i++) 
			attrs[i] = new EAttribute(mObject.elements[i]);
		
		return attrs;
	}

}
