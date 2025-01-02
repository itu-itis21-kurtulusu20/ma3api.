using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer
{
    /**
 * Represents signable text data
 */

    public class TextSignable : ISignable
    {
        private readonly String _plainText;

        private readonly SignatureType _signatureType = SignatureType.PKCS7;
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