using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider
{
    /**
    * Gets main Interfaces (Status, Signature, Profile) of a provider
    */

    public interface IMSSProvider
    {
        IProfileRequest getProfileRequester(MSSParams aParams);
        IStatusRequest getStatusRequester(MSSParams aParams);
        ISignatureRequest getSignatureRequester(MSSParams aParams);
    }
}