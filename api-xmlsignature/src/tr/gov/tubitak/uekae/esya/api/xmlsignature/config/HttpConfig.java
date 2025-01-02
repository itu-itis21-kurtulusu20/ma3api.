package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Dec 4, 2009
 * @deprecated
 * @see tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig
 */
@Deprecated
public class HttpConfig extends tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig
{
    public HttpConfig(Element aElement)
    {
        super(aElement);
    }

    public HttpConfig(tr.gov.tubitak.uekae.esya.api.signature.config.HttpConfig config)
    {
        super();
        setBasicAuthenticationPassword(config.getBasicAuthenticationPassword());
        setBasicAuthenticationUsername(config.getBasicAuthenticationUsername());
        setConnectionTimeoutInMilliseconds(config.getConnectionTimeoutInMilliseconds());
        setProxyHost(config.getProxyHost());
        setProxyPort(config.getProxyPort());
        setProxyPassword(config.getProxyPassword());
        setProxyUsername(config.getProxyUsername());
    }

    /*
   private String mProxyHost;
   private String mProxyPort;
   private String mProxyUsername;
   private String mProxyPassword;
   private String mBasicAuthenticationUsername;
   private String mBasicAuthenticationPassword;
   private int mConnectionTimeoutInMilliseconds = 5000;


   public HttpConfig(Element aElement)
   {
       super(aElement);

       if (aElement!=null){
           mProxyHost                   = getChildText(Constants.NS_MA3, TAG_PROXY_HOST);
           mProxyPort                   = getChildText(Constants.NS_MA3, TAG_PROXY_PORT);
           mProxyUsername               = getChildText(Constants.NS_MA3, TAG_PROXY_USERNAME);
           mProxyPassword               = getChildText(Constants.NS_MA3, TAG_PROXY_PASSWORD);
           mBasicAuthenticationUsername = getChildText(Constants.NS_MA3, TAG_BASICAUTH_USERNAME);
           mBasicAuthenticationPassword = getChildText(Constants.NS_MA3, TAG_BASICAUTH_PASSWORD);
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
   } */
}
