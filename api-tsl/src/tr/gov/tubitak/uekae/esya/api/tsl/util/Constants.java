package tr.gov.tubitak.uekae.esya.api.tsl.util;

public class Constants {
	public static final String TSL_NAME = "Name";
	public static final String TSL_URI = "URI";
	public static final String TSL_LANG_ATTR = "lang";

	public static final String TSL_PREFIX = "tsl:";
	public static final String TSLX_PREFIX = "tslx:";
	public static final String XML_PREFIX = "xml:";
	public static final String XMLNS_PREFIX = "xmlns:";
	public static final String DS_PREFIX = "ds:";

	// tags
	public static final String TAG_SCHEMEOPERATORNAME = "SchemeOperatorName";
	public static final String TAG_SCHEMEINFORMATIONURI = "SchemeInformationURI";
	public static final String TAG_SCHEMENAME = "SchemeName";
	public static final String TAG_ELECTRONICADDRESS = "ElectronicAddress";
	public static final String TAG_POSTALADDRESS = "PostalAddress";
	public static final String TAG_STREETADDRESS = "StreetAddress";
	public static final String TAG_LOCALITY = "Locality";
	public static final String TAG_POSTALCODE = "PostalCode";
	public static final String TAG_COUNTRYNAME = "CountryName";
	public static final String TAG_POSTALADDRESSES = "PostalAddresses";
	public static final String TAG_SCHEMEOPERATORADDRESS = "SchemeOperatorAddress";
	public static final String TAG_SCHEMETYPECOMMUNITYRULES = "SchemeTypeCommunityRules";
	public static final String TAG_SCHEMEINFORMATION = "SchemeInformation";
	public static final String TAG_TSLVERSIONIDENTIFIER = "TSLVersionIdentifier";
	public static final String TAG_TSLSEQUENCENUMBER = "TSLSequenceNumber";
	public static final String TAG_TSLSTATUSDETERMINATIONAPPROACH = "StatusDeterminationApproach";
	public static final String TAG_HISTORICALINFORMATIONPERIOD = "HistoricalInformationPeriod";
	public static final String TAG_TSLTYPE = "TSLType";
	public static final String TAG_SCHEMETERRITORY = "SchemeTerritory";
	public static final String TAG_TSLLEGALNOTICE = "TSLLegalNotice";
	public static final String TAG_POLICYORLEGALNOTICE = "PolicyOrLegalNotice";
	public static final String TAG_SERVICEDIGITALIDENTITIES = "ServiceDigitalIdentities";
	public static final String TAG_SERVICEDIGITALIDENTITY = "ServiceDigitalIdentity";
	public static final String TAG_DIGITALID = "DigitalId";
	public static final String TAG_X509CERTIFICATE = "X509Certificate";
	public static final String TAG_TSLLOCATION = "TSLLocation";
	public static final String TAG_MIMETYPE = "MimeType";
	public static final String TAG_OTHERINFORMATION = "OtherInformation";
	public static final String TAG_TSLADDITIONALINFORMATION = "AdditionalInformation";
	public static final String TAG_OTHERTSLPOINTER = "OtherTSLPointer";
	public static final String TAG_POINTERSTOOTHERTSL = "PointersToOtherTSL";
	public static final String TAG_LISTISSUEDATETIME = "ListIssueDateTime";
	public static final String TAG_NEXTUPDATE = "NextUpdate";
	public static final String TAG_DATETIME = "dateTime";
	public static final String TAG_DISTRIBUTIONPOINTS = "DistributionPoints";

	public static final String TAG_TRUSTSERVICEPROVIDERLIST = "TrustServiceProviderList";
	public static final String TAG_TRUSTSERVICEPROVIDER = "TrustServiceProvider";
	public static final String TAG_TSPINFORMATION = "TSPInformation";
	public static final String TAG_TSPNAME = "TSPName";
	public static final String TAG_TSPTRADENAME = "TSPTradeName";
	public static final String TAG_TSPADDRESS = "TSPAddress";
	public static final String TAG_TSPINFORMATIONURI = "TSPInformationURI";

	public static final String TAG_TSPSERVICES = "TSPServices";
	public static final String TAG_TSPSERVICE = "TSPService";
	public static final String TAG_SERVICEINFORMATION = "ServiceInformation";
	public static final String TAG_SERVICETYPEIDENTIFIER = "ServiceTypeIdentifier";
	public static final String TAG_SERVICENAME = "ServiceName";
	public static final String TAG_SERVICESTATUS = "ServiceStatus";
	public static final String TAG_STATUSSTARTINGTIME = "StatusStartingTime";
	public static final String TAG_SCHEMESERVICEDEFINITIONURI = "SchemeServiceDefinitionURI";
	public static final String TAG_TSPSERVICEDEFINITONURI = "TSPServiceDefinitionURI";
	public static final String TAG_SERVICESUPPLYPOINTS = "ServiceSupplyPoints";
	public static final String TAG_SERVICESUPPLYPOINT = "ServiceSupplyPoint";

	public static final String TAG_TRUSTSERVICESTATUSLIST = "TrustServiceStatusList";

	public static final String TAG_SIGNATURE = "Signature";

	// namespaces
	public static final String NS_TSL = "http://uri.etsi.org/02231/v2#"; // xmlns:tsl
	public static final String NS_XMLDSIG = "http://www.w3.org/2000/09/xmldsig#"; // xmlns:ds
	public static final String NS_ECC = "http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#"; // xmlns:ecc
	public static final String NS_TSLX = "http://uri.etsi.org/02231/v2/additionaltypes#"; // xmlns:tslx
	public static final String NS_XADES = "http://uri.etsi.org/01903/v1.3.2#"; // xmlns:tslx
	public static final String TSLTAG = "http://uri.etsi.org/02231/TSLTag";

	public static final String NS_NAMESPACESPEC = "http://www.w3.org/2000/xmlns/";
	public static final String NS_XMLLANGSPACESPEC = "http://www.w3.org/XML/1998/namespace";

	// attributes
	public static final String ATTR_TSL = "xmlns:tsl";
	public static final String ATTR_XMLDSIG = "xmlns:ds";
	public static final String ATTR_ECC = "xmlns:ecc";
	public static final String ATTR_TSLX = "xmlns:tslx";
	public static final String ATTR_XADES = "xmlns:xades";
	public static final String ATTR_ID = "Id";
	public static final String ATTR_TSLTAG = "TSLTag";

	// Tree Node Names
	public static final String TREE_NODE_TSL = "TSL";
	public static final String TREE_NODE_TSP = "Trust Service Provider";
	public static final String TREE_NODE_SERVICE = "Trust Service";
}
