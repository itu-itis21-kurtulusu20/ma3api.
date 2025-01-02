using System;
using System.Reflection;
using System.Resources;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.bundle
{
    public static class Msg
    {
        private static readonly ResourceManager mResources = new ResourceManager("tr.gov.tubitak.uekae.esya.api.cmssignature.Properties.Resource", Assembly.GetExecutingAssembly());        

        // General Start
		public static readonly String _0_MISSING_PARAMETER = "_0_MISSING_PARAMETER";//"{0} parameter is missing." },
        public static readonly String _0_WRONG_PARAMETER_TYPE_1_ = "_0_WRONG_PARAMETER_TYPE_1_";//"{0} parameter is not of type {1}";
		
        //Signer
        public static readonly String PARENT_SIGNER_ESAv2 = "PARENT_SIGNER_ESAv2";
        public static readonly String PARENT_SIGNER_ESAv3 = "PARENT_SIGNER_ESAv3";

        //Signing in two steps
        public static readonly String NO_UNFINISHED_SIGNATURE = "Unfinished signature could not be found.";
        public static readonly String NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE = "The given signature value is not valid for unfinished signature.";

		// Archive Time Stamp  Attribute Checker Start
		public static readonly String NO_ARCHIVE_TSA_IN_SIGNEDDATA = "NO_ARCHIVE_TSA_IN_SIGNEDDATA";//"Archive Time Stamp attribute not found in Signed Data." },
		public static readonly String ARCHIVE_TSA_DECODE_ERROR = "ARCHIVE_TSA_DECODE_ERROR";//"Archive Time Stamp attribute can not be decoded in Signed Data." },
		public static readonly String ARCHIVE_TSA_CHECK_UNSUCCESSFUL = "ARCHIVE_TSA_CHECK_UNSUCCESSFUL";//"Archive Time Stamp attribute check is unsuccessful." },		      
		public static readonly String ARCHIVE_TSA_CHECK_SUCCESSFUL = "ARCHIVE_TSA_CHECK_SUCCESSFUL";//"Archive Time Stamp attribute check is successful." },
		
		// Archive Time Stamp V2 Attribute Checker Start
		public static readonly String NO_ARCHIVE_TSA_V2_IN_SIGNEDDATA="NO_ARCHIVE_TSA_V2_IN_SIGNEDDATA";//"Archive Time Stamp V2 attribute not found in Signed Data." },
		public static readonly String ARCHIVE_TSA_V2_DECODE_ERROR = "ARCHIVE_TSA_V2_DECODE_ERROR";//"Archive Time Stamp V2 attribute can not be decoded in Signed Data." },
		public static readonly String ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL="ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL";//"Archive Time Stamp V2 attribute check is unsuccessful." },		      
		public static readonly String ARCHIVE_TSA_V2_CHECK_SUCCESSFUL="ARCHIVE_TSA_V2_CHECK_SUCCESSFUL";//"Archive Time Stamp V2 attribute check is successful." },

        // Archive Time Stamp V3 Attribute Checker Start
        public static readonly String NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA = "NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA";//"Archive Time Stamp V3 attribute not found in Signed Data." },
        public static readonly String ARCHIVE_TSA_V3_DECODE_ERROR = "ARCHIVE_TSA_V3_DECODE_ERROR";//"Archive Time Stamp V3 attribute can not be decoded in Signed Data." },
        public static readonly String ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL = "ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL";//"Archive Time Stamp V3 attribute check is unsuccessful." },		      
        public static readonly String ARCHIVE_TSA_V3_CHECK_SUCCESSFUL = "ARCHIVE_TSA_V3_CHECK_SUCCESSFUL";//"Archive Time Stamp V3 attribute check is successful." },
		
		// CAsES_C Time Stamp Attribute Checker Start
		public static readonly String NO_CADESC_TSA_IN_SIGNEDDATA = "NO_CADESC_TSA_IN_SIGNEDDATA";//"CAdES_C Time Stamp attribute not found in Signed Data." },
		public static readonly String CADESC_TSA_DECODE_ERROR = "CADESC_TSA_DECODE_ERROR";//"CAdES_C Time Stamp attribute can not be decoded in Signed Data." },
		public static readonly String CADESC_TSA_CHECK_UNSUCCESSFUL = "CADESC_TSA_CHECK_UNSUCCESSFUL";//"CAdES_C Time Stamp attribute check is unsuccessful." },		      
		public static readonly String CADESC_TSA_CHECK_SUCCESSFUL = "CADESC_TSA_CHECK_SUCCESSFUL";//"CAdES_C Time Stamp attribute check is successful." },

		//Certificate Checker Start
        public static readonly String CERTIFICATE_EXPLANATION = "CERTIFICATE_EXPLANATION";
        public static readonly String NO_SIGNING_TIME = "NO_SIGNING_TIME";//"Error in getting signing time." },
		public static readonly String CERTIFICATE_VALIDATION_SUCCESSFUL = "CERTIFICATE_VALIDATION_SUCCESSFUL";//"Signer Certificate validation is successful" },
		public static readonly String CERTIFICATE_VALIDATION_UNSUCCESSFUL = "CERTIFICATE_VALIDATION_UNSUCCESSFUL";//"Signer Certificate validation is unsuccessful. Validation status : {0}" },
        public static readonly String CERTIFICATE_NO_PATH_FOUND = "CERTIFICATE_NO_PATH_FOUND";
        public static readonly String CERTIFICATE_CHECKER_FAIL = "CERTIFICATE_CHECKER_FAIL";// "{0} failed"},
        public static readonly String CERTIFICATE_REVOCATION_MAP_INCOMPLETE = "CERTIFICATE_REVOCATION_MAP_INCOMPLETE";

        //Certificate References Values Match Checker
		public static readonly String NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA = "NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA";//"Complete Certificate References attribute not found in Signed Data."
		public static readonly String COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR = "COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR";//"Complete Certificate References attribute can not be decoded in Signed Data."
        public static readonly String NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA ="NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA";//"Certificate Values attribute not found in Signed Data."
        public static readonly String CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR = "CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR";//"Certificate Values attribute can not be decoded in Signed Data."
        public static readonly String CertificateRefsValuesMatchChecker_UNSUCCESSFUL = "CertificateRefsValuesMatchChecker_UNSUCCESSFUL";//"Certificate References Values Match check is unsuccessful."
        public static readonly String CertificateRefsValuesMatchChecker_SUCCESSFUL = "CertificateRefsValuesMatchChecker_SUCCESSFUL";//"Certificate References Values Match check is successful."

        //Check All Checker
        public static readonly String NO_MUST_ATTRIBUTE_IN_SIGNED_DATA = "NO_MUST_ATTRIBUTE_IN_SIGNED_DATA";
        public static readonly String NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA = "NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA";
        public static readonly String ALL_CHECKERS_SUCCESSFULL = "ALL_CHECKERS_SUCCESSFULL";
        public static readonly String ALL_CHECKERS_UNSUCCESSFULL = "ALL_CHECKERS_UNSUCCESSFULL";

        //Check One Checker
        public static readonly String NO_CHECKER_SUCCESSFULL = "NO_CHECKER_SUCCESSFULL";

        //Content Timestamp Checker
        public static readonly String NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA = "NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA";
        public static readonly String CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR = "CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR";
        public static readonly String CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL = "CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL = "CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL";

        //Content Type Checker
        public static readonly String NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA = "NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA";
        public static readonly String CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR = "CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR";
        public static readonly String CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL = "CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL = "CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL";

        //Crypto Checker
        public static readonly String NO_SIGNER_CERTIFICATE_FOUND = "NO_SIGNER_CERTIFICATE_FOUND";
        public static readonly String SIGNED_ATTRIBUTES_ENCODE_ERROR = "SIGNED_ATTRIBUTES_ENCODE_ERROR";
        public static readonly String SIGNATURE_VERIFICATION_ERROR = "SIGNATURE_VERIFICATION_ERROR";
        public static readonly String SIGNATURE_VERIFICATION_SUCCESSFUL = "SIGNATURE_VERIFICATION_SUCCESSFUL";
        public static readonly String SIGNATURE_VERIFICATION_UNSUCCESSFUL = "SIGNATURE_VERIFICATION_UNSUCCESSFUL";

        //MessageDigest Checker
        public static readonly String NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND = "NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND";
        public static readonly String MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR = "MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR";
        public static readonly String SIGNER_DIGEST_ALGORITHM_UNKNOWN = "SIGNER_DIGEST_ALGORITHM_UNKNOWN";
        public static readonly String MESSAGE_DIGEST_CHECKER_ERROR = "MESSAGE_DIGEST_CHECKER_ERROR";
        public static readonly String MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL = "MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL = "MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL";

        //RevocationRefsValuesMatchChecker
        public static readonly String REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND = "REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND";
        public static readonly String REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR = "REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR";
        public static readonly String REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND = "REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND";
        public static readonly String REVOCATION_VALUES_DECODE_ERROR = "REVOCATION_VALUES_DECODE_ERROR";
        public static readonly String REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL = "REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL";
        public static readonly String REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL = "REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL";

        //Signature Policy Checker
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_NOT_FOUND = "SIGNATURE_POLICY_ATTRIBUTE_NOT_FOUND";
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR = "SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR";
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR = "SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR";
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL = "SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL = "SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL";
        public static readonly String SIGNATURE_POLICY_VALUE_NOT_FOUND = "SIGNATURE_POLICY_VALUE_NOT_FOUND";

        //SignatureTimeStamp Checker
        public static readonly String SIGNATURE_TS_NOT_FOUND = "SIGNATURE_TS_NOT_FOUND";
        public static readonly String SIGNATURE_TS_DECODE_ERROR = "SIGNATURE_TS_DECODE_ERROR";
        public static readonly String SIGNATURE_TS_CHECK_SUCCESSFUL = "SIGNATURE_TS_CHECK_SUCCESSFUL";
        public static readonly String SIGNATURE_TS_CHECK_UNSUCCESSFUL = "SIGNATURE_TS_CHECK_UNSUCCESSFUL";

        //SigningTime Checker
        public static readonly String SIGNING_TIME_ATTRIBUTE_DECODE_ERROR = "SIGNING_TIME_ATTRIBUTE_DECODE_ERROR";
        public static readonly String SIGNING_TIME_CHECKER_SUCCESSFUL = "SIGNING_TIME_CHECKER_SUCCESSFUL";
        public static readonly String SIGNING_TIME_CHECKER_UNSUCCESSFUL = "SIGNING_TIME_CHECKER_UNSUCCESSFUL";
        public static readonly String NO_SIGNING_TIME_ATTRIBUTE = "NO_SIGNING_TIME_ATTRIBUTE";

        //Timestamp Certificate Checker
        public static readonly String TS_CERTIFICATE_NOT_FOUND = "TS_CERTIFICATE_NOT_FOUND";
        public static readonly String TS_CERTIFICATE_NOT_QUALIFIED = "TS_CERTIFICATE_NOT_QUALIFIED";

        //TimestampedCertsCrlsRefsAttrChecker
        public static readonly String TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND = "TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND";
        public static readonly String TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR = "TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR";
        public static readonly String TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL = "TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL = "TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL";


        //TimestampMessageDigest Checker
        public static readonly String TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR = "TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR";
        public static readonly String TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR = "TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR";
        public static readonly String TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL = "TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL";
        public static readonly String TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL = "TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL";

        //TimestampSignatureChecker
        public static readonly String TS_SIGNATURE_CHECKER_SUCCESSFUL = "TS_SIGNATURE_CHECKER_SUCCESSFUL";
        public static readonly String TS_SIGNATURE_CHECKER_UNSUCCESSFUL = "TS_SIGNATURE_CHECKER_UNSUCCESSFUL";

        //TimestampTimeChecker
        public static readonly String TS_TIME_CHECKER_SIGNATURE_TS_NOT_FOUND = "TS_TIME_CHECKER_SIGNATURE_TS_NOT_FOUND";
        public static readonly String TS_TIME_CHECKER_TIME_ERROR = "TS_TIME_CHECKER_TIME_ERROR";
        public static readonly String TS_TIME_CHECKER_COMPARISON_ERROR = "TS_TIME_CHECKER_COMPARISON_ERROR";
        public static readonly String TS_TIME_CHECKER_ARCHIVE_BEFORE_EST = "TS_TIME_CHECKER_ARCHIVE_BEFORE_EST";
        public static readonly String TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC = "TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC";
        public static readonly String TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS = "TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS";
        public static readonly String TS_TIME_CHECKER_SUCCESSFUL = "TS_TIME_CHECKER_SUCCESSFUL";
        public static readonly String TS_TIME_CHECKER_UNSUCCESSFUL = "TS_TIME_CHECKER_UNSUCCESSFUL";

        //SigningCertificate Attribute Checker
        public static readonly String SIGNING_CERTIFICATE_DECODE_ERROR = "SIGNING_CERTIFICATE_DECODE_ERROR";
        public static readonly String ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER = "ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER";
        public static readonly String CERT_HASH_DOESNOT_MATCH = "CERT_HASH_DOESNOT_MATCH";
        public static readonly String SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL = "SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL";
        public static readonly String SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR = "SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR";
        public static readonly String ISSUER_SERIAL_DOESNOT_EXISTS = "ISSUER_SERIAL_DOESNOT_EXISTS";

		 //ATSHashIndexAttrChecker
        public static readonly String ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL = "ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL";
        public static readonly String UNSIGNED_ATTRIBUTE_NOT_INCLUDED = "UNSIGNED_ATTRIBUTE_NOT_INCLUDED";
        public static readonly String UNSIGNED_ATTRIBUTE_MISSING = "UNSIGNED_ATTRIBUTE_MISSING";

        //Attribute 
        public static readonly String UNKNOWN_COMMITMENT_TYPE = "UNKNOWN_COMMITMENT_TYPE";


        //Checker name
        public static readonly String ARCHIVE_TIMESTAMP_ATTRIBUTE_CHECKER = "ARCHIVE_TIMESTAMP_ATTRIBUTE_CHECKER";
        public static readonly String ARCHIVE_TIMESTAMP_V2_ATTRIBUTE_CHECKER = "ARCHIVE_TIMESTAMP_V2_ATTRIBUTE_CHECKER";
        public static readonly String ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER = "ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER";
        public static readonly String CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER = "CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER";
        public static readonly String CERTIFICATE_VALIDATION_CHECKER = "CERTIFICATE_VALIDATION_CHECKER";
        public static readonly String CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER = "CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER";
        public static readonly String CHECK_ALL_CHECKER = "CHECK_ALL_CHECKER";
        public static readonly String CHECK_ONE_CHECKER = "CHECK_ONE_CHECKER";
        public static readonly String CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER = "CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER";
        public static readonly String CONTENT_TYPE_ATTRIBUTE_CHECKER = "CONTENT_TYPE_ATTRIBUTE_CHECKER";
        public static readonly String SIGNATURE_CHECKER = "SIGNATURE_CHECKER";
        public static readonly String MESSAGE_DIGEST_ATTRIBUTE_CHECKER = "MESSAGE_DIGEST_ATTRIBUTE_CHECKER";
        public static readonly String REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER = "REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER";
        public static readonly String SIGNATURE_POLICY_ATTRIBUTE_CHECKER = "SIGNATURE_POLICY_ATTRIBUTE_CHECKER";
        public static readonly String SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER = "SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER";
        public static readonly String SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER = "SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER";
        public static readonly String SIGNING_CERTIFICATE_V2_ATTRIBUTE_CHECKER = "SIGNING_CERTIFICATE_V2_ATTRIBUTE_CHECKER";
        public static readonly String SIGNING_TIME_CHECKER = "SIGNING_TIME_CHECKER";
        public static readonly String TIMESTAMP_CERTIFICATE_CHECKER = "TIMESTAMP_CERTIFICATE_CHECKER";
        public static readonly String TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER = "TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER";
        public static readonly String TIMESTAMP_MESSAGE_DIGEST_CHECKER = "TIMESTAMP_MESSAGE_DIGEST_CHECKER";
        public static readonly String TIMESTAMP_SIGNATURE_CHECKER = "TIMESTAMP_SIGNATURE_CHECKER";
        public static readonly String TIMESTAMP_TIME_CHECKER = "TIMESTAMP_TIME_CHECKER";
        public static readonly String PROFILE_REVOCATION_VALUE_MATCHER_CHECKER = "PROFILE_REVOCATION_VALUE_MATCHER_CHECKER";
        public static readonly String TURKISH_PROFILE_ATTRIBUTES_CHECKER = "TURKISH_PROFILE_ATTRIBUTES_CHECKER";
        public static readonly String ATS_HASH_INDEX_ATTRIBUTE_CHECKER = "ATS_HASH_INDEX_ATTRIBUTE_CHECKER";

        //ProfileRevocationValueMatcherChecker
        public static readonly String PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL = "PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL";
        public static readonly String PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL = "PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL";
        public static readonly String PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL = "PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL";
        public static readonly String PROFILE_P3_DOESNOT_USE_CRL = "PROFILE_P3_DOESNOT_USE_CRL";
        public static readonly String PROFILE_P4_DOESNOT_USE_OCSP = "PROFILE_P4_DOESNOT_USE_OCSP";

        //TurkishProfileAttributesChecker
        public static readonly String TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL = "TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL";
        public static readonly String NOT_A_TURKISH_PROFILE = "NOT_A_TURKISH_PROFILE";
        public static readonly String PROFILE_POLICY_HASH_NOT_SHA256 = "PROFILE_POLICY_HASH_NOT_SHA256";
        public static readonly String SIGNING_TIME_ATTRIBUTE_MISSING = "SIGNING_TIME_ATTRIBUTE_MISSING";
        public static readonly String SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING = "SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING";
        public static readonly String TS_TIME_NOT_AFTER_2H = "TS_TIME_NOT_AFTER_2H";
        public static readonly String SIGNATURE_TIME_ERROR = "SIGNATURE_TIME_ERROR";

        //SignedDataValidationResuls
        public static readonly String SIGNATURE_CHECKED_RESULTS = "SIGNATURE_CHECKED_RESULTS";
        public static readonly String SIGNER_CERTIFICATE = "SIGNER_CERTIFICATE";

        //SignedDataValidationResuls
        public static readonly String SUB_CHECKER_RESULTS = "SUB_CHECKER_RESULTS";

        //SignatureValidationResuls
        public static readonly String COUNTER_SIGNATURE_VERIFICATION_RESULTS = "COUNTER_SIGNATURE_VERIFICATION_RESULTS";
        public static readonly String COUNTER_SIGNATURE_CHECKED = "COUNTER_SIGNATURE_CHECKED";
        public static readonly String SIGNATURE_CHECKER_RESULTS = "SIGNATURE_CHECKER_RESULTS";
        public static readonly String PRE_VERIFICATION_DONE = "PRE_VERIFICATION_DONE";

        public static readonly String CERT_EXPIRED_ERROR_IN_TS = "CERT_EXPIRED_ERROR_IN_TS";

        public static readonly String CONTENT_AND_SIGNER_DOESNT_MATCH = "CONTENT_AND_SIGNER_DOESNT_MATCH";
        public static readonly String EXTERNAL_CONTENT_CANT_ATTACH = "EXTERNAL_CONTENT_CANT_ATTACH";
        public static readonly String CONTENT_NULL = "CONTENT_NULL";

        //VALIDATE_TIMESTAMP_WHILE_SIGNING
        public static readonly String GETTING_TS_CERTIFICATE_VALIDATION_DATA_ERROR =
            "GETTING_TS_CERTIFICATE_VALIDATION_DATA_ERROR";
        public static readonly String SIGNATURE_TIMESTAMP_INVALID = "SIGNATURE_TIMESTAMP_INVALID";
        public static readonly String CADES_C_TIMESTAMP_INVALID = "CADES_C_TIMESTAMP_INVALID";
        public static readonly String CERTSCRLS_TIMESTAMP_INVALID = "CERTSCRLS_TIMESTAMP_INVALID";
        public static readonly String ARCHIVE_TIMESTAMP_INVALID = "ARCHIVE_TIMESTAMP_INVALID";

        public static String getMsg(String aMessage)
        {
            //String str = CultureInfo.CurrentCulture.ToString();
            //String str1 = Thread.CurrentThread.CurrentCulture.ToString();
            //String resource = mResources.GetString(aMessage);
            //String resource1 = mResources.GetString(aMessage, Thread.CurrentThread.CurrentCulture);
            return (mResources.GetString(aMessage, I18nSettings.getLocale()));
        }
        public static String getMsg(String aMessage, String[] aArgs)
        {
            return String.Format(getMsg(aMessage), aArgs);            
        }
    }
}
