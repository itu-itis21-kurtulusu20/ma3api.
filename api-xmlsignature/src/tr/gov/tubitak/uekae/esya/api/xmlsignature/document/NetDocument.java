package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.HttpResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * Document for data over network.
 *
 * @author ahmety
 * date: May 14, 2009
 */
public class NetDocument extends Document {
    protected static Logger logger = LoggerFactory.getLogger(NetDocument.class);
    private static Context EMPTY_CONTEXT = new Context();
    private static HttpResolver msResolver = new HttpResolver();

    private StreamingDocument mInternalDocument;


    public NetDocument(String aURL, String aMimeType, String aEncoding)
            throws IOException
    {
        super(aURL, aMimeType, aEncoding);
        try {
            mInternalDocument = msResolver.resolve(aURL, EMPTY_CONTEXT);
        } catch (IOException e){
            // shouldnt happen
            logger.error("Error in NetDocument", e);
        }
    }

    public DataType getType()
    {
        return DataType.OCTETSTREAM;
    }

    public InputStream getStream() throws UnsupportedOperationException
    {
        return mInternalDocument.getStream();
    }

}
