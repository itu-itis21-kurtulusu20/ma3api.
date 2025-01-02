package tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;

import java.io.InputStream;

/**
 * Document for input streams.
 *
 * @author ahmety
 * date: May 14, 2009
 */
public class StreamingDocument extends Document
{
    InputStream mStream;
    byte[] mBytes;

    public StreamingDocument(InputStream aStream,
                             String aURI,
                             String aMIMEType,
                             String aEncoding)
    {
        super(aURI, aMIMEType, aEncoding);
        mStream = aStream;
    }

    public DataType getType()
    {
        return DataType.OCTETSTREAM;
    }

    public InputStream getStream() 
    {
        return mStream;
    }

}
