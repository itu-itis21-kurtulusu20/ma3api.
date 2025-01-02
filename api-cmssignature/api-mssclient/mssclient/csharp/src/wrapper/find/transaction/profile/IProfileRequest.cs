using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile
{
    /**
 * Generic mobile signature profile request
 */

    public interface IProfileRequest : IRequest
    {
        /**
     * Sends mobil signature profile request
     * @param aMSISDN MSISDN whose Mobile Signature Profile must be retrieved
     * @param aApTransId    Transaction number created by AP on a new transaction
     * @return  ProfileResponse
     */
        IProfileResponse sendRequest(String aMSISDN, String aApTransId);
    }
}