namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core
{

	using C14nMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using SignatureMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;

	/// <summary>
	/// @author ahmety
	/// date: May 29, 2009
	/// </summary>
	public static class Constants
	{
		// namespaces
		public const string NS_MA3 = "http://uekae.tubitak.gov.tr/xml/signature#";
		public const string NS_XMLDSIG = "http://www.w3.org/2000/09/xmldsig#";
		public const string NS_XMLDSIG_MORE = "http://www.w3.org/2001/04/xmldsig-more#";
	    public const string NS_XMLDSIG_MORE_2007 = "http://www.w3.org/2007/05/xmldsig-more#";
		public const string NS_XMLDSIG_11 = "http://www.w3.org/2009/xmldsig11#";

		public const string NS_XADES_1_1_1 = "http://uri.etsi.org/01903/v1.1.1#";
		public const string NS_XADES_1_2_2 = "http://uri.etsi.org/01903/v1.2.2#";
		public const string NS_XADES_1_3_2 = "http://uri.etsi.org/01903/v1.3.2#";
		public const string NS_XADES_1_4_1 = "http://uri.etsi.org/01903/v1.4.1#";

		public const string NS_NAMESPACESPEC = "http://www.w3.org/2000/xmlns/";
		public const string NS_XMLLANGSPACESPEC = "http://www.w3.org/XML/1998/namespace";

		public const string NS_MA3_PREFIX = "ma3";
        public const string NS_DS_PREFIX = "ds";
        public const string NS_XADES_PREFIX = "xades";

		// tags
		public const string TAG_SIGNATURE = "Signature";

		public const string TAG_SIGNEDINFO = "SignedInfo";
		public const string TAG_C14NMETHOD = "CanonicalizationMethod";
		public const string TAG_SIGNATUREMETHOD = "SignatureMethod";

		public const string TAG_SIGNATUREVALUE = "SignatureValue";
		public const string TAG_OBJECT = "Object";

		public const string TAG_MANIFEST = "Manifest";
		public const string TAG_REFERENCE = "Reference";
		public const string TAG_DIGESTMETHOD = "DigestMethod";
		public const string TAG_DIGESTVALUE = "DigestValue";

		public const string TAG_TRANSFORMS = "Transforms";
		public const string TAG_TRANSFORM = "Transform";
		public const string TAG_XPATH = "XPath";

		public const string TAG_KEYINFO = "KeyInfo";
		public const string TAG_KEYNAME = "KeyName";
		public const string TAG_KEYVALUE = "KeyValue";
		public const string TAG_X509DATA = "X509Data";

		public const string TAG_ECKEYVALUE = "ECKeyValue";
		public const string TAG_NAMEDCURVE = "NamedCurve";
		public const string TAG_PUBLICKEY = "PublicKey";

		public const string TAG_RSAKEYVALUE = "RSAKeyValue";
		public const string TAG_MODULUS = "Modulus";
		public const string TAG_EXPONENT = "Exponent";

		public const string TAG_DSAKEYVALUE = "DSAKeyValue";
		public const string TAG_G = "G";
		public const string TAG_P = "P";
		public const string TAG_Q = "Q";
		public const string TAG_Y = "Y";

		public const string TAG_RETRIEVALMETHOD = "RetrievalMethod";
		public const string TAG_PGPDATA = "PGPData";
		public const string TAG_SPKIDATA = "SPKIData";
		public const string TAG_MGMTDATA = "MgmtData";

		public const string TAG_X509ISSUERSERIAL = "X509IssuerSerial";
		public const string TAG_X509SKI = "X509SKI";
		public const string TAG_X509SUBJECTNAME = "X509SubjectName";
		public const string TAG_X509CERTIFICATE = "X509Certificate";
		public const string TAG_X509CRL = "X509CRL";
		public const string TAG_X509ISSUERNAME = "X509IssuerName";
		public const string TAG_X509SERIALNUMBER = "X509SerialNumber";

		// etsi XAdES tags
		public const string TAGX_QUALIFYINGPROPERTIES = "QualifyingProperties";
		public const string TAGX_SIGNEDPROPERTIES = "SignedProperties";
		public const string TAGX_SIGNEDSIGNATUREPROPERTIES = "SignedSignatureProperties";
		public const string TAGX_SIGNINGTIME = "SigningTime";

		// signature production place
		public const string TAGX_SIGNATUREPRODUCTIONPLACE = "SignatureProductionPlace";
		public const string TAGX_CITY = "City";
		public const string TAGX_STATEORPROVINCE = "StateOrProvince";
		public const string TAGX_POSTALCODE = "PostalCode";
		public const string TAGX_COUNTRYNAME = "CountryName";

		// signer role
		public const string TAGX_SIGNERROLE = "SignerRole";
		public const string TAGX_CLAIMEDROLES = "ClaimedRoles";
		public const string TAGX_CLAIMEDROLE = "ClaimedRole";
		public const string TAGX_CERTIFIEDROLES = "CertifiedRoles";
		public const string TAGX_CERTIFIEDROLE = "CertifiedRole";

		// signing certificate
		public const string TAGX_SIGNINGCERTIFICATE = "SigningCertificate";
		public const string TAGX_CERTID = "Cert";
		public const string TAGX_CERTDIGEST = "CertDigest";
		public const string TAGX_ISSUERSERIAL = "IssuerSerial";

		// signed data object
		public const string TAGX_SIGNEDATAOBJECTPROPERIES = "SignedDataObjectProperties";
		public const string TAGX_DATAOBJECTFORMAT = "DataObjectFormat";
		public const string TAGX_OBJECTIDENTIFIER = "ObjectIdentifier";
		public const string TAGX_IDENTIFIER = "Identifier";
		public const string TAGX_DOCUMENTATIONREFERENCES = "DocumentationReferences";
		public const string TAGX_DOCUMENTATIONREFERENCE = "DocumentationReference";
		public const string TAGX_DESCRIPTION = "Description";
		public const string TAGX_MIMETYPE = "MimeType";
		public const string TAGX_ENCODING = "Encoding";

		// commitment type
		public const string TAGX_COMMITMENTTYPEINDICATION = "CommitmentTypeIndication";
		public const string TAGX_COMMITMENTTYPEID = "CommitmentTypeId";
		public const string TAGX_OBJECTREFERENCE = "ObjectReference";
		public const string TAGX_COMMITMENTTYPEQUALIFIERS = "CommitmentTypeQualifiers";
		public const string TAGX_COMMITMENTTYPEQUALIFIER = "CommitmentTypeQualifier";
		public const string TAGX_ALLSIGNEDDATAOBJECTS = "AllSignedDataObjects";

		// signature policy
		public const string TAGX_SIGNATUREPOLICYIDENTIFIER = "SignaturePolicyIdentifier";
		public const string TAGX_SIGNATUREPOLICYID = "SignaturePolicyId";
		public const string TAGX_SIGNATUREPOLICYIMPLIED = "SignaturePolicyImplied";
		public const string TAGX_SIGPOLICYID = "SigPolicyId";
		public const string TAGX_SIGPOLICYHASH = "SigPolicyHash";
		public const string TAGX_SIGPOLICYQUALIFIERS = "SigPolicyQualifiers";
		public const string TAGX_SIGPOLICYQUALIFIER = "SigPolicyQualifier";
		public const string TAGX_SPURI = "SPURI";
		public const string TAGX_SPUSERNOTICE = "SPUserNotice";
		public const string TAGX_NOTICEREF = "NoticeRef";
		public const string TAGX_EXPLICITTEXT = "ExplicitText";
		public const string TAGX_ORGANIZATION = "Organization";
		public const string TAGX_NOTICENUMBERS = "NoticeNumbers";
		public const string TAGX_INT = "int";

		// unsigned props
		public const string TAGX_UNSIGNEDPROPERTIES = "UnsignedProperties";
		public const string TAGX_UNSIGNEDDATAOBJECTPROPERTIES = "UnsignedDataObjectProperties";
		public const string TAGX_UNSIGNEDDATAOBJECTPROPERTY = "UnsignedDataObjectProperty";
		public const string TAGX_UNSIGNEDSIGNATUREPROPERTIES = "UnsignedSignatureProperties";
		public const string TAGX_COUNTERSIGNATURE = "CounterSignature";

		// vrefs (validation references)
		public const string TAGX_COMPLETECERTREFS = "CompleteCertificateRefs";
		public const string TAGX_CERTREFS = "CertRefs";
		public const string TAGX_COMPLETEREVOCATIONREFS = "CompleteRevocationRefs";
		public const string TAGX_CRLREFS = "CRLRefs";
		public const string TAGX_OCSPREFS = "OCSPRefs";
		public const string TAGX_OTHERREFS = "OtherRefs";
		public const string TAGX_CRLREF = "CRLRef";
		public const string TAGX_OCSPREF = "OCSPRef";
		public const string TAGX_OTHERREF = "OtherRef";
		public const string TAGX_CRLIDENTIFIER = "CRLIdentifier";
		public const string TAGX_ISSUER = "Issuer";
		public const string TAGX_ISSUETIME = "IssueTime";
		public const string TAGX_NUMBER = "Number";
		public const string TAGX_RESPONDERID = "ResponderID";
		public const string TAGX_PRODUCEDAT = "ProducedAt";
		public const string TAGX_BYNAME = "ByName";
		public const string TAGX_BYKEY = "ByKey";
		public const string TAGX_OCSPIDENTIFIER = "OCSPIdentifier";
		public const string TAGX_DIGESTALGANDVALUE = "DigestAlgAndValue";

		public const string TAGX_ATTRIBUTECERTIFICATEREFS = "AttributeCertificateRefs";
		public const string TAGX_ATTRIBUTEREVOCATIONREFS = "AttributeRevocationRefs";

		public const string TAGX_SIGANDREFSTIMESTAMP = "SigAndRefsTimeStamp";
		public const string TAGX_REFSONLYTIMESTAMP = "RefsOnlyTimeStamp";

		public const string TAGX_ARCHIVETIMESTAMP = "ArchiveTimeStamp";

		public const string TAGX_TIMESTAMPVALIDATIONDATA = "TimeStampValidationData";

		public const string TAGX_CERTIFICATEVALUES = "CertificateValues";
		public const string TAGX_ENCAPSULATEDX509CERTIFICATE = "EncapsulatedX509Certificate";

		public const string TAGX_REVOCATIONVALUES = "RevocationValues";
		public const string TAGX_CRLVALUES = "CRLValues";
		public const string TAGX_OCSPVALUES = "OCSPValues";
		public const string TAGX_ENCAPSULATEDCRLVALUE = "EncapsulatedCRLValue";
		public const string TAGX_ENCAPSULATEDOCSPVALUE = "EncapsulatedOCSPValue";

		public const string TAGX_ATTRAUHORITIESCERTVALUES = "AttrAuthoritiesCertValues";
		public const string TAGX_ATTRIBUTEREVOCATIONVALUES = "AttributeRevocationValues";

		// timestamp
		public const string TAGX_INCLUDE = "Include";
		public const string TAGX_REFERENCEINFO = "ReferenceInfo";
		public const string TAGX_ALLDATAOBJECTSTIMESTAMP = "AllDataObjectsTimeStamp";
		public const string TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP = "IndividualDataObjectsTimeStamp";

		public const string TAGX_SIGNATURETIMESTAMP = "SignatureTimeStamp";

		public const string TAGX_ENCAPSULATEDTIMESTAMP = "EncapsulatedTimeStamp";
		public const string TAGX_XMLTIMESTAMP = "XMLTimeStamp";

		// attributes
		public const string ATTR_ID = "Id";
		public const string ATTR_ALGORITHM = "Algorithm";
		public const string ATTR_URI = "URI";
		public const string ATTR_TYPE = "Type";
		public const string ATTR_MIMETYPE = "MimeType";
		public const string ATTR_ENCODING = "Encoding";
		public const string ATTR_TARGET = "Target";
		public const string ATTR_OBJECTREFERENCE = "ObjectReference";
		public const string ATTR_QUALIFIER = "Qualifier";
		public const string ATTR_REFERENCEDDATA = "referencedData";


		// defaults
		public static readonly SignatureType DEFAULT_SIGNATURE_TYPE = SignatureType.XAdES_BES;

		// algorithm defaults
		public static readonly C14nMethod DEFAULT_REFERENCE_C14N = C14nMethod.INCLUSIVE;
		public static readonly SignatureMethod DEFAULT_SIGNATURE_METHOD = SignatureMethod.RSA_SHA256;
		public static readonly PKIEncodingType DEFAULT_PKI_ENCODING = PKIEncodingType.DER;


		public const string REFERENCE_TYPE_SIGNED_PROPS = "http://uri.etsi.org/01903#SignedProperties";
		public const string REFERENCE_TYPE_COUNTER_SIGNATURE = "http://uri.etsi.org/01903#CountersignedSignature";

	}

}