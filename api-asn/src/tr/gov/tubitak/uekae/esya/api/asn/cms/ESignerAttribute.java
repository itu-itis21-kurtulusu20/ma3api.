package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerAttribute;

public class ESignerAttribute extends BaseASNWrapper<SignerAttribute>
{
	public ESignerAttribute(byte[] aBytes) throws ESYAException 
	{
		super(aBytes, new SignerAttribute());
	}
	
	public ESignerAttribute(SignerAttribute aObject)
	{
		super(aObject);
	}
	
	public ESignerAttribute_element [] getElements() 
	{
		ESignerAttribute_element [] elements = new ESignerAttribute_element[mObject.elements.length];
		for (int i = 0; i < elements.length; i++) 
		{
			elements[i] = new ESignerAttribute_element(mObject.elements[i]);
		}
		
		return elements;

	}
}
