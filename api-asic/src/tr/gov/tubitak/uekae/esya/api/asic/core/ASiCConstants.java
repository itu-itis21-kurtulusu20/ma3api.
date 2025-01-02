package tr.gov.tubitak.uekae.esya.api.asic.core;

/**
 * @author ayetgin
 */
public class ASiCConstants
{
    public static final String NS_XMLDSIG   = "http://www.w3.org/2000/09/xmldsig#";
    public static final String NS_ASiC      = "http://uri.etsi.org/02918/v1.2.1#";

    public static final String TAG_SIGNATURE    = "Signature";
    public static final String TAG_ASICMANIFEST = "ASiCManifest";
    public static final String TAG_SIGREFERENCE = "SigReference";
    public static final String TAG_DATAOBJECTREFERENCE = "DataObjectReference";
    public static final String TAG_DIGESTMETHOD = "DigestMethod";
    public static final String TAG_DIGESTVALUE  = "DigestValue";

    public static final String TAG_DATAREFERENCE_EXTENSIONS = "DataObjectReferenceExtensions";
    public static final String TAG_ASICMANIFEST_EXTENSIONS = "ASiCManifestExtensions";
    public static final String TAG_EXTENSION    = "Extension";

    public static final String TAG_SIGNATURES   = "XAdESSignatures";

    public static final String ATTR_URI      = "URI";
    public static final String ATTR_MIME     = "MimeType";
    public static final String ATTR_ROOTFILE = "Rootfile";
    public static final String ATTR_CRITICAL = "Critical";
    public static final String ATTR_ALGO     = "Algorithm";


}
