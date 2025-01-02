using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.provider
{
    public class VodafoneMSSProvider: IMSSProvider
    {
        public IProfileRequest getProfileRequester(MSSParams aParams)
        {
            return new VodafoneProfileRequest(aParams);
        }

        public IStatusRequest getStatusRequester(MSSParams aParams)
        {
            throw new ESYAException("Not supported function!");
        }

        public ISignatureRequest getSignatureRequester(MSSParams aParams)
        {
            return new VodafoneSignatureRequest(aParams);
        }
    }
}
