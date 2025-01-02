using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.stub;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature
{
    /**
 * Turkcell mobile signature response implementation
 * @see ISignatureResponse
 */

    public class TurkcellSignatureResponse : ISignatureResponse
    {
        private readonly MSS_SignatureRespType _trcellResp;

        public TurkcellSignatureResponse(MSS_SignatureRespType aResponse)
        {
            _trcellResp = aResponse;
        }

        public Status getStatus()
        {
            return new Status(_trcellResp.Status.StatusCode.Value, _trcellResp.Status.StatusMessage);
        }

        public byte[] getSignature()
        {
            return (byte[]) _trcellResp.MSS_Signature.Item;
        }

        public byte[] getRawSignature()
        {
            return null;
        }

        public String getTransId()
        {
            return _trcellResp.MSSP_TransID;
        }

        public String getMSISDN()
        {
            return _trcellResp.MobileUser.MSISDN;
        }
    }
}