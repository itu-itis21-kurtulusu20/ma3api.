package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.asn.cms.ClaimedAttributes;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerAttribute_element;

public class ESignerAttribute_element extends BaseASNWrapper<SignerAttribute_element>
{

	public ESignerAttribute_element(byte[] aBytes) throws ESYAException 
	{
		super(aBytes, new SignerAttribute_element());
	}
	
	public ESignerAttribute_element(SignerAttribute_element aObject)
	{
		super(aObject);
	}
	
	public EClaimedAttributes getClaimedAttributes()
	{
		if(mObject.getChoiceID() == SignerAttribute_element._CLAIMEDATTRIBUTES)
			return new EClaimedAttributes((ClaimedAttributes) mObject.getElement());
		
		return null;
	}
	
	public EAttributeCertificate getAttributeCertificate()
	{
		if(mObject.getChoiceID() == SignerAttribute_element._CERTIFIEDATTRIBUTES)
			return new EAttributeCertificate((AttributeCertificate) mObject.getElement());
		
		return null;
	}
	
	
	

}
