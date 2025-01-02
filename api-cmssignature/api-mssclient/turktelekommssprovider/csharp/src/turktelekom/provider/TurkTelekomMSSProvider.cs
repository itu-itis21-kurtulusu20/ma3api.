using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;

using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.provider
{
    /**
 * Gets main Interfaces (Status, Signature, Profile) of Turk Telekom MSSP
 * @see IMSSProvider
 */

    public class TurkTelekomMSSProvider : IMSSProvider
    {
        public IProfileRequest getProfileRequester(MSSParams aParams)
        {
            return new TurkTelekomProfileRequest(aParams);
        }

        public IStatusRequest getStatusRequester(MSSParams aParams)
        {
            return new TurkTelekomStatusRequest(aParams);
        }

        public ISignatureRequest getSignatureRequester(MSSParams aParams)
        {
            return new TurkTelekomSignatureRequest(aParams);
        }
    }
}