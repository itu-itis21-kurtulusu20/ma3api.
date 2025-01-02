/**
 * @author yavuz.kahveci
 */
namespace tr.gov.tubitak.uekae.esya.api.asic.core
{
    public static class ASiCConstants
    {
        public const string NS_XMLDSIG = "http://www.w3.org/2000/09/xmldsig#";
        public const string NS_ASiC = "http://uri.etsi.org/02918/v1.2.1#";

        public const string TAG_SIGNATURE = "Signature";
        public const string TAG_ASICMANIFEST = "ASiCManifest";
        public const string TAG_SIGREFERENCE = "SigReference";
        public const string TAG_DATAOBJECTREFERENCE = "DataObjectReference";
        public const string TAG_DIGESTMETHOD = "DigestMethod";
        public const string TAG_DIGESTVALUE = "DigestValue";

        public const string TAG_DATAREFERENCE_EXTENSIONS = "DataObjectReferenceExtensions";
        public const string TAG_ASICMANIFEST_EXTENSIONS = "ASiCManifestExtensions";
        public const string TAG_EXTENSION = "Extension";

        public const string TAG_SIGNATURES = "XAdESSignatures";

        public const string ATTR_URI = "URI";
        public const string ATTR_MIME = "MimeType";
        public const string ATTR_ROOTFILE = "Rootfile";
        public const string ATTR_CRITICAL = "Critical";
        public const string ATTR_ALGO = "Algorithm";
    }
}
