package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerLocation;
import tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString;

public class ESignerLocation  extends BaseASNWrapper<SignerLocation>  
{
	public ESignerLocation(byte[] aBytes)throws ESYAException 
	{
		super(aBytes, new SignerLocation());
	}
	
	public String getCountry()
	{
		return mObject.countryName.getElement().toString();
	}
	
	public String getLocalityName()
	{
		return mObject.localityName.getElement().toString();
	}
	
	public String []  getPostalAddress()
	{
		String [] postalAdress = null;
		if(mObject.postalAdddress != null)
		{
			DirectoryString [] directoryStrings = mObject.postalAdddress.elements;
			postalAdress = new String[directoryStrings.length];
			for (int i = 0; i < directoryStrings.length; i++) 
				postalAdress[i] = directoryStrings[i].getElement().toString(); 
			return postalAdress;
		}
		
		return null;
	}
}
