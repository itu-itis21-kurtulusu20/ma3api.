using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer
{
    /**
 *  Represents signable base64 encoded binary data
 */

    public class BinarySignable : ISignable
    {
        private readonly String _base64EncodedHashVal;
        private readonly String _dataToBeDisplayed;

        private readonly SignatureType _signatureType = SignatureType.PKCS7;
        private readonly String mimeType = "application/octet-stream";
        private readonly String encoding = "Base64";
        private String hashURI;

        public BinarySignable(String aBase64EncodedHashVal, String aDataToBeDisplayed, SignatureType aSignatureType)
        {
            _base64EncodedHashVal = aBase64EncodedHashVal;
            _dataToBeDisplayed = aDataToBeDisplayed;
            if (aSignatureType != null)
                _signatureType = aSignatureType;
        }

        public BinarySignable(String aBase64EncodedHashVal, String aDataToBeDisplayed, SignatureType aSignatureType, String aMimeType, String aEncoding)
        {
            _base64EncodedHashVal = aBase64EncodedHashVal;
            _dataToBeDisplayed = aDataToBeDisplayed;
            _signatureType = aSignatureType;
            mimeType = aMimeType;
            encoding = aEncoding;

        }

        public String getValueToBeDisplayed()
        {
            return _dataToBeDisplayed;
        }

        public String getValueToBeSigned()
        {
            return _base64EncodedHashVal;
        }

        public String getEncoding()
        {
            return encoding;
        }

        public String getMimeType()
        {
            return mimeType;
        }

        public SignatureType getSignatureType()
        {
            return _signatureType;
        }

        public String getHashURI()
        {
            return hashURI;
        }

        public void setHashURI(String hashURI)
        {
            this.hashURI = hashURI;
        }
    }
}