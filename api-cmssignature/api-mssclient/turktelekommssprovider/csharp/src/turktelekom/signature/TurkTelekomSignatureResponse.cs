using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.stub.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;
using SignatureType = tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.stub.signature.SignatureType;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.signature
{
  /**
 * Turk Telekom mobile signature response implementation
 * @see ISignatureResponse
 */

    public class TurkTelekomSignatureResponse : ISignatureResponse
    {
        private readonly MSS_SignatureRespType _trcellResp;

        public TurkTelekomSignatureResponse(MSS_SignatureRespType aResponse)
        {
            _trcellResp = aResponse;
        }

        public Status getStatus()
        {
            return new Status(_trcellResp.Status.StatusCode.Value, _trcellResp.Status.StatusMessage);
        }

        public byte[] getSignature()
        {
            SignatureType mssSignature = _trcellResp.MSS_Signature;
            if(mssSignature==null)
            {
                return null;
            }
            return (byte[]) mssSignature.Base64Signature;
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