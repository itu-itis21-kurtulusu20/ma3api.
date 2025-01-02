using System;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Stores the result of the OCSP Response validation 
     *
     * @author IH
     */
    [Serializable]
    public class OCSPResponseStatusInfo : StatusInfo
    {
        public enum OCSPResponseStatus
        {
            INVALID_RESPONSE, VALID, PATH_VALIDATION_FAILURE
        }

        private EOCSPResponse mOCSPResponse;

        private OCSPResponseStatus mOCSPResponseStatus;

        public OCSPResponseStatusInfo() { }

        public OCSPResponseStatusInfo(EOCSPResponse aOCSPResponse)
        {
            mOCSPResponse = aOCSPResponse;
        }

        public OCSPResponseStatus getOCSPResponseStatus()
        {
            return mOCSPResponseStatus;
        }

        public void setOCSPResponseStatus(OCSPResponseStatus aOCSPResponseStatus)
        {
            mOCSPResponseStatus = aOCSPResponseStatus;
        }

        public EOCSPResponse getOCSPResponse()
        {
            return mOCSPResponse;
        }

        public void setOCSPResponse(EOCSPResponse aOCSPCevabi)
        {
            mOCSPResponse = aOCSPCevabi;
        }
    }
}
