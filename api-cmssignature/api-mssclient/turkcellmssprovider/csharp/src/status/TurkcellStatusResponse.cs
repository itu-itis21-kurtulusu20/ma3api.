using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.stub;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.status
{
    /**
 * Turkcell mobile signature status response implementation
 * @see IStatusResponse
 */

    public class TurkcellStatusResponse : IStatusResponse
    {
        private readonly MSS_StatusRespType _trcellStatusResp;

        public TurkcellStatusResponse(MSS_StatusRespType aStatusResponse)
        {
            _trcellStatusResp = aStatusResponse;
        }

        public String getMSISDN()
        {
            return _trcellStatusResp.MobileUser.MSISDN;
        }

        public Status getStatus()
        {
            return new Status(_trcellStatusResp.Status.StatusCode.Value, _trcellStatusResp.Status.StatusMessage);
        }

        public byte[] getSignature()
        {
            return (byte[]) _trcellStatusResp.MSS_Signature.Item;
        }
    }
}