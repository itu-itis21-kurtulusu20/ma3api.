using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.stub.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status
{
    /**
 * Turkcell mobile signature status response implementation
 * @see IStatusResponse
 */

    public class TurkTelekomStatusResponse : IStatusResponse
    {
        private readonly MSS_StatusRespType _statusResp;

        public TurkTelekomStatusResponse(MSS_StatusRespType aStatusResponse)
        {
            _statusResp = aStatusResponse;
        }

        public String getMSISDN()
        {
            return _statusResp.MobileUser.MSISDN;
        }

        public Status getStatus()
        {
            return new Status(_statusResp.Status.StatusCode.Value, _statusResp.Status.StatusMessage);
        }

        public byte[] getSignature()
        {
            return (byte[]) _statusResp.MSS_Signature.Base64Signature;
        }
    }
}