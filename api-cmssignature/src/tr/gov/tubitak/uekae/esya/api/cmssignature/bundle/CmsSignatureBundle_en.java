package tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;

import java.util.ListResourceBundle;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS.*;


public class CmsSignatureBundle_en extends ListResourceBundle 
{
	private Object[][] mContents = {
		// General Start
		{ _0_MISSING_PARAMETER.name(),"{0} parameter is missing." },
		{ _0_WRONG_PARAMETER_TYPE_1_.name(),"{0} parameter is not of type {1}" },
		
		// Archive Time Stamp  Attribute Checker Start
		{ NO_ARCHIVE_TSA_IN_SIGNEDDATA.name(),"Archive Time Stamp attribute not found in Signed Data." },
		{ ARCHIVE_TSA_DECODE_ERROR.name(),"Archive Time Stamp attribute can not be decoded in Signed Data." },
		{ ARCHIVE_TSA_CHECK_UNSUCCESSFUL.name(),"Archive Time Stamp attribute check is unsuccessful." },		      
		{ ARCHIVE_TSA_CHECK_SUCCESSFUL.name(),"Archive Time Stamp attribute check is successful." },
		
		//Signer
		{ PARENT_SIGNER_ESAv2.name(),"One of parent signer type is ESA with v2." },
		{ PARENT_SIGNER_ESAv3.name(),"One of parent signer type is ESA with v3." },

		//Signing in two steps
		{ NO_UNFINISHED_SIGNATURE.name(),"Unfinished signature could not be found." },
		{ NOT_VALID_SIGNATURE_VALUE_FOR_UNFINISHED_SIGNATURE.name(),"The given signature value is not valid for unfinished signature."},
		
		// Archive Time Stamp V2 Attribute Checker Start
		{ NO_ARCHIVE_TSA_V2_IN_SIGNEDDATA.name(),"Archive Time Stamp V2 attribute not found in Signed Data." },
		{ ARCHIVE_TSA_V2_DECODE_ERROR.name(),"Archive Time Stamp V2 attribute can not be decoded in Signed Data." },
		{ ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL.name(),"Archive Time Stamp V2 attribute check is unsuccessful." },		      
		{ ARCHIVE_TSA_V2_CHECK_SUCCESSFUL.name(),"Archive Time Stamp V2 attribute check is successful." },

		// Archive Time Stamp V3 Attribute Checker Start
		{ NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA.name(),"Archive Time Stamp V3 attribute not found in Signed Data." },
		{ ARCHIVE_TSA_V3_DECODE_ERROR.name(),"Archive Time Stamp V3 attribute can not be decoded in Signed Data." },
		{ ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL.name(),"Archive Time Stamp V3 attribute check is unsuccessful." },		      
		{ ARCHIVE_TSA_V3_CHECK_SUCCESSFUL.name(),"Archive Time Stamp V3 attribute check is successful." },
		
		// CAsES_C Time Stamp Attribute Checker Start
		{ NO_CADESC_TSA_IN_SIGNEDDATA.name(),"CAdES_C Time Stamp attribute not found in Signed Data." },
		{ CADESC_TSA_DECODE_ERROR.name(),"CAdES_C Time Stamp attribute can not be decoded in Signed Data." },
		{ CADESC_TSA_CHECK_UNSUCCESSFUL.name(),"CAdES_C Time Stamp attribute check is unsuccessful." },		      
		{ CADESC_TSA_CHECK_SUCCESSFUL.name(),"CAdES_C Time Stamp attribute check is successful." },

		//Certificate Checker Start
		{ NO_SIGNING_TIME.name(),"Error in getting signing time." },
		{ CERTIFICATE_VALIDATION_SUCCESSFUL.name(),"Signer Certificate validation is successful" },
		{ CERTIFICATE_VALIDATION_UNSUCCESSFUL.name(),"Signer Certificate validation is unsuccessful. Validation status : {0}" },
		{ CERTIFICATE_NO_PATH_FOUND.name(), "No trusted path found. Certificate's root sertificate may not be one of your trusted certificates"},
		{ CERTIFICATE_CHECKER_FAIL.name(), "{0} failed"},
		{ CERTIFICATE_REVOCATION_MAP_INCOMPLETE.name(), "Some references or values are missing"},
		
		//Certificate References Values Match Checker
		{ NO_COMPLETE_CERTIFICATE_REFERENCES_IN_SIGNEDDATA.name(),"Complete Certificate References attribute not found in Signed Data." },
		{ COMPLETE_CERTIFICATE_REFERENCES_DECODE_ERROR.name(),"Complete Certificate References attribute can not be decoded in Signed Data." },
		{ NO_CERTIFICATE_VALUES_ATTRIBUTE_IN_SIGNEDDATA.name(),"Certificate Values attribute not found in Signed Data." },
		{ CERTIFICATE_VALUES_ATTRIBUTE_DECODE_ERROR.name(),"Certificate Values attribute can not be decoded in Signed Data." },
		{ CertificateRefsValuesMatchChecker_UNSUCCESSFUL.name(),"Certificate References and Values matching is unsuccessful." },		      
		{ CertificateRefsValuesMatchChecker_SUCCESSFUL.name(),"Certificate References and Values matching is successful." },
		
		//Check All Checker
		{  NO_MUST_ATTRIBUTE_IN_SIGNED_DATA.name(),"Must attribute not found in signed data."},
		{  NO_OPTIONAL_ATTRIBUTE_IN_SIGNED_DATA.name(),"Optional attribute(CADES_C or Reference Timestamp) not found in signed data."},
		{  ALL_CHECKERS_SUCCESSFULL.name(),"All checker results are successful."},
		{  ALL_CHECKERS_UNSUCCESSFULL.name(),"All checker results are unsuccessful."},

		//Check One Checker
		{ NO_CHECKER_SUCCESSFULL.name(),"No checker is found."},
		
		//Content Timestamp Checker
		{ NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA.name(),"Content Timestamp not found in signed data."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR.name(),"Content Timestamp can not be decoded."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Content Timestamp check is successful."},
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Content Timestamp check is unsuccessful."},
		
		//Content Type Checker
		{ NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA.name(),"Content Type attribute not found in signed data."},
		{ CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR.name(),"Content Type attribute can not be decoded."},
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Content Type attribute check is successful."},
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Content Type,is not same with the one in EncapsulatedContentInfo.Check is unsuccessful."},
		
		//Crypto Checker
		{ NO_SIGNER_CERTIFICATE_FOUND.name(),"Signer certificate not found."},
		{ SIGNED_ATTRIBUTES_ENCODE_ERROR.name(),"Signed attributes can not be encoded."},
		{ SIGNATURE_VERIFICATION_ERROR.name(),"Crypto error in signature verification."},
		{ SIGNATURE_VERIFICATION_SUCCESSFUL.name(),"Signature crypto verification is successful."},
		{ SIGNATURE_VERIFICATION_UNSUCCESSFUL.name(),"Signature crypto verification is unsuccessful."},
		
		//MessageDigest Checker
		{ NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND.name(),"Message Digest attribute not found in signed data."},
		{ MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR.name(),"Message Digest attribute can not be decoded."},
		{ SIGNER_DIGEST_ALGORITHM_UNKNOWN.name(),"Signer digest algorithm is unknown."},
		{ MESSAGE_DIGEST_CHECKER_ERROR.name(),"Error in message digest attribute checker."},
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Message digest check is successful."},
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Value in message digest attribute does not match with the one calculated.Check is unsuccessful."},
		
		//RevocationRefsValuesMatchChecker
		{ REVOCATION_REFERENCES_ATTRIBUTE_NOT_FOUND.name(),"Complete Revocation References attribute not found."},
		{ REVOCATION_REFERENCES_ATTRIBUTE_DECODE_ERROR.name(),"Complete Revocation References attribute can not be decoded."},
		{ REVOCATION_VALUES_ATTRIBUTE_NOT_FOUND.name(),"Revocation Values attribute not found."},
		{ REVOCATION_VALUES_DECODE_ERROR.name(),"Revocation Values attribute can not be decoded."},
		{ REVOCATION_REFS_VALUES_MATCH_SUCCESSFUL.name(),"Revocation References and Values matching is successful."},
		{ REVOCATION_REFS_VALUES_MATCH_UNSUCCESSFUL.name(),"Revocation References and Values matching is unsuccessful."},
		
		//Signature Policy Checker
		{ SIGNATURE_POLICY_ATTRIBUTE_NOT_FOUND.name(),"Signature Policy Identifier attribute not found."},
		{ SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR.name(),"Signature Policy Identifier attribute can not be decoded."},
		{ SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR.name(),"Error in signature policy attribute hash control."},
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Hash in signature policy attribute is same as the calculated one.Check is successful."},
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Hash in signature policy attribute is not same as the calculated one.Check is unsuccessful."},
		{ SIGNATURE_POLICY_VALUE_NOT_FOUND.name(),"Signature policy could not be found."},
		
		//SignatureTimeStamp Checker
		{ SIGNATURE_TS_NOT_FOUND.name(),"Signature Timestamp attribute not found."},
		{ SIGNATURE_TS_DECODE_ERROR.name(),"Signature Timestamp attribute can not be decoded."},
		{ SIGNATURE_TS_CHECK_SUCCESSFUL.name(),"Signature Timestamp attribute check is successful."},
		{ SIGNATURE_TS_CHECK_UNSUCCESSFUL.name(),"Signature Timestamp attribute check is unsuccessful."},
		
		//SigningTime Checker
		{ SIGNING_TIME_ATTRIBUTE_DECODE_ERROR.name(),"SigningTime attribute can not be decoded."},
		{ SIGNING_TIME_CHECKER_SUCCESSFUL.name(),"Time in signaturetimestamp attribute is after time in signingtime attribute"},
		{ SIGNING_TIME_CHECKER_UNSUCCESSFUL.name(),"Time in signaturetimestamp attribute is before time in signingtime attribute. Declared Time: {0}. TimeStamp: {1}."},
		{ SIGNING_TIME_EXISTS.name(), "Signing time attribute exists"},
		{ NO_SIGNING_TIME_ATTRIBUTE.name(),"No signing time attribute"},
		
		//TS
		{ TS_DECODE_ERROR.name(),"Error at decoding time stamp."},
		
		//Timestamp Certificate Checker
		{ TS_CERTIFICATE_NOT_FOUND.name(),"Certificate of timestamp server not found."},
		{ TS_CERTIFICATE_NOT_QUALIFIED.name(),"Certificate is not authorized for timestamping."},//TODO duzelt
		
		//TimestampedCertsCrlsRefsAttrChecker
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND.name(),"Time-stamped-certs-crls-references attribute not found."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR.name(),"Time-stamped-certs-crls-references attribute can not be decoded."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL.name(),"Time-stamped-certs-crls-references attribute check is successful."},
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL.name(),"Time-stamped-certs-crls-references attribute check is unsuccessful."},
		
		
		//TimestampMessageDigest Checker
		{ TS_MESSAGE_DIGEST_CHECKER_DECODE_ERROR.name(),"Error while decoding TSTInfo from timestamp attribute."},
		{ TS_MESSAGE_DIGEST_CHECKER_DIGEST_CALCULATION_ERROR.name(),"Error while calculating digest value."},
		{ TS_MESSAGE_DIGEST_CHECKER_SUCCESSFUL.name(),"Digest check is successful."},
		{ TS_MESSAGE_DIGEST_CHECKER_UNSUCCESSFUL.name(),"Digest value in timestamp is not same as the calculated one.Check is unsuccessful."},
		
		//TimestampSignatureChecker
		{ TS_SIGNATURE_CHECKER_SUCCESSFUL.name(),"Signature of timestamp is verified."},
		{ TS_SIGNATURE_CHECKER_UNSUCCESSFUL.name(),"Signature of timestamp is not verified."},
		
		
		//TimestampTimeChecker
		{ TS_TIME_CHECKER_SIGNATURE_TS_NOT_FOUND.name(),"Signature timestamp not found."},
		{ TS_TIME_CHECKER_TIME_ERROR.name(),"Error while getting time info from timestamp."},
		{ TS_TIME_CHECKER_COMPARISON_ERROR.name(),"Error while comparing dates of timestamps."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_EST.name(),"Date of archive timestamp is before date of signature timestamp."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC.name(),"Date of archive timestamp is before date of CAdES_C_TimeStamp."},
		{ TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS.name(),"Date of archive timestamp is before date of references timestamp."},
		{ TS_TIME_CHECKER_SUCCESSFUL.name(),"Dates of timestamps are ordered."},
		{ TS_TIME_CHECKER_UNSUCCESSFUL.name(),"Dates of timestamps are not ordered."},
		
		
		//SigningCertificate Attribute Checker
		{ SIGNING_CERTIFICATE_DECODE_ERROR.name(),"Signing certificate attribute can not be decoded."},
		{ ISSUER_SERIAL_DOESNOT_MATCH_SIGNER_IDENTIFIER.name(),"IssuerSerial field in attribute does not match with signeridentifier field of signerinfo."},
		{ CERT_HASH_DOESNOT_MATCH.name(),"Certificate hash value in attribute is not same as signer certificate hash value."},
		{ SIGNING_CERTIFICATE_ATTRIBUTE_CHECK_SUCCESSFUL.name(),"Signing certificate attribute check is successful."},
		{ SIGNING_CERTIFICATE_ATTRIBUTE_HASH_CALCULATION_ERROR.name(),"Error in hash calculation of signer certificate."},
		{ ISSUER_SERIAL_DOESNOT_EXISTS.name(),"Attribute does not contain issuerSerial field."},
		
		//ParentSignatureChecker
		{ PARENT_SIGNATURE_INVALID.name(), "Parent signature is invalid."},
		{ PARENT_SIGNATURE_VALID.name(), "Parent signature is valid."},
		
		//ProfileRevocationValueMatcherChecker
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_SUCCESSFUL.name(), "Profile and revocation values match is successful."},
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER_UNSUCCESSFUL.name(), "Error in matching of profile and revocation values."},
		{ PROFILE_AND_SIGNATURE_TYPE_MATCH_UNSUCCESSFUL.name(), "Profile is \"{0}\" and signature type is \"{1}\"."},
		{ PROFILE_P3_DOESNOT_USE_CRL.name(), "Profile is P3 and CRL is not used."},
		{ PROFILE_P4_DOESNOT_USE_OCSP.name(), "Profile is P4 and OCSP is not used."},

		//ProfileAttributesChecker
		{ TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL.name(), "Profile attributes compatible with turkish standards."},
		{ NOT_A_TURKISH_PROFILE.name(), "Not one of Turkish profile.No check done."},		
		{ PROFILE_POLICY_HASH_NOT_SHA256.name(), "Policy digest algorithm is not SHA-256."},
		{ SIGNING_TIME_ATTRIBUTE_MISSING.name(), "SigningTime attribute missing."},
		{ SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING.name(), "Signing Certificate V2 Attribute missing."},
		{ TS_TIME_NOT_AFTER_2H.name(), "Signature time stamp must be taken within 2 hours after signing time."},
		{ SIGNATURE_TIME_ERROR.name(), "Error while getting times from signature."},
		
		//ATSHashIndexAttrChecker
		{ ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL.name(), "Archive TimeStamp V3 Component Checker is successful."},
		{ UNSIGNED_ATTRIBUTE_NOT_INCLUDED.name(), "One of unsigned attributes not included to archive timestamp."},
		{ UNSIGNED_ATTRIBUTE_MISSING.name(), "One of unsigned attributes missing."},	
		
		//Attributes
		{UNKNOWN_COMMITMENT_TYPE.name(), "Unknown commitment type."},
		
		//Checker names
		{ ARCHIVE_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"Archive TimeStamp Attribute Checker" },
		{ ARCHIVE_TIMESTAMP_V2_ATTRIBUTE_CHECKER.name(),"Archive TimeStamp V2 Attribute Checker" },
		{ ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER.name(),"Archive TimeStamp V3 Attribute Checker" },		
		{ CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"CAdES_C_TimeStamp Attribute Checker" },
		{ CERTIFICATE_VALIDATION_CHECKER.name(),"Certificate Validation Checker" },
		{ CERTIFICATE_REFERENCES_VALUES_MATCH_CHECKER.name(),"Certificate References Values Match Checker" },		      
		{ CHECK_ALL_CHECKER.name(),"Check All Checker" },
		{ CHECK_ONE_CHECKER.name(),"Check One Checker" },
		{ CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER.name(),"Content TimeStamp Attribute Checker" },
		{ CONTENT_TYPE_ATTRIBUTE_CHECKER.name(),"Content Type Attribute Checker" },
		{ SIGNATURE_CHECKER.name(),"Signature Checker" },
		{ MESSAGE_DIGEST_ATTRIBUTE_CHECKER.name(),"Message Digest Attribute Checker" },
		{ REVOCATION_REFERENCES_AND_VALUES_MATCH_CHECKER.name(),"Revocation References and Values Match Checker" },
		{ SIGNATURE_POLICY_ATTRIBUTE_CHECKER.name(),"Signature Policy Attribute Checker" },		      
		{ SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER.name(),"SignatureTimeStamp Attribute Checker" },
		{ SIGNING_CERTIFICATE_ATTRIBUTE_CHECKER.name(),"Signing Certificate Attribute Checker" },
		{ SIGNING_CERTIFICATE_V2_ATTRIBUTE_CHECKER.name(),"Signing Certificate V2 Attribute Checker" },
		{ SIGNING_TIME_CHECKER.name(),"Signing Time Checker" },
		{ SIGNING_TIME_EXISTENCE_CHECKER.name(),"Signing Time Existence Checker" },
		{ TIMESTAMP_CERTIFICATE_CHECKER.name(),"TimeStamp Certificate Checker" },
		{ TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER.name(),"Timestamped-Certs-Crls-Refs Attribute Checker" },		      
		{ TIMESTAMP_MESSAGE_DIGEST_CHECKER.name(),"TimeStamp Message Digest Checker" },
		{ TIMESTAMP_SIGNATURE_CHECKER.name(),"TimeStamp Signature Checker" },
		{ TIMESTAMP_TIME_CHECKER.name(),"TimeStamp Time Checker" },
		{ PARENT_SIGNATURE_CHECKER.name(), "Parent Signature Checker"},
		{ PROFILE_REVOCATION_VALUE_MATCHER_CHECKER.name(), "Profile and Revocation Values Match Checker"},
		{ TURKISH_PROFILE_ATTRIBUTES_CHECKER.name(), "Turkish Profile Attributes Checker"},
		{ ATS_HASH_INDEX_ATTRIBUTE_CHECKER.name(), "Archive TimeStamp V3 Component Checker"},
		
		 //SignedDataValidationResult
		{ SIGNATURE_CHECKED_RESULTS.name(),". Signature is checked. \nResults:\n"},
		{ SIGNER_CERTIFICATE.name(),"Signer Certificate:"},

		//SignatureValidationResult
		{ COUNTER_SIGNATURE_VERIFICATION_RESULTS.name(),"Verification Results of Counter Signatures of the Signature:\n"},
		{ COUNTER_SIGNATURE_CHECKED.name(),". counter signature is checked:\n"},
		{ SIGNATURE_CHECKER_RESULTS.name(),"Signature Checker Results:\n"},
		{ PRE_VERIFICATION_DONE.name(),"Pre-verification is done.\n"},
		
		{ SUB_CHECKER_RESULTS.name(),"\tSub Checker Results:\n"},
		
		
		{ CERT_EXPIRED_ERROR_IN_TS.name(), "Certificate expired. Can not get time stampt to that signature."},
		{ CONTENT_AND_SIGNER_DOESNT_MATCH.name(), "The content that wants to be added to sign and the content that is signed by signer does not match."},
		{ EXTERNAL_CONTENT_CANT_ATTACH.name(), "External content could not added to signed data."},
		{ CONTENT_NULL.name(), "ContentInfo is null."},

		{ CERTIFICATE_VALIDATION_EXCEPTION.name(), "Certificate validation exception."},
		
		  //VALIDATE_TIMESTAMP_WHILE_SIGNING
		{  SIGNATURE_TIMESTAMP_INVALID.name(),"Taken signaturetimestamp is invalid." },
		{  CADES_C_TIMESTAMP_INVALID.name(),"Taken CAdES_C_TimeStampAttr is invalid." },
		{  CERTSCRLS_TIMESTAMP_INVALID.name(), "Taken TimeStampedCertsCrlsAttr is invalid."},
		{  ARCHIVE_TIMESTAMP_INVALID.name(),"Taken ArchiveTimeStampAttr is invalid." },

		//Pades Signature
		{ ONLY_LAST_USER_SIGNATURE_CAN_BE_UPGRADED.name(), "Only last user signature can be upgraded!"}
	};		  

	@Override
	protected Object[][] getContents() 
	{
		return mContents;
	}

}
