using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes
{
    public class PasswordRecipient : RecipientInfo
    {
        PasswordRecipientInfo ri;

        public CMSVersion getCMSVersion()
        {
            return ri.version;
        }
    }
}
