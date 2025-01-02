package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;

import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType;

/**
 *  Represents signable base64 encoded binary data
 */
public class BinarySignable implements ISignable {
    private String base64EncodedHashVal;
    private String dataToBeDisplayed;
    private SignatureType signatureType = SignatureType.PKCS7;
    private String hashURI;
    private String mimeType = "application/octet-stream";
    private String encoding = "Base64"; 


    public BinarySignable(String aBase64EncodedHashVal, String aDataToBeDisplayed, SignatureType aSignatureType) {
        base64EncodedHashVal = aBase64EncodedHashVal;
        dataToBeDisplayed = aDataToBeDisplayed; 
        signatureType = aSignatureType;
    }
    
    public BinarySignable(String aBase64EncodedHashVal, String aDataToBeDisplayed, SignatureType aSignatureType, String aMimeType, String aEncoding) {
        base64EncodedHashVal = aBase64EncodedHashVal;
        dataToBeDisplayed = aDataToBeDisplayed;        
        signatureType = aSignatureType;
        mimeType = aMimeType;
        encoding = aEncoding;
        
    }

    public String getValueToBeDisplayed() {
        return dataToBeDisplayed;
    }

    public String getValueToBeSigned() {
        return base64EncodedHashVal;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getMimeType() {
        return mimeType;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public String getHashURI(){
        return hashURI;
    }

    public void setHashURI(String hashURI){
        this.hashURI = hashURI;
    }
}
