package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;

/**
 * Generic mobile signature request
 */
public interface ISignatureRequest extends IRequest {
    /**
     * Sends Mobile signature request for a given MSISDN
     * @param aTransId  Transaction number created by AP on a new transaction
     * @param aMSISDN   MSISDN whose Mobile Signature Profile must be retrieved
     * @param aSignable   Data that will be signed
     * @return  SignatureResponse
     */
    ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable) throws ESYAException;
}
