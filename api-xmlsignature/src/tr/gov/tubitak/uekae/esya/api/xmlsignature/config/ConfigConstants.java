package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;


/**
 * @author ahmety
 * date: Dec 4, 2009
 */
class ConfigConstants
{
    public static final String CONFIG_FILE_NAME = "xmlsignature-config.xml";


    public static final String TAG_HTTP                 = "http";
    public static final String TAG_VALIDATION           = "validation";
    public static final String TAG_TIMESTAMPSERVER      = "timestamp-server";
    public static final String TAG_ALGORITHMS           = "algorithms";
    public static final String TAG_PARAMETERS           = "parameters";

    // http
    public static final String TAG_PROXY_HOST           = "proxy-host";
    public static final String TAG_PROXY_PORT           = "proxy-port";
    public static final String TAG_PROXY_USERNAME       = "proxy-username";
    public static final String TAG_PROXY_PASSWORD       = "proxy-password";
    public static final String TAG_BASICAUTH_USERNAME   = "basic-authentication-username";
    public static final String TAG_BASICAUTH_PASSWORD   = "basic-authentication-password";
    public static final String TAG_CONNECTION_TIMEOUT   = "connection-timeout-in-milliseconds";

    // validation
    public static final String TAG_CERTIFICATE_VALIDATION_POLICY = "certificate-validation-policy-file";

    public static final String TAG_VALIDATORS   = "validators";
    public static final String TAG_PROFILE      = "profile";
    public static final String TAG_VALIDATOR    = "validator";

    public static final String TAG_GRACE_PERIOD_IN_SECONDS              = "grace-period-in-seconds";
    public static final String TAG_LAST_REVOCATION_PERIOD_IN_SECONDS    = "last-revocation-period-in-seconds";
    public static final String TAG_TOLERATE_SIGNING_TIME_IN_SECONDS = "tolerate-signing-time-in-seconds";
    public static final String TAG_VALIDATION_PROFILE = "validation-profile";

    public static final String TAG_FORCE_STRICT_REFERENCES                      = "force-strict-reference-use";
    public static final String TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION = "use-validation-data-published-after-creation";
    public static final String TAG_TRUST_SIGNING_TIME                           = "trust-signing-time";

    public static final String TAG_CHECK_POLICY_URI = "check-policy-uri";

    // resolver
    public static final String TAG_RESOLVERS    = "resolvers";
    public static final String TAG_RESOLVER     = "resolver";


    public static final String ATTR_TYPE                = "type";
    public static final String ATTR_CLASS               = "class";
    public static final String ATTR_LANGUAGE            = "language";
    public static final String ATTR_COUNTRY             = "country";
    public static final String ATTR_INHERIT_VALIDATORS  = "inherit-validators-from";

    // timestamp
    public static final String TAG_HOST         = "host";
    public static final String TAG_USERID       = "userid";
    public static final String TAG_PASSWORD     = "password";
    public static final String TAG_DIGESTALG    = "digest-alg";
    public static final String TAG_C14N         = "c14n";

    // algoritm
    public static final String TAG_DIGESTMETHOD = "digest-method";

    // paramaters
    public static final String TAG_WRITE_REFERENCED_PKI_DATA = "write-referencedvalidationdata-to-file-on-upgrade";
    public static final String TAG_ADD_TIMESTAMP_PKI_DATA = "add-timestamp-validation-data";

}
