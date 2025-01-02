using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status
{
    /**
 * Generic Mobile signature status request interface
 */

    public interface IStatusRequest : IRequest
    {
        /**
     * Sends status request
     *
     * @param aMsspTransId Transaction number whose information is looked for
     * @param aApTransId   Transaction number created by AP on a new transaction
     * @return  StatusResponse
     */
        IStatusResponse sendRequest(String aMsspTransId, String aApTransId);
    }
}