namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{


	/// <summary>
	/// @author ahmety
	/// date: Dec 4, 2009
	/// </summary>
	internal static class ConfigConstants
	{
		public const string CONFIG_FILE_NAME = "xmlsignature-config.xml";

		public const string TAG_HTTP = "http";
		public const string TAG_VALIDATION = "validation";
		public const string TAG_TIMESTAMPSERVER = "timestamp-server";
		public const string TAG_ALGORITHMS = "algorithms";
		public const string TAG_PARAMETERS = "parameters";

		// http
		public const string TAG_PROXY_HOST = "proxy-host";
		public const string TAG_PROXY_PORT = "proxy-port";
		public const string TAG_PROXY_USERNAME = "proxy-username";
		public const string TAG_PROXY_PASSWORD = "proxy-password";
		public const string TAG_BASICAUTH_USERNAME = "basic-authentication-username";
		public const string TAG_BASICAUTH_PASSWORD = "basic-authentication-password";
		public const string TAG_CONNECTION_TIMEOUT = "connection-timeout-in-milliseconds";

		// validation
		public const string TAG_CERTIFICATE_VALIDATION_POLICY = "certificate-validation-policy-file";

		public const string TAG_VALIDATORS = "validators";
		public const string TAG_PROFILE = "profile";
		public const string TAG_VALIDATOR = "validator";

		public const string TAG_GRACE_PERIOD_IN_SECONDS = "grace-period-in-seconds";
		public const string TAG_LAST_REVOCATION_PERIOD_IN_SECONDS = "last-revocation-period-in-seconds";

		public const string TAG_FORCE_STRICT_REFERENCES = "force-strict-reference-use";
		public const string TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION = "use-validation-data-published-after-creation";
	    public const string TAG_TRUST_SIGNING_TIME = "trust-signing-time";

		public const string TAG_CHECK_POLICY_URI = "check-policy-uri";

		// resolver
		public const string TAG_RESOLVERS = "resolvers";
		public const string TAG_RESOLVER = "resolver";


		public const string ATTR_TYPE = "type";
		public const string ATTR_CLASS = "class";
		public const string ATTR_LANGUAGE = "language";
		public const string ATTR_COUNTRY = "country";
		public const string ATTR_INHERIT_VALIDATORS = "inherit-validators-from";

		// timestamp
		public const string TAG_HOST = "host";
		public const string TAG_USERID = "userid";
		public const string TAG_PASSWORD = "password";
		public const string TAG_DIGESTALG = "digest-alg";
        public const string TAG_C14N = "c14n";

		// algoritm
		public const string TAG_DIGESTMETHOD = "digest-method";

		// paramaters
		public const string TAG_WRITE_REFERENCED_PKI_DATA = "write-referencedvalidationdata-to-file-on-upgrade";
		public const string TAG_ADD_TIMESTAMP_PKI_DATA = "add-timestamp-validation-data";

	    public const string TAG_TOLERATE_SIGNING_TIME_IN_SECONDS  = "tolerate-signing-time-in-seconds";	  
	}
}