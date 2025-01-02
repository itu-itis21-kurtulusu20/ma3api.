using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature
{
    public class VodafoneSignatureResponse: ISignatureResponse
    {
        string transId;
        string msisdn;
        byte[] rawSignature;
        Status status;

        public VodafoneSignatureResponse(string transId, string msisdn, byte[] rawSignature, Status status)
        {
            this.transId = transId;
            this.msisdn = msisdn;
            this.rawSignature = rawSignature;
            this.status = status;
        }

        public Status getStatus()
        {
            return status;
        }

        public string getTransId()
        {
            return transId;
        }

        public string getMSISDN()
        {
            return msisdn;
        }

        public byte[] getSignature()
        {
            throw new ESYARuntimeException("Vodafone do not support CAdES signature");
        }

        public byte[] getRawSignature()
        {
            return rawSignature;
        }
    }
}
