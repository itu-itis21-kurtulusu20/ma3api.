using System;
using System.Xml;
namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{
	using Element = XmlElement;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	
    /// <summary>
	/// @author ahmety
	/// @deprecated
	/// @see tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig
	/// date: Dec 4, 2009
	/// </summary>
	
    [Obsolete]
	public class HttpConfig : tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig
	{
        public HttpConfig(Element aElement) : base(aElement)
        {
            
        }

        public HttpConfig(tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig config) : base()
        {
            BasicAuthenticationPassword = config.BasicAuthenticationPassword;
            BasicAuthenticationUsername = config.BasicAuthenticationUsername;
            ConnectionTimeoutInMilliseconds = config.ConnectionTimeoutInMilliseconds;
            ProxyHost = config.ProxyHost;
            ProxyPassword = config.ProxyPassword;
            ProxyUsername = config.ProxyUsername;
        }

        /*
		private string mProxyHost;
		private string mProxyPort;
		private string mProxyUsername;
		private string mProxyPassword;
		private string mBasicAuthenticationUsername;
		private string mBasicAuthenticationPassword;
		private int mConnectionTimeoutInMilliseconds = 5000;


		public HttpConfig(Element aElement) : base(aElement)
		{

			if (aElement != null)
			{
				mProxyHost = getChildText(Constants.NS_MA3, ConfigConstants.TAG_PROXY_HOST);
				mProxyPort = getChildText(Constants.NS_MA3, ConfigConstants.TAG_PROXY_PORT);
				mProxyUsername = getChildText(Constants.NS_MA3, ConfigConstants.TAG_PROXY_USERNAME);
				mProxyPassword = getChildText(Constants.NS_MA3, ConfigConstants.TAG_PROXY_PASSWORD);
				mBasicAuthenticationUsername = getChildText(Constants.NS_MA3, ConfigConstants.TAG_BASICAUTH_USERNAME);
				mBasicAuthenticationPassword = getChildText(Constants.NS_MA3, ConfigConstants.TAG_BASICAUTH_PASSWORD);
                int ? conTimeOut = getChildInteger(ConfigConstants.TAG_CONNECTION_TIMEOUT);
			    if (conTimeOut != null)
			        mConnectionTimeoutInMilliseconds = (int) conTimeOut;
			}
		}

		public virtual string ProxyHost
		{
			get
			{
				return mProxyHost;
			}
		}

		public virtual string ProxyPort
		{
			get
			{
				return mProxyPort;
			}
		}

		public virtual string ProxyUsername
		{
			get
			{
				return mProxyUsername;
			}
		}

		public virtual string ProxyPassword
		{
			get
			{
				return mProxyPassword;
			}
		}

		public virtual string BasicAuthenticationUsername
		{
			get
			{
				return mBasicAuthenticationUsername;
			}
		}

		public virtual string BasicAuthenticationPassword
		{
			get
			{
				return mBasicAuthenticationPassword;
			}
		}

		public virtual int ConnectionTimeoutInMilliseconds
		{
			get
			{
				return mConnectionTimeoutInMilliseconds;
			}
		}
         */
	}

}