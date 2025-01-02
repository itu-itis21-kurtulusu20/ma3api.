package tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;

import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;
import tr.gov.tubitak.uekae.esya.asn.cms.PasswordRecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;

@SuppressWarnings("serial")
public class PasswordRecipient extends RecipientInfo
{
	PasswordRecipientInfo ri;
	
	public CMSVersion getCMSVersion()
	{
		return ri.version;
	}
}
