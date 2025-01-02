package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IRequest;

/**
 * Generic mobile signature profile request
 */
public interface IProfileRequest extends IRequest {
    /**
     * Sends mobil signature profile request
     * @param aMSISDN MSISDN whose Mobile Signature Profile must be retrieved
     * @param aApTransId    Transaction number created by AP on a new transaction
     * @return  ProfileResponse
     */
    IProfileResponse sendRequest(String aMSISDN, String aApTransId) throws ESYAException;
}
