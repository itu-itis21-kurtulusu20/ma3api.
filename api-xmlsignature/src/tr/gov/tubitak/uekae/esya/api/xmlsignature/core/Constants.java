package tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;

/**
 * @author ahmety
 * date: May 29, 2009
 */
public class Constants
{
    // namespaces
    public static final String NS_MA3               = "http://uekae.tubitak.gov.tr/xml/signature#";
    public static final String NS_XMLDSIG           = "http://www.w3.org/2000/09/xmldsig#";
    public static final String NS_XMLDSIG_MORE      = "http://www.w3.org/2001/04/xmldsig-more#";
    public static final String NS_XMLDSIG_MORE_2007 = "http://www.w3.org/2007/05/xmldsig-more#";
    public static final String NS_XMLDSIG_11        = "http://www.w3.org/2009/xmldsig11#";

    public static final String NS_XADES_1_1_1       = "http://uri.etsi.org/01903/v1.1.1#";
    public static final String NS_XADES_1_2_2       = "http://uri.etsi.org/01903/v1.2.2#";
    public static final String NS_XADES_1_3_2       = "http://uri.etsi.org/01903/v1.3.2#";
    public static final String NS_XADES_1_4_1       = "http://uri.etsi.org/01903/v1.4.1#";

    public static final String NS_NAMESPACESPEC     = "http://www.w3.org/2000/xmlns/";
    public static final String NS_XMLLANGSPACESPEC  = "http://www.w3.org/XML/1998/namespace";

    public static final String NS_MA3_PREFIX        = "ma3";

    // tags
    public static final String TAG_SIGNATURE            = "Signature";

    public static final String TAG_SIGNEDINFO           = "SignedInfo";
    public static final String TAG_C14NMETHOD           = "CanonicalizationMethod";
    public static final String TAG_SIGNATUREMETHOD      = "SignatureMethod";

    public static final String TAG_SIGNATUREVALUE       = "SignatureValue";
    public static final String TAG_OBJECT               = "Object";

    public static final String TAG_MANIFEST             = "Manifest";
    public static final String TAG_REFERENCE            = "Reference";
    public static final String TAG_DIGESTMETHOD         = "DigestMethod";
    public static final String TAG_DIGESTVALUE          = "DigestValue";

    public static final String TAG_TRANSFORMS           = "Transforms";
    public static final String TAG_TRANSFORM            = "Transform";
    public static final String TAG_XPATH                = "XPath";

    public static final String TAG_KEYINFO              = "KeyInfo";
    public static final String TAG_KEYNAME              = "KeyName";
    public static final String TAG_KEYVALUE             = "KeyValue";
    public static final String TAG_X509DATA             = "X509Data";

    public static final String TAG_ECKEYVALUE           = "ECKeyValue";
    public static final String TAG_NAMEDCURVE           = "NamedCurve";
    public static final String TAG_PUBLICKEY            = "PublicKey";

    public static final String TAG_RSAKEYVALUE          = "RSAKeyValue";
    public static final String TAG_MODULUS              = "Modulus";
    public static final String TAG_EXPONENT             = "Exponent";

    public static final String TAG_DSAKEYVALUE          = "DSAKeyValue";
    public static final String TAG_G                    = "G";
    public static final String TAG_P                    = "P";
    public static final String TAG_Q                    = "Q";
    public static final String TAG_Y                    = "Y";

    public static final String TAG_RETRIEVALMETHOD      = "RetrievalMethod";
    public static final String TAG_PGPDATA              = "PGPData";
    public static final String TAG_SPKIDATA             = "SPKIData";
    public static final String TAG_MGMTDATA             = "MgmtData";

    public static final String TAG_X509ISSUERSERIAL     = "X509IssuerSerial";
    public static final String TAG_X509SKI              = "X509SKI";
    public static final String TAG_X509SUBJECTNAME      = "X509SubjectName";
    public static final String TAG_X509CERTIFICATE      = "X509Certificate";
    public static final String TAG_X509CRL              = "X509CRL";
    public static final String TAG_X509ISSUERNAME       = "X509IssuerName";
    public static final String TAG_X509SERIALNUMBER     = "X509SerialNumber";

    // etsi XAdES tags
    public static final String TAGX_QUALIFYINGPROPERTIES        = "QualifyingProperties";
    public static final String TAGX_SIGNEDPROPERTIES            = "SignedProperties";
    public static final String TAGX_SIGNEDSIGNATUREPROPERTIES   = "SignedSignatureProperties";
    public static final String TAGX_SIGNINGTIME                 = "SigningTime";

    // signature production place
    public static final String TAGX_SIGNATUREPRODUCTIONPLACE    = "SignatureProductionPlace";
    public static final String TAGX_CITY                        = "City";
    public static final String TAGX_STATEORPROVINCE             = "StateOrProvince";
    public static final String TAGX_POSTALCODE                  = "PostalCode";
    public static final String TAGX_COUNTRYNAME                 = "CountryName";

    // signer role
    public static final String TAGX_SIGNERROLE                  = "SignerRole";
    public static final String TAGX_CLAIMEDROLES                = "ClaimedRoles";
    public static final String TAGX_CLAIMEDROLE                 = "ClaimedRole";
    public static final String TAGX_CERTIFIEDROLES              = "CertifiedRoles";
    public static final String TAGX_CERTIFIEDROLE               = "CertifiedRole";

    // signing certificate
    public static final String TAGX_SIGNINGCERTIFICATE          = "SigningCertificate";
    public static final String TAGX_CERTID                      = "Cert";
    public static final String TAGX_CERTDIGEST                  = "CertDigest";
    public static final String TAGX_ISSUERSERIAL                = "IssuerSerial";

    // signed data object
    public static final String TAGX_SIGNEDATAOBJECTPROPERIES    = "SignedDataObjectProperties";
    public static final String TAGX_DATAOBJECTFORMAT            = "DataObjectFormat";
    public static final String TAGX_OBJECTIDENTIFIER            = "ObjectIdentifier";
    public static final String TAGX_IDENTIFIER                  = "Identifier";
    public static final String TAGX_DOCUMENTATIONREFERENCES     = "DocumentationReferences";
    public static final String TAGX_DOCUMENTATIONREFERENCE      = "DocumentationReference";
    public static final String TAGX_DESCRIPTION                 = "Description";
    public static final String TAGX_MIMETYPE                    = "MimeType";
    public static final String TAGX_ENCODING                    = "Encoding";

    // commitment type
    public static final String TAGX_COMMITMENTTYPEINDICATION    = "CommitmentTypeIndication";
    public static final String TAGX_COMMITMENTTYPEID            = "CommitmentTypeId";
    public static final String TAGX_OBJECTREFERENCE             = "ObjectReference";
    public static final String TAGX_COMMITMENTTYPEQUALIFIERS    = "CommitmentTypeQualifiers";
    public static final String TAGX_COMMITMENTTYPEQUALIFIER     = "CommitmentTypeQualifier";
    public static final String TAGX_ALLSIGNEDDATAOBJECTS        = "AllSignedDataObjects";

    // signature policy
    public static final String TAGX_SIGNATUREPOLICYIDENTIFIER   = "SignaturePolicyIdentifier";
    public static final String TAGX_SIGNATUREPOLICYID           = "SignaturePolicyId";
    public static final String TAGX_SIGNATUREPOLICYIMPLIED      = "SignaturePolicyImplied";
    public static final String TAGX_SIGPOLICYID                 = "SigPolicyId";
    public static final String TAGX_SIGPOLICYHASH               = "SigPolicyHash";
    public static final String TAGX_SIGPOLICYQUALIFIERS         = "SigPolicyQualifiers";
    public static final String TAGX_SIGPOLICYQUALIFIER          = "SigPolicyQualifier";
    public static final String TAGX_SPURI                       = "SPURI";
    public static final String TAGX_SPUSERNOTICE                = "SPUserNotice";
    public static final String TAGX_NOTICEREF                   = "NoticeRef";
    public static final String TAGX_EXPLICITTEXT                = "ExplicitText";
    public static final String TAGX_ORGANIZATION                = "Organization";
    public static final String TAGX_NOTICENUMBERS               = "NoticeNumbers";
    public static final String TAGX_INT                         = "int";

    // unsigned props
    public static final String TAGX_UNSIGNEDPROPERTIES              = "UnsignedProperties";
    public static final String TAGX_UNSIGNEDDATAOBJECTPROPERTIES    = "UnsignedDataObjectProperties";
    public static final String TAGX_UNSIGNEDDATAOBJECTPROPERTY      = "UnsignedDataObjectProperty";
    public static final String TAGX_UNSIGNEDSIGNATUREPROPERTIES     = "UnsignedSignatureProperties";
    public static final String TAGX_COUNTERSIGNATURE                = "CounterSignature";

    // vrefs (validation references)
    public static final String TAGX_COMPLETECERTREFS                = "CompleteCertificateRefs";
    public static final String TAGX_CERTREFS                        = "CertRefs";
    public static final String TAGX_COMPLETEREVOCATIONREFS          = "CompleteRevocationRefs";
    public static final String TAGX_CRLREFS                         = "CRLRefs";
    public static final String TAGX_OCSPREFS                        = "OCSPRefs";
    public static final String TAGX_OTHERREFS                       = "OtherRefs";    
    public static final String TAGX_CRLREF                          = "CRLRef";
    public static final String TAGX_OCSPREF                         = "OCSPRef";
    public static final String TAGX_OTHERREF                        = "OtherRef";
    public static final String TAGX_CRLIDENTIFIER                   = "CRLIdentifier";
    public static final String TAGX_ISSUER                          = "Issuer";
    public static final String TAGX_ISSUETIME                       = "IssueTime";
    public static final String TAGX_NUMBER                          = "Number";
    public static final String TAGX_RESPONDERID                     = "ResponderID";
    public static final String TAGX_PRODUCEDAT                      = "ProducedAt";
    public static final String TAGX_BYNAME                          = "ByName";
    public static final String TAGX_BYKEY                           = "ByKey";
    public static final String TAGX_OCSPIDENTIFIER                  = "OCSPIdentifier";
    public static final String TAGX_DIGESTALGANDVALUE               = "DigestAlgAndValue";

    public static final String TAGX_ATTRIBUTECERTIFICATEREFS        = "AttributeCertificateRefs";
    public static final String TAGX_ATTRIBUTEREVOCATIONREFS         = "AttributeRevocationRefs";

    public static final String TAGX_SIGANDREFSTIMESTAMP             = "SigAndRefsTimeStamp";
    public static final String TAGX_REFSONLYTIMESTAMP               = "RefsOnlyTimeStamp";

    public static final String TAGX_ARCHIVETIMESTAMP                = "ArchiveTimeStamp";

    public static final String TAGX_TIMESTAMPVALIDATIONDATA         = "TimeStampValidationData";
    
    public static final String TAGX_CERTIFICATEVALUES               = "CertificateValues";
    public static final String TAGX_ENCAPSULATEDX509CERTIFICATE     = "EncapsulatedX509Certificate";

    public static final String TAGX_REVOCATIONVALUES                = "RevocationValues";
    public static final String TAGX_CRLVALUES                       = "CRLValues";
    public static final String TAGX_OCSPVALUES                      = "OCSPValues";
    public static final String TAGX_ENCAPSULATEDCRLVALUE            = "EncapsulatedCRLValue";
    public static final String TAGX_ENCAPSULATEDOCSPVALUE           = "EncapsulatedOCSPValue";

    public static final String TAGX_ATTRAUHORITIESCERTVALUES        = "AttrAuthoritiesCertValues";
    public static final String TAGX_ATTRIBUTEREVOCATIONVALUES       = "AttributeRevocationValues";

    // timestamp
    public static final String TAGX_INCLUDE                         = "Include";
    public static final String TAGX_REFERENCEINFO                   = "ReferenceInfo";
    public static final String TAGX_ALLDATAOBJECTSTIMESTAMP         = "AllDataObjectsTimeStamp";
    public static final String TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP  = "IndividualDataObjectsTimeStamp";

    public static final String TAGX_SIGNATURETIMESTAMP              = "SignatureTimeStamp";

    public static final String TAGX_ENCAPSULATEDTIMESTAMP           = "EncapsulatedTimeStamp";
    public static final String TAGX_XMLTIMESTAMP                    = "XMLTimeStamp";

    // attributes
    public static final String ATTR_ID                  = "Id";
    public static final String ATTR_ALGORITHM           = "Algorithm";
    public static final String ATTR_URI                 = "URI";
    public static final String ATTR_TYPE                = "Type";
    public static final String ATTR_MIMETYPE            = "MimeType";
    public static final String ATTR_ENCODING            = "Encoding";
    public static final String ATTR_TARGET              = "Target";
    public static final String ATTR_OBJECTREFERENCE     = "ObjectReference";
    public static final String ATTR_QUALIFIER           = "Qualifier";
    public static final String ATTR_REFERENCEDDATA      = "referencedData";


    // defaults
    public static final SignatureType     DEFAULT_SIGNATURE_TYPE      = SignatureType.XAdES_BES;

    // algorithm defaults
    public static final C14nMethod        DEFAULT_REFERENCE_C14N      = C14nMethod.INCLUSIVE;
    public static final SignatureMethod   DEFAULT_SIGNATURE_METHOD    = SignatureMethod.RSA_SHA256;
    public static final PKIEncodingType   DEFAULT_PKI_ENCODING        = PKIEncodingType.DER;


    public static final String      REFERENCE_TYPE_SIGNED_PROPS  ="http://uri.etsi.org/01903#SignedProperties";
    public static final String      REFERENCE_TYPE_COUNTER_SIGNATURE = "http://uri.etsi.org/01903#CountersignedSignature";

}
