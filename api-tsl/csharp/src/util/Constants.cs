using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.tsl.util
{
    public class Constants
    {
        public const string TSL_NAME = "Name";
        public const string TSL_URI = "URI";
        public const string TSL_LANG_ATTR = "lang";

        public const string TSL_PREFIX = "tsl:";
        public const string TSLX_PREFIX = "tslx:";
        public const string XML_PREFIX = "xml:";
        public const string XMLNS_PREFIX = "xmlns:";
        public const string DS_PREFIX = "ds:";

        //tags
        public const string TAG_SCHEMEOPERATORNAME = "SchemeOperatorName";
        public const string TAG_SCHEMEINFORMATIONURI = "SchemeInformationURI";
        public const string TAG_SCHEMENAME = "SchemeName";
        public const string TAG_ELECTRONICADDRESS = "ElectronicAddress";
        public const string TAG_POSTALADDRESS = "PostalAddress";
        public const string TAG_STREETADDRESS = "StreetAddress";
        public const string TAG_LOCALITY = "Locality";
        public const string TAG_POSTALCODE = "PostalCode";
        public const string TAG_COUNTRYNAME = "CountryName";
        public const string TAG_POSTALADDRESSES = "PostalAddresses";
        public const string TAG_SCHEMEOPERATORADDRESS = "SchemeOperatorAddress";
        public const string TAG_SCHEMETYPECOMMUNITYRULES = "SchemeTypeCommunityRules";
        public const string TAG_SCHEMEINFORMATION = "SchemeInformation";
        public const string TAG_TSLVERSIONIDENTIFIER = "TSLVersionIdentifier";
        public const string TAG_TSLSEQUENCENUMBER = "TSLSequenceNumber";
        public const string TAG_TSLSTATUSDETERMINATIONAPPROACH = "StatusDeterminationApproach";
        public const string TAG_HISTORICALINFORMATIONPERIOD = "HistoricalInformationPeriod";
        public const string TAG_TSLTYPE = "TSLType";
        public const string TAG_SCHEMETERRITORY = "SchemeTerritory";
        public const string TAG_TSLLEGALNOTICE = "TSLLegalNotice";
        public const string TAG_POLICYORLEGALNOTICE = "PolicyOrLegalNotice";
        public const string TAG_SERVICEDIGITALIDENTITIES = "ServiceDigitalIdentities";
        public const string TAG_SERVICEDIGITALIDENTITY = "ServiceDigitalIdentity";
        public const string TAG_DIGITALID = "DigitalId";
        public const string TAG_X509CERTIFICATE = "X509Certificate";
        public const string TAG_TSLLOCATION = "TSLLocation";
        public const string TAG_MIMETYPE = "MimeType";
        public const string TAG_OTHERINFORMATION = "OtherInformation";
        public const string TAG_TSLADDITIONALINFORMATION = "AdditionalInformation";
        public const string TAG_OTHERTSLPOINTER = "OtherTSLPointer";
        public const string TAG_POINTERSTOOTHERTSL = "PointersToOtherTSL";
        public const string TAG_LISTISSUEDATETIME = "ListIssueDateTime";
        public const string TAG_NEXTUPDATE = "NextUpdate";
        public const string TAG_DATETIME = "dateTime";
        public const string TAG_DISTRIBUTIONPOINTS = "DistributionPoints";

        public const string TAG_TRUSTSERVICEPROVIDERLIST = "TrustServiceProviderList";
        public const string TAG_TRUSTSERVICEPROVIDER = "TrustServiceProvider";
        public const string TAG_TSPINFORMATION = "TSPInformation";
        public const string TAG_TSPNAME = "TSPName";
        public const string TAG_TSPTRADENAME = "TSPTradeName";
        public const string TAG_TSPADDRESS = "TSPAddress";
        public const string TAG_TSPINFORMATIONURI = "TSPInformationURI";

        public const string TAG_TSPSERVICES = "TSPServices";
        public const string TAG_TSPSERVICE = "TSPService";
        public const string TAG_SERVICEINFORMATION = "ServiceInformation";
        public const string TAG_SERVICETYPEIDENTIFIER = "ServiceTypeIdentifier";
        public const string TAG_SERVICENAME = "ServiceName";
        public const string TAG_SERVICESTATUS = "ServiceStatus";
        public const string TAG_STATUSSTARTINGTIME = "StatusStartingTime";
        public const string TAG_SCHEMESERVICEDEFINITIONURI = "SchemeServiceDefinitionURI";
        public const string TAG_TSPSERVICEDEFINITONURI = "TSPServiceDefinitionURI";
        public const string TAG_SERVICESUPPLYPOINTS = "ServiceSupplyPoints";
        public const string TAG_SERVICESUPPLYPOINT = "ServiceSupplyPoint";

        public const string TAG_TRUSTSERVICESTATUSLIST = "TrustServiceStatusList";

        public const string TAG_SIGNATURE = "Signature";

        //namespaces
        public const string NS_TSL = "http://uri.etsi.org/02231/v2#";   //xmlns:tsl
        public const string NS_XMLDSIG = "http://www.w3.org/2000/09/xmldsig#";  //xmlns:ds
        public const string NS_ECC = "http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#";  //xmlns:ecc
        public const string NS_TSLX = "http://uri.etsi.org/02231/v2/additionaltypes#";  //xmlns:tslx
        public const string NS_XADES = "http://uri.etsi.org/01903/v1.3.2#";  //xmlns:tslx
        public const string TSLTAG = "http://uri.etsi.org/02231/TSLTag";
        
        public const string NS_NAMESPACESPEC = "http://www.w3.org/2000/xmlns/";
        public const string NS_XMLLANGSPACESPEC = "http://www.w3.org/XML/1998/namespace";

        // attributes
        public const string ATTR_TSL = "xmlns:tsl";
        public const string ATTR_XMLDSIG = "xmlns:ds";
        public const string ATTR_ECC = "xmlns:ecc";
        public const string ATTR_TSLX = "xmlns:tslx";
        public const string ATTR_XADES = "xmlns:xades";
        public const string ATTR_ID = "Id";
        public const string ATTR_TSLTAG = "TSLTag";


        //Tree Node Names
        public const string TREE_NODE_TSL = "TSL";
        public const string TREE_NODE_TSP = "Trust Service Provider";
        public const string TREE_NODE_SERVICE = "Trust Service";
    }
}
