using System;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    public class EParameters
    {
        /**
        * Content is not added to the signed data. While signing big sized files external content is
        * suggested
        * ISignable object
        */
        public static readonly String P_EXTERNAL_CONTENT = "P_EXTERNAL_CONTENT";
        /**
        * If you want to define a content type, you can use P_CONTENT_TYPE parameter. The default value of
        * the content type is data (1, 2, 840, 113549, 1, 7, 1)
        * Asn1ObjectIdentifier object
        */
        public static readonly String P_CONTENT_TYPE = "P_CONTENT_TYPE";
        /**
         * It defines the policy of certificate validation. It must be assigned to an object in both signing a data or
         * validating a signed data unless the P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter assigned to false.
        * ValidationPolicy object
        */
        public static readonly String P_CERT_VALIDATION_POLICY = "P_CERT_VALIDATION_POLICY";

        /**
         * It defines policies for certificate validation.It is optional. It can define different 
         * policies for different type of certificate like MaliMuhur,TimeStamp,Qualified certificate.
         * CertValidationPolicies object
         */
        public static readonly String P_CERT_VALIDATION_POLICIES = "P_CERT_VALIDATION_POLICYMAP";
        /**
         * Digest algorithm for time stamp 
         * DigestAlg object, Default Value: SHA1
         * Don't use this anymore, USE TSSettings with DigestAlg.
         */

        public static readonly String P_TS_DIGEST_ALG = "P_TS_DIGEST_ALG";
        /**
      * Time Stamp setting to get time stamp
      * TSSettings object
      */
        public static readonly String P_TSS_INFO = "P_TSS_INFO";

        /**
     * When two independent parties want to evaluate an electronic signature, it is fundamental that they get 
     * the same result. This requirement can be met using comprehensive signature policies that ensure consistency 
     * of signature validation
     * SignaturePolicyIdentifierAttr is used to define policy.
     * To use policy the signature type must be EPES or above. If the signature type is EPES, 
     * SignaturePolicyIdentifierAttr is added to signature by default.  
     * int[] object
     */
        public static readonly String P_POLICY_ID = "P_POLICY_ID";
        /**
     * P_POLICY_VALUE parameter refers to value of policy. 
     * byte[] object
     */
        public static readonly String P_POLICY_VALUE = "P_POLICY_VALUE";
        /**
     * P_POLICY_DIGEST_ALGORITHM parameter refers to the algorithm 
     * DigestAlg object, Default Value: SHA1
     */
        public static readonly String P_POLICY_DIGEST_ALGORITHM = "P_POLICY_DIGEST_ALGORITHM";
        /**
     * The certificates to pass signature validation.
     * List<ECertificate> object
     */
        public static readonly String P_INITIAL_CERTIFICATES = "P_INITIAL_CERTIFICATES";
        /**
     * The crls to pass signature validation.
     * List<ECRL> object
     */
        public static readonly String P_INITIAL_CRLS = "P_INITIAL_CRLS";
        /**
     * The ocsp responses to pass signature validation.
     * List<EOCSPResponse> object
     */
        public static readonly String P_INITIAL_OCSP_RESPONSES = "P_INITIAL_OCSP_RESPONSES";

        /**
	 * Signing time attribute is a user defined attribute. The signer can assign signing time anything that he/she 
	 * wants. Parameter defines whether trusting the signing time
	 * Boolean object, Default Value: true
	 */
        public static readonly String P_TRUST_SIGNINGTIMEATTR = "P_TRUST_SIGNINGTIMEATTR";

        /**
	 * Signing time can be set while validating a document. If there is no time stamp in the signature and it is set,
	 * the signing time of signatures will be the set time. 
     * Calendar object
     */

        public static readonly String P_SIGNING_TIME = "P_SIGNING_TIME";

        /**
	    * Current time can be set when the system time is not trusted. It is used while validating the signature.
        * Calendar object, Default Value: System Time
        */
        public static readonly String P_CURRENT_TIME = "P_CURRENT_TIME";
        /**
         * Grace Period is a time period which permits the certificate revocation information to propagate through the
         * revocation process to relying parties.
         * Long object that corresponds the the time period in seconds , Default Value: 86400 (24 hours) 
         */
        public static readonly String P_GRACE_PERIOD = "P_GRACE_PERIOD";
        /**
         * Revocation info components must be found in a time interval after the signing time. This interval can expand 
         * until to the certificate expiration date.  If no value passed to the parameter (it is null) or 
         * the calculated last revocation info components finding time is
         * after the certificate expiration date, it is set to the certificate expiration date. 
         * Long object that corresponds the the time period in seconds , Default Value: null.
         */
        public static readonly String P_REVOCINFO_PERIOD = "P_LAST_REVOCINFO_PERIOD";

        /**
     * While signing a content or validating a signed data, certificates must be validated. If you want,
     * you can skip validating certificates while signing a content.
     * Boolean object, Default Value: true
     */
        public static readonly String P_VALIDATE_CERTIFICATE_BEFORE_SIGNING = "P_VALIDATE_CERTIFICATE_BEFORE_SIGNING";
        /**
     * The digest algorithm that will be used for the attributes SigningCertificateV1/V2,
     * CompleteCertificateReferences,CompleteRevocationReferences
     * DigestAlg object, Default Value: SHA1
     */
        public static readonly String P_REFERENCE_DIGEST_ALG = "P_REFERENCE_DIGEST_ALG";

        /**
     * Counter signature can be added as an attribute to the signed data. In order to add counter signature as an
     * attribute signature certificate must be set. It is used with CounterSignatureAttr.
     * ECertificate object
     */
        public static readonly String P_COUNTER_SIGNATURE_CERTIFICATE = "P_COUNTER_SIGNATURE_CERTIFICATE";
        /** 
     * CounterSignature can be added as an attribute to the signed data. In order to add counter signature as an
     * attribute, BaseSigner must be set.
     * BaseSigner object
     */
        public static readonly String P_COUNTER_SIGNATURE_SIGNER_INTERFACE = "P_COUNTER_SIGNATURE_SIGNER_INTERFACE";

        /**
	 * The content-reference attribute is a link from one SignedData to another. It may be used to link a 
	 * reply to the original message to which it refers, or to incorporate by reference one SignedData into 
	 * another. It is used with ContentRefAttr. It is signature value of the SignedData.
     * byte [] object
     */
        public static readonly String P_SIGNATURE = "P_SIGNATURE";

        /**
	    * Grace period is ignored. If there is a CRL that covers the signature time, it can be used in the validation of
	    * signature. 
	    */
        public static readonly String P_IGNORE_GRACE = "P_IGNORE_GRACE";
        /**
       * Just use references and their corresponding values to validate signature  
       * Also check order of the references and values.
       */
        public static readonly String P_FORCE_STRICT_REFERENCE_USE = "P_FORCE_STRICT_REFERENCE_USE";

        /**
        * While signing a content or validating a signed data with timestamp, timestamp must be validated. If you want,
        * you can skip validating timestamp while signing a content with timestamp.
        * Boolean object, Default Value: false
        */
        public static readonly String P_VALIDATE_TIMESTAMP_WHILE_SIGNING = "P_VALIDATE_TIMESTAMP_WHILE_SIGNING";
        /**
         * Where to find referenced validation data
         * @see tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver
         */
        public static readonly String P_VALIDATION_INFO_RESOLVER = "P_VALIDATION_INFO_RESOLVER";
        /**
        * Digest algorithm used for OCSP request
        */
        public static readonly String P_OCSP_DIGEST_ALG = "P_OCSP_DIGEST_ALG";

        public static readonly String P_TOLERATE_SIGNING_TIME_BY_SECONDS = "P_TOLERATE_SIGNING_TIME_BY_SECONDS";

        public static readonly String P_VALIDATION_PROFILE = "P_VALIDATION_PROFILE";
    }
}
