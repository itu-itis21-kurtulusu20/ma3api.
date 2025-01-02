using System;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature
{
    /**
 * Generic mobile signature request
 */

    public interface ISignatureRequest : IRequest
    {
        /**
     * Sends Mobile signature request for a given MSISDN
     * @param aTransId  Transaction number created by AP on a new transaction
     * @param aMSISDN   MSISDN whose Mobile Signature Profile must be retrieved
     * @param aSignable   Data that will be signed
     * @return  SignatureResponse
     */
        ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable);
    }
}