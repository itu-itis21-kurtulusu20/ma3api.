package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.w3c.dom.Element;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * @author ayetgin
 */
public class HttpConfig extends BaseConfigElement
{
    private String mProxyHost;
    private String mProxyPort;
    private String mProxyUsername;
    private String mProxyPassword;
    private String mBasicAuthenticationUsername;
    private String mBasicAuthenticationPassword;
    private int mConnectionTimeoutInMilliseconds = 5000;

    public HttpConfig()
    {
    }

    public HttpConfig(Element aElement)
    {
        super(aElement);

        if (aElement!=null){
            mProxyHost                   = getChildText(NS_MA3, TAG_PROXY_HOST);
            mProxyPort                   = getChildText(NS_MA3, TAG_PROXY_PORT);
            mProxyUsername               = getChildText(NS_MA3, TAG_PROXY_USERNAME);
            mProxyPassword               = getChildText(NS_MA3, TAG_PROXY_PASSWORD);
            mBasicAuthenticationUsername = getChildText(NS_MA3, TAG_BASICAUTH_USERNAME);
            mBasicAuthenticationPassword = getChildText(NS_MA3, TAG_BASICAUTH_PASSWORD);
            mConnectionTimeoutInMilliseconds = getChildInteger(TAG_CONNECTION_TIMEOUT);
        }
    }

    public String getProxyHost()
    {
        return mProxyHost;
    }

    public String getProxyPort()
    {
        return mProxyPort;
    }

    public String getProxyUsername()
    {
        return mProxyUsername;
    }

    public String getProxyPassword()
    {
        return mProxyPassword;
    }

    public String getBasicAuthenticationUsername()
    {
        return mBasicAuthenticationUsername;
    }

    public String getBasicAuthenticationPassword()
    {
        return mBasicAuthenticationPassword;
    }

    public int getConnectionTimeoutInMilliseconds()
    {
        return mConnectionTimeoutInMilliseconds;
    }

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

    public void setConnectionTimeoutInMilliseconds(int aConnectionTimeoutInMilliseconds)
    {
        mConnectionTimeoutInMilliseconds = aConnectionTimeoutInMilliseconds;
    }
}

