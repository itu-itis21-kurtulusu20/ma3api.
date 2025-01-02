using System;
using System.IO;
using System.Net;
using System.Security.Cryptography.Xml;
using System.Text;
using Org.BouncyCastle.Utilities.Encoders;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver
{

	using Logger = log4net.ILog;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using HttpConfig = tr.gov.tubitak.uekae.esya.api.xmlsignature.config.HttpConfig;
	using StreamingDocument = tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;


	/// <summary>
	/// @author ahmety
	/// date: May 14, 2009
	/// </summary>
	public class HttpResolver : IResolver
	{

		internal static readonly Logger logger = log4net.LogManager.GetLogger(typeof(HttpResolver));

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument resolve(String uri, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws java.io.IOException
		public virtual Document resolve(string uri, Context aContext)
		{
            string baseURIStr = aContext.BaseURIStr;
           
		    try
			{
				bool useProxy = false;
				HttpConfig config = aContext.Config.HttpConfig;
				string proxyPort = config.ProxyPort;
				string proxyHost = config.ProxyHost;

				if ((proxyHost != null) && (proxyHost.Length > 0) && (proxyPort != null) && (proxyPort.Length > 0))
				{
					useProxy = true;
				}

				string oldProxySet = null;
				string oldProxyHost = null;
				string oldProxyPort = null;
				// switch on proxy usage
				if (useProxy)
				{
				    if (logger.IsDebugEnabled)
				    {
				        logger.Debug("Use of HTTP proxy enabled: " + proxyHost + ":" + proxyPort);
				    }

                    //TODO Burda sistem proxy ayarlar� nereden yap�lacak ?
                    /*	oldProxySet = System.getProperty("http.proxySet");
                        oldProxyHost = System.getProperty("http.proxyHost");
                        oldProxyPort = System.getProperty("http.proxyPort");
                        System.setProperty("http.proxySet", "true");
                        System.setProperty("http.proxyHost", proxyHost);
                        System.setProperty("http.proxyPort", proxyPort);*/
                }

				bool switchBackProxy = (oldProxySet != null) && (oldProxyHost != null) && (oldProxyPort != null);

				// calculate new URI
				Uri httpUri = getNewURI(uri, baseURIStr);

                //TODO Fragment kald�rma i�lemi nas�l olacak.
				// if the URI contains a fragment, ignore it
                //Uri uriNewNoFrag = new Uri(uriNew);
				//uriNewNoFrag.Fragmen= null

                  HttpWebRequest httpRequest = (HttpWebRequest) WebRequest.Create(httpUri);
                  httpRequest.Timeout = (int)config.ConnectionTimeoutInMilliseconds;
                 httpRequest.Proxy = new WebProxy();
                    
				{
					// set proxy pass
					string proxyUser = config.ProxyUsername;
					string proxyPass = config.ProxyPassword;

					if ((proxyUser != null) && (proxyUser.Length > 0) && (proxyPass != null) && proxyPass.Length > 0)
					{
				    	string password = proxyUser + ":" + proxyPass;
                        byte[] passwordByte = Encoding.UTF8.GetBytes(password);
                        string encodedPassword = Convert.ToBase64String(passwordByte);
						// or was it Proxy-Authenticate ?
                        httpRequest.Headers.Add("Proxy-Authorization", encodedPassword);
					}
				}

				{

                    // check if Basic authentication is required
					string auth = httpRequest.Headers.Get("WWW-Authenticate");

					if (auth != null
					     && auth.StartsWith("Basic")) // do http basic authentication
                    {			
							string user = config.BasicAuthenticationUsername;
							string pass = config.BasicAuthenticationPassword;

							if ((user != null) && (pass != null))
							{
                                string password = user + ":" + pass;
                                byte[] passwordByte = Encoding.UTF8.GetBytes(password);
                                string encodedPassword = Convert.ToBase64String(passwordByte);

								// set authentication property in the http header
								httpRequest.Headers.Set("Authorization", "Basic " + encodedPassword);
							}					
					}
				}
                
                string mimeType = httpRequest.Headers.Get("Content-Type");
			    HttpWebResponse httpResponse = (HttpWebResponse) httpRequest.GetResponse();
                StreamingDocument result = new StreamingDocument(httpResponse.GetResponseStream(), httpUri.ToString(), mimeType, null);


                //TODO Sistem proxy ayarlar�n� geri yukleme
				// switch off proxy usage
                /*
				if (useProxy && switchBackProxy)
				{
					System.setProperty("http.proxySet", oldProxySet);
					System.setProperty("http.proxyHost", oldProxyHost);
					System.setProperty("http.proxyPort", oldProxyPort);
				}*/

				return result;
			}
			catch (Exception ex)
			{
				logger.Error(I18n.translate("resolver.cantResolveUri", uri), ex);
				throw new IOException(I18n.translate("resolver.cantResolveUri", uri));
			}
		}

		/// <summary>
		/// We resolve http URIs <I>without</I> fragment... </summary>
		/// <returns> true if can be resolved </returns>
		public virtual bool isResolvable(string aURI, Context aBaglam)
		{
			if (aURI == null)
			{
				logger.Debug("quick fail, uri == null");

				return false;
			}

			if (aURI.Equals("") || (aURI[0] == '#'))
			{
				logger.Debug("quick fail for empty URIs and local ones");

				return false;
			}

			if (logger.IsDebugEnabled)
			{
				logger.Debug("I was asked whether I can resolve " + aURI);
			}

			string baseURI = aBaglam.BaseURIStr;

			if (aURI.StartsWith("http:") || (baseURI != null && baseURI.StartsWith("http:")))
			{
				if (logger.IsDebugEnabled)
				{
					logger.Debug("I state that I can resolve " + aURI);
				}

				return true;
			}

            if (logger.IsDebugEnabled)
			{
				logger.Debug("I state that I can't resolve " + aURI);
			}

			return false;
		}

		private Uri getNewURI(string uri, string BaseURI)
		{
            //TODO burdaki baseuri i�lemi tam olarak nas�l olacak anlayamad�m.
			if ((BaseURI == null) || "".Equals(BaseURI))
			{
                return new Uri(uri);
			}

            Uri baseUri = new Uri(BaseURI);
            Uri newUri = new Uri(baseUri, uri);

		    return newUri;
		}
	}

}