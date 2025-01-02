using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status
{
    /**
 *Generic Mobile Signature Status response
 */

    public interface IStatusResponse : IResponse
    {
        String getMSISDN();
        byte[] getSignature();
    }
}