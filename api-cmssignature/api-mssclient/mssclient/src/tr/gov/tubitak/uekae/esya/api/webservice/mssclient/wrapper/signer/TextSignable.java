package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;

import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.SignatureType;

/**
 * Represents signable text data
 */
public class TextSignable implements ISignable {

    private String _plainText;
    private SignatureType _signatureType = SignatureType.PKCS7;
    private String hashURI;

    public TextSignable(String aPlainText, SignatureType aSignatureType)
    {
        _plainText = aPlainText;
        if (aSignatureType != null)
            _signatureType = aSignatureType;
    }

    public String getValueToBeSigned()
    {
        return _plainText;
    }
    public String getValueToBeDisplayed()
    {
        return _plainText;
    }
    public String getEncoding()
    {
        return "UTF-8";
    }
    public String getMimeType()
    {
        return "text/plain";
    }

    public SignatureType getSignatureType()
    {
        return _signatureType;
    }

    public String getHashURI(){
        return hashURI;
    }

    public void setHashURI(String hashURI){
        this.hashURI = hashURI;
    }
}
