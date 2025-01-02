using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.provider
{
    /**
 * Gets main Interfaces (Status, Signature, Profile) of Turkcell MSSP
 * @see IMSSProvider
 */

    public class TurkcellMSSProvider : IMSSProvider
    {
        public IProfileRequest getProfileRequester(MSSParams aParams)
        {
            return new TurkcellProfileRequest(aParams);
        }

        public IStatusRequest getStatusRequester(MSSParams aParams)
        {
            return new TurkcellStatusRequest(aParams);
        }

        public ISignatureRequest getSignatureRequester(MSSParams aParams)
        {
            return new TurkcellSignatureRequest(aParams);
        }
    }
}