package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IRequest;

/**
 * Generic Mobile signature status request interface
 */
public interface IStatusRequest extends IRequest {
    /**
     * Sends status request
     *
     * @param aMsspTransId Transaction number whose information is looked for
     * @param aApTransId   Transaction number created by AP on a new transaction
     * @return  StatusResponse
     */
    IStatusResponse sendRequest(String aMsspTransId, String aApTransId) throws ESYAException;
}
