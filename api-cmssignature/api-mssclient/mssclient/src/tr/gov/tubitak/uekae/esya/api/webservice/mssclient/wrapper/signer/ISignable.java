package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;

import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType;

/**
 * Basic signable interface used in web service request generation
 */
public interface ISignable {
    String getValueToBeDisplayed();

    String getValueToBeSigned();

    String getEncoding();

    String getMimeType();

    SignatureType getSignatureType();

    String getHashURI();
    void setHashURI(String hashURI);
}
