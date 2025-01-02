using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    public class HttpConfig : BaseConfigElement
    {
        private String mProxyHost;
        private String mProxyPort;
        private String mProxyUsername;
        private String mProxyPassword;
        private String mBasicAuthenticationUsername;
        private String mBasicAuthenticationPassword;
        private int? mConnectionTimeoutInMilliseconds = 5000;

        public HttpConfig()
        {
        }

        public HttpConfig(XmlElement aElement)
            : base(aElement)
        {

            if (aElement != null)
            {
                mProxyHost = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_PROXY_HOST);
                mProxyPort = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_PROXY_PORT);
                mProxyUsername = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_PROXY_USERNAME);
                mProxyPassword = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_PROXY_PASSWORD);
                mBasicAuthenticationUsername = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_BASICAUTH_USERNAME);
                mBasicAuthenticationPassword = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_BASICAUTH_PASSWORD);
                mConnectionTimeoutInMilliseconds = getChildInteger(ConfigConstants.TAG_CONNECTION_TIMEOUT);
            }
        }

        public String ProxyHost
        {
            get { return mProxyHost; }
            set { mProxyHost = value; }
        }

        public String ProxyPort
        {
            get { return mProxyPort; }
            set { mProxyPort = value; }
        }

        public String ProxyUsername
        {
            get { return mProxyUsername; }
            set { mProxyUsername = value; }
        }

        public String ProxyPassword
        {
            get { return mProxyPassword; }
            set { mProxyPassword = value; }
        }

        public String BasicAuthenticationUsername
        {
            get { return mBasicAuthenticationUsername; }
            set { mBasicAuthenticationUsername = value; }
        }

        public String BasicAuthenticationPassword
        {
            get { return mBasicAuthenticationPassword; }
            set { mBasicAuthenticationPassword = value; }
        }

        public int? ConnectionTimeoutInMilliseconds
        {
            get { return mConnectionTimeoutInMilliseconds; }
            set { mConnectionTimeoutInMilliseconds = value; }
        }

        /*
        public void setProxyHost(String aProxyHost)
        {
            mProxyHost = aProxyHost;
        }

        public void setProxyPort(String aProxyPort)
        {
            mProxyPort = aProxyPort;
        }

        public void setProxyUsername(String aProxyUsername)
        {
            mProxyUsername = aProxyUsername;
        }

        public void setProxyPassword(String aProxyPassword)
        {
            mProxyPassword = aProxyPassword;
        }

        public void setBasicAuthenticationUsername(String aBasicAuthenticationUsername)
        {
            mBasicAuthenticationUsername = aBasicAuthenticationUsername;
        }

        public void setBasicAuthenticationPassword(String aBasicAuthenticationPassword)
        {
            mBasicAuthenticationPassword = aBasicAuthenticationPassword;
        }

        public void setConnectionTimeoutInMilliseconds(int? aConnectionTimeoutInMilliseconds)
        {
            mConnectionTimeoutInMilliseconds = aConnectionTimeoutInMilliseconds;
        }
         * */
    }
}
