package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.common.IResponse;

/**
 *Generic Mobile Signature response
 */
public interface ISignatureResponse extends IResponse {
    String getTransId();

    String getMSISDN();

    byte[] getSignature();

    byte[] getRawSignature() throws ESYAException;
}
