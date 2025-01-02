using System;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    /**
 * Signature types supported by MSSP, additional types may be added...
 */

    public class SignatureType
    {
        public static readonly SignatureType PKCS7 = new SignatureType("http://uri.etsi.org/TS102204/v1.1.2#PKCS7");
        public static readonly SignatureType XML = new SignatureType("http://uri.etsi.org/TS102204/v1.1.2#XML-Signature");
        public static readonly SignatureType MULTIPLESIGNATURE = new SignatureType("trmobilesignature:MultipleSignature");
  

        private readonly String _signatureType;

        private SignatureType(String aSignatureUrl)
        {
            _signatureType = aSignatureUrl;
        }

        public String getSignatureUrl()
        {
            return _signatureType;
        }
    }
}