package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;

import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IResponse;

/**
 *Generic Mobile Signature Status response
 */
public interface IStatusResponse extends IResponse {
    String getMSISDN();

    byte[] getSignature();
}
