package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

/**
 * Basic request methods
 */
public interface IRequest {
    /**
     * Sets new requests parameters
     * @param aParams   Request parameter
     */
    void setMSSParams(MSSParams aParams) throws ESYAException;

    /**
     * Sets service endpoint
     * @param aServiceUrl   Service endpoint Url
     */
    void setServiceUrl(String aServiceUrl);
}
