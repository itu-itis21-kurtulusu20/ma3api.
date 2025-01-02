package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

/**
 * Paramater constants for signature attributes
 */

public
class AllEParameters extends EParameters 
{
	/**
	 * ISignable object
	 */
	public static final String P_CONTENT = "P_CONTENT"; 
    /**
     * DigestAlg object
     * signer s digest algorithm, used for message digest attribute.
     */
	public static final String P_DIGEST_ALGORITHM = "P_DIGEST_ALGORITHM";  
    /**
     * ECertificate object
     */
	public static final String  P_SIGNING_CERTIFICATE = "P_SIGNING_CERTIFICATE"; 
    /**
     * ECertificate object
     * given by user when certificate is not in signeddata
     */
	public static final String  P_EXTERNAL_SIGNING_CERTIFICATE = "P_EXTERNAL_SIGNING_CERTIFICATE";
    /**
     * ESignerInfo object
     */
	public static final String P_SIGNER_INFO = "P_SIGNER_INFO";
    /**
     * ESignedData object
     */
	public static final String P_SIGNED_DATA = "P_SIGNED_DATA";  
    /**
     * List{@literal <ECertificate>} object
     */
	public static final String P_TRUSTED_CERTIFICATES = "P_TRUSTED_CERTIFICATES";  
    /**
     * List{@literal <ECertificate>} object
     */
	public static final String P_ALL_CERTIFICATES = "P_ALL_CERTIFICATES";  
    /**
     * List{@literal <ECRL>} object
     */
	public static final String P_ALL_CRLS = "P_ALL_CRLS";  
    /**
     * List{@literal <EBasicOCSPResponse>} object
     */
	public static final String P_ALL_BASIC_OCSP_RESPONSES = "P_ALL_BASIC_OCSP_RESPONSES";  
    /**
     * CertRevocationInfo object
     */
	public static final String P_CERTIFICATE_REVOCATION_LIST = "P_CERTIFICATE_REVOCATION_LIST";  
    /**
     * ESignerInfo object
     */
	public static final String P_PARENT_SIGNER_INFO = "P_PARENT_SIGNER_INFO";   
    /**
     * byte [] object
     */
	public static final String P_CONTENT_INFO_BYTES = "P_CONTENT_INFO_BYTES"; 
	/**
     * byte [] object
     */
	public static final String P_PRE_CALCULATED_TIMESTAMP_HASH = "P_PRE_CALCULATED_TIMESTAMP_HASH"; 	
	/**
     * boolean object
     */
	public static final String P_ESAV3_ABOVE_EST = "P_ESAV3_ABOVE_EST";
    /**
     * Calendar object
     */
    public static final String P_PARENT_ESA_TIME = "P_PARENT_ESA_TIME";
    /**
     * Boolean object, use validation data within signature only
     */
    public static final String P_VALIDATION_WITHOUT_FINDERS = "P_VALIDATION_WITHOUT_FINDERS";
    /**
     * SigningCertificate object
     */
    public static final String P_MOBILE_SIGNER_SIGNING_CERT_ATTR = "P_MOBILE_SIGNER_SIGNING_CERT_ATTR";
    /**
     * SigningCertificatev2 object
     */
    public static final String P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2 = "P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2";
    /**
     * boolean object
     */
    public static final String P_PADES_SIGNATURE = "P_PADES_SIGNATURE";    	
    /**
	 * Calendar Signing time(from attr) object
     */
	public static final String P_SIGNING_TIME_ATTR = "P_SIGNING_TIME_ATTR"; 
}
