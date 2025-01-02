using System;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * Paramter constants for signature attributes
 */
    public class AllEParameters : EParameters
    {
        /**
	 * ISignable object
	 */
        public static readonly String P_CONTENT = "P_CONTENT";

        /**
         * DigestAlg object
         * signer s digest algorithm, used for message digest attribute.
         */
        public static readonly String P_DIGEST_ALGORITHM = "P_DIGEST_ALGORITHM";
        /**
         * ECertificate object
         */
        public static readonly String P_SIGNING_CERTIFICATE = "P_SIGNING_CERTIFICATE";
        /**
         * ECertificate object
         * given by user when certificate is not in signeddata
         */
        public static readonly String P_EXTERNAL_SIGNING_CERTIFICATE = "P_EXTERNAL_SIGNING_CERTIFICATE";
        /**
         * ESignerInfo object
         */
        public static readonly String P_SIGNER_INFO = "P_SIGNER_INFO";
        /**
         * ESignedData object
         */
        public static readonly String P_SIGNED_DATA = "P_SIGNED_DATA";
        /**
         * List<ECertificate> object
         */
        public static readonly String P_TRUSTED_CERTIFICATES = "P_TRUSTED_CERTIFICATES";
        /**
         * List<ECertificate> object
         */
        public static readonly String P_ALL_CERTIFICATES = "P_ALL_CERTIFICATES";
        /**
         * List<ECRL> object
         */
        public static readonly String P_ALL_CRLS = "P_ALL_CRLS";
        /**
         * List<EBasicOCSPResponse> object
         */
        public static readonly String P_ALL_BASIC_OCSP_RESPONSES = "P_ALL_BASIC_OCSP_RESPONSES";
        /**
         * CertRevocationInfo object
         */
        public static readonly String P_CERTIFICATE_REVOCATION_LIST = "P_CERTIFICATE_REVOCATION_LIST";
        /**
         * ESignerInfo object
         */
        public static readonly String P_PARENT_SIGNER_INFO = "P_PARENT_SIGNER_INFO";
        /**
         * byte [] object
         */
        public static readonly String P_CONTENT_INFO_BYTES = "P_CONTENT_INFO_BYTES";
        /**
         * byte [] object
         */
        public static readonly String P_PRE_CALCULATED_TIMESTAMP_HASH = "P_PRE_CALCULATED_TIMESTAMP_HASH";
        /**
    * boolean object
    */
        public static readonly String P_ESAV3_ABOVE_EST = "P_ESAV3_ABOVE_EST";
        /**
        * DateTime object
        */
        public static readonly String P_PARENT_ESA_TIME = "P_PARENT_ESA_TIME";
        /**
          * Boolean object, use validation data within signature only
          */
        public static readonly String P_VALIDATION_WITHOUT_FINDERS = "P_VALIDATION_WITHOUT_FINDERS";
        /**
         * SigningCertificate object
        */
        public static readonly String P_MOBILE_SIGNER_SIGNING_CERT_ATTR = "P_MOBILE_SIGNER_SIGNING_CERT_ATTR";
        /**
         * SigningCertificatev2 object
         */
        public static readonly String P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2 = "P_MOBILE_SIGNER_SIGNING_CERT_ATTRv2";
    }
}
