package tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;

import java.io.IOException;
import java.io.FileInputStream;
import java.net.URLConnection;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ahmety
 * date: Jul 2, 2009
 */
public class OfflineResolver implements IResolver
{
    private static Logger logger = LoggerFactory.getLogger(OfflineResolver.class);

    private Map<String, Entry> mUrlToEntry = new HashMap<String, Entry>(1);

    public OfflineResolver()
    {
    }

    public OfflineResolver(String aURL, String aFileUrl, String aMIMEType)
    {
        register(aURL, aFileUrl, aMIMEType);
    }

    public boolean isResolvable(String aURI, Context aBaglam)
    {
        boolean resolvable = mUrlToEntry.containsKey(aURI);
        if (logger.isDebugEnabled()){
            logger.debug("I "+(resolvable? "can" : "cant")+" resolve: "+aURI);
        }
        return resolvable;
    }

    public void register(String aURL, String aFileUrl, String aMIMEType){
        mUrlToEntry.put(aURL, new Entry(aURL, aFileUrl, aMIMEType));
    }

    public Document resolve(String aURI, Context aBaglam)
            throws IOException
    {
        Entry e = mUrlToEntry.get(aURI);

        if (e==null){
            throw new IOException("Unknown offline entry: "+aURI);
        }
        FileInputStream fis = new FileInputStream(e.mFileUrl);
        String mime = e.mMIMEType;
        if (e.mMIMEType==null)
        {
            mime = URLConnection.guessContentTypeFromName(aURI);
            if (mime==null){
                mime = URLConnection.guessContentTypeFromStream(fis);
            }
        }
        return new StreamingDocument(fis, e.mURL, mime, null);
    }

    class Entry {
        String mURL;
        String mMIMEType;
        String mFileUrl;

        Entry(String aURL, String aFileUrl, String aMIMEType)
        {
            mURL = aURL;
            mFileUrl = aFileUrl;
            mMIMEType = aMIMEType;
        }
    }
}
