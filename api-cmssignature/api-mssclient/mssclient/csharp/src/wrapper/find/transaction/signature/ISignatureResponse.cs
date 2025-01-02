using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature
{
    /**
 *Generic Mobile Signature response
 */

    public interface ISignatureResponse : IResponse
    {
        String getTransId();
        String getMSISDN();
        byte[] getSignature();
        byte[] getRawSignature();
    }
}