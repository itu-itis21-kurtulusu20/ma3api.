package tr.gov.tubitak.uekae.esya.api.signature.config;

/**
 * @author ayetgin
 */
public class ConfigConstants
{
    public static final String CONFIG_FILE_NAME = "esya-signature-config.xml";

    public static final String NS_MA3               = "http://uekae.tubitak.gov.tr/xml/signature#";

    // root tags
    public static final String TAG_HTTP                 = "http";
    //public static final String TAG_VALIDATION           = "validation";
    public static final String TAG_PARAMS               = "params";
    public static final String TAG_TIMESTAMPSERVER      = "timestamp-server";
    public static final String TAG_ALGORITHMS           = "algorithms";
    public static final String TAG_CERTVALIDATION       = "certificate-validation";


    public static final String ATTR_LANGUAGE            = "language";
    public static final String ATTR_COUNTRY             = "country";
    public static final String ATTR_FOR                 = "for";

    // timestamp
    public static final String TAG_HOST         = "host";
    public static final String TAG_USERID       = "userid";
    public static final String TAG_PASSWORD     = "password";
    public static final String TAG_DIGESTALG    = "digest-alg";

    // http
    public static final String TAG_PROXY_HOST           = "proxy-host";
    public static final String TAG_PROXY_PORT           = "proxy-port";
    public static final String TAG_PROXY_USERNAME       = "proxy-username";
    public static final String TAG_PROXY_PASSWORD       = "proxy-password";
    public static final String TAG_BASICAUTH_USERNAME   = "basic-authentication-username";
    public static final String TAG_BASICAUTH_PASSWORD   = "basic-authentication-password";
    public static final String TAG_CONNECTION_TIMEOUT   = "connection-timeout-in-milliseconds";

    // resolver
    public static final String TAG_RESOLVERS    = "resolvers";
    public static final String TAG_RESOLVER     = "resolver";

    // algorithm
    public static final String TAG_SIGNATUREALG = "signature-alg";
    public static final String TAG_OCSP_DIGEST_ALG = "ocsp-digest-algorithm";

    // cert validation
    public static final String TAG_CERTIFICATE_VALIDATION_POLICY    = "certificate-validation-policy-file";

    public static final String TAG_GRACE_PERIOD_IN_SECONDS          = "grace-period-in-seconds";
    public static final String TAG_LAST_REVOCATION_PERIOD_IN_SECONDS= "last-revocation-period-in-seconds";

    public static final String TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION = "use-validation-data-published-after-creation";

    public static final String TAG_VALIDATE_CERT_BEFORE_SIGNING = "validate-certificate-before-signing";

    // params
    public static final String TAG_FORCE_STRICT_REFERENCES      = "force-strict-reference-use";

    public static final String TAG_CHECK_POLICY_URI             = "check-policy-uri";

    public static final String TAG_VALIDATE_TS_WHILE_SIGNING    = "validate-timestamp-while-signing";

    public static final String TAG_TRUST_SIGNING_TIME           = "trust-signing-time";

    public static final String TAG_WRITE_VALIDATIONDATA_ON_UPGRADE = "write-referencedvalidationdata-to-file-on-upgrade";
    
    public static final String TAG_USE_CAdES_ATSv2              = "use-CAdES-Archive-TimeStamp-V2";

    public static final String TAG_TOLERATE_SIGNING_TIME_IN_SECONDS = "tolerate-signing-time-in-seconds";

    public static final String TAG_VALIDATION_PROFILE = "validation-profile";
}
