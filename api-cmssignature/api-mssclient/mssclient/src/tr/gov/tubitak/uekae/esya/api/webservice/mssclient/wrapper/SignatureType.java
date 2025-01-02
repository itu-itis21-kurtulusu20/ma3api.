package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

/**
 * Signature types supported by MSSP, additional types may be added...
 */
public enum SignatureType {
	PKCS7("http://uri.etsi.org/TS102204/v1.1.2#PKCS7"),
    XML("http://uri.etsi.org/TS102204/v1.1.2#XML-Signature"),
	MULTIPLESIGNATURE("TRMobileSignature:MultipleSignature");

    private String _signatureType;

    private SignatureType(String aSignatureUrl) {
        _signatureType = aSignatureUrl;
    }

    public String getSignatureUrl() {
        return _signatureType;
    }


}
