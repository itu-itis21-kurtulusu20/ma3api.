package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.HttpConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.URI;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author ahmety
 * date: May 14, 2009
 */
public class HttpResolver implements IResolver
{

    static Logger logger = LoggerFactory.getLogger(HttpResolver.class);

    public StreamingDocument resolve(String uri, Context aContext)
            throws IOException
    {
        String baseURI = aContext.getBaseURIStr();
        try {
            boolean useProxy = false;
            HttpConfig config = aContext.getConfig().getHttpConfig();
            String proxyPort = config.getProxyPort();
            String proxyHost = config.getProxyHost();

            if ((proxyHost != null) && (proxyHost.length()>0) && (proxyPort != null) && (proxyPort.length()>0)) {
                useProxy = true;
            }

            String oldProxySet = null;
            String oldProxyHost = null;
            String oldProxyPort = null;
            // switch on proxy usage
            if (useProxy) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Use of HTTP proxy enabled: "+proxyHost + ":" + proxyPort);
                }
                oldProxySet = System.getProperty("http.proxySet");
                oldProxyHost = System.getProperty("http.proxyHost");
                oldProxyPort = System.getProperty("http.proxyPort");
                System.setProperty("http.proxySet", "true");
                System.setProperty("http.proxyHost", proxyHost);
                System.setProperty("http.proxyPort", proxyPort);
            }

            boolean switchBackProxy = (oldProxySet != null)
                                           && (oldProxyHost != null)
                                           && (oldProxyPort != null);

            // calculate new URI
            URI uriNew = getNewURI(uri, baseURI);

            // if the URI contains a fragment, ignore it
            URI uriNewNoFrag = new URI(uriNew);

            uriNewNoFrag.setFragment(null);

            URL url = new URL(uriNewNoFrag.toString());

            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(config.getConnectionTimeoutInMilliseconds());

            {

                // set proxy pass
                String proxyUser = config.getProxyUsername();
                String proxyPass = config.getProxyPassword();

                if ((proxyUser != null) && (proxyUser.length()>0) && (proxyPass != null) && proxyPass.length()>0) {
                    String password = proxyUser + ":" + proxyPass;
                    String encodedPassword = Base64.encode(password.getBytes());

                    // or was it Proxy-Authenticate ?
                    urlConnection.setRequestProperty("Proxy-Authorization", encodedPassword);
                }
            }

            {

                // check if Basic authentication is required
                String auth = urlConnection.getHeaderField("WWW-Authenticate");

                if (auth != null) {

                    // do http basic authentication
                    if (auth.startsWith("Basic")) {
                        String user = config.getBasicAuthenticationUsername();
                        String pass = config.getBasicAuthenticationPassword();

                        if ((user != null) && (pass != null)) {
                            urlConnection = url.openConnection();

                            String password = user + ":" + pass;
                            String encodedPassword = Base64.encode(password.getBytes());

                            // set authentication property in the http header
                            urlConnection.setRequestProperty("Authorization", "Basic " + encodedPassword);
                        }
                    }
                }
            }

            String mimeType = urlConnection.getHeaderField("Content-Type");
            InputStream inputStream = urlConnection.getInputStream();
            StreamingDocument result = new StreamingDocument(inputStream, uriNew.toString(), mimeType, null);


            // switch off proxy usage
            if (useProxy && switchBackProxy) {
                System.setProperty("http.proxySet", oldProxySet);
                System.setProperty("http.proxyHost", oldProxyHost);
                System.setProperty("http.proxyPort", oldProxyPort);
            }

            return result;
        }
        catch (Exception ex) {
            logger.error(I18n.translate("resolver.cantResolveUri", uri), ex);
            throw new IOException(I18n.translate("resolver.cantResolveUri", uri));
        }
    }

    /**
     * We resolve http URIs <I>without</I> fragment...
     * @return true if can be resolved
     */
    public boolean isResolvable(String aURI, Context aBaglam)
    {
        if (aURI == null) {
            logger.debug("quick fail, uri == null");

            return false;
        }

        if (aURI.equals("") || (aURI.charAt(0) == '#')) {
            logger.debug("quick fail for empty URIs and local ones");

            return false;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("I was asked whether I can resolve " + aURI);
        }

        String baseURI = aBaglam.getBaseURIStr();

        if (aURI.startsWith("http:") || (baseURI != null && baseURI.startsWith("http:"))) {
            if (logger.isDebugEnabled()) {
                logger.debug("I state that I can resolve " + aURI);
            }

            return true;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("I state that I can't resolve " + aURI);
        }

        return false;
    }

    private URI getNewURI(String uri, String BaseURI)
            throws URI.MalformedURIException
    {

        if ((BaseURI == null) || "".equals(BaseURI)) {
            return new URI(uri);
        }
        return new URI(new URI(BaseURI), uri);
    }
}
